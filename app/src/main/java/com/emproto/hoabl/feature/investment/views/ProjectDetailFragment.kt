package com.emproto.hoabl.feature.investment.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.core.Constants
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ProjectDetailLayoutBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.chat.views.fragments.ChatsFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.home.views.Mixpanel
import com.emproto.hoabl.feature.home.views.fragments.Testimonials
import com.emproto.hoabl.feature.investment.adapters.ProjectDetailAdapter
import com.emproto.hoabl.feature.investment.dialogs.ApplicationSubmitDialog
import com.emproto.hoabl.feature.investment.dialogs.ConfirmationDialog
import com.emproto.hoabl.feature.investment.views.mediagallery.MediaGalleryFragment
import com.emproto.hoabl.feature.investment.views.mediagallery.YoutubeActivity
import com.emproto.hoabl.feature.promises.HoablPromises
import com.emproto.hoabl.feature.promises.PromisesDetailsFragment
import com.emproto.hoabl.model.MapLocationModel
import com.emproto.hoabl.model.MediaViewItem
import com.emproto.hoabl.model.ProjectLocation
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.utils.Extensions.hideKeyboard
import com.emproto.hoabl.utils.Extensions.toHomePagesOrPromise
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.utils.MapItemClickListener
import com.emproto.hoabl.utils.SimilarInvItemClickListener
import com.emproto.hoabl.utils.YoutubeItemClickListener
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.request.investment.AddInventoryBody
import com.emproto.networklayer.request.investment.VideoCallBody
import com.emproto.networklayer.request.investment.WatchListBody
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.investment.*
import com.emproto.networklayer.response.watchlist.Data
import java.io.Serializable
import javax.inject.Inject


class ProjectDetailFragment : BaseFragment() {

    companion object {
        const val MEDIA_ACTIVE = "1001"
    }

    @Inject
    lateinit var homeFactory: HomeFactory
    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var appPreference: AppPreference

    @Inject
    lateinit var investmentFactory: InvestmentFactory
    lateinit var investmentViewModel: InvestmentViewModel
    private lateinit var binding: ProjectDetailLayoutBinding

    private var projectId = 0
    private lateinit var oppDocData: OpportunityDoc
    private lateinit var mediaData: List<MediaGalleryOrProjectContent>
    private lateinit var coverImages: ProjectCoverImages
    private lateinit var promisesData: List<PmData>
    private lateinit var landSkusData: List<InventoryBucketContent>
    private lateinit var mapLocationData: LocationInfrastructure
    private var watchList = ArrayList<Data>()
    private lateinit var similarInvestments: List<SimilarInvestment>
    private lateinit var allData: PdData

    private var faqData: List<ProjectContentsAndFaq> = mutableListOf()
    private var appUrl = Constants.PLAY_STORE
    private var isBookmarked = false
    private var watchListId = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProjectDetailLayoutBinding.inflate(layoutInflater)
        arguments?.let {
            projectId = it.getInt(Constants.PROJECT_ID, 0)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpInitialization()
        setUpUI()
        callApi()
    }

    private fun setUpInitialization() {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        investmentViewModel =
            ViewModelProvider(requireActivity(), investmentFactory)[InvestmentViewModel::class.java]
        homeViewModel =
            ViewModelProvider(requireActivity(), homeFactory)[HomeViewModel::class.java]
        (requireActivity() as HomeActivity).showHeader()
    }

    private fun setUpUI() {

        (requireActivity() as HomeActivity).showBackArrow()
        (requireActivity() as HomeActivity).hideBottomNavigation()
        hideKeyboard()
        binding.slSwipeRefresh.setOnRefreshListener {
            binding.slSwipeRefresh.isRefreshing = true
            binding.rvProjectDetail.visibility = View.GONE
            callApi()
        }
    }

