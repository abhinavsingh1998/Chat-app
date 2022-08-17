package com.emproto.hoabl.feature.investment.views.mediagallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.emproto.core.BaseFragment
import com.emproto.core.Constants
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentVideoViewBinding
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer

class MediaViewVideoFragment:BaseFragment() {

    lateinit var binding:FragmentVideoViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoViewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        lifecycle.addObserver(binding.activityMainYoutubePlayerView)
//
//        (requireActivity() as HomeActivity).hideHeader()
//        binding.activityMainYoutubePlayerView.addYouTubePlayerListener(object:
//            AbstractYouTubePlayerListener() {
//            override fun onReady(youTubePlayer: YouTubePlayer) {
//                super.onReady(youTubePlayer)
//                val videoId = "1FJHYqE0RDg"
//                youTubePlayer.loadVideo(videoId, 0F)
//            }
//        })
        binding.ytPlayer.initialize(resources.getString(R.string.youtube_api_key),object:
            YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubePlayer?,
                    p2: Boolean
                ) {
                    p1?.loadVideo("HzeK7g8cD0Y")
                    p1?.play()
                }

                override fun onInitializationFailure(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubeInitializationResult?
                ) {
                    Toast.makeText(this@MediaViewVideoFragment.requireContext(), Constants.VIDEO_PLAYER_FAILED, Toast.LENGTH_SHORT).show();
                }

            })

    }

}