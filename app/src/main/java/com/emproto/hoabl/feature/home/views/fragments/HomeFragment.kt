package com.emproto.hoabl.feature.home.views.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseFragment
import com.emproto.core.Constants
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentHomeBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.chat.views.fragments.ChatsFragment
import com.emproto.hoabl.feature.home.adapters.HomeAdapter
import com.emproto.hoabl.feature.home.data.LatesUpdatesPosition
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.home.views.Mixpanel
import com.emproto.hoabl.feature.investment.views.LandSkusFragment
import com.emproto.hoabl.feature.investment.views.ProjectDetailFragment
import com.emproto.hoabl.feature.portfolio.views.BookingJourneyFragment
import com.emproto.hoabl.feature.promises.HoablPromises
import com.emproto.hoabl.feature.promises.PromisesDetailsFragment
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
import com.emproto.networklayer.response.bookingjourney.BJHeader
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.home.MastheadSection
import com.emproto.networklayer.response.home.PageManagementsOrNewInvestment
import com.emproto.networklayer.response.marketingUpdates.Data
import javax.inject.Inject
import kotlin.collections.ArrayList


class HomeFragment : BaseFragment() {

    lateinit var binding: FragmentHomeBinding
    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var homeAdapter: HomeAdapter

    private lateinit var homeData: com.emproto.networklayer.response.home.Data

    private val appURL = Constants.PLAY_STORE
    private var projectId = 0


    @Inject
    lateinit var factory: HomeFactory
    lateinit var investmentFactory: InvestmentFactory
    lateinit var homeViewModel: HomeViewModel
    val list = ArrayList<PageManagementsOrNewInvestment>()
    private var latestUpdatesListCount: Int = 0
    private var insightsListCount: Int = 0
    var testimonialsListCount: Int = 0

    lateinit var testimonialsHeading: String
    lateinit var testimonialsSubHeading: String
    private var actionItemType = ArrayList<com.emproto.networklayer.response.actionItem.Data>()
    private final var state = IntArray(1)

    private var topText = ""

    @Inject
    lateinit var portfolioFactory: PortfolioFactory
    lateinit var portfolioViewModel: PortfolioViewModel

