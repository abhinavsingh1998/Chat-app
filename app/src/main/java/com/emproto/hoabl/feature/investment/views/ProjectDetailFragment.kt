package com.emproto.hoabl.feature.investment.views

import android.os.Bundle
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
import com.emproto.hoabl.feature.investment.adapters.ProjectAmenitiesAdapter
import com.emproto.hoabl.feature.investment.adapters.ProjectDetailAdapter
import com.emproto.hoabl.feature.investment.dialogs.ApplicationSubmitDialog
import com.emproto.hoabl.feature.investment.views.mediagallery.MediaGalleryFragment
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.investment.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.Serializable
import javax.inject.Inject


class ProjectDetailFragment:BaseFragment() {

    @Inject
    lateinit var investmentFactory: InvestmentFactory
    lateinit var investmentViewModel: InvestmentViewModel
    private lateinit var binding:ProjectDetailLayoutBinding

    private var projectId = 0
    private lateinit var oppDocData:List<OpprotunityDoc>
    private lateinit var mediaData : ProjectCoverImages
    private lateinit var promisesData : List<PmData>
    private lateinit var landSkusData : List<InventoryBucketContent>
    private lateinit var mapLocationData : LocationInfrastructure
    private var faqData: List<ProjectContentsAndFaq> = mutableListOf()

    val onItemClickListener =
        View.OnClickListener { view ->
            when (view.id) {
                R.id.project_detail_map -> {
                    val bundle = Bundle()
                    bundle.putSerializable("MapLocationData",mapLocationData as Serializable)
                    val mapFragment = MapFragment()
                    (requireActivity() as HomeActivity).replaceFragment(mapFragment.javaClass, "", true, bundle, null, 0, true)
                }
                R.id.cl_not_convinced_promises -> {
                    val applicationSubmitDialog = ApplicationSubmitDialog("Video Call request sent successfully.","Our sales person will reach out to you soon!",false)
                    applicationSubmitDialog.show(parentFragmentManager,"ApplicationSubmitDialog")
                }
                R.id.tv_faq_read_all -> {
                    val faqDetailFragment = FaqDetailFragment()
                    (requireActivity() as HomeActivity).replaceFragment(faqDetailFragment.javaClass, "", true, null, null, 0, false)
                }
                R.id.cl_not_convinced ->{
                    val bundle = Bundle()
                    bundle.putSerializable("OppDocData",oppDocData as Serializable)
                    val opportunityDocsFragment = OpportunityDocsFragment()
                    (requireActivity() as HomeActivity).replaceFragment(opportunityDocsFragment.javaClass, "", true, bundle, null, 0, false)
                }
                R.id.tv_skus_see_all -> {
                    val bundle = Bundle()
                    bundle.putSerializable("skusData",landSkusData as Serializable)
                    val landSkusFragment = LandSkusFragment()
                    (requireActivity() as HomeActivity).replaceFragment(landSkusFragment.javaClass, "", true, bundle, null, 0, false)
                }
                R.id.tv_video_drone_see_all -> {
                    val bundle = Bundle()
                    bundle.putSerializable("MediaGalleryData",mediaData as Serializable)
                    val mediaGalleryFragment = MediaGalleryFragment()
                    (requireActivity() as HomeActivity).replaceFragment(mediaGalleryFragment.javaClass,"",true,bundle,null,0,false)
                }
                R.id.tv_project_amenities_all -> {
                    val docsBottomSheet = BottomSheetDialog(this.requireContext(),R.style.BottomSheetDialogTheme)
                    docsBottomSheet.setContentView(R.layout.project_amenities_dialog_layout)
                    val adapter = ProjectAmenitiesAdapter(this.requireContext(),oppDocData[0].projectAminities)
                    docsBottomSheet.findViewById<RecyclerView>(R.id.rv_project_amenities_item_recycler)?.adapter = adapter
                    docsBottomSheet.findViewById<ImageView>(R.id.iv_project_amenities_close)?.setOnClickListener {
                        docsBottomSheet.dismiss()
                    }
                    docsBottomSheet.show()
                }
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ProjectDetailLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpInitialization()
        setUpUI()
        callApi()
    }

    private fun setUpInitialization() {
//        projectId = arguments?.getInt("ProjectId") as Int
        projectId = 5
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        investmentViewModel =
            ViewModelProvider(requireActivity(), investmentFactory).get(InvestmentViewModel::class.java)
    }

    private fun callApi() {
        investmentViewModel.getInvestmentsPromises().observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.LOADING -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
                }
                Status.SUCCESS -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                    it.data?.data?.let {  data ->
                        promisesData = data

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

        investmentViewModel.getInvestmentsDetail(projectId).observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.LOADING -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
                }
                Status.SUCCESS -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                    it.data?.data?.let {  data ->
                        oppDocData = data.opprotunityDocs
                        mediaData= data.projectCoverImages
                        landSkusData = data.inventoryBucketContents
                        faqData = data.projectContentsAndFaqs
                        mapLocationData = data.locationInfrastructure
                        setUpRecyclerView(data,promisesData)
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

    private fun setUpUI() {
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility = View.GONE
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
        list.add(RecyclerViewItem(ProjectDetailAdapter.VIEW_TYPE_FOURTEEN))

        val adapter = ProjectDetailAdapter(this.requireContext(),list,data,promisesData)
        binding.rvProjectDetail.adapter = adapter
        adapter.setItemClickListener(onItemClickListener)
    }

}