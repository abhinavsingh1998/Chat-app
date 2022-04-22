package com.emproto.hoabl.feature.investment.views.mediagallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
import com.emproto.hoabl.HomeActivity
import com.emproto.hoabl.databinding.FragmentPhotosBinding
import com.emproto.hoabl.databinding.FragmentVideosBinding
import com.emproto.hoabl.feature.investment.adapters.MediaPhotosAdapter
import com.emproto.hoabl.model.MediaGalleryItem
import com.emproto.hoabl.utils.ItemClickListener

class VideosFragment:BaseFragment() {

    lateinit var binding:FragmentPhotosBinding
    lateinit var mediaPhotosAdapter: MediaPhotosAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPhotosBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = ArrayList<MediaGalleryItem>()
        list.add(MediaGalleryItem(1,"Photos"))
        list.add(MediaGalleryItem(2,"Photos"))

        mediaPhotosAdapter = MediaPhotosAdapter(list,itemClickListener)
        binding.rvMainPhotos.adapter = mediaPhotosAdapter

    }

    private val itemClickListener = object : ItemClickListener {
        override fun onItemClicked(view: View, position: Int, item: String) {
            (requireActivity() as HomeActivity).addFragment(MediaViewFragment(), true)
        }
    }
}