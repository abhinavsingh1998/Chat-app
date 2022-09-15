package com.emproto.hoabl.feature.investment.views.mediagallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.emproto.core.BaseFragment
import com.emproto.core.Constants
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentVideoViewBinding
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer

class MediaViewVideoFragment : BaseFragment() {

    lateinit var binding: FragmentVideoViewBinding
    private val encryptedMapKey = "3j+SjzloU3Keq7Fk+lbKW9W2kdodK3M59ZmL10v+AOctU/oems5E2ANOrD73gB59"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoViewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ytPlayer.initialize(Utility.decrypt(encryptedMapKey), object :
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
                Toast.makeText(
                    this@MediaViewVideoFragment.requireContext(),
                    Constants.VIDEO_PLAYER_FAILED,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

    }

}