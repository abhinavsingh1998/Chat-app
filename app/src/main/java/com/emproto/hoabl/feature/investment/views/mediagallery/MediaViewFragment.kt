package com.emproto.hoabl.feature.investment.views.mediagallery

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.emproto.core.BaseFragment
import com.emproto.core.Constants
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

    private var mediaData = ArrayList<MediaViewItem>()
    private lateinit var data:MediaViewItem
    private var index = 0

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
        (requireActivity() as HomeActivity).hideHeader()
        (requireActivity() as HomeActivity).hideBottomNavigation()
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun setUpUI() {

        //if not from investment module
        arguments?.let {
            it.getSerializable(Constants.DATA)?.let {
                data = it as MediaViewItem
                binding.tvMediaImageName.text = data.name
                showMedia(data)
            }
            it.getInt(Constants.IMAGE_POSITION).let{
                index = it
            }
        }

        investmentViewModel.getMediaListItem().observe(viewLifecycleOwner,Observer{
            mediaData.clear()
            for (item in it) {
                mediaData.add(item)
            }
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

}