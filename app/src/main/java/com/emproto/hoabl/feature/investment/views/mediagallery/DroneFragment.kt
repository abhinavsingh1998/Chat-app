package com.emproto.hoabl.feature.investment.views.mediagallery

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentVideosBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.adapters.MediaPhotosAdapter
import com.emproto.hoabl.model.MediaGalleryItem
import com.emproto.hoabl.model.MediaViewItem
import com.emproto.hoabl.utils.MediaItemClickListener
import com.emproto.hoabl.utils.YoutubeItemClickListener
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import javax.inject.Inject

class DroneFragment:BaseFragment() {

    @Inject
    lateinit var investmentFactory: InvestmentFactory
    lateinit var investmentViewModel: InvestmentViewModel
    lateinit var binding:FragmentVideosBinding
    lateinit var mediaPhotosAdapter: MediaPhotosAdapter

    private var isYoutubeVideo = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentVideosBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initObserver()
    }

    private fun initObserver() {
        val medialist = investmentViewModel.getMediaContent().filter { it.title == "DroneShoots" }
        val list = ArrayList<MediaGalleryItem>()
//        list.add(MediaGalleryItem(1, "Videos"))
        list.add(MediaGalleryItem(2, "Videos"))

        val videoList = arrayListOf<String>()
        for(item in medialist){
            videoList.add(item.media)
        }
        investmentViewModel.getDroneActive().observe(viewLifecycleOwner,Observer{
            when(it){
                true -> {
                    binding.tvNoData.visibility = View.GONE
                    binding.ivNoData.visibility = View.GONE
                    binding.rvMainVideos.visibility = View.VISIBLE
                }
                false -> {
                    binding.tvNoData.visibility = View.VISIBLE
                    binding.ivNoData.visibility = View.VISIBLE
                    binding.rvMainVideos.visibility = View.GONE
                }
            }
        })
        mediaPhotosAdapter =
            MediaPhotosAdapter(
                this.requireContext(),
                list,
                mediaItemClickListener,
                medialist,
                itemClickListener
            )
        binding.rvMainVideos.adapter = mediaPhotosAdapter
    }

    private fun initViewModel() {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        investmentViewModel =
            ViewModelProvider(requireActivity(), investmentFactory)[InvestmentViewModel::class.java]
        (requireActivity() as HomeActivity).showBackArrow()
        (requireActivity() as HomeActivity).showHeader()

    }

    private val mediaItemClickListener = object:MediaItemClickListener{
        override fun onItemClicked(view: View, position: Int, item: MediaViewItem) {

        }
    }

    private val itemClickListener = object : YoutubeItemClickListener {
        override fun onItemClicked(view: View, position: Int, url: String, title: String) {
            when(isYoutubeVideo){
                true -> {
                    val intent = Intent(this@DroneFragment.requireActivity(),YoutubeActivity::class.java)
                    intent.putExtra("YoutubeVideoId",url)
                    intent.putExtra("VideoTitle",title)
                    startActivity(intent)
//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=${url}"))
//                    startActivity(intent)
                }
            }
        }
    }

}