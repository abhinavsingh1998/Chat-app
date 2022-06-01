package com.emproto.hoabl.feature.investment.views.mediagallery

import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.databinding.FragmentPhotosBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.investment.adapters.MediaPhotosAdapter
import com.emproto.hoabl.model.MediaGalleryItem
import com.emproto.hoabl.model.MediaViewItem
import com.emproto.hoabl.utils.MediaItemClickListener
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import javax.inject.Inject

class PhotosFragment : BaseFragment() {

    companion object {
        const val TAG = "PhotosFragment"
    }

    @Inject
    lateinit var investmentFactory: InvestmentFactory
    lateinit var investmentViewModel: InvestmentViewModel
    lateinit var binding: FragmentPhotosBinding
    lateinit var mediaPhotosAdapter: MediaPhotosAdapter

    private var allImageList = ArrayList<MediaViewItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotosBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initObserver()
    }

    private fun initViewModel() {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        investmentViewModel =
            ViewModelProvider(
                requireActivity(),
                investmentFactory
            ).get(InvestmentViewModel::class.java)
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.imageBack.visibility =
            View.VISIBLE
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility =
            View.VISIBLE
    }

    private fun initObserver() {
        val list1 = investmentViewModel.getMediaContent().filter { it.title == "Images" }
        setUpRecyclerView(list1)
//        investmentViewModel.getMedia().observe(viewLifecycleOwner, Observer {
//            setUpRecyclerView(it)
//        })
    }

    private fun setUpRecyclerView(list1: List<MediaViewItem>) {
        val list = ArrayList<MediaGalleryItem>()
        list.add(MediaGalleryItem(1, "Photos"))
        list.add(MediaGalleryItem(2, "Photos"))

        val listImage = ArrayList<MediaViewItem>()
        listImage.add(MediaViewItem("image", "https://www.gstatic.com/webp/gallery/1.jpg", id=10, title = "Images"))
        listImage.add(MediaViewItem("image", "https://www.gstatic.com/webp/gallery/2.jpg",id=11, title = "Images"))
        listImage.add(MediaViewItem("image", "https://www.gstatic.com/webp/gallery/1.jpg",id=12, title = "Images"))
        listImage.add(MediaViewItem("image", "https://www.gstatic.com/webp/gallery/2.jpg",id=13, title = "Images"))
        listImage.add(MediaViewItem("image", "https://www.gstatic.com/webp/gallery/1.jpg",id=14, title = "Images"))
        listImage.add(MediaViewItem("image", "https://www.gstatic.com/webp/gallery/2.jpg",id=15, title = "Images"))
        allImageList.clear()
        for(item in list1){
            allImageList.add(item)
        }
        Log.d("jshdjshds",allImageList.toString())
        mediaPhotosAdapter =
            MediaPhotosAdapter(this.requireContext(), list, itemClickListener, list1)
        binding.rvMainPhotos.adapter = mediaPhotosAdapter
    }

    private val itemClickListener = object : MediaItemClickListener {
        override fun onItemClicked(view: View, position: Int, item: MediaViewItem) {
            Log.d("jdjisjd",item.toString())
            investmentViewModel.setMediaItem(item)
            investmentViewModel.setMediaListItem(allImageList)
            investmentViewModel.setMediaListPosition(position)
            val mediaViewFragment = MediaViewFragment()
            (requireActivity() as HomeActivity).addFragment(mediaViewFragment, false)
        }
    }

}