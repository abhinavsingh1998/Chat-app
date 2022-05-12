package com.emproto.hoabl.feature.investment.views.mediagallery

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import com.bumptech.glide.Glide
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentMediaViewBinding


class MediaViewFragment:BaseFragment() {

    lateinit var binding:FragmentMediaViewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMediaViewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
    }

    private fun setUpUI() {
        val mediaList = arguments?.getString("ImageData")
        when(arguments?.getString("MediaType")){
            "Photo" -> {
                binding.ivMediaPhoto.visibility= View.VISIBLE
                binding.vvMediaVideo.visibility = View.GONE
                Glide
                    .with(requireContext())
                    .load(mediaList.toString())
                    .into(binding.ivMediaPhoto)
            }
            "Video" -> {
                binding.ivMediaPhoto.visibility= View.GONE
                binding.vvMediaVideo.visibility = View.VISIBLE
                setupRawVideo()
            }
        }
    }

    private fun setupRawVideo() {
//        val path = "android.resource://" + requireActivity().packageName + "/" + "https://www.youtube.com/watch?v=N-8QUdOdXls"
        val uri = Uri.parse("https://www.youtube.com/watch?v=868a9F93_lY")
        val mediaController = MediaController(requireActivity())
        mediaController.setAnchorView(binding.vvMediaVideo)
        mediaController.setMediaPlayer(binding.vvMediaVideo)
        binding.vvMediaVideo.setVideoURI(uri)
        binding.vvMediaVideo.setMediaController(mediaController)
        binding.vvMediaVideo.requestFocus()
        binding.vvMediaVideo.start()
//        binding.vvMediaVideo.setOnPreparedListener {
//
//        }
    }
}