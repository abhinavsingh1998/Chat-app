package com.emproto.hoabl.feature.investment.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.utils.Extensions.toHomePagesOrPromise
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.investment.*
import com.emproto.networklayer.response.promises.HomePagesOrPromise
import com.google.android.material.bottomsheet.BottomSheetDialog
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
    private lateinit var mediaData: ProjectCoverImages
    private lateinit var promisesData: List<PmData>
    private lateinit var landSkusData: List<InventoryBucketContent>
    private lateinit var mapLocationData: LocationInfrastructure

    private var faqData: List<ProjectContentsAndFaq> = mutableListOf()
    private var APP_URL = "https://www.google.com/"

    val onItemClickListener =
        View.OnClickListener { view ->
            when (view.id) {
                R.id.project_detail_map -> {
                    investmentViewModel.setMapLocationInfrastructure(mapLocationData)
                    (requireActivity() as HomeActivity).addFragment(MapFragment(), false)
                }
                R.id.cl_not_convinced_promises -> {
                    val applicationSubmitDialog = ApplicationSubmitDialog(
                        "Video Call request sent successfully.",
                        "Our Project Manager will reach out to you soon!",
                        false
                    )
                    applicationSubmitDialog.show(parentFragmentManager, "ApplicationSubmitDialog")
                }
                R.id.tv_faq_read_all -> {
                    (requireActivity() as HomeActivity).addFragment(FaqDetailFragment(), false)
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
                    investmentViewModel.setSkus(landSkusData)
                    (requireActivity() as HomeActivity).addFragment(LandSkusFragment(), false)
                }
                R.id.tv_video_drone_see_all -> {
                    investmentViewModel.setMedia(mediaData)
                    (requireActivity() as HomeActivity).addFragment(MediaGalleryFragment(), false)
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
                    investmentViewModel.setSkus(landSkusData)
                    (requireActivity() as HomeActivity).addFragment(LandSkusFragment(), false)
                }
                R.id.tv_location_infrastructure_all -> {
                    investmentViewModel.setMapLocationInfrastructure(mapLocationData)
                    (requireActivity() as HomeActivity).addFragment(MapFragment(), false)
                }
            }
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
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
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
        investmentViewModel.getInvestmentsDetail(projectId).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
                }
                Status.SUCCESS -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                    it.data?.data?.let { data ->
                        oppDocData = data.opprotunityDocs
                        mediaData = data.projectCoverImages
                        landSkusData = data.inventoryBucketContents
                        faqData = data.projectContentsAndFaqs
                        mapLocationData = data.locationInfrastructure
                        setUpRecyclerView(data, promiseData)
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

    private fun setUpRecyclerView(data: PdData, promisesData: List<PmData>) {
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
        list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_ELEVEN))
        list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_TWELVE))
        when {
            data.similarInvestments.isNotEmpty() -> {
                list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_FOURTEEN))
            }
        }
        val adapter =
            ProjectDetailAdapter(this.requireContext(), list, data, promisesData, itemClickListener)
        binding.rvProjectDetail.adapter = adapter
        adapter.setItemClickListener(onItemClickListener)
    }

    private val itemClickListener = object : ItemClickListener {
        override fun onItemClicked(view: View, position: Int, item: String) {
            when(view.id) {
                R.id.cl_outer_item_skus -> {
                    investmentViewModel.setSku(landSkusData[position])
                    val confirmationDialog = ConfirmationDialog(investmentViewModel)
                    confirmationDialog.show(parentFragmentManager,"ConfirmationDialog")
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
            }
        }
    }
}