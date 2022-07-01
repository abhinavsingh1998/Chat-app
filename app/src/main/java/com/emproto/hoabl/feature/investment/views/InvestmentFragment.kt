package com.emproto.hoabl.feature.investment.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentInvestmentLayoutBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.investment.adapters.NewInvestmentAdapter
import com.emproto.hoabl.model.*
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.investment.*
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

    private val onInvestmentItemClickListener =
        View.OnClickListener { view ->
            when (view.id) {
                R.id.iv_smart_deals_see_all -> {
                    investmentViewModel.setSd(true)
                    investmentViewModel.setSmartDealsList(smartDealsList)
                    (requireActivity() as HomeActivity).addFragment(CategoryListFragment(), true)
                }
                R.id.tv_smart_deals_see_all -> {
                    investmentViewModel.setSd(true)
                    investmentViewModel.setSmartDealsList(smartDealsList)
                    (requireActivity() as HomeActivity).addFragment(CategoryListFragment(), true)
                }
                R.id.iv_trending_projects_see_all -> {
                    investmentViewModel.setTp(true)
                    investmentViewModel.setTrendingList(trendingProjectsList)
                    (requireActivity() as HomeActivity).addFragment(CategoryListFragment(),true)
                }
                R.id.tv_trending_projects_see_all -> {
                    investmentViewModel.setTp(true)
                    investmentViewModel.setTrendingList(trendingProjectsList)
                    (requireActivity() as HomeActivity).addFragment(CategoryListFragment(),true)
                }
                R.id.iv_new_launch_see_all -> {
                    investmentViewModel.setNl(true)
                    investmentViewModel.setNewInvestments(newInvestmentsList)
                    (requireActivity() as HomeActivity).addFragment(CategoryListFragment(),true)
                }
                R.id.tv_new_launch_see_all -> {
                    investmentViewModel.setNl(true)
                    investmentViewModel.setNewInvestments(newInvestmentsList)
                    (requireActivity() as HomeActivity).addFragment(CategoryListFragment(),true)
                }
                R.id.cl_place_info -> {
                    navigateToDetailScreen(newInvestmentsList[0].id)
                }
                R.id.tv_apply_now -> {
                    investmentViewModel.setProjectId(newInvestmentsList[0].id)
                    navigateToSkuScreen(newInvestmentsList[0].id)
                }
                R.id.cl_btn_discover -> {
                    callProjectContentAPi()
                }
                R.id.iv_dont_miss_image -> {
                    navigateToDetailScreen(projectId)
                }
            }
        }

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
        callApi()
    }

    private fun setUpViewModel() {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        investmentViewModel =
            ViewModelProvider(
                requireActivity(),
                investmentFactory
            ).get(InvestmentViewModel::class.java)
    }

    private fun setUpUI() {
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility =
            View.VISIBLE
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility =
            View.VISIBLE
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.imageBack.visibility = View.GONE
        binding.slSwipeRefresh.setOnRefreshListener {
            binding.slSwipeRefresh.isRefreshing = true
            binding.slSwipeRefresh.visibility = View.GONE
            callApi()
        }
    }

    private fun callApi() {
        investmentViewModel.getInvestments(5002).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBar.hide()
                    binding.slSwipeRefresh.visibility = View.VISIBLE
                    it.data?.data?.let { data ->
                        binding.slSwipeRefresh.isRefreshing = false
                        newInvestmentsList = data.pageManagementsOrNewInvestments
                        smartDealsList = data.pageManagementsOrCollectionOneModels
                        trendingProjectsList = data.pageManagementsOrCollectionTwoModels
                        projectId = data.page.promotionAndOffersProjectContentId
                        mediaGalleryApi(data)
                    }
                }
                Status.ERROR -> {
                    binding.progressBar.hide()
                    (requireActivity() as HomeActivity).showErrorToast(
                        it.message!!
                    )
                }
            }
        })
    }

    private fun mediaGalleryApi(invData:Data){
        investmentViewModel.getInvestmentsMediaGallery(newInvestmentsList[0].id).observe(viewLifecycleOwner,Observer{
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.data?.let { data ->
                        if(data!=null){
                            setUpRecyclerView(invData,data.mediaGalleries)
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
        })
    }

    private fun setUpRecyclerView(data: Data,mediaGalleries: MediaGalleries) {
        val list = ArrayList<RecyclerViewItem>()
        list.add(RecyclerViewItem(NewInvestmentAdapter.TYPE_NEW_LAUNCH))
        when(data.page.isCollectionOneActive){
            true -> list.add(RecyclerViewItem(NewInvestmentAdapter.TYPE_LAST_PLOTS))
        }
        when(data.page.isCollectionTwoActive){
            true -> list.add(RecyclerViewItem(NewInvestmentAdapter.TYPE_TRENDING_PROJECTS))
        }
        newInvestmentAdapter = NewInvestmentAdapter(
            (requireActivity() as HomeActivity),
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
        investmentViewModel.getAllInvestmentsProjects().observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.LOADING -> {
                    binding.progressBar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.data?.let {  data ->
                        investmentViewModel.setAp(true)
                        investmentViewModel.setAllInvestments(data)
                        (requireActivity() as HomeActivity).addFragment(CategoryListFragment(),true)
                    }
                }
                Status.ERROR -> {
                    binding.progressBar.hide()
                    (requireActivity() as HomeActivity).showErrorToast(
                        it.message!!
                    )
                }
            }
        })
    }

    private val itemClickListener = object : ItemClickListener {
        override fun onItemClicked(view: View, position: Int, item: String) {
            when(position){
                0-> navigateToDetailScreen(item.toInt())
                1 -> navigateToDetailScreen(item.toInt())
                2 -> navigateToDetailScreen(item.toInt())
                3 -> navigateToSkuScreen(item.toInt())
                4 -> navigateToDetailScreen(item.toInt())
            }

        }
    }

    private fun navigateToDetailScreen(id: Int) {
        val bundle = Bundle()
        bundle.putInt("ProjectId", id)
        val fragment = ProjectDetailFragment()
        fragment.arguments = bundle
        (requireActivity() as HomeActivity).addFragment(
            fragment, false
        )
    }

    private fun navigateToSkuScreen(id:Int){
        val fragment = LandSkusFragment()
        val bundle = Bundle()
        bundle.putInt("ProjectId", id)
        fragment.arguments = bundle
        (requireActivity() as HomeActivity).addFragment(fragment,false)
    }

}
