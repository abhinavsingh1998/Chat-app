package com.emproto.hoabl.feature.investment.views

import android.os.Bundle
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

    private val onInvestmentItemClickListener =
        View.OnClickListener { view ->
            when (view.id) {
                R.id.tv_smart_deals_see_all -> {
//                    val smartDealsData = SmartDealsModel(smartDealsList,true)
                    investmentViewModel.setSd(true)
                    investmentViewModel.setSmartDealsList(smartDealsList)
                    (requireActivity() as HomeActivity).addFragment(CategoryListFragment(), true)
                }
                R.id.tv_trending_projects_see_all -> {
                    investmentViewModel.setTp(true)
                    investmentViewModel.setTrendingList(trendingProjectsList)
                    (requireActivity() as HomeActivity).addFragment(CategoryListFragment(),true)
                }
                R.id.tv_new_launch_see_all -> {
//                    val newLaunchData = NewLaunchModel(newInvestmentsList,true)
                    investmentViewModel.setNl(true)
                    investmentViewModel.setNewInvestments(newInvestmentsList)
                    (requireActivity() as HomeActivity).addFragment(CategoryListFragment(),true)
                }
                R.id.cl_place_info -> {
                    //investmentViewModel.setProjectId(newInvestmentsList[0].id)
                    //(requireActivity() as HomeActivity).addFragment(ProjectDetailFragment(),false)
                    val bundle = Bundle()
                    bundle.putInt("ProjectId", newInvestmentsList[0].id)
                    val fragment = ProjectDetailFragment()
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(
                        fragment, false
                    )
                }
                R.id.tv_apply_now -> {
                    investmentViewModel.setProjectId(newInvestmentsList[0].id)
                    callProjectDataApi()
                }
                R.id.btn_discover -> {
                    callProjectContentAPi()
                }
            }
        }

    private fun callProjectDataApi() {
        investmentViewModel.getProjectId().observe(viewLifecycleOwner, Observer {
            investmentViewModel.getInvestmentsDetail(it).observe(viewLifecycleOwner, Observer {
                when(it.status){
                    Status.LOADING -> {
                        (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
                    }
                    Status.SUCCESS -> {
                        (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                        it.data?.data?.let {  data ->
                            investmentViewModel.setSkus(data.inventoryBucketContents)
                            (requireActivity() as HomeActivity).addFragment(LandSkusFragment(),false)
                        }
                    }
                    Status.ERROR -> {
                        (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                        (requireActivity() as HomeActivity).showErrorToast(
                            it.message!!
                        )
                    }
                }
            })
        })
    }

    private fun callProjectContentAPi() {
        investmentViewModel.getAllInvestmentsProjects().observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.LOADING -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
                }
                Status.SUCCESS -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                    it.data?.data?.let {  data ->
//                        val allProjectsData = AllProjectsModel(data,true)
                        investmentViewModel.setAp(true)
                        investmentViewModel.setAllInvestments(data)
                        (requireActivity() as HomeActivity).addFragment(CategoryListFragment(),true)
                    }
                }
                Status.ERROR -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                    (requireActivity() as HomeActivity).showErrorToast(
                        it.message!!
                    )
                }
            }
        })
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
    }

    private fun callApi() {
        investmentViewModel.getInvestments(5002).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
                }
                Status.SUCCESS -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                    it.data?.data?.let { data ->
                        setUpRecyclerView(data)
                        newInvestmentsList = data.page.pageManagementsOrNewInvestments
                        smartDealsList = data.pageManagementsOrCollectionOneModels
                        trendingProjectsList = data.pageManagementsOrCollectionTwoModels
                    }
                }
                Status.ERROR -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                    (requireActivity() as HomeActivity).showErrorToast(
                        it.message!!
                    )
                }
            }
        })
    }

    private fun setUpRecyclerView(data: Data) {
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
            itemClickListener
        )
        binding.rvInvestmentPage.adapter = newInvestmentAdapter
        newInvestmentAdapter.setItemClickListener(onInvestmentItemClickListener)
    }

    private val itemClickListener = object : ItemClickListener {
        override fun onItemClicked(view: View, position: Int, item: String) {
            //investmentViewModel.setProjectId(item.toInt())
            //(requireActivity() as HomeActivity).addFragment(ProjectDetailFragment(),false)
            val bundle = Bundle()
            bundle.putInt("ProjectId", item.toInt())
            val fragment = ProjectDetailFragment()
            fragment.arguments = bundle
            (requireActivity() as HomeActivity).addFragment(
                fragment, false
            )
        }
    }

}
