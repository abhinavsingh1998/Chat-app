package com.emproto.hoabl.feature.investment.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ProjectDetailLayoutBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.chat.views.fragments.ChatsFragment
import com.emproto.hoabl.feature.home.views.fragments.Testimonials
import com.emproto.hoabl.feature.investment.adapters.ProjectDetailAdapter
import com.emproto.hoabl.feature.investment.dialogs.ApplicationSubmitDialog
import com.emproto.hoabl.feature.investment.dialogs.ConfirmationDialog
import com.emproto.hoabl.feature.investment.views.mediagallery.MediaGalleryFragment
import com.emproto.hoabl.feature.investment.views.mediagallery.YoutubeActivity
import com.emproto.hoabl.feature.promises.PromisesDetailsFragment
import com.emproto.hoabl.model.MapLocationModel
import com.emproto.hoabl.model.MediaViewItem
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.utils.Extensions.toHomePagesOrPromise
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.utils.MapItemClickListener
import com.emproto.hoabl.utils.SimilarInvItemClickListener
import com.emproto.hoabl.utils.YoutubeItemClickListener
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import com.emproto.networklayer.request.investment.AddInventoryBody
import com.emproto.networklayer.request.investment.VideoCallBody
import com.emproto.networklayer.request.investment.WatchListBody
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.investment.*
import com.emproto.networklayer.response.watchlist.Data
import java.io.Serializable
import javax.inject.Inject


class ProjectDetailFragment : BaseFragment() {

    @Inject
    lateinit var homeFactory: HomeFactory
    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var investmentFactory: InvestmentFactory
    lateinit var investmentViewModel: InvestmentViewModel
    private lateinit var binding: ProjectDetailLayoutBinding

    private var projectId = 0
    private lateinit var oppDocData: OpprotunityDoc
    private lateinit var mediaData: List<MediaGalleryOrProjectContent>
    private lateinit var promisesData: List<PmData>
    private lateinit var landSkusData: List<InventoryBucketContent>
    private lateinit var mapLocationData: LocationInfrastructure
    private var watchList: MutableList<Data> = mutableListOf()
    private lateinit var inventoryList: List<Inventory>
    private lateinit var similarInvestments: List<SimilarInvestment>
    private lateinit var allData: PdData

    private var faqData: List<ProjectContentsAndFaq> = mutableListOf()
    private var APP_URL = "https://www.google.com/"
    private var isBookmarked = false
    private var watchListId = 0
    private var isImagesActive = false
    private var isVideosActive = false
    private var isDroneActive = false
    private var isThreeSixtyActive = false

