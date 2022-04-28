package com.emproto.hoabl.feature.investment.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ProjectDetailLayoutBinding
import com.emproto.hoabl.feature.investment.adapters.ProjectAmenitiesAdapter
import com.emproto.hoabl.feature.investment.adapters.ProjectDetailAdapter
import com.emproto.hoabl.feature.investment.views.mediagallery.MediaGalleryFragment
import com.emproto.hoabl.model.RecyclerViewItem
import com.google.android.material.bottomsheet.BottomSheetDialog


class ProjectDetailFragment:BaseFragment() {

    private lateinit var binding:ProjectDetailLayoutBinding

    val onItemClickListener =
        View.OnClickListener { view ->
            when (view.id) {
                R.id.iv_why_invest ->{
                    val opportunityDocsFragment = OpportunityDocsFragment()
                    (requireActivity() as HomeActivity).replaceFragment(opportunityDocsFragment.javaClass, "", true, null, null, 0, false)
                }
                R.id.tv_skus_see_all -> {
                    val landSkusFragment = LandSkusFragment()
                    (requireActivity() as HomeActivity).replaceFragment(landSkusFragment.javaClass, "", true, null, null, 0, false)
                }
                R.id.tv_video_drone_see_all -> {
                    val mediaGalleryFragment = MediaGalleryFragment()
                    (requireActivity() as HomeActivity).replaceFragment(mediaGalleryFragment.javaClass,"",true,null,null,0,false)
                }
                R.id.tv_project_amenities_all -> {
                    val docsBottomSheet = BottomSheetDialog(this.requireContext(),R.style.BottomSheetDialogTheme)
                    docsBottomSheet.setContentView(R.layout.project_amenities_dialog_layout)
                    val list = arrayListOf<String>("1","2","3","4","1","2","3","4","1","2","3","4","1","2","3","4","1","2","3","4","1","2","3","4","1","2","3","4","1","2","3","4")
                    val adapter = ProjectAmenitiesAdapter(this.requireContext(),list)
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
        setUpUI()
        setUpRecyclerView()
    }

    private fun setUpUI() {
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility = View.GONE
    }

    private fun setUpRecyclerView(){
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

        val adapter = ProjectDetailAdapter(this.requireContext(),list)
        binding.rvProjectDetail.adapter = adapter
        adapter.setItemClickListener(onItemClickListener)
    }

}