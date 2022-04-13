package com.emproto.hoabl.feature.investment.views.mediagallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentVideosBinding
import com.emproto.hoabl.feature.investment.adapters.MediaPhotosAdapter
import com.emproto.hoabl.model.MediaGalleryItem

class VideosFragment:BaseFragment() {

    lateinit var binding:FragmentVideosBinding
//    lateinit var mediaPhotosAdapter: MediaPhotosAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentVideosBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = ArrayList<MediaGalleryItem>()
        list.add(MediaGalleryItem(1,"Videos"))
        list.add(MediaGalleryItem(2,"Videos"))
//        mediaPhotosAdapter = MediaPhotosAdapter(requireParentFragment(),list)
//        binding.rvMainVideos.adapter = mediaPhotosAdapter
    }
}