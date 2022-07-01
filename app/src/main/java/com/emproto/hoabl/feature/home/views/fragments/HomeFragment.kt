package com.emproto.hoabl.feature.home.views.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.OnSwipe
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentHomeBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.chat.views.fragments.ChatsFragment
import com.emproto.hoabl.feature.home.adapters.*
import com.emproto.hoabl.feature.home.data.LatesUpdatesPosition
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.adapters.NewInvestmentAdapter
import com.emproto.hoabl.feature.investment.views.CategoryListFragment
import com.emproto.hoabl.feature.investment.views.LandSkusFragment
import com.emproto.hoabl.feature.investment.views.ProjectDetailFragment
import com.emproto.hoabl.feature.portfolio.views.FmFragment
import com.emproto.hoabl.feature.promises.PromisesDetailsFragment
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.utils.Extensions.toData
import com.emproto.hoabl.utils.Extensions.toHomePagesOrPromise
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import com.emproto.networklayer.enum.ModuleEnum
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.home.HomeResponse
import com.emproto.networklayer.response.home.PageManagementsOrNewInvestment
import com.emproto.networklayer.response.marketingUpdates.Data
import com.emproto.networklayer.response.portfolio.fm.FMResponse
import com.google.android.material.tabs.TabLayoutMediator
import java.io.Serializable
import javax.inject.Inject
import kotlin.properties.Delegates


class HomeFragment : BaseFragment() {

    lateinit var binding: FragmentHomeBinding
    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var homeAdapter: HomeAdapter

    private lateinit var homeData:com.emproto.networklayer.response.home.Data


    val appURL = "https://hoabl.in/"
    private var projectId = 0

    @Inject
    lateinit var factory: HomeFactory
    lateinit var investmentFactory: InvestmentFactory
    lateinit var homeViewModel: HomeViewModel
    val list = ArrayList<PageManagementsOrNewInvestment>()
     var latestUptaesListCount:Int= 0
     var InsightsListCount:Int= 0
     var testimonialsListCount:Int= 0
    lateinit var latestHeading:String
    lateinit var latestSubHeading:String

    lateinit var insightsHeading:String
    lateinit var insightsSubHeading:String

    lateinit var testimonilalsHeading:String
    lateinit var testimonilalsSubHeading:String



    var fmData: FMResponse? = null

