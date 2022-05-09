package com.emproto.hoabl.feature.investment.views.mediagallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentMediaGalleryBinding
import com.emproto.hoabl.feature.investment.adapters.MediaViewPagerAdapter
import com.emproto.networklayer.response.investment.MediaGallery
import com.google.android.material.tabs.TabLayoutMediator

class MediaGalleryFragment:BaseFragment() {

    lateinit var binding: FragmentMediaGalleryBinding
    lateinit var mediaViewPagerAdapter: MediaViewPagerAdapter

    private val tabList = arrayListOf<String>("Photos","Videos","Drone Shoots","360 photos")

    lateinit var data:List<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMediaGalleryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        data = arguments?.getSerializable("MediaGalleryData") as List<String>
        setUpTabLayoutViewPager()
    }

    private fun setUpTabLayoutViewPager() {
        mediaViewPagerAdapter = MediaViewPagerAdapter(childFragmentManager,lifecycle)
        binding.vpMediaGallery.adapter = mediaViewPagerAdapter

        TabLayoutMediator(binding.tlMediaGallery,binding.vpMediaGallery){ tab,position ->
            tab.text = tabList[position]
        }.attach()
    }

}