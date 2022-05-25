package com.emproto.hoabl.feature.investment.views.mediagallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentMediaGalleryBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.adapters.MediaViewPagerAdapter
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import com.emproto.networklayer.response.investment.MediaGallery
import com.emproto.networklayer.response.investment.ProjectCoverImages
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class MediaGalleryFragment:BaseFragment() {

    @Inject
    lateinit var investmentFactory: InvestmentFactory
    lateinit var investmentViewModel: InvestmentViewModel
    lateinit var binding: FragmentMediaGalleryBinding
    lateinit var mediaViewPagerAdapter: MediaViewPagerAdapter

    private val tabList = arrayListOf<String>("Photos","Videos","Drone Shoots","360 photos")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMediaGalleryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setUpTabLayoutViewPager()
    }

    private fun initViewModel() {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        investmentViewModel =
            ViewModelProvider(requireActivity(), investmentFactory)[InvestmentViewModel::class.java]
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.imageBack.visibility = View.VISIBLE
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility = View.VISIBLE

    }

    private fun setUpTabLayoutViewPager() {
        investmentViewModel.getMedia().observe(viewLifecycleOwner, Observer {
            mediaViewPagerAdapter = MediaViewPagerAdapter(childFragmentManager,lifecycle)
            binding.vpMediaGallery.adapter = mediaViewPagerAdapter

            TabLayoutMediator(binding.tlMediaGallery,binding.vpMediaGallery){ tab,position ->
                tab.text = tabList[position]
            }.attach()
        })


    }

}