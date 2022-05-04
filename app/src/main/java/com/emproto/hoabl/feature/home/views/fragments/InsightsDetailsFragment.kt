package com.emproto.hoabl.feature.home.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentInsightsDetailsBinding
import com.emproto.hoabl.databinding.FragmentLatestUpdatesBinding
import com.emproto.hoabl.databinding.FragmentLatestUpdatesDetailsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.adapters.AllLatestUpdatesAdapter
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.promises.adapter.PromiseDetailsAdapter
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import javax.inject.Inject

class InsightsDetailsFragment : BaseFragment() {

    lateinit var mBinding: FragmentInsightsDetailsBinding

    @Inject
    lateinit var factory: HomeFactory
    lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = FragmentInsightsDetailsBinding.inflate(inflater, container, false)

        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility =
            View.GONE

        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel = ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]

        initObserver()
        initClickListener()

        mBinding.backBtn.setOnClickListener(View.OnClickListener {

        })
        return mBinding.root
    }

    private fun initObserver() {
        homeViewModel.getSelectedInsights().observe(viewLifecycleOwner, Observer {
            mBinding.title.text= it.displayTitle
            mBinding.firstDetails.text= it.insightsMedia[0].description
            mBinding.thirdDetails.text= it.insightsMedia[0].description
            mBinding.secondDetails.text= it.insightsMedia[0].description

            mBinding.imageDesc.text= it.insightsMedia[0].media.mediaDescription

            Glide.with(requireContext())
                .load(it.insightsMedia[0].media.value.url)
                .into(mBinding.image1)
            Glide.with(requireContext())
                .load(it.insightsMedia[0].media.value.url)
                .into(mBinding.image2)
            Glide.with(requireContext())
                .load(it.insightsMedia[0].media.value.url)
                .into(mBinding.image3)
        })
    }

    private fun initClickListener() {
        mBinding.backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

}