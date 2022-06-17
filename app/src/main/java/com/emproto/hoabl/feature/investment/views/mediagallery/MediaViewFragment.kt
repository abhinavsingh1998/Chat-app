package com.emproto.hoabl.feature.investment.views.mediagallery

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.bumptech.glide.Glide
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentMediaViewBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.model.MediaViewItem
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import javax.inject.Inject


class MediaViewFragment : BaseFragment() {

    @Inject
    lateinit var investmentFactory: InvestmentFactory
    lateinit var investmentViewModel: InvestmentViewModel
    lateinit var binding: FragmentMediaViewBinding

    private var player: ExoPlayer? = null
    private var playWhenReady = true
    private var currentItem = 0
    private var playbackPosition = 0L
    private var mediaData = ArrayList<MediaViewItem>()
    private lateinit var data:MediaViewItem
    private var index = 0

    private lateinit var dataSourceFactory: DataSource.Factory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediaViewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setUpUI()
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun onStart() {
        super.onStart()

    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun initViewModel() {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        investmentViewModel =
            ViewModelProvider(requireActivity(), investmentFactory)[InvestmentViewModel::class.java]
        dataSourceFactory = DefaultDataSourceFactory(this.requireContext(), "exoplayer-sample")
        (requireActivity() as HomeActivity).hideHeader()
        (requireActivity() as HomeActivity).hideBottomNavigation()
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun setUpUI() {
//        investmentViewModel.getMediaItem().observe(viewLifecycleOwner, Observer {
//            data = it
//            binding.tvMediaImageName.text = data.name
//            showMedia(it)
//        })

        //if not from investment module
        arguments?.let {
            it.getSerializable("Data")?.let {
                data = it as MediaViewItem
                binding.tvMediaImageName.text = data.name
                showMedia(data)
            }
            it.getInt("ImagePosition").let{
                index = it
            }
        }

        investmentViewModel.getMediaListItem().observe(viewLifecycleOwner,Observer{
            mediaData.clear()
            for (item in it) {
                mediaData.add(item)
            }
            Log.d("jbcjadfaj", "${mediaData[index].toString()}  ge= ${data.toString()} index=$index")
//            for (i in 0..mediaData.size - 1) {
//                if (data.id == it[i].id) {
//                    index = i
//                }
//            }
            Log.d("dhdhhd","${mediaData[index].toString()}, ${index.toString()}")
            when(mediaData.size){
                1 -> {
                    binding.ivMediaRightArrow.visibility = View.GONE
                    binding.ivMediaLeftArrow.visibility = View.GONE
                }
                else -> {
                    when {
                        mediaData.size-1 == index -> {
                            binding.ivMediaRightArrow.visibility = View.GONE
                            binding.ivMediaLeftArrow.visibility = View.VISIBLE
                        }
                        index == 0 -> {
                            binding.ivMediaRightArrow.visibility = View.VISIBLE
                            binding.ivMediaLeftArrow.visibility = View.GONE
                        }
                        else -> {
                            binding.ivMediaRightArrow.visibility = View.VISIBLE
                            binding.ivMediaLeftArrow.visibility = View.VISIBLE
                        }
                    }
                }
            }
        })

        binding.ivMediaRightArrow.setOnClickListener {
            if(index < mediaData.size-1){
                binding.ivMediaRightArrow.visibility = View.VISIBLE
                binding.ivMediaLeftArrow.visibility = View.VISIBLE
                index++
                showMedia(mediaData[index])
            }
            if(index == mediaData.size-1){
                binding.ivMediaRightArrow.visibility = View.GONE
                binding.ivMediaLeftArrow.visibility = View.VISIBLE
            }
        }

        binding.ivMediaLeftArrow.setOnClickListener {
            if(index > 0){
                binding.ivMediaLeftArrow.visibility = View.VISIBLE
                binding.ivMediaRightArrow.visibility = View.VISIBLE
                index--
                showMedia(mediaData[index])
            }
            if(index == 0){
                binding.ivMediaLeftArrow.visibility = View.GONE
                binding.ivMediaRightArrow.visibility = View.VISIBLE
            }
        }

        binding.ivCloseButton.setOnClickListener{
            (requireActivity() as HomeActivity).onBackPressed()
        }

    }

    @OptIn(UnstableApi::class)
    fun showMedia(mediaType: MediaViewItem) {
        when (mediaType.mediaType) {
            "image" -> {
                binding.ivMediaPhoto.visibility = View.VISIBLE
                Glide
                    .with(requireContext())
                    .load(mediaType.media)
                    .into(binding.ivMediaPhoto)
            }
            "Video" -> {
                binding.ivMediaPhoto.visibility = View.GONE
            }
        }
    }

//    @SuppressLint("InlinedApi")
//    private fun hideSystemUi() {
//        WindowCompat.setDecorFitsSystemWindows(this.requireActivity().window, false)
//        WindowInsetsControllerCompat(this.requireActivity().window, binding.videoView).let { controller ->
//            controller.hide(WindowInsetsCompat.Type.systemBars())
//            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//        }
//    }
}