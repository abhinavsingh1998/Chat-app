package com.emproto.hoabl.feature.home.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentLatestUpdatesDetailsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import javax.inject.Inject

class LatestUpdatesDetailsFragment : BaseFragment() {

    lateinit var mBinding: FragmentLatestUpdatesDetailsBinding

    @Inject
    lateinit var factory: HomeFactory
    lateinit var homeViewModel: HomeViewModel

    lateinit var bundle: Bundle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = FragmentLatestUpdatesDetailsBinding.inflate(inflater, container, false)

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
        homeViewModel.getSelectedLatestUpdates().observe(viewLifecycleOwner, Observer {
            mBinding.title.text= it.displayTitle
            mBinding.firstDetails.text= it.detailedInfo[0].description
            mBinding.tvLocation.text= it.subTitle
            Glide.with(requireContext()).load(it.detailedInfo[0].media.value.url)
                .into(mBinding.image1)
        })
    }

    private fun initClickListener() {
        mBinding.backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()

        }
    }

}