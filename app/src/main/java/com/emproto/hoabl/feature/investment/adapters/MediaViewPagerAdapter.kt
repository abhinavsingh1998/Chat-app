package com.emproto.hoabl.feature.investment.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.emproto.hoabl.feature.investment.views.mediagallery.DroneFragment
import com.emproto.hoabl.feature.investment.views.mediagallery.PhotosFragment
import com.emproto.hoabl.feature.investment.views.mediagallery.VideosFragment

class MediaViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    :FragmentStateAdapter(fragmentManager,lifecycle) {

    companion object {
        const val NUM_TABS = 4
    }

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> PhotosFragment()
            1 -> VideosFragment()
            2 -> VideosFragment()
            else -> VideosFragment()
        }
    }



}