    val onItemClickListener =
        View.OnClickListener { view ->
            when (view.id) {
                R.id.bn_ask_here -> {
                    val bundle = Bundle()
                    val chatsFragment = ChatsFragment()
                    chatsFragment.arguments = bundle
                    (requireActivity() as HomeActivity).replaceFragment(
                        chatsFragment.javaClass, "", true, bundle, null, 0, false
                    )
                }
//                R.id.iv_similar_inv_arrow -> {
//                    navigateToCategory()
//                }
                R.id.tv_similar_investment_see_all -> {
                    navigateToCategory()
                }
                R.id.btn_view_on_map -> {
                    investmentViewModel.setMapLocationInfrastructure(mapLocationData)
                    (requireActivity() as HomeActivity).addFragment(MapFragment(), false)
                }
                R.id.cl_not_convinced_promises -> {
                    callVideoCallApi()
                }
                R.id.iv_see_all_arrow -> {
                    navigateToFaqDetail()
                }
                R.id.tv_faq_read_all -> {
                    navigateToFaqDetail()
                }
                R.id.cv_why_invest_card -> {
                    investmentViewModel.setOpportunityDoc(oppDocData)
                    investmentViewModel.setSkus(landSkusData)
                    val fragment = OpportunityDocsFragment()
                    val bundle = Bundle()
                    bundle.putInt("ProjectId", projectId)
                    bundle.putString("ProjectName", allData.launchName)
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(
                        fragment,
                        false
                    )
                }
                /*R.id.iv_skus_arrow -> {
                    navigateToSkuScreen()
                }*/
                R.id.tv_skus_see_all -> {
                    navigateToSkuScreen()
                }
                /*   R.id.iv_video_drone_arrow -> {
                       navigateToMediaGallery(true)
                   }*/
                R.id.tv_video_drone_see_all -> {
                    navigateToMediaGallery(true)
                }
               /* R.id.iv_project_amenities_arrow -> {
                    navigateToOppDoc()
                }*/
                R.id.tv_project_amenities_all -> {
                    navigateToOppDoc()
                }
                R.id.iv_share_icon -> {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        "The House Of Abhinandan Lodha $APP_URL"
                    )
                    startActivity(shareIntent)
                }
                R.id.tv_promises_see_all -> {
                    (requireActivity() as HomeActivity).navigate(R.id.navigation_promises)
                }
               /* R.id.iv_promises_arrow -> {
                    (requireActivity() as HomeActivity).navigate(R.id.navigation_promises)
                }*/
                R.id.tv_full_apply_now -> {
                    val fragment = LandSkusFragment()
                    val bundle = Bundle()
                    bundle.putInt("ProjectId", projectId)
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(fragment, false)
                }
                R.id.tv_apply_now -> {
                    val fragment = LandSkusFragment()
                    val bundle = Bundle()
                    bundle.putInt("ProjectId", projectId)
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(fragment, false)
                }
                /*R.id.iv_location_infrastructure_arrow -> {
                    investmentViewModel.setMapLocationInfrastructure(mapLocationData)
                    (requireActivity() as HomeActivity).addFragment(MapFragment(), false)
                }*/
                R.id.tv_location_infrastructure_all -> {
                    investmentViewModel.setMapLocationInfrastructure(mapLocationData)
                    (requireActivity() as HomeActivity).addFragment(MapFragment(), false)
                }
            }
        }

    private fun navigateToCategory() {
        val list = CategoryListFragment()
        val bundle = Bundle()
        bundle.putString("Category", "SimilarInvestments")
        bundle.putSerializable("SimilarInvestmentsData", similarInvestments as Serializable)
        list.arguments = bundle
        (requireActivity() as HomeActivity).addFragment(list, false)
    }

    private fun navigateToFaqDetail() {
        val fragment = FaqDetailFragment()
        val bundle = Bundle()
        bundle.putInt("ProjectId", projectId)
        bundle.putBoolean("isFromInvestment", true)
        bundle.putString("ProjectName", allData.launchName)
        fragment.arguments = bundle
        (requireActivity() as HomeActivity).addFragment(fragment, false)
    }

    private fun navigateToOppDoc() {
        investmentViewModel.setOpportunityDoc(oppDocData)
        investmentViewModel.setSkus(landSkusData)
        val fragment = OpportunityDocsFragment()
        val bundle = Bundle()
        bundle.putInt("ProjectId", projectId)
        bundle.putString("ProjectName", allData.launchName)
        bundle.putBoolean("isProjectAmenitiesClicked", true)
        fragment.arguments = bundle
        (requireActivity() as HomeActivity).addFragment(
            fragment,
            false
        )
    }

    private fun navigateToMediaGallery(isVideoAllCLicked: Boolean) {
        val imagesList = ArrayList<MediaViewItem>()
        Log.d("cscscs", mediaData.toString())
        var itemId = 0
        for (i in 0..mediaData.size - 1) {
            for (item in mediaData[i].droneShoots) {
                itemId++
                imagesList.add(
                    MediaViewItem(
                        item.mediaContentType,
                        item.mediaContent.value.url,
                        title = "DroneShoots",
                        id = itemId,
                        name = item.name
                    )
                )
            }
            for (item in mediaData[i].images) {
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
            for (item in mediaData[i].videos) {
                itemId++
                imagesList.add(
                    MediaViewItem(
                        item.mediaContentType,
                        item.mediaContent.value.url,
                        title = "Videos",
                        id = itemId,
                        name = item.name
                    )
                )
            }
            for (item in mediaData[i].threeSixtyImages) {
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
            for (item in mediaData[i].coverImage) {
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
        val fragment = MediaGalleryFragment()
        val bundle = Bundle()
        bundle.putSerializable("Data", imagesList)
//        bundle.putBoolean("isVideoSeeAllClicked",isVideoAllCLicked)
        investmentViewModel.isVideoSeeAllClicked = true
        fragment.arguments = bundle
        (requireActivity() as HomeActivity).addFragment(fragment, false)
    }

    private fun callVideoCallApi() {
        investmentViewModel.scheduleVideoCall(
            VideoCallBody(
                caseType = "1004",
                description = "I want to know more about ${allData.launchName}",
                issueType = "Schedule a video call",
                projectId = projectId
            )
        ).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.data?.let { data ->
                        val applicationSubmitDialog = ApplicationSubmitDialog(
                            "Video Call request sent successfully.",
                            "Our Project Manager will reach out to you soon!",
                            false
                        )
                        applicationSubmitDialog.show(
                            parentFragmentManager,
                            "ApplicationSubmitDialog"
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
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProjectDetailLayoutBinding.inflate(layoutInflater)
        arguments?.let {
            projectId = it.getInt("ProjectId", 0)
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
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility =
            View.GONE
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.imageBack.visibility =
            View.VISIBLE
        (requireActivity() as HomeActivity).hideBottomNavigation()
        binding.slSwipeRefresh.setOnRefreshListener {
            binding.slSwipeRefresh.isRefreshing = true
            binding.rvProjectDetail.visibility = View.GONE
            callApi()
        }
    }

    private fun callApi() {
        investmentViewModel.getInvestmentsPromises().observe(viewLifecycleOwner, Observer {
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
        })

    }

    private fun callProjectIdApi(promiseData: List<PmData>) {
        investmentViewModel.getInvestmentsDetail(projectId).observe(viewLifecycleOwner, Observer {
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
                        if (watchList != null) {
                            watchList = data.projectContent.watchlist.toMutableList()
                        }
                        oppDocData = data.projectContent.opportunityDoc
                        mediaData = data.projectContent.mediaGalleryOrProjectContent
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
                        investmentViewModel.setImageActive(data.projectContent.mediaGalleryOrProjectContent[0].isImagesActive)
                        investmentViewModel.setVideoActive(data.projectContent.mediaGalleryOrProjectContent[0].isVideosActive)
                        investmentViewModel.setDroneActive(data.projectContent.mediaGalleryOrProjectContent[0].isDroneShootsActive)
                        investmentViewModel.setThreeSixtyActive(data.projectContent.mediaGalleryOrProjectContent[0].isThreeSixtyImagesActive)
                        similarInvestments = data.projectContent.similarInvestments
                        inventoryList = data.projectContent.inventoriesList.data
                        setUpRecyclerView(
                            data.projectContent,
                            promiseData,
                            inventoryList,
                            data.projectContentsAndFaqs
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
        })
    }

    private fun addWatchList() {
        investmentViewModel.addWatchList(WatchListBody(projectId))
            .observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Status.SUCCESS -> {
                        binding.progressBar.hide()
                        it.data?.let { data ->
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
            })
    }

    private fun setUpRecyclerView(
        data: PdData,
        promisesData: List<PmData>,
        inventoryList: List<Inventory>,
        projectContentsAndFaqs: List<ProjectContentsAndFaq>
    ) {
        val list = ArrayList<RecyclerViewItem>()
        list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_PROJECT_DETAIL))
        list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_MAP))
        when (allData.isEscalationGraphActive) {
            true -> {
                list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_PRICE_TRENDS))
            }
        }
        when (allData.isKeyPillarsActive) {
            true -> {
                list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_KEY_PILLARS))
            }
        }
        list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_VIDEO_DRONE))
        when (allData.isOffersAndPromotionsActive) {
            true -> {
                list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_DONT_MISS))
            }
        }
        when (allData.isInventoryBucketActive) {
            true -> {
                list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_SKUS))
            }
        }
        when (allData.opportunityDoc.isProjectAminitiesActive) {
            true -> {
                list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_AMENITIES))
            }
        }
        when (allData.isLocationInfrastructureActive) {
            true -> {
                list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_LOCATION_INFRASTRUCTURE))
            }
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
                list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_SIMILAR_INVESTMENT))
            }
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
                projectContentsAndFaqs
            )
        binding.rvProjectDetail.adapter = adapter
        adapter.setItemClickListener(onItemClickListener)
    }

    val mapItemClickListener = object : MapItemClickListener {
        override fun onItemClicked(view: View, position: Int, latitude: Double, longitude: Double) {
            when (view.id) {
                R.id.cv_location_infrastructure_card -> {
                    investmentViewModel.setMapLocationInfrastructure(mapLocationData)
                    val bundle = Bundle()
                    bundle.putInt("ItemPosition", position)
                    bundle.putSerializable(
                        "Location",
                        MapLocationModel(12.9274, 77.586387, latitude, longitude) as Serializable
                    )
                    val fragment = MapFragment()
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(
                        fragment,
                        false
                    )
                }
            }
        }
    }

    val similarInvItemClickListener = object : SimilarInvItemClickListener {
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
        bundle.putInt("ProjectId", item)
        val fragment = ProjectDetailFragment()
        fragment.arguments = bundle
        (requireActivity() as HomeActivity).addFragment(
            fragment, false
        )
    }

    private val itemClickListener = object : ItemClickListener {
        override fun onItemClicked(view: View, position: Int, item: String) {
            when (view.id) {
//                R.id.iv_testimonials_arrow -> {
//                    val fragment = Testimonials()
//                    val bundle = Bundle()
//                    bundle.putInt("testimonials",item.toInt())
//                    bundle.putString("testimonialsHeading", allData.otherSectionHeadings.testimonials.sectionHeading)
//                    bundle.putString("testimonialsSubHeading",  "Hello")
//                    fragment.arguments = bundle
//                    (requireActivity() as HomeActivity).addFragment(fragment, false)
//                }
                R.id.tv_hear_speak_see_all -> {
                    val fragment = Testimonials()
                    val bundle = Bundle()
                    bundle.putInt("testimonials", item.toInt())
                    bundle.putString(
                        "testimonialsHeading",
                        allData.otherSectionHeadings.testimonials.sectionHeading
                    )
                    bundle.putString("testimonialsSubHeading", "Hello")
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(fragment, false)
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
                            false
                        )
                    }
                }
                R.id.iv_bookmark_icon -> {
                    when (item) {
                        "true" -> addWatchList()
                        "false" -> deleteWatchList()
                    }
                }
                R.id.cv_faq_card -> {
                    val fragment = FaqDetailFragment()
                    val bundle = Bundle()
                    bundle.putInt("ProjectId", projectId)
                    bundle.putInt("FaqId", item.toInt())
                    bundle.putBoolean("isFromInvestment", true)
                    bundle.putString("ProjectName", allData.launchName)
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(fragment, false)
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
                    ).observe(viewLifecycleOwner, Observer {
                        when (it.status) {
                            Status.LOADING -> {
                                binding.progressBar.show()
                            }
                            Status.SUCCESS -> {
                                binding.progressBar.hide()
                                it.data?.let { data ->
                                    val applicationSubmitDialog = ApplicationSubmitDialog(
                                        "Thank you for your interest!",
                                        "Our Project Manager will reach out to you in 24 hours!"
                                    )
                                    applicationSubmitDialog.show(
                                        parentFragmentManager,
                                        "ApplicationSubmitDialog"
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
                    })
                }
            }
        }
    }

    val videoItemClickListener = object : YoutubeItemClickListener {
        override fun onItemClicked(view: View, position: Int, url: String, title: String) {
            when (view.id) {
                R.id.iv_latest_image -> {
                    val intent = Intent(
                        this@ProjectDetailFragment.requireActivity(),
                        YoutubeActivity::class.java
                    )
                    intent.putExtra("YoutubeVideoId", url)
                    intent.putExtra("VideoTitle", title)
                    startActivity(intent)
//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=${url}"))
//                    startActivity(intent)
                }
            }
        }
    }

    private fun navigateToSkuScreen() {
        val fragment = LandSkusFragment()
        val bundle = Bundle()
        bundle.putInt("ProjectId", projectId)
        fragment.arguments = bundle
        (requireActivity() as HomeActivity).addFragment(fragment, false)
    }

    private fun refreshingPage(id: Int) {
        projectId = id
        callApi()
    }

    private fun openDialog() {
        val confirmationDialog = ConfirmationDialog(investmentViewModel, itemClickListener)
        confirmationDialog.show(parentFragmentManager, "ConfirmationDialog")
    }

    private fun deleteWatchList() {
        val id = investmentViewModel.getWatchListId().value
        if (id != null) {
            investmentViewModel.deleteWatchList(id).observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Status.SUCCESS -> {
                        binding.progressBar.hide()
                        it.data?.let { data ->
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
            })
        }
    }

}