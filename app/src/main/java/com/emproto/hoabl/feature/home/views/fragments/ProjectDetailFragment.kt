package com.emproto.hoabl.feature.home.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.ProjectDetailLayoutBinding
import com.emproto.hoabl.feature.home.adapters.*
import com.emproto.hoabl.model.ProjectDetailItem

class ProjectDetailFragment:BaseFragment() {

    private lateinit var binding:ProjectDetailLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ProjectDetailLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = ArrayList<ProjectDetailItem>()
        list.add(ProjectDetailItem(ProjectDetailAdapter.VIEW_TYPE_ONE))
        list.add(ProjectDetailItem(ProjectDetailAdapter.VIEW_TYPE_TWO))
        list.add(ProjectDetailItem(ProjectDetailAdapter.VIEW_TYPE_THREE))
        list.add(ProjectDetailItem(ProjectDetailAdapter.VIEW_TYPE_FOUR))
        list.add(ProjectDetailItem(ProjectDetailAdapter.VIEW_TYPE_FIVE))
        list.add(ProjectDetailItem(ProjectDetailAdapter.VIEW_TYPE_SIX))
        list.add(ProjectDetailItem(ProjectDetailAdapter.VIEW_TYPE_SEVEN))
        list.add(ProjectDetailItem(ProjectDetailAdapter.VIEW_TYPE_EIGHT))
        list.add(ProjectDetailItem(ProjectDetailAdapter.VIEW_TYPE_NINE))
        list.add(ProjectDetailItem(ProjectDetailAdapter.VIEW_TYPE_TEN))
        list.add(ProjectDetailItem(ProjectDetailAdapter.VIEW_TYPE_ELEVEN))
        list.add(ProjectDetailItem(ProjectDetailAdapter.VIEW_TYPE_TWELVE))
        list.add(ProjectDetailItem(ProjectDetailAdapter.VIEW_TYPE_THIRTEEN))
        list.add(ProjectDetailItem(ProjectDetailAdapter.VIEW_TYPE_FOURTEEN))

        val adapter = ProjectDetailAdapter(this.requireContext(),list)
        binding.rvProjectDetail.adapter = adapter
    }

}