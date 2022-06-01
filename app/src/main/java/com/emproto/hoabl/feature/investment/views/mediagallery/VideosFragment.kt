package com.emproto.hoabl.feature.investment.views.mediagallery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.databinding.FragmentVideosBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.investment.adapters.MediaPhotosAdapter
import com.emproto.hoabl.model.MediaGalleryItem
import com.emproto.hoabl.model.MediaViewItem
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.utils.MediaItemClickListener
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import javax.inject.Inject

class VideosFragment:BaseFragment() {

    @Inject
    lateinit var investmentFactory: InvestmentFactory
    lateinit var investmentViewModel: InvestmentViewModel
    lateinit var binding:FragmentVideosBinding
    lateinit var mediaPhotosAdapter: MediaPhotosAdapter

    private var isYoutubeVideo = true

    private var videoList = ArrayList<MediaViewItem>()

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
        val medialist = investmentViewModel.getMediaContent().filter { it.title == "Videos" }
        val list = ArrayList<MediaGalleryItem>()
        list.add(MediaGalleryItem(1, "Videos"))
        list.add(MediaGalleryItem(2, "Videos"))

        for(item in medialist){
            videoList.add(item)
        }
        videoList.add(MediaViewItem(mediaType = "video",media = "https://www.youtube.com/embed/nc5Lj90BzSQ","",33,"Videos"))
        videoList.add(MediaViewItem(mediaType = "video",media = "https://www.youtube.com/embed/g0W0s_Z6Je4","",35,"Videos"))
        mediaPhotosAdapter =
            MediaPhotosAdapter(this.requireContext(), list, itemClickListener, medialist)
        binding.rvMainVideos.adapter = mediaPhotosAdapter
    }

    private fun initViewModel() {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        investmentViewModel =
            ViewModelProvider(requireActivity(), investmentFactory)[InvestmentViewModel::class.java]
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.imageBack.visibility = View.VISIBLE
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility = View.VISIBLE

    }

    private val itemClickListener = object : MediaItemClickListener {
        override fun onItemClicked(view: View, position: Int, item: MediaViewItem) {
            when(isYoutubeVideo){
                true -> {
                    val intent = Intent(this@VideosFragment.requireActivity(),YoutubeActivity::class.java)
                    intent.putExtra("YoutubeVideoId","Bl1FOKpFY2Q")
                    intent.putExtra("VideoTitle","")
                    intent.putExtra("VideoList",videoList)
                    startActivity(intent)
                }
                false -> {
                    investmentViewModel.setMediaItem(item)
                    val mediaViewFragment = MediaViewFragment()
                    (requireActivity() as HomeActivity).replaceFragment(mediaViewFragment.javaClass, "", true, null, null, 0, false)
                }
            }
        }
    }
}