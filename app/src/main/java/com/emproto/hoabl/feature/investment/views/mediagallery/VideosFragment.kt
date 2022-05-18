package com.emproto.hoabl.feature.investment.views.mediagallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.databinding.FragmentPhotosBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.investment.adapters.MediaPhotosAdapter
import com.emproto.hoabl.model.MediaGalleryItem
import com.emproto.hoabl.model.MediaViewItem
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import javax.inject.Inject

class VideosFragment:BaseFragment() {

    @Inject
    lateinit var investmentFactory: InvestmentFactory
    lateinit var investmentViewModel: InvestmentViewModel
    lateinit var binding:FragmentPhotosBinding
    lateinit var mediaPhotosAdapter: MediaPhotosAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPhotosBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initObserver()
    }

    private fun initObserver() {
        investmentViewModel.getMedia().observe(viewLifecycleOwner, Observer {
            val list = ArrayList<MediaGalleryItem>()
            list.add(MediaGalleryItem(1,"Videos"))
            list.add(MediaGalleryItem(2,"Videos"))

            val videoList = arrayListOf<String>(it.newInvestmentPageMedia.value.url)
            mediaPhotosAdapter = MediaPhotosAdapter(this.requireContext(),list, itemClickListener, videoList)
            binding.rvMainPhotos.adapter = mediaPhotosAdapter
        })
    }

    private fun initViewModel() {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        investmentViewModel =
            ViewModelProvider(requireActivity(), investmentFactory)[InvestmentViewModel::class.java]
    }

    private val itemClickListener = object : ItemClickListener {
        override fun onItemClicked(view: View, position: Int, item: String) {
            val mediaViewItem = MediaViewItem("Video",item)
            investmentViewModel.setMediaItem(mediaViewItem)
            val mediaViewFragment = MediaViewFragment()
            (requireActivity() as HomeActivity).replaceFragment(mediaViewFragment.javaClass, "", true, null, null, 0, false)
        }
    }
}