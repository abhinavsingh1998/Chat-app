package com.emproto.hoabl.feature.investment.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.core.Constants
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentInvestmentLayoutBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.home.views.Mixpanel
import com.emproto.hoabl.feature.investment.adapters.NewInvestmentAdapter
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.investment.*
import java.io.Serializable
import javax.inject.Inject

class InvestmentFragment : BaseFragment() {

    @Inject
    lateinit var investmentFactory: InvestmentFactory
    lateinit var investmentViewModel: InvestmentViewModel
    private lateinit var binding: FragmentInvestmentLayoutBinding
    private lateinit var newInvestmentAdapter: NewInvestmentAdapter
    private lateinit var smartDealsList: List<PageManagementsOrCollectionOneModel>
    private lateinit var trendingProjectsList: List<PageManagementsOrCollectionTwoModel>
    private lateinit var newInvestmentsList: List<PageManagementsOrNewInvestment>
    private var projectId = 0
    @Inject
    lateinit var appPreference: AppPreference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInvestmentLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        setUpUI()
        eventTrackingInvestment()
        callApi(false)
    }

    private fun eventTrackingInvestment() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(),Mixpanel.INVESTMENT)
    }

    private fun setUpViewModel() {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        investmentViewModel =
            ViewModelProvider(
                requireActivity(),
                investmentFactory
            )[InvestmentViewModel::class.java]
    }

    private fun setUpUI() {
        (requireActivity() as HomeActivity).showHeader()
        (requireActivity() as HomeActivity).showBottomNavigation()
        (requireActivity() as HomeActivity).hideBackArrow()
        binding.slSwipeRefresh.setOnRefreshListener {
            binding.slSwipeRefresh.isRefreshing = true
            binding.rvInvestmentPage.hide()
            callApi(true)
        }
    }

    private fun callApi(refresh: Boolean) {
        if (isNetworkAvailable()) {
            investmentViewModel.getInvestments(5002, refresh).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.LOADING -> {
                        binding.progressBar.show()
                        binding.noInternetView.mainContainer.hide()
                    }
                    Status.SUCCESS -> {
                        binding.progressBar.hide()
                        binding.noInternetView.mainContainer.hide()
                        binding.slSwipeRefresh.isRefreshing = false
                        it.data?.data?.let { data ->
                            binding.rvInvestmentPage.show()
                            newInvestmentsList = data.pageManagementsOrNewInvestments
                            smartDealsList = data.pageManagementsOrCollectionOneModels
                            trendingProjectsList = data.pageManagementsOrCollectionTwoModels
                            projectId = data.page.promotionAndOffersProjectContentId
                            mediaGalleryApi(data)
                        }
                    }
                    Status.ERROR -> {
                        binding.slSwipeRefresh.isRefreshing = false
                        binding.progressBar.hide()
                        (requireActivity() as HomeActivity).showErrorToast(
                            it.message!!
                        )
                    }
                }
            }
        } else {
            binding.slSwipeRefresh.isRefreshing = false
            binding.progressBar.hide()
            binding.rvInvestmentPage.hide()
            binding.noInternetView.mainContainer.show()
            binding.noInternetView.textView6.setOnClickListener {
                callApi(true)
            }
        }

    }

    private fun mediaGalleryApi(invData: Data) {
        if (newInvestmentsList[0] == null) {
            (requireActivity() as HomeActivity).showErrorToast(
                "Media gallery Id is null"
            )
        } else {
            investmentViewModel.getInvestmentsMediaGallery(newInvestmentsList[0].id)
                .observe(viewLifecycleOwner) {
                    when (it.status) {
                        Status.LOADING -> {
                            binding.progressBar.show()
                        }
                        Status.SUCCESS -> {
                            binding.progressBar.hide()
                            it.data?.data?.let { data ->
                                if (data != null) {
                                    setUpRecyclerView(invData, data.mediaGalleries)
                                }
                            }
                        }
                        Status.ERROR -> {
                            binding.progressBar.hide()
                            (requireActivity() as HomeActivity).showErrorToast(
                                it.message!!
                            )
                        }
                    }
                }
        }
    }

    private fun setUpRecyclerView(data: Data, mediaGalleries: MediaGalleries) {
        val list = ArrayList<RecyclerViewItem>()
        list.add(RecyclerViewItem(NewInvestmentAdapter.TYPE_NEW_LAUNCH))
        when (data.page.isCollectionOneActive) {
            true -> {
                list.add(RecyclerViewItem(NewInvestmentAdapter.TYPE_LAST_PLOTS))
            }
            else -> {}
        }
        when (data.page.isCollectionTwoActive) {
            true -> list.add(RecyclerViewItem(NewInvestmentAdapter.TYPE_TRENDING_PROJECTS))
            else -> {}
        }
        newInvestmentAdapter = NewInvestmentAdapter(
            this.requireContext(),
            list,
            data,
            itemClickListener,
            mediaGalleries
        )
        binding.rvInvestmentPage.adapter = newInvestmentAdapter
        newInvestmentAdapter.setItemClickListener(onInvestmentItemClickListener)
    }

    private fun callProjectContentAPi() {
        investmentViewModel.getAllInvestmentsProjects().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.data?.let { data ->
                        val list = CategoryListFragment()
                        val bundle = Bundle()
                        bundle.putString("Category", "AllInvestments")
                        bundle.putSerializable("AllInvestmentsData", data as Serializable)
                        list.arguments = bundle
                        (requireActivity() as HomeActivity).addFragment(list, true)
                    }
                }
                Status.ERROR -> {
                    binding.progressBar.hide()
                    (requireActivity() as HomeActivity).showErrorToast(
                        it.message!!
                    )
                }
            }
        }
    }

    private val itemClickListener = object : ItemClickListener {
        override fun onItemClicked(view: View, position: Int, item: String) {
            when (position) {
                0 -> navigateToDetailScreen(item.toInt())
                1 -> navigateToDetailScreen(item.toInt())
                2 -> navigateToDetailScreen(item.toInt())
                3 -> navigateToSkuScreen(item.toInt())
                4 -> navigateToDetailScreen(item.toInt())
            }

        }
    }

    private fun navigateToDetailScreen(id: Int) {
        val bundle = Bundle()
        bundle.putInt(Constants.PROJECT_ID, id)
        val fragment = ProjectDetailFragment()
        fragment.arguments = bundle
        (requireActivity() as HomeActivity).addFragment(
            fragment, true
        )
    }

    private fun navigateToSkuScreen(id: Int) {
        val fragment = LandSkusFragment()
        val bundle = Bundle()
        bundle.putInt(Constants.PROJECT_ID, id)
        fragment.arguments = bundle
        (requireActivity() as HomeActivity).addFragment(fragment, true)
    }

    private val onInvestmentItemClickListener =
        View.OnClickListener { view ->
            when (view.id) {
                R.id.tv_smart_deals_see_all -> {
                    val list = CategoryListFragment()
                    val bundle = Bundle()
                    bundle.putString("Category", "LastFewPLots")
                    bundle.putSerializable("LastFewPLotsData", smartDealsList as Serializable)
                    list.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(list, true)
                }
                R.id.tv_trending_projects_see_all -> {
                    val list = CategoryListFragment()
                    val bundle = Bundle()
                    bundle.putString("Category", "TrendingProjects")
                    bundle.putSerializable(
                        "TrendingProjectsData",
                        trendingProjectsList as Serializable
                    )
                    list.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(list, true)
                }
                R.id.tv_new_launch_see_all -> {
                    val list = CategoryListFragment()
                    val bundle = Bundle()
                    bundle.putString("Category", "NewLaunches")
                    bundle.putSerializable("NewLaunchesData", newInvestmentsList as Serializable)
                    list.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(list, true)
                }
                R.id.cl_place_info -> {
                    eventTrackingNewLaunchCard()
                    navigateToDetailScreen(newInvestmentsList[0].id)
                }
                R.id.tv_apply_now -> {
                    eventTrackingApplyNow()
                    investmentViewModel.setProjectId(newInvestmentsList[0].id)
                    navigateToSkuScreen(newInvestmentsList[0].id)
                }
                R.id.cl_btn_discover -> {
                    callProjectContentAPi()
                }
                R.id.iv_dont_miss_image -> {
                    eventTrackingDontMissOut()
                    navigateToDetailScreen(projectId)
                }
            }
        }

    private fun eventTrackingDontMissOut() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(), Mixpanel.INVESTMENTDONTMISSOUT2)
    }

    private fun eventTrackingApplyNow() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(), Mixpanel.INVESTMENTAPPLYNOW)
    }

    private fun eventTrackingNewLaunchCard() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(), Mixpanel.NEWLAUNCHCARD)
    }

}
