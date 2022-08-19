package com.emproto.hoabl.feature.home.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.emproto.core.BaseFragment
import com.emproto.core.Constants
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentHomeBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.chat.views.fragments.ChatsFragment
import com.emproto.hoabl.feature.home.adapters.*
import com.emproto.hoabl.feature.home.data.LatesUpdatesPosition
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.views.LandSkusFragment
import com.emproto.hoabl.feature.investment.views.ProjectDetailFragment
import com.emproto.hoabl.feature.portfolio.views.BookingjourneyFragment
import com.emproto.hoabl.feature.portfolio.views.FmFragment
import com.emproto.hoabl.feature.promises.HoablPromises
import com.emproto.hoabl.feature.promises.PromisesDetailsFragment
import com.emproto.hoabl.fragments.PromisesFragment
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.utils.Extensions.toData
import com.emproto.hoabl.utils.Extensions.toHomePagesOrPromise
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.PortfolioViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import com.emproto.hoabl.viewmodels.factory.PortfolioFactory
import com.emproto.networklayer.enum.ModuleEnum
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.HomeActionItemResponse
import com.emproto.networklayer.response.actionItem.HomeActionItem
import com.emproto.networklayer.response.bookingjourney.BJHeader
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.home.HomeResponse
import com.emproto.networklayer.response.home.PageManagementsOrNewInvestment
import com.emproto.networklayer.response.marketingUpdates.Data
import com.emproto.networklayer.response.notification.dataResponse.NotificationResponse
import com.emproto.networklayer.response.portfolio.fm.FMResponse
import javax.inject.Inject


class HomeFragment : BaseFragment() {

    lateinit var binding: FragmentHomeBinding
    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var homeAdapter: HomeAdapter

    private lateinit var homeData: com.emproto.networklayer.response.home.Data


    val appURL = Constants.APP_URL
    private var projectId = 0

    @Inject
    lateinit var factory: HomeFactory
    lateinit var investmentFactory: InvestmentFactory
    lateinit var homeViewModel: HomeViewModel
    val list = ArrayList<PageManagementsOrNewInvestment>()
    var latestUptaesListCount: Int = 0
    var InsightsListCount: Int = 0
    var testimonialsListCount: Int = 0
    lateinit var latestHeading: String
    lateinit var latestSubHeading: String

    lateinit var insightsHeading: String
    lateinit var insightsSubHeading: String

    lateinit var testimonilalsHeading: String
    lateinit var testimonilalsSubHeading: String
    private var actionItemType= ArrayList<com.emproto.networklayer.response.actionItem.Data>()


    var fmData: FMResponse? = null

    @Inject
    lateinit var portfolioFactory: PortfolioFactory
    lateinit var portfolioViewModel: PortfolioViewModel

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

