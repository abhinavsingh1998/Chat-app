package com.emproto.hoabl.feature.investment.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ProjectDetailLayoutBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.fragments.Testimonials
import com.emproto.hoabl.feature.investment.adapters.ProjectAmenitiesAdapter
import com.emproto.hoabl.feature.investment.adapters.ProjectDetailAdapter
import com.emproto.hoabl.feature.investment.dialogs.ApplicationSubmitDialog
import com.emproto.hoabl.feature.investment.dialogs.ConfirmationDialog
import com.emproto.hoabl.feature.investment.views.mediagallery.MediaGalleryFragment
import com.emproto.hoabl.feature.promises.HoablPromises
import com.emproto.hoabl.feature.promises.PromisesDetailsFragment
import com.emproto.hoabl.model.MapLocationModel
import com.emproto.hoabl.model.MediaViewItem
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.utils.Extensions.toHomePagesOrPromise
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import com.emproto.networklayer.request.investment.AddInventoryBody
import com.emproto.networklayer.request.investment.VideoCallBody
import com.emproto.networklayer.request.investment.WatchListBody
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.investment.*
import com.emproto.networklayer.response.portfolio.ivdetails.LatestMediaGalleryOrProjectContent
import com.emproto.networklayer.response.watchlist.Data
import com.google.android.material.bottomsheet.BottomSheetDialog
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
    private lateinit var oppDocData: List<OpprotunityDoc>
    private lateinit var mediaData: List<LatestMediaGalleryOrProjectContent>
    private lateinit var promisesData: List<PmData>
    private lateinit var landSkusData: List<InventoryBucketContent>
    private lateinit var mapLocationData: LocationInfrastructure
    private lateinit var watchList: List<Data>
    private lateinit var inventoryList : List<Inventory>

    private var faqData: List<ProjectContentsAndFaq> = mutableListOf()
    private var APP_URL = "https://www.google.com/"
    private var isBookmarked = false
    private var watchListId = 0

    val onItemClickListener =
        View.OnClickListener { view ->
            when (view.id) {
                R.id.project_detail_map -> {
                    investmentViewModel.setMapLocationInfrastructure(mapLocationData)
                    (requireActivity() as HomeActivity).addFragment(MapFragment(), false)
                }
                R.id.cl_not_convinced_promises -> {
                    callVideoCallApi()
                }
                R.id.tv_faq_read_all -> {
                    val fragment = FaqDetailFragment()
                    val bundle = Bundle()
                    bundle.putInt("ProjectId", projectId)
                    fragment.arguments = arguments
                    (requireActivity() as HomeActivity).addFragment(fragment, false)
                }
                R.id.cl_why_invest -> {
                    investmentViewModel.setOpportunityDoc(oppDocData)
                    investmentViewModel.setSkus(landSkusData)
                    (requireActivity() as HomeActivity).addFragment(
                        OpportunityDocsFragment(),
                        false
                    )
                }
                R.id.tv_skus_see_all -> {
                    val fragment = LandSkusFragment()
                    val bundle = Bundle()
                    bundle.putInt("ProjectId", projectId)
                    fragment.arguments = arguments
                    investmentViewModel.setSkus(landSkusData)
                    (requireActivity() as HomeActivity).addFragment(fragment, false)
                }
                R.id.tv_video_drone_see_all -> {
                    val imagesList = ArrayList<MediaViewItem>()
                    for(i in 0..mediaData.size-1){
                        for (item in mediaData[i].droneShoots) {
                            imagesList.add(MediaViewItem(item.mediaContentType, item.mediaContent.value.url))
                        }
                        for (item in mediaData[i].images) {
                            imagesList.add(MediaViewItem(item.mediaContentType, item.mediaContent.value.url))
                            imagesList.add(MediaViewItem(item.mediaContentType, item.mediaContent.value.url))

                        }
                        for (item in mediaData[i].videos) {
                            imagesList.add(MediaViewItem(item.mediaContentType, item.mediaContent.value.url))
                        }
                        for (item in mediaData[i].threeSixtyImages) {
                            imagesList.add(MediaViewItem(item.mediaContentType, item.mediaContent.value.url))
                        }
                    }
                    Log.d("cscscs",imagesList.toString())
                    val fragment = MediaGalleryFragment()
                    val bundle = Bundle()
                    bundle.putSerializable("Data", imagesList)
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(fragment, false)
                }
                R.id.tv_project_amenities_all -> {
                    val docsBottomSheet =
                        BottomSheetDialog(this.requireContext(), R.style.BottomSheetDialogTheme)
                    docsBottomSheet.setContentView(R.layout.project_amenities_dialog_layout)
                    val adapter = ProjectAmenitiesAdapter(
                        this.requireContext(),
                        oppDocData[0].projectAminities
                    )
                    docsBottomSheet.findViewById<RecyclerView>(R.id.rv_project_amenities_item_recycler)?.adapter =
                        adapter
                    docsBottomSheet.findViewById<ImageView>(R.id.iv_project_amenities_close)
                        ?.setOnClickListener {
                            docsBottomSheet.dismiss()
                        }
                    docsBottomSheet.show()
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
                R.id.tv_hear_speak_see_all -> {
                    (requireActivity() as HomeActivity).addFragment(Testimonials(), false)
                }
                R.id.tv_promises_see_all -> {
                    (requireActivity() as HomeActivity).addFragment(HoablPromises(), false)
                }
                R.id.tv_apply_now -> {
                    val fragment = LandSkusFragment()
                    val bundle = Bundle()
                    bundle.putInt("ProjectId", projectId)
                    fragment.arguments = arguments
                    (requireActivity() as HomeActivity).addFragment(fragment, false)
                }
                R.id.tv_location_infrastructure_all -> {
                    investmentViewModel.setMapLocationInfrastructure(mapLocationData)
                    (requireActivity() as HomeActivity).addFragment(MapFragment(), false)
                }
            }
        }

    private fun callVideoCallApi() {
        investmentViewModel.scheduleVideoCall(VideoCallBody(caseType = "1003",
        description = "",
        issueType = "Schedule a video call",
        projectId= projectId)).observe(viewLifecycleOwner,Observer{
            when (it.status) {
                Status.LOADING -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
                }
                Status.SUCCESS -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                    it.data?.data?.let { data ->
                        val applicationSubmitDialog = ApplicationSubmitDialog(
                            "Video Call request sent successfully.",
                            "Our Project Manager will reach out to you soon!",
                            false
                        )
                        applicationSubmitDialog.show(parentFragmentManager, "ApplicationSubmitDialog")
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
    }

    private fun setUpUI() {
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility =
            View.GONE
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.imageBack.visibility = View.VISIBLE
    }

    private fun callApi() {
        investmentViewModel.getInvestmentsPromises().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
                }
                Status.SUCCESS -> {
//                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                    it.data?.data?.let { data ->
                        promisesData = data
                        callProjectIdApi(promisesData)
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

    private fun callProjectIdApi(promiseData: List<PmData>) {
        val pjId = arguments?.getInt("ProjectId") as Int
            projectId = pjId
            investmentViewModel.getInvestmentsDetail(projectId).observe(viewLifecycleOwner, Observer {
                when(it.status){
                    Status.LOADING -> {
                        (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
                    }
                    Status.SUCCESS -> {
                        (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                        it.data?.data?.let {  data ->
                            oppDocData = data.opprotunityDocs
                            mediaData= data.latestMediaGalleryOrProjectContent
                            landSkusData = data.inventoryBucketContents
                            faqData = data.projectContentsAndFaqs
                            mapLocationData = data.locationInfrastructure
                            watchList = data.watchlist
                            inventoryList = data.inventoriesList.data
                            setUpRecyclerView(data, promiseData, inventoryList)
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
//
//        investmentViewModel.getInvestmentsDetail(projectId).observe(viewLifecycleOwner, Observer {
//            when (it.status) {
//                Status.LOADING -> {
//                    (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
//                }
//                Status.SUCCESS -> {
//                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
//                    it.data?.data?.let { data ->
//                        oppDocData = data.opprotunityDocs
//                        mediaData = data.projectCoverImages
//                        landSkusData = data.inventoryBucketContents
//                        faqData = data.projectContentsAndFaqs
//                        mapLocationData = data.locationInfrastructure
//                        setUpRecyclerView(data, promiseData)
//                    }
//                }
//                Status.ERROR -> {
//                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
//                    (requireActivity() as HomeActivity).showErrorToast(
//                        it.message!!
//                    )
//                }
//            }
//        })

    }

    private fun addWatchList(){
        investmentViewModel.addWatchList(WatchListBody(projectId)).observe(viewLifecycleOwner,Observer{
            when(it.status){
                Status.LOADING -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
                }
                Status.SUCCESS -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                    it.data?.let { data ->
                        Toast.makeText(this.requireContext(), "Project added to watchlist successfully", Toast.LENGTH_SHORT).show()
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

    private fun setUpRecyclerView(
        data: PdData,
        promisesData: List<PmData>,
        inventoryList: List<Inventory>
    ) {
        val list = ArrayList<RecyclerViewItem>()
        list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_ONE))
        list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_TWO))
        list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_THREE))
        list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_FOUR))
        list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_FIVE))
        list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_SIX))
        list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_SEVEN))
        list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_EIGHT))
        list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_NINE))
        list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_TEN))
        when{
            data.projectContentsAndFaqs.isNotEmpty() -> {
                list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_ELEVEN))
            }
        }
        list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_TWELVE))
        when {
            data.similarInvestments.isNotEmpty() -> {
                list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_FOURTEEN))
            }
        }
        for(item in watchList){
            if(item.project.id == projectId){
                isBookmarked = true
                watchListId = item.watchlist.id
            }
        }
        val adapter =
            ProjectDetailAdapter(this.requireContext(), list, data, promisesData, itemClickListener,isBookmarked,investmentViewModel)
        binding.rvProjectDetail.adapter = adapter
        adapter.setItemClickListener(onItemClickListener)
    }

    private val itemClickListener = object : ItemClickListener {
        override fun onItemClicked(view: View, position: Int, item: String) {
            when(view.id) {
                R.id.cl_outer_item_skus -> {
                    openDialog()
                }
                R.id.cv_promises_card -> {
                    if(promisesData[position] != null){
                        val promiseData = promisesData[position].toHomePagesOrPromise()
                        homeViewModel.setSelectedPromise(promiseData)
                        (requireActivity() as HomeActivity).addFragment(
                            PromisesDetailsFragment(),
                            false
                        )
                    }
                }
                R.id.cv_location_infrastructure_card -> {
                    investmentViewModel.setMapLocationInfrastructure(mapLocationData)
                    val bundle = Bundle()
                    bundle.putSerializable("Location",MapLocationModel(12.9274,77.586387,12.9287469,77.5867364) as Serializable)
                    val fragment = MapFragment()
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(
                        fragment,
                        false
                    )
                }
                R.id.iv_bookmark_icon -> {
                    when(item){
                        "true" -> addWatchList()
                        "false" -> deleteWatchList()
                    }
                }
            }
            when(item){
                "Yes" -> {
                    investmentViewModel.addInventory(
                        AddInventoryBody(
                            inventoryBucketId = position,
                            launchPhaseId = projectId
                        )
                    ).observe(viewLifecycleOwner,Observer{
                        when(it.status){
                            Status.LOADING -> {
                                (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
                            }
                            Status.SUCCESS -> {
                                (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                                it.data?.let { data ->
                                    val applicationSubmitDialog = ApplicationSubmitDialog("Thank you for your interest!","Our Project Manager will reach out to you in 24 hours!")
                                    applicationSubmitDialog.show(parentFragmentManager,"ApplicationSubmitDialog")
                                    callApi()
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
            }
        }
    }

    private fun openDialog() {
        val confirmationDialog = ConfirmationDialog(investmentViewModel,itemClickListener)
        confirmationDialog.show(parentFragmentManager,"ConfirmationDialog")
    }

    private fun deleteWatchList() {
        investmentViewModel.deleteWatchList(watchListId).observe(viewLifecycleOwner,Observer{
            when(it.status){
                Status.LOADING -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
                }
                Status.SUCCESS -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                    it.data?.let { data ->
                        Toast.makeText(this.requireContext(), "Project removed from watchlist successfully", Toast.LENGTH_SHORT).show()
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

}