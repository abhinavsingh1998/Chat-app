package com.emproto.hoabl.feature.investment.views.mediagallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentMediaViewBinding
import com.emproto.hoabl.feature.investment.adapters.ProjectDetailViewPagerAdapter
import com.emproto.hoabl.model.ViewItem

class MediaViewFragment:BaseFragment() {

    lateinit var binding:FragmentMediaViewBinding
    private lateinit var adapter:ProjectDetailViewPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMediaViewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val list = ArrayList<ViewItem>()
        val list = arrayListOf<Int>(
            R.drawable.media_video_beach_image, R.drawable.media_video_isle_image,
            R.drawable.media_video_mountain_image, R.drawable.media_video_beach_image, R.drawable.media_video_mountain_image,
            R.drawable.media_video_beach_image, R.drawable.media_video_beach_image, R.drawable.media_video_isle_image,
            R.drawable.media_video_mountain_image, R.drawable.media_video_beach_image, R.drawable.media_video_mountain_image,
            R.drawable.media_video_isle_image, R.drawable.media_video_mountain_image, R.drawable.media_video_isle_image)
//        adapter = ProjectDetailViewPagerAdapter()
    }
}