        if (isNetworkAvailable()) {
            homeViewModel.getDashBoardData(ModuleEnum.HOME.value, refresh, 20, 1)
                .observe(viewLifecycleOwner, object : Observer<BaseResponse<HomeResponse>> {
                    override fun onChanged(it: BaseResponse<HomeResponse>?) {
                        when (it!!.status) {
                            Status.LOADING -> {
                                binding.dashBoardRecyclerView.hide()
                                binding.noInternetView.mainContainer.hide()
                                (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.rotateText.hide()
                                (requireActivity() as HomeActivity).hideHeader()
                                (requireActivity() as HomeActivity).hideBottomNavigation()
                                binding.shimmerLayout.shimmerViewContainer.show()

                            }
                            Status.SUCCESS -> {
                                binding.dashBoardRecyclerView.show()
                                binding.noInternetView.mainContainer.hide()
                                (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.rotateText.show()
                                binding.shimmerLayout.shimmerViewContainer.hide()
                                (requireActivity() as HomeActivity).showHeader()
                                (requireActivity() as HomeActivity).showBottomNavigation()
                                binding.refressLayout.isRefreshing = false
                                setParentRecycler(it.data!!.data)
                                appPreference.saveUserType(it?.data?.data!!.contactType)

                                homeData = it!!.data!!.data
                                latestUptaesListCount = it!!.data!!.data.page.totalUpdatesOnListView
                                InsightsListCount = it!!.data!!.data.page.totalInsightsOnListView
                                testimonialsListCount =
                                    it!!.data!!.data.page.totalTestimonialsOnListView
                                appPreference.setPromisesCount(it!!.data!!.data.page.totalPromisesOnHomeScreen)

                                homeViewModel.setHeaderAndList(it!!.data!!.data.page)
                                testimonilalsHeading = it!!.data!!.data.page.testimonialsHeading
                                testimonilalsSubHeading =
                                    it!!.data!!.data.page.testimonialsSubHeading

                                if (it!!.data!!.data.actionItem != null) {
                                    for (item in it!!.data!!.data!!.actionItem) {
                                        actionItemType.add(item)
                                    }
                                }

                                it.data.let {
                                    if (it != null) {
                                        projectId = it.data.page.promotionAndOffersProjectContentId
                                        appPreference.saveOfferId(projectId)
                                        //appPreference.saveOfferUrl(it.data.page.promotionAndOffersMedia.value.url)
                                        homeViewModel.setDashBoardData(it)
                                        appPreference.setFacilityCard(it.data.isFacilityVisible)
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
                                        true
                                    )
                                }

                            }
                            Status.ERROR -> {
                                binding.shimmerLayout.shimmerViewContainer.hide()
                                (requireActivity() as HomeActivity).showErrorToast(
                                    it.message!!
                                )
                                binding.dashBoardRecyclerView.show()
                            }
                        }
                    }

                })

            homeViewModel.getNewNotification(20,1, refresh )
                .observe(viewLifecycleOwner, object : Observer<BaseResponse<NotificationResponse>>{
                    override fun onChanged(it: BaseResponse<NotificationResponse>?) {
                        when (it!!.status){
                            Status.SUCCESS ->{
                                if (it?.data?.data!=null){
                                    var itemList = ArrayList<Int>()
                                    for (i in 0..it.data?.data!!.size - 1) {
                                        if (!it!!.data!!.data[i].readStatus) {
                                            itemList.add(it!!.data!!.data[i].id)
                                        }
                                    }

                                    if(itemList.isEmpty()){
                                        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.notification.setImageDrawable(
                                            resources.getDrawable(R.drawable.normal_notification))
                                    } else{
                                        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.notification.setImageDrawable(
                                            resources.getDrawable(R.drawable.ic_notification))

                                    }
                                }
                            }
                        }
                    }

                })

            homeViewModel.getFacilityManagment()
                .observe(viewLifecycleOwner, Observer {
                    when (it.status) {
                        Status.SUCCESS -> {
                            it!!.data!!.let {
                                if (it != null) {
                                    appPreference.setFmUrl(it?.data?.web_url)

                                } else {
                                    (requireActivity() as HomeActivity).showErrorToast(
                                        Constants.SOMETHING_WENT_WRONG
                                    )
                                }
                            }
                        }
                        Status.ERROR ->{
                            (requireActivity() as HomeActivity).showErrorToast(
                                it.message!!
                            )
                        }
                    }
                })


        } else {
            binding.refressLayout.isRefreshing = false
            binding.shimmerLayout.shimmerViewContainer.hide()
            (requireActivity() as HomeActivity).showHeader()
            (requireActivity() as HomeActivity).showBottomNavigation()
            binding.dashBoardRecyclerView.hide()
            binding.noInternetView.mainContainer.show()
            binding.noInternetView.textView6.setOnClickListener(View.OnClickListener {
                initObserver(true)
                (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.rotateText.hide()
            })
        }
    }


    private val itemClickListener = object : ItemClickListener {
        override fun onItemClicked(view: View, position: Int, item: String) {
            when (view.id) {
                R.id.cv_top_view -> {
                    val fragment = ProjectDetailFragment()
                    val bundle = Bundle()
                    bundle.putInt(Constants.PROJECT_ID, item.toInt())
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(
                        fragment,
                        true
                    )
                }
                R.id.tv_apply_now -> {
                    val fragment = LandSkusFragment()
                    val bundle = Bundle()
                    bundle.putInt(Constants.PROJECT_ID, item.toInt())
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(
                        fragment,
                        true
                    )
                }
                R.id.tv_item_location_info -> {
                    val fragment = ProjectDetailFragment()
                    val bundle = Bundle()
                    bundle.putInt(Constants.PROJECT_ID, item.toInt())
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(
                        fragment,
                        true
                    )
                }
                R.id.iv_bottom_arrow -> {
                    val fragment = ProjectDetailFragment()
                    val bundle = Bundle()
                    bundle.putInt(Constants.PROJECT_ID, item.toInt())
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(
                        fragment,
                        true
                    )
                }
                R.id.home_latest_update_card -> {
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
                        true
                    )
                }
                R.id.home_promises_item -> {
                    val data = homeData?.homePagesOrPromises[position].toHomePagesOrPromise()
                    homeViewModel.setSelectedPromise(data)
                    (requireActivity() as HomeActivity).addFragment(
                        PromisesDetailsFragment(),
                        true
                    )
                }
                R.id.facility_management_card -> {

                    (requireActivity() as HomeActivity).navigate(R.id.navigation_promises)
                }
                R.id.home_insights_card -> {
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
                        true
                    )
                }
                R.id.dont_miss_out_card -> {
                    val bundle = Bundle()
                    bundle.putInt(Constants.PROJECT_ID, projectId)
                    val fragment = ProjectDetailFragment()
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(
                        fragment, true
                    )
                }
                R.id.tv_see_all_update -> {
                    val fragment = LatestUpdatesFragment()
                    (requireActivity() as HomeActivity).addFragment(fragment, true)
                }
                R.id.tv_seeall_insights -> {
                    val fragment = InsightsFragment()
                    (requireActivity() as HomeActivity).addFragment(fragment, true)

                }
                R.id.tv_seeall_promise -> {

                    if(appPreference.isFacilityCard()){
                        val fragment = HoablPromises()
                        (requireActivity() as HomeActivity).addFragment(fragment, true)
                    } else{
                        (requireActivity() as HomeActivity).navigate(R.id.navigation_promises)

                    }

                }
                R.id.tv_seeall_testimonial -> {
                    val fragment = Testimonials()
                    val bundle = Bundle()
                    bundle.putInt(Constants.TESTIMONALS, testimonialsListCount)

                    bundle.putString(Constants.TESTIMONALS_HEADING, testimonilalsHeading)

                    bundle.putString(Constants.TESTIMONALS_SUB_HEADING, testimonilalsSubHeading)
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(fragment, true)
                }
                R.id.tv_viewall_investments -> {
                    (requireActivity() as HomeActivity).navigate(R.id.navigation_investment)
                }
                R.id.btn_refer_now -> {
                    referNow()
                }
                R.id.app_share_view -> {
                    share_app()
                }

                R.id.see_all_pending_payment -> {
                    if (actionItemType[position].actionItemType == 50) {
                        val actionItemData = actionItemType[position]
                        portfolioViewModel =
                            ViewModelProvider(
                                requireActivity(),
                                portfolioFactory
                            )[PortfolioViewModel::class.java]

                        val bjHeader = BJHeader(
                            actionItemData.launchName,
                            actionItemData.address?.let { it.city } + " , " + actionItemData.address?.let { it.state },
                            actionItemData.bookingStatus,
                            actionItemData.primaryOwner,
                            actionItemData.inventoryId
                        )
                        portfolioViewModel.saveBookingHeader(bjHeader)

                        (requireActivity() as HomeActivity).addFragment(
                            BookingjourneyFragment.newInstance(
                                actionItemData.investmentId,
                                ""
                            ), true
                        )

                    } else {
                        (requireActivity() as HomeActivity).navigate(R.id.navigation_profile)
                    }
                }

                R.id.view_portfolio_btn->{
                    (requireActivity() as HomeActivity).navigate(R.id.navigation_portfolio)
                }
            }

        }
    }