    private fun callApi() {
        investmentViewModel.getInvestmentsPromises().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.show()
                }
                Status.SUCCESS -> {
                    it.data?.data?.let { data ->
                        promisesData = data
                        callProjectIdApi(promisesData)
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

    private fun callProjectIdApi(promiseData: List<PmData>) {
        investmentViewModel.getInvestmentsDetail(projectId).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBar.hide()
                    binding.rvProjectDetail.visibility = View.VISIBLE
                    it.data?.data?.let { data ->
                        binding.slSwipeRefresh.isRefreshing = false
                        allData = data.projectContent
                        if (data.projectContent.watchlist != null) {
                            for (item in data.projectContent.watchlist) {
                                watchList.add(item)
                            }
                        }
                        oppDocData = data.projectContent.opportunityDoc
                        mediaData = data.projectContent.mediaGalleryOrProjectContent
                        coverImages = data.projectContent.projectCoverImages
                        landSkusData = data.projectContent.inventoryBucketContents
                        faqData = data.projectContentsAndFaqs
                        mapLocationData = data.projectContent.locationInfrastructure
                        for (item in watchList) {
                            if (item.watchlist.projectContentId.toInt() == projectId) {
                                isBookmarked = true
                                watchListId = item.watchlist.id
                                investmentViewModel.setWatchListId(item.watchlist.id)
                            }
                        }
                        investmentViewModel.setImageActive(data.projectContent.mediaGalleryOrProjectContent[0].isImagesActive!!)
                        investmentViewModel.setVideoActive(data.projectContent.mediaGalleryOrProjectContent[0].isVideosActive!!)
                        investmentViewModel.setDroneActive(data.projectContent.mediaGalleryOrProjectContent[0].isDroneShootsActive!!)
                        investmentViewModel.setThreeSixtyActive(data.projectContent.mediaGalleryOrProjectContent[0].isThreeSixtyImagesActive!!)
                        similarInvestments = data.projectContent.similarInvestments
                        setUpRecyclerView(
                            data.projectContent,
                            promiseData,
                            data.projectContentsAndFaqs,
                            data.pageManagementContent
                        )
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


    private fun addWatchList() {
        investmentViewModel.addWatchList(WatchListBody(projectId))
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Status.SUCCESS -> {
                        binding.progressBar.hide()
                        it.data?.let { _ ->
                            Toast.makeText(
                                this.requireContext(),
                                "Project added to watchlist successfully",
                                Toast.LENGTH_SHORT
                            ).show()
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

    private fun eventTrackingWishlist() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(), Mixpanel.WISHLIST)
    }

    private fun setUpRecyclerView(
        data: PdData,
        promisesData: List<PmData>,
        projectContentsAndFaqs: List<ProjectContentsAndFaq>,
        pageManagementContent: PageManagementContent
    ) {
        val list = ArrayList<RecyclerViewItem>()
        list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_PROJECT_DETAIL))
        when (allData.address.isMapDetailsActive) {
            true -> list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_MAP))
            else -> {}
        }
        when (allData.isEscalationGraphActive) {
            true -> {
                list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_PRICE_TRENDS))
            }
            else -> {}
        }
        when (allData.isKeyPillarsActive) {
            true -> {
                list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_KEY_PILLARS))
            }
            else -> {}
        }
        when (allData.isMediaGalleryActive) {
            true -> {
                list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_VIDEO_DRONE))
            }
            else -> {}
        }
        when (allData.isOffersAndPromotionsActive) {
            true -> {
                list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_DONT_MISS))
            }
            else -> {}
        }
        when (allData.isInventoryBucketActive) {
            true -> {
                list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_SKUS))
            }
            else -> {}
        }
        when (allData.opportunityDoc != null && allData.opportunityDoc.isProjectAminitiesActive) {
            true -> {
                list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_AMENITIES))
            }
            else -> {}
        }
        when (allData.isLocationInfrastructureActive) {
            true -> {
                list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_LOCATION_INFRASTRUCTURE))
            }
            else -> {}
        }
        list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_PROMISES))
        when {
            projectContentsAndFaqs.isNotEmpty() -> {
                list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_FAQ))
            }
        }
        list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_TESTIMONIALS))
        when (allData.isSimilarInvestmentActive) {
            true -> {
                if (allData.similarInvestments.isNotEmpty()) {
                    list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_SIMILAR_INVESTMENT))
                }
            }
            else -> {}
        }

        val adapter =
            ProjectDetailAdapter(
                this.requireContext(),
                list,
                data,
                promisesData,
                itemClickListener,
                isBookmarked,
                investmentViewModel,
                videoItemClickListener,
                similarInvItemClickListener,
                mapItemClickListener,
                projectContentsAndFaqs,
                pageManagementContent
            )
        binding.rvProjectDetail.adapter = adapter
        adapter.
        setItemClickListener(onItemClickListener)
    }

    val onItemClickListener =
        View.OnClickListener { view ->
            when (view.id) {
                R.id.bn_ask_here -> {
                    eventTrackingHaveAnyQuestionCard()
                    val bundle = Bundle()
                    val chatsFragment = ChatsFragment()
                    chatsFragment.arguments = bundle
                    (requireActivity() as HomeActivity).replaceFragment(
                        chatsFragment.javaClass, "", true, bundle, null, 0, false
                    )
                }
                R.id.tv_similar_investment_see_all -> {
                    navigateToCategory()
                }
                R.id.iv_share_icon -> {
                    eventTrackingShare()
                    (requireActivity() as HomeActivity).share_app()
                }
                R.id.btn_view_on_map -> {
                    if (isNetworkAvailable()) {
                        eventTrackingViewOnMap()
                        val fragment = MapFragment()
                        val bundle = Bundle()
                        val projectLocation =
                            ProjectLocation(
                                allData.crmProject.lattitude,
                                allData.crmProject.longitude
                            )
                        bundle.putSerializable("ProjectLocation", projectLocation as Serializable)
                        fragment.arguments = bundle
                        investmentViewModel.setMapLocationInfrastructure(mapLocationData)
                        (requireActivity() as HomeActivity).addFragment(fragment, true)
                    } else {
                        Toast.makeText(
                            this.requireContext(),
                            "Network not available",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                R.id.cl_not_convinced_promises -> {
                    callVideoCallApi()
                }
                R.id.iv_see_all_arrow -> {
                    navigateToFaqDetail()
                }
                R.id.tv_faq_read_all -> {
                    eventTrackingFaqReadAll()
                    navigateToFaqDetail()
                }
                R.id.cv_why_invest_card -> {
                    eventTrackingWhyInvestCard()
                    investmentViewModel.setOpportunityDoc(oppDocData)
                    investmentViewModel.setSkus(landSkusData)
                    val fragment = OpportunityDocsFragment()
                    val bundle = Bundle()
                    bundle.putInt(Constants.PROJECT_ID, projectId)
                    bundle.putString(Constants.PROJECT_NAME, allData.launchName)
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(
                        fragment,
                        true
                    )
                }
                R.id.tv_skus_see_all -> {
                    navigateToSkuScreen()
                }
                R.id.tv_video_drone_see_all -> {
                    eventTrackingSeeAllVideoImage()
                    navigateToMediaGallery(true)
                }
                R.id.tv_project_amenities_all -> {
                    eventTrackingSeeAllProjectAmenitites()
                    navigateToOppDoc()
                }
                R.id.iv_share_icon -> {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        "The House Of Abhinandan Lodha $appUrl"
                    )
                    startActivity(shareIntent)
                }
                R.id.tv_promises_see_all -> {
                    if (appPreference.isFacilityCard()) {
                        val fragment = HoablPromises()
                        (requireActivity() as HomeActivity).addFragment(fragment, true)
                    } else {
                        (requireActivity() as HomeActivity).navigate(R.id.navigation_promises)

                    }
                }
                R.id.tv_full_apply_now -> {
                    val fragment = LandSkusFragment()
                    val bundle = Bundle()
                    bundle.putInt(Constants.PROJECT_ID, projectId)
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(fragment, true)
                }
                R.id.tv_apply_now -> {
                    val fragment = LandSkusFragment()
                    val bundle = Bundle()
                    bundle.putInt(Constants.PROJECT_ID, projectId)
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(fragment, true)
                }
                R.id.tv_location_infrastructure_all -> {
                    if (isNetworkAvailable()) {
                        eventTrackingLocationInfraSeeAll()
                        val fragment = MapFragment()
                        val bundle = Bundle()
                        val projectLocation =
                            ProjectLocation(
                                allData.crmProject.lattitude,
                                allData.crmProject.longitude
                            )
                        bundle.putSerializable("ProjectLocation", projectLocation as Serializable)
                        fragment.arguments = bundle
                        investmentViewModel.setMapLocationInfrastructure(mapLocationData)
                        (requireActivity() as HomeActivity).addFragment(fragment, true)
                    } else {
                        Toast.makeText(
                            this.requireContext(),
                            "Network not available",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }

    private fun eventTrackingLocationInfraSeeAll() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(), Mixpanel.LOCATIONINFRASEEALL)
    }

    private fun eventTrackingSeeAllProjectAmenitites() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(), Mixpanel.PROJECTAMENITITIESSEEALL)
    }

    private fun eventTrackingFaqReadAll() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(), Mixpanel.FAQREADALL)
    }

    private fun eventTrackingHaveAnyQuestionCard() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(), Mixpanel.HAVEANYQUESTIONCARD)
    }

    private fun eventTrackingSeeAllVideoImage() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(), Mixpanel.SEEALLVIDEOIMAGE)
    }

    private fun eventTrackingWhyInvestCard() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(), Mixpanel.WHYINVESTCARD)
    }

    private fun eventTrackingShare() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(), Mixpanel.INVESTMENTSHARE)
    }

    private fun eventTrackingViewOnMap() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(), Mixpanel.VIEWONMAP)

    }

    private fun navigateToCategory() {
        val list = CategoryListFragment()
        val bundle = Bundle()
        val priorityList = similarInvestments.sortedBy {
            it.priority
        }
        bundle.putString("Category", "SimilarInvestments")
        bundle.putSerializable("SimilarInvestmentsData", priorityList as Serializable)
        list.arguments = bundle
        (requireActivity() as HomeActivity).addFragment(list, true)
    }

    private fun navigateToFaqDetail() {
        val fragment = FaqDetailFragment()
        val bundle = Bundle()
        bundle.putInt(Constants.PROJECT_ID, projectId)
        bundle.putBoolean(Constants.IS_FROM_INVESTMENT, true)
        bundle.putString(Constants.PROJECT_NAME, allData.launchName)
        fragment.arguments = bundle
        (requireActivity() as HomeActivity).addFragment(fragment, true)
    }

    private fun navigateToOppDoc() {
        investmentViewModel.setOpportunityDoc(oppDocData)
        investmentViewModel.setSkus(landSkusData)
        val fragment = OpportunityDocsFragment()
        val bundle = Bundle()
        bundle.putInt(Constants.PROJECT_ID, projectId)
        bundle.putString(Constants.PROJECT_NAME, allData.launchName)
        bundle.putBoolean(Constants.IS_PROJECT_AMENITIES_CLICKED, true)
        fragment.arguments = bundle
        (requireActivity() as HomeActivity).addFragment(
            fragment,
            true
        )
    }

    private fun navigateToMediaGallery(isVideoAllCLicked: Boolean) {
        val imagesList = ArrayList<MediaViewItem>()
        var itemId = 0
        for (i in mediaData.indices) {
            for (item in mediaData[i].droneShoots!!) {
                if (item.status == MEDIA_ACTIVE) {
                    itemId++
                    imagesList.add(
                        MediaViewItem(
                            item.mediaContentType,
                            item.mediaContent.value.url,
                            title = Constants.DRONE_SHOOT,
                            id = itemId,
                            name = item.name
                        )
                    )
                }
            }
            for (item in mediaData[i].images!!) {
                if (item.status == MEDIA_ACTIVE) {
                    itemId++
                    imagesList.add(
                        MediaViewItem(
                            item.mediaContentType,
                            item.mediaContent.value.url,
                            title = "Images",
                            id = itemId,
                            name = item.name
                        )
                    )
                }
            }
            for (item in mediaData[i].videos!!) {
                if (item.status == MEDIA_ACTIVE) {
                    itemId++
                    imagesList.add(
                        MediaViewItem(
                            item.mediaContentType,
                            item.mediaContent.value.url,
                            title = Constants.VIDEOS,
                            id = itemId,
                            name = item.name
                        )
                    )
                }
            }
            for (item in mediaData[i].threeSixtyImages!!) {
                if (item.status == MEDIA_ACTIVE) {
                    itemId++
                    imagesList.add(
                        MediaViewItem(
                            item.mediaContentType,
                            item.mediaContent.value.url,
                            title = "ThreeSixtyImages",
                            id = itemId,
                            name = item.name
                        )
                    )
                }
            }
            //Adding cover Images
            itemId++
            imagesList.add(
                MediaViewItem(
                    coverImages.newInvestmentPageMedia.value.mediaType,
                    coverImages.newInvestmentPageMedia.value.url,
                    title = "Images",
                    id = itemId,
                    name = allData.launchName
                )
            )
        }
        val fragment = MediaGalleryFragment()
        val bundle = Bundle()
        bundle.putSerializable(Constants.DATA, imagesList)
        investmentViewModel.isVideoSeeAllClicked = isVideoAllCLicked
        fragment.arguments = bundle
        (requireActivity() as HomeActivity).addFragment(fragment, true)
    }

    private fun callVideoCallApi() {
        investmentViewModel.scheduleVideoCall(
            VideoCallBody(
                caseType = "1004",
                description = "I want to know more about ${allData.launchName}",
                issueType = "Schedule a video call",
                projectId = projectId
            )
        ).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.data?.let { _ ->
                        val applicationSubmitDialog = ApplicationSubmitDialog(
                            "Video Call request sent successfully.",
                            "Our Project Manager will reach out to you soon!",
                            false
                        )
                        applicationSubmitDialog.show(
                            parentFragmentManager,
                            Constants.APPLICATION_SUBMIT_DIALOG
                        )
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

    private val mapItemClickListener = object : MapItemClickListener {
        override fun onItemClicked(view: View, position: Int, latitude: Double, longitude: Double) {
            when (view.id) {
                R.id.cv_location_infrastructure_card -> {
                    investmentViewModel.setMapLocationInfrastructure(mapLocationData)
                    val bundle = Bundle()
                    bundle.putInt("ItemPosition", position)
                    bundle.putSerializable(
                        "Location",
                        MapLocationModel(
                            allData.crmProject.lattitude.toDouble(),
                            allData.crmProject.longitude.toDouble(),
                            latitude,
                            longitude
                        ) as Serializable
                    )
                    val projectLocation =
                        ProjectLocation(allData.crmProject.lattitude, allData.crmProject.longitude)
                    bundle.putSerializable("ProjectLocation", projectLocation as Serializable)
                    if (isNetworkAvailable()) {
                        val fragment = MapFragment()
                        fragment.arguments = bundle
                        (requireActivity() as HomeActivity).addFragment(
                            fragment,
                            true
                        )
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Network not available",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private val similarInvItemClickListener = object : SimilarInvItemClickListener {
        override fun onItemClicked(view: View, position: Int, item: String) {
            when (view.id) {
                R.id.cv_top_view -> navigateToDetailScreen(item.toInt())
                R.id.tv_item_location_info -> navigateToDetailScreen(item.toInt())
                R.id.iv_bottom_arrow -> navigateToDetailScreen(item.toInt())
                R.id.tv_apply_now -> navigateToSkuScreen()
                R.id.cl_item_info -> navigateToDetailScreen(item.toInt())

            }
        }
    }

    private fun navigateToDetailScreen(item: Int) {
        val bundle = Bundle()
        bundle.putInt(Constants.PROJECT_ID, item)
        val fragment = ProjectDetailFragment()
        fragment.arguments = bundle
        (requireActivity() as HomeActivity).addFragment(
            fragment, true
        )
    }

    private val itemClickListener = object : ItemClickListener {
        override fun onItemClicked(view: View, position: Int, item: String) {
            when (view.id) {
                R.id.tv_hear_speak_see_all -> {
                    val fragment = Testimonials()
                    val bundle = Bundle()
                    bundle.putInt(Constants.TESTIMONALS, item.toInt())
                    bundle.putString(
                        Constants.TESTIMONALS_HEADING,
                        allData.otherSectionHeadings.testimonials.sectionHeading
                    )
                    bundle.putString(
                        Constants.TESTIMONALS_SUB_HEADING,
                        allData.otherSectionHeadings.testimonials.subHeading
                    )
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(fragment, true)
                }
                R.id.tv_apply -> {
                    openDialog()
                }
                R.id.item_card -> {
                    if (promisesData[position] != null) {
                        val promiseData = promisesData[position].toHomePagesOrPromise()
                        homeViewModel.setSelectedPromise(promiseData)
                        (requireActivity() as HomeActivity).addFragment(
                            PromisesDetailsFragment(),
                            true
                        )
                    }
                }
                R.id.iv_bookmark_icon -> {
                    when (item) {
                        Constants.TRUE -> addWatchList()
                        Constants.FALSE -> deleteWatchList()
                    }
                }
                R.id.cv_faq_card -> {
                    eventTrackingFaqCard()
                    val fragment = FaqDetailFragment()
                    val bundle = Bundle()
                    bundle.putInt(Constants.PROJECT_ID, projectId)
                    bundle.putInt(Constants.FAQ_ID, item.toInt())
                    bundle.putBoolean(Constants.IS_FROM_INVESTMENT, true)
                    bundle.putString(Constants.PROJECT_NAME, allData.launchName)
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(fragment, true)
                }
                R.id.cl_see_all -> {
                    navigateToMediaGallery(false)
                }
            }
            when (item) {
                "Yes" -> {
                    investmentViewModel.addInventory(
                        AddInventoryBody(
                            inventoryBucketId = position,
                            launchPhaseId = projectId
                        )
                    ).observe(viewLifecycleOwner) {
                        when (it.status) {
                            Status.LOADING -> {
                                binding.progressBar.show()
                            }
                            Status.SUCCESS -> {
                                binding.progressBar.hide()
                                it.data?.let { _ ->
                                    val applicationSubmitDialog = ApplicationSubmitDialog(
                                        "Thank you for your interest!",
                                        "Our Project Manager will reach out to you in 24 hours!"
                                    )
                                    applicationSubmitDialog.show(
                                        parentFragmentManager,
                                        Constants.APPLICATION_SUBMIT_DIALOG
                                    )
                                    callApi()
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
        }
    }

    private fun eventTrackingFaqCard() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(),Mixpanel.FAQCARD)
    }


    private val videoItemClickListener = object : YoutubeItemClickListener {
        override fun onItemClicked(view: View, position: Int, url: String, title: String) {
            when (view.id) {
                R.id.iv_latest_image -> {
                    eventTrackingVideoImageCard()
                    val intent = Intent(
                        this@ProjectDetailFragment.requireActivity(),
                        YoutubeActivity::class.java
                    )
                    intent.putExtra(Constants.YOUTUBE_VIDEO_ID, url)
                    intent.putExtra(Constants.VIDEO_TITLE, title)
                    startActivity(intent)
                }
            }
        }
    }

    private fun eventTrackingVideoImageCard() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(), Mixpanel.VIDEOIMAGECARD)
    }

    private fun navigateToSkuScreen() {
        eventTrackingApply()
        val fragment = LandSkusFragment()
        val bundle = Bundle()
        bundle.putInt(Constants.PROJECT_ID, projectId)
        fragment.arguments = bundle
        (requireActivity() as HomeActivity).addFragment(fragment, true)
    }

    private fun eventTrackingApply() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(), Mixpanel.INVESTMENTNEWLAUNCHAPPLYNOW)
    }

    private fun openDialog() {
        val confirmationDialog = ConfirmationDialog(investmentViewModel, itemClickListener)
        confirmationDialog.show(parentFragmentManager, "ConfirmationDialog")
    }

    private fun deleteWatchList() {
        investmentViewModel.getWatchListId().observe(viewLifecycleOwner) {
            if (it != null) {
                investmentViewModel.deleteWatchList(it).observe(viewLifecycleOwner) { it ->
                    when (it.status) {
                        Status.LOADING -> {
                            binding.progressBar.show()
                        }
                        Status.SUCCESS -> {
                            binding.progressBar.hide()
                            it.data?.let { _ ->
                                Toast.makeText(
                                    this.requireContext(),
                                    "Project removed from watchlist successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
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
    }
}