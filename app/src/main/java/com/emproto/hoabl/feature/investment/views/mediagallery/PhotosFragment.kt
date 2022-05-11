package com.emproto.hoabl.feature.investment.views.mediagallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.databinding.FragmentPhotosBinding
import com.emproto.hoabl.feature.investment.adapters.MediaPhotosAdapter
import com.emproto.hoabl.model.MediaGalleryItem
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.response.investment.MediaGallery
import com.emproto.networklayer.response.investment.ProjectCoverImages
import java.io.Serializable

class PhotosFragment:BaseFragment() {

    companion object{
        const val TAG = "PhotosFragment"
    }

    lateinit var binding: FragmentPhotosBinding
    lateinit var mediaPhotosAdapter: MediaPhotosAdapter
    lateinit var mediaData:ProjectCoverImages

//    val onPhotosItemClickListener = View.OnClickListener {  view ->
//        when(view.id){
//           R.id.iv_media_photo -> {
//               Log.d(TAG,"photos clicked")
//               val mediaViewFragment = MediaViewFragment()
//               (requireActivity() as HomeActivity).replaceFragment(mediaViewFragment.javaClass, "", true, null, null, 0, false)
//           }
//        }
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPhotosBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaData = (requireParentFragment() as MediaGalleryFragment).data
        setUpRecyclerView(mediaData)
    }

    private fun setUpRecyclerView(mediaData: ProjectCoverImages) {
        val list = ArrayList<MediaGalleryItem>()
        list.add(MediaGalleryItem(1,"Photos"))
        list.add(MediaGalleryItem(2,"Photos"))

        val imageList = arrayListOf<String>()
        imageList.add(mediaData.newInvestmentPageMedia.value.url)

        mediaPhotosAdapter = MediaPhotosAdapter(this.requireContext(),list,itemClickListener,imageList)
        binding.rvMainPhotos.adapter = mediaPhotosAdapter
    }

//    override fun onItemClicked(view: View, position: Int, item: String) {
//        when(view.id){
//            R.id.iv_media_photo -> {
//                Toast.makeText(this.requireContext(), "Photo clicked", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    private val itemClickListener = object : ItemClickListener {
        override fun onItemClicked(view: View, position: Int, item: String) {
            val bundle = Bundle()
            bundle.putString("MediaType","Photo")
//            val list = arrayListOf<String>()
//            list.add()
            bundle.putString("ImageData",mediaData.newInvestmentPageMedia.value.url)
            val mediaViewFragment = MediaViewFragment()
            (requireActivity() as HomeActivity).replaceFragment(mediaViewFragment.javaClass, "", true, bundle, null, 0, false)
        }
    }

}