    private fun setParentRecycler(
        data: com.emproto.networklayer.response.home.Data,
    ) {

        val list = ArrayList<RecyclerViewItem>()

        homeAdapter = HomeAdapter(requireContext(), data, list, itemClickListener)

        linearLayoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        )

        list.add(RecyclerViewItem(HomeAdapter.HOME_PORTFOLIO))
        list.add(RecyclerViewItem(HomeAdapter.NEW_PROJECT))
        list.add(RecyclerViewItem(HomeAdapter.INCOMPLETED_KYC))
        list.add(RecyclerViewItem(HomeAdapter.LATEST_UPDATES))
        list.add(RecyclerViewItem(HomeAdapter.PROMISES))
        list.add(RecyclerViewItem(HomeAdapter.FACILITY_MANAGMENT))
        list.add(RecyclerViewItem(HomeAdapter.INSIGHTS))
        list.add(RecyclerViewItem(HomeAdapter.TESTIMONIAS))
        list.add(RecyclerViewItem(HomeAdapter.SHARE_APP))

        binding.dashBoardRecyclerView.adapter = homeAdapter
        binding.dashBoardRecyclerView.layoutManager = linearLayoutManager

        binding.refressLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            //binding.loader.show()
            initObserver(refresh = true)
            binding.refressLayout.isRefreshing = false

        })

    }

    private fun initView() {

        (requireActivity() as HomeActivity).hideBackArrow()
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility =
            View.VISIBLE
        (requireActivity() as HomeActivity).showBottomNavigation()

    }

    private fun referNow() {
        val dialog = ReferralDialog()
        dialog.isCancelable = true
        dialog.show(parentFragmentManager, Constants.REFERRAL_CARD)
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
