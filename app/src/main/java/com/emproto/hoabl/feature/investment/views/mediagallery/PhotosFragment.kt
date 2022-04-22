package com.emproto.hoabl.feature.investment.views.mediagallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
import com.emproto.hoabl.HomeActivity
import com.emproto.hoabl.databinding.FragmentPhotosBinding
import com.emproto.hoabl.feature.investment.adapters.MediaPhotosAdapter
import com.emproto.hoabl.model.MediaGalleryItem
import com.emproto.hoabl.utils.ItemClickListener

class PhotosFragment:BaseFragment() {

    companion object{
        const val TAG = "PhotosFragment"
    }

    lateinit var binding: FragmentPhotosBinding
    lateinit var mediaPhotosAdapter: MediaPhotosAdapter

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
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val list = ArrayList<MediaGalleryItem>()
        list.add(MediaGalleryItem(1,"Photos"))
        list.add(MediaGalleryItem(2,"Photos"))

        mediaPhotosAdapter = MediaPhotosAdapter(list,itemClickListener)
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
            (requireActivity() as HomeActivity).addFragment(MediaViewFragment(), true)
        }
    }

}