    @Inject
    lateinit var appPreference: AppPreference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel = ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]

        appPreference.setFmUrl("")
        initObserver(refresh = false)
        initView()

        return binding.root
    }


    private fun initObserver(refresh: Boolean) {

        if (isNetworkAvailable()) {
            getDashBoardData(refresh)

            getNewNotification(refresh)

        } else {
            noNetworkState()
        }


    }

    private fun getDashBoardData(refresh: Boolean) {

        homeViewModel.getDashBoardData(ModuleEnum.HOME.value, refresh)
            .observe(
                viewLifecycleOwner
            ) { it ->
                when (it!!.status) {
                    Status.LOADING -> {
                        loadingState()
                    }
                    Status.SUCCESS -> {
                        successState()
                        setParentRecycler(it.data!!.data)
                        appPreference.saveUserType(it?.data?.data!!.contactType)
                        setMastheadSection(it?.data?.data?.page?.mastheadSection!!)

                        homeData = it!!.data!!.data
                        latestUpdatesListCount = it!!.data!!.data.page.totalUpdatesOnListView
                        insightsListCount = it!!.data!!.data.page.totalInsightsOnListView
                        testimonialsListCount =
                            it!!.data!!.data.page.totalTestimonialsOnListView
                        appPreference.setPromisesCount(it!!.data!!.data.page.totalPromisesOnHomeScreen)

                        appPreference.setFacilityManagementUrl(it!!.data!!.data.page.facilityManagement.value.url)


                        homeViewModel.setHeaderAndList(it!!.data!!.data.page)
                        testimonialsHeading = it!!.data!!.data.page.testimonialsHeading
                        testimonialsSubHeading =
                            it!!.data!!.data.page.testimonialsSubHeading

                        if (it.data?.data?.actionItem != null) {
                            for (item in it!!.data!!.data!!.actionItem) {
                                actionItemType.add(item)
                            }
                        }

                        it.data.let {
                            if (it != null) {
                                projectId = it.data.page.promotionAndOffersProjectContentId
                                appPreference.saveOfferId(projectId)
                                appPreference.saveOfferUrl(it.data.page.promotionAndOffersMedia.value.url)
                                homeViewModel.setDashBoardData(it)
                                appPreference.setFacilityCard(it.data.isFacilityVisible)
                            }
                        }

                        chatNavigation()

                        if (appPreference.isFacilityCard() && it.data?.data!!.facilityUrl != null) {
                            val item =
                                (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.menu.getItem(
                                    3
                                )
                            item.title = getString(R.string.my_services)
                            item.setIcon(R.drawable.ic_access_card)
                            appPreference.setFmUrl(it.data?.data!!.facilityUrl)

                        } else if (appPreference.isFacilityCard() && it.data?.data!!.facilityUrl == null) {
                            val item =
                                (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.menu.getItem(
                                    3
                                )
                            item.title = getString(R.string.my_services)
                            item.setIcon(R.drawable.ic_access_card)
                            homeViewModel.getFacilityManagment()
                                .observe(viewLifecycleOwner) { it ->
                                    when (it.status) {
                                        Status.SUCCESS -> {
                                            it!!.data!!.let {
                                                if (it != null && it.data.web_url != null) {
                                                    appPreference.setFmUrl(it.data.web_url)
                                                } else {
                                                    (requireActivity() as HomeActivity).showErrorToast(
                                                        Constants.NO_FM_URL
                                                    )
                                                }
                                            }
                                        }
                                        Status.ERROR -> {
                                            when (it.message) {
                                                Constants.ACCESS_DENIED -> {
                                                    (requireActivity() as HomeActivity).showErrorToast(
                                                        it.message!!
                                                    )
                                                    (requireActivity() as HomeActivity).logoutFromAllDevice()
                                                }
                                                else -> {
                                                    (requireActivity() as HomeActivity).showErrorToast(
                                                        it.message!!
                                                    )
                                                }
                                            }
                                        }
                                        Status.LOADING -> {}
                                    }
                                }
                        }


                    }
                    Status.ERROR -> {
                        when (it.message) {
                            Constants.ACCESS_DENIED -> {
                                (requireActivity() as HomeActivity).showErrorToast(it.message!!)
                                (requireActivity() as HomeActivity).logoutFromAllDevice()
                            }
                            else -> {
                                (requireActivity() as HomeActivity).showErrorToast(it.message!!)
                                binding.shimmerLayout.shimmerViewContainer.hide()
                                binding.dashBoardRecyclerView.show()
                            }
                        }
                    }
                }
            }
    }

    private fun setMastheadSection(data: MastheadSection) {

        var totalLandsold = ""
        var totalAmtLandSold = ""
        var grossWeight = ""
        var num_User = ""

        data.let {

            if (it.totalSqftOfLandTransacted.shouldDisplay) {
                totalLandsold = String.format(
                    getString(R.string.header),
                    it.totalSqftOfLandTransacted.displayName,
                    it.totalSqftOfLandTransacted.value
                )
            }


            if (it.totalAmoutOfLandTransacted.shouldDisplay) {
                totalAmtLandSold = String.format(
                    getString(R.string.header),
                    it.totalAmoutOfLandTransacted?.displayName,
                    it.totalAmoutOfLandTransacted?.value
                )
            }

            if (it.grossWeightedAvgAppreciation?.shouldDisplay) {
                grossWeight = String.format(
                    getString(R.string.header),
                    it.grossWeightedAvgAppreciation?.displayName,
                    it.grossWeightedAvgAppreciation?.value
                )
            }

            if (it.totalNumberOfUsersWhoBoughtTheLand.shouldDisplay) {
                num_User = String.format(
                    getString(R.string.header),
                    it.totalNumberOfUsersWhoBoughtTheLand.displayName,
                    it.totalNumberOfUsersWhoBoughtTheLand.value
                )
            }

            (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.rotateText.text =
                showHTMLText(
                    "$totalLandsold    $totalAmtLandSold    $grossWeight    $num_User"
                )
            topText = showHTMLText(
                "$totalLandsold   $totalAmtLandSold   $grossWeight    $num_User"
            ).toString()
        }
    }

    private fun loadingState() {
        binding.dashBoardRecyclerView.hide()
        binding.noInternetView.mainContainer.hide()
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.rotateText.hide()
        (requireActivity() as HomeActivity).hideHeader()
        (requireActivity() as HomeActivity).hideBottomNavigation()
        binding.shimmerLayout.shimmerViewContainer.show()

    }

    private fun successState() {
        binding.dashBoardRecyclerView.show()
        binding.noInternetView.mainContainer.hide()
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.rotateText.show()
        binding.shimmerLayout.shimmerViewContainer.hide()
        (requireActivity() as HomeActivity).showHeader()
        (requireActivity() as HomeActivity).showBottomNavigation()
        binding.refressLayout.isRefreshing = false
        Handler().postDelayed({
            (requireActivity() as HomeActivity).createTourGuide()
            true
        }, 500)


    }

    private fun chatNavigation() {
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

    private fun noNetworkState() {
        binding.refressLayout.isRefreshing = false
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.rotateText.hide()
        binding.shimmerLayout.shimmerViewContainer.hide()
        (requireActivity() as HomeActivity).showHeader()
        (requireActivity() as HomeActivity).showBottomNavigation()
        binding.dashBoardRecyclerView.hide()
        binding.noInternetView.mainContainer.show()
        binding.noInternetView.textView6.setOnClickListener {
            initObserver(true)
        }
//        (requireActivity() as HomeActivity).createTourGuide()

    }

    private fun getNewNotification(refresh: Boolean) {
        homeViewModel.getNewNotification(20, 1, refresh)
            .observe(
                viewLifecycleOwner
            ) {
                when (it!!.status) {
                    Status.SUCCESS -> {
                        if (it?.data?.data != null) {
                            val itemList = ArrayList<Int>()
                            for (i in 0 until it.data?.data!!.size) {
                                if (!it!!.data!!.data[i].readStatus) {
                                    itemList.add(it!!.data!!.data[i].id)
                                }
                            }
                            if (itemList.isEmpty()) {
                                (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.notification.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        requireContext(),
                                        R.drawable.ic_notification_inactive
                                    )
                                )
                            } else {
                                (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.notification.setImageDrawable(
                                    context?.let { it1 ->
                                        ContextCompat.getDrawable(
                                            it1,
                                            R.drawable.ic_notification
                                        )
                                    }
                                )

                            }
                        }
                    }
                    Status.ERROR -> {
                        when (it.message) {
                            Constants.ACCESS_DENIED -> {
                                (requireActivity() as HomeActivity).showErrorToast(it.message!!)
                                (requireActivity() as HomeActivity).logoutFromAllDevice()
                            }
                            else -> {
                                (requireActivity() as HomeActivity).showErrorToast(it.message!!)
                            }
                        }
                    }
                    Status.LOADING -> {}
                }
            }
    }

    private val itemClickListener = object : ItemClickListener {
        override fun onItemClicked(
            view: View,
            position: Int,
            item: String) {
            when (view.id) {
                R.id.cv_top_view -> {
                    eventTrackingProjectCard()
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
                    trackApplyNow()
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
                    eventTrackinghomelatestupdatecard()
                    val convertedData =
                        homeData?.pageManagementOrLatestUpdates[position].toData()
                    val list = ArrayList<Data>()
                    for (item in (homeData!!.pageManagementOrLatestUpdates)) {
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
                    eventTrackingPromisesCard()
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
                    eventTrackingCard()
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
                    eventTrackingDontMiss()
                    val bundle = Bundle()
                    bundle.putInt(Constants.PROJECT_ID, projectId)
                    val fragment = ProjectDetailFragment()
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(
                        fragment, true
                    )
                }
                R.id.tv_see_all_update -> {
                    eventTrackingSeeAllUpdates()
                    val fragment = LatestUpdatesFragment()
                    (requireActivity() as HomeActivity).addFragment(fragment, true)
                }
                R.id.tv_seeall_insights -> {
                    eventTrackingInsightsCard()
                    val fragment = InsightsFragment()
                    (requireActivity() as HomeActivity).addFragment(fragment, true)

                }
                R.id.tv_seeall_promise -> {
                    eventTrackingSeeAllPromises()
                    if (appPreference.isFacilityCard()) {
                        val fragment = HoablPromises()
                        (requireActivity() as HomeActivity).addFragment(fragment, true)
                    } else {
                        (requireActivity() as HomeActivity).navigate(R.id.navigation_promises)

                    }

                }
                R.id.tv_seeall_testimonial -> {
                    eventTrackingseealltestimonial()
                    val fragment = Testimonials()
                    val bundle = Bundle()
                    bundle.putInt(Constants.TESTIMONALS, testimonialsListCount)

                    bundle.putString(Constants.TESTIMONALS_HEADING, testimonialsHeading)

                    bundle.putString(Constants.TESTIMONALS_SUB_HEADING, testimonialsSubHeading)
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(fragment, true)
                }
                R.id.tv_viewall_investments -> {
                    eventTrackingViewAllInvestments()
                    (requireActivity() as HomeActivity).navigate(R.id.navigation_investment)
                }
                R.id.btn_refer_now -> {
                    eventTrackingReferNow()
                    referNow()
                }
                R.id.app_share_view -> {
                    eventTrackingShare()
                    shareApp()
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
                            actionItemData.address?.let { it.city } + "," + actionItemData.address?.let { it.state },
                            actionItemData.bookingStatus,
                            actionItemData.primaryOwner,
                            actionItemData.inventoryId
                        )
                        portfolioViewModel.saveBookingHeader(bjHeader)

                        (requireActivity() as HomeActivity).addFragment(
                            BookingJourneyFragment.newInstance(
                                actionItemData.investmentId,
                                ""
                            ), true
                        )

                    } else {
                        (requireActivity() as HomeActivity).navigate(R.id.navigation_profile)
                    }
                }

                R.id.upload_kyc_statement -> {

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

                            BookingJourneyFragment.newInstance(
                                actionItemData.investmentId,
                                ""
                            ), true
                        )

                    } else {
                        (requireActivity() as HomeActivity).navigate(R.id.navigation_profile)
                    }
                }

                R.id.view_portfolio_btn -> {
                    (requireActivity() as HomeActivity).navigate(R.id.navigation_portfolio)
                }
            }

        }
    }

    private fun eventTrackinghomelatestupdatecard() {
        Mixpanel(requireContext()).identifyFunction(
            appPreference.getMobilenum(),
            Mixpanel.LATESTUPDATECARD
        )
    }

    private fun eventTrackingCard() {
        Mixpanel(requireContext()).identifyFunction(
            appPreference.getMobilenum(),
            Mixpanel.INSIGHTSCARD
        )
    }

    private fun eventTrackingseealltestimonial() {
        Mixpanel(requireContext()).identifyFunction(
            appPreference.getMobilenum(),
            Mixpanel.TESTIMONIALSEEALL
        )
    }

    private fun eventTrackingInsightsCard() {
        Mixpanel(requireContext()).identifyFunction(
            appPreference.getMobilenum(),
            Mixpanel.SEEALLINSIGHTS
        )
    }

    private fun eventTrackingSeeAllPromises() {
        Mixpanel(requireContext()).identifyFunction(
            appPreference.getMobilenum(),
            Mixpanel.SEEALLPROMISES
        )
    }

    private fun eventTrackingSeeAllUpdates() {
        Mixpanel(requireContext()).identifyFunction(
            appPreference.getMobilenum(),
            Mixpanel.SEEALLUPDATES
        )

    }

    private fun eventTrackingViewAllInvestments() {
        Mixpanel(requireContext()).identifyFunction(
            appPreference.getMobilenum(),
            Mixpanel.VIEWALLPROPERTIES
        )
    }

    private fun eventTrackingProjectCard() {
        Mixpanel(requireContext()).identifyFunction(
            appPreference.getMobilenum(),
            Mixpanel.PROJECTCARD
        )
    }

    private fun trackApplyNow() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(), Mixpanel.APPLYNOW)
    }

    private fun eventTrackingPromisesCard() {
        Mixpanel(requireContext()).identifyFunction(
            appPreference.getMobilenum(),
            Mixpanel.PROMISESCARD
        )
    }


    private fun eventTrackingDontMiss() {
        Mixpanel(requireContext()).identifyFunction(
            appPreference.getMobilenum(),
            Mixpanel.DONTMISSOUT
        )
    }

    private fun eventTrackingShare() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(), Mixpanel.SHARE)
    }

    private fun eventTrackingReferNow() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(), Mixpanel.REFERNOW)
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
        list.add(RecyclerViewItem(HomeAdapter.INCOMPLETE_KYC))
        list.add(RecyclerViewItem(HomeAdapter.LATEST_UPDATES))
        list.add(RecyclerViewItem(HomeAdapter.PROMISES))
        list.add(RecyclerViewItem(HomeAdapter.FACILITY_MANAGEMENT))
        list.add(RecyclerViewItem(HomeAdapter.INSIGHTS))
        list.add(RecyclerViewItem(HomeAdapter.TESTIMONIALS))
        list.add(RecyclerViewItem(HomeAdapter.SHARE_APP))

        binding.dashBoardRecyclerView.adapter = homeAdapter
        binding.dashBoardRecyclerView.layoutManager = linearLayoutManager

        binding.refressLayout.setOnRefreshListener {
            initObserver(refresh = true)
            binding.refressLayout.isRefreshing = false

        }

    }

    private fun initView() {

        (requireActivity() as HomeActivity).hideBackArrow()
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility =
            View.VISIBLE
        (requireActivity() as HomeActivity).showBottomNavigation()

        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.search.setOnClickListener {
            val fragment = SearchResultFragment()
            val bundle = Bundle()
            bundle.putString(Constants.TOP_TEXT, topText)
            fragment.arguments = bundle
            (requireActivity() as HomeActivity).addFragment(fragment, true)
        }

    }

    private fun referNow() {
        val dialog = ReferralDialog()
        dialog.isCancelable = true
        dialog.show(parentFragmentManager, Constants.REFERRAL_CARD)
    }

    private fun shareApp() {
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