    @Inject
    lateinit var appPreference: AppPreference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel = ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]
        initObserver(refresh = false)
        initView()
        return binding.root
    }


    private fun initObserver(refresh: Boolean) {

        homeViewModel.getDashBoardData(ModuleEnum.HOME.value, refresh)
            .observe(viewLifecycleOwner, object : Observer<BaseResponse<HomeResponse>> {
                override fun onChanged(it: BaseResponse<HomeResponse>?) {
                    when (it!!.status) {
                        Status.LOADING -> {
                            binding.dashBoardRecyclerView.hide()
                            binding.loader.show()

                        }
                        Status.SUCCESS -> {
                            binding.dashBoardRecyclerView.show()
                            binding.loader.hide()

                            callfacilityManagement(it.data!!.data)
                            homeData= it!!.data!!.data
                            latestUptaesListCount= it!!.data!!.data.page.totalUpdatesOnListView
                            InsightsListCount= it!!.data!!.data.page.totalInsightsOnListView
                            testimonialsListCount=it!!.data!!.data.page.totalTestimonialsOnListView


                            latestHeading= it!!.data!!.data.page.latestUpdates.heading
                            latestSubHeading= it!!.data!!.data.page.latestUpdates.subHeading

                            insightsHeading= it!!.data!!.data.page.insightsHeading
                            insightsSubHeading= it!!.data!!.data.page.insightsSubHeading

                            testimonilalsHeading= it!!.data!!.data.page.testimonialsHeading
                            testimonilalsSubHeading= it!!.data!!.data.page.testimonialsSubHeading

                            it.data.let {
                                if (it != null) {
                                    projectId = it.data.page.promotionAndOffersProjectContentId
                                    appPreference.saveOfferId(projectId)
                                    appPreference.saveOfferUrl(it.data.page.promotionAndOffersMedia.value.url)
                                    homeViewModel.setDashBoardData(it)
                                }
                            }

                            (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.headset.setOnClickListener {
                                val bundle = Bundle()
                                val chatsFragment = ChatsFragment()
                                chatsFragment.arguments = bundle
                                (requireActivity() as HomeActivity).replaceFragment(
                                    chatsFragment.javaClass,
                                    "",
                                    true,
                                    bundle,
                                    null,
                                    0,
                                    false
                                )
                            }

                        }
                        Status.ERROR -> {
                            binding.loader.hide()
                            (requireActivity() as HomeActivity).showErrorToast(
                                it.message!!
                            )
                            binding.dashBoardRecyclerView.show()
                        }
                    }
                }

            })
    }

    private fun callfacilityManagement(data:com.emproto.networklayer.response.home.Data){
        homeViewModel.getFacilityManagment()
            .observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data.let {
                            fmData = it!!
                            setParentRecycler(data, fmData!!)
                        }
                    }
                }
            })
    }

    private val itemClickListener = object : ItemClickListener {
        override fun onItemClicked(view: View, position: Int, item: String) {
            when(view.id){
                R.id.cv_top_view -> {
                    val fragment = ProjectDetailFragment()
                    val bundle = Bundle()
                    bundle.putInt("ProjectId", item.toInt())
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(
                        fragment,
                        false
                    )
                }
                R.id.tv_apply_now -> {
                    val fragment = LandSkusFragment()
                    val bundle = Bundle()
                    bundle.putInt("ProjectId", item.toInt())
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(
                        fragment,
                        false
                    )
                }
                R.id.tv_item_location_info -> {
                    val fragment = ProjectDetailFragment()
                    val bundle = Bundle()
                    bundle.putInt("ProjectId", item.toInt())
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(
                        fragment,
                        false
                    )
                }
                R.id.iv_bottom_arrow -> {
                    val fragment = ProjectDetailFragment()
                    val bundle = Bundle()
                    bundle.putInt("ProjectId", item.toInt())
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(
                        fragment,
                        false
                    )
                }
                R.id.home_latest_update_card->{
                        val convertedData =
                            homeData?.pageManagementOrLatestUpdates[position].toData()
                        val list = ArrayList<Data>()
                        for (item in homeData!!.pageManagementOrLatestUpdates) {
                            list.add(item.toData())
                        }
                        homeViewModel.setLatestUpdatesData(list)
                        homeViewModel.setSeLectedLatestUpdates(convertedData)
                        homeViewModel.setSelectedPosition(
                            LatesUpdatesPosition(
                                position,
                                homeData!!.pageManagementOrLatestUpdates.size
                            )
                        )
                        (requireActivity() as HomeActivity).addFragment(
                            LatestUpdatesDetailsFragment(),
                            false
                        )
                    }
                R.id.home_promises_item->{
                           val data= homeData?.homePagesOrPromises[position].toHomePagesOrPromise()
                        homeViewModel.setSelectedPromise(data)
                        (requireActivity() as HomeActivity).addFragment(
                            PromisesDetailsFragment(),
                            false
                        )
                    }
                R.id.facility_management_card->{
                    if (fmData != null) {
                (requireActivity() as HomeActivity).addFragment(
                    FmFragment.newInstance(
                        fmData!!.data.web_url,
                        ""
                    ), false
                )

            } else {
                (requireActivity() as HomeActivity).showErrorToast(
                    "Something Went Wrong"
                )
            }
        }
                R.id.home_insights_card->{
                        val convertedData =
                            homeData?.pageManagementOrInsights[position].toData()
                        val list =
                            ArrayList<com.emproto.networklayer.response.insights.Data>()
                        for (item in homeData?.pageManagementOrInsights) {
                            list.add(item.toData())
                        }
                        homeViewModel.setInsightsData(list)
                        homeViewModel.setSeLectedInsights(convertedData)

                        (requireActivity() as HomeActivity).addFragment(
                            InsightsDetailsFragment(),
                            false
                        )
                    }
                R.id.dont_miss_out_card->{
                    val bundle = Bundle()
                    bundle.putInt("ProjectId", projectId)
                    val fragment = ProjectDetailFragment()
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(
                        fragment, false
                    )
                }
                R.id.tv_see_all_update->{
                    val fragment = LatestUpdatesFragment()
                    val bundle = Bundle()
                    bundle.putInt("UpdateList", latestUptaesListCount)
                    bundle.putString("heading", latestHeading)

                    bundle.putString("subheading", latestSubHeading)
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(fragment, false)
                }
                R.id.tv_seeall_insights->{
                    val fragment = InsightsFragment()
                    val bundle = Bundle()
                    bundle.putInt("insightsCounts", InsightsListCount)
                    bundle.putString("insightsHeading", insightsHeading)

                    bundle.putString("insightsSubHeading",  insightsSubHeading)
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(fragment, false)

                }
                R.id.tv_seeall_promise->{
                    (requireActivity() as HomeActivity).navigate(R.id.navigation_promises)

                    Toast.makeText(requireContext(), "Hello", Toast.LENGTH_LONG).show()

                }
                R.id.tv_seeall_testimonial->{
                    val fragment =Testimonials()
                    val bundle = Bundle()
                    bundle.putInt("testimonials",testimonialsListCount)

                    bundle.putString("testimonialsHeading", testimonilalsHeading)

                    bundle.putString("testimonialsSubHeading",  testimonilalsSubHeading)
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(fragment, false)
                }
                R.id.tv_viewall_investments->{
                    (requireActivity() as HomeActivity).navigate(R.id.navigation_investment)
                }
                R.id.btn_refer_now->{
                    referNow()
                }
                R.id.app_share_view->{
                    share_app()
                }
            }

        }
    }

    private fun setParentRecycler(data:com.emproto.networklayer.response.home.Data, fmData:FMResponse){

        val list = ArrayList<RecyclerViewItem>()

        homeAdapter= HomeAdapter(requireContext(),data,list, itemClickListener )

        linearLayoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        )

        list.add(RecyclerViewItem(HomeAdapter.NEW_PROJECT))
        list.add(RecyclerViewItem(HomeAdapter.INCOMPLETED_KYC))
        list.add(RecyclerViewItem(HomeAdapter.LATEST_UPDATES))
        list.add(RecyclerViewItem(HomeAdapter.PROMISES))
        list.add(RecyclerViewItem(HomeAdapter.FACILITY_MANAGMENT))
        list.add(RecyclerViewItem(HomeAdapter.INSIGHTS))
        list.add(RecyclerViewItem(HomeAdapter.TESTIMONIAS))
        list.add(RecyclerViewItem(HomeAdapter.SHARE_APP))

        binding.dashBoardRecyclerView.adapter= homeAdapter
        binding.dashBoardRecyclerView.layoutManager= linearLayoutManager

        binding.refressLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            binding.loader.show()
            initObserver(refresh = true)

            binding.refressLayout.isRefreshing = false

        })

    }

    private fun initView() {

//        binding.facilityManagementCardLayout.isVisible = false
//        binding.kycLayout.isVisible = false

        (requireActivity() as HomeActivity).hideBackArrow()
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility =
            View.VISIBLE
        (requireActivity() as HomeActivity).showBottomNavigation()

    }

    private fun referNow() {
        val dialog = ReferralDialog()
        dialog.isCancelable = true
        dialog.show(parentFragmentManager, "Refrral card")
    }

    private fun share_app() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "The House Of Abhinandan Lodha $appURL")
        startActivity(shareIntent)
    }

    override fun onResume() {
        super.onResume()
        hideSoftKeyboard()
    }
}
