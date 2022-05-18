package com.emproto.hoabl.feature.investment.views.mediagallery

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentMediaViewBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import javax.inject.Inject


class MediaViewFragment:BaseFragment() {

    @Inject
    lateinit var investmentFactory: InvestmentFactory
    lateinit var investmentViewModel: InvestmentViewModel
    lateinit var binding:FragmentMediaViewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMediaViewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setUpUI()
    }

    private fun initViewModel() {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        investmentViewModel =
            ViewModelProvider(requireActivity(), investmentFactory)[InvestmentViewModel::class.java]
    }

    private fun setUpUI() {
        investmentViewModel.getMediaItem().observe(viewLifecycleOwner, Observer {
            when(it.mediaType){
                "Photo" -> {
                    binding.ivMediaPhoto.visibility= View.VISIBLE
                    binding.vvMediaVideo.visibility = View.GONE
                    Glide
                        .with(requireContext())
                        .load(it.media)
                        .into(binding.ivMediaPhoto)
                }
                "Video" -> {
                    binding.ivMediaPhoto.visibility= View.GONE
                    binding.vvMediaVideo.visibility = View.VISIBLE
                    setupRawVideo()
                }
            }
        })

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