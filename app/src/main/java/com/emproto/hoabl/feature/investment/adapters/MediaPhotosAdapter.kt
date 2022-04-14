package com.emproto.hoabl.feature.investment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.*
import com.emproto.hoabl.feature.investment.views.mediagallery.PhotosFragment
import com.emproto.hoabl.feature.investment.views.mediagallery.VideosFragment
import com.emproto.hoabl.model.MediaGalleryItem
import com.emproto.hoabl.utils.ItemClickListener

class MediaPhotosAdapter(private val fragment:PhotosFragment,private val itemList:List<MediaGalleryItem>,private val clickListener: ItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        const val PHOTOS_VIEW_TYPE_ONE = 1
        const val PHOTOS_VIEW_TYPE_TWO = 2
    }

    private lateinit var mediaPhotosFilterAdapter: MediaPhotosFilterAdapter
    private lateinit var mediaPhotosPictureAdapter: MediaPhotosPictureAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            PHOTOS_VIEW_TYPE_ONE -> FilterViewHolder(MediaPhotosFilterLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            else -> PhotoViewHolder(MediaPhotosPictureLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(itemList[position].viewType){
            PHOTOS_VIEW_TYPE_ONE -> (holder as FilterViewHolder).bind(position)
            PHOTOS_VIEW_TYPE_TWO -> (holder as PhotoViewHolder).bind(position)
        }
    }

    override fun getItemCount(): Int = itemList.size

    private inner class FilterViewHolder(val binding: MediaPhotosFilterLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val list = arrayListOf<String>("All Photos","Plots","Roads","Amenities","Hospitals","Swimming pools","Transport","HeliPad")
            mediaPhotosFilterAdapter = MediaPhotosFilterAdapter(list)
            binding.rvFilterPhotos.adapter = mediaPhotosFilterAdapter
        }
    }

    private inner class PhotoViewHolder(val binding:MediaPhotosPictureLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            when(itemList[position].type){
                "Photos" -> {
                    val list = arrayListOf<Int>(R.drawable.media_photo_isle_bliss,R.drawable.media_photo_beach,
                        R.drawable.media_photo_mountain,R.drawable.media_photo_beach,R.drawable.media_photo_isle_bliss,
                        R.drawable.media_photo_mountain,R.drawable.media_photo_beach,R.drawable.media_photo_isle_bliss,
                        R.drawable.media_photo_mountain,R.drawable.media_photo_beach,R.drawable.media_photo_isle_bliss,
                        R.drawable.media_photo_mountain,R.drawable.media_photo_beach,R.drawable.media_photo_isle_bliss)
                    mediaPhotosPictureAdapter = MediaPhotosPictureAdapter(list,clickListener)
                    binding.rvPhotos.adapter = mediaPhotosPictureAdapter
//                    mediaPhotosPictureAdapter.setMediaPhotoItemClickListener(fragment.onPhotosItemClickListener)
                }
                "Videos" -> {
                    val list = arrayListOf<Int>(R.drawable.media_video_beach_image,R.drawable.media_video_isle_image,
                        R.drawable.media_video_mountain_image,R.drawable.media_video_beach_image,R.drawable.media_video_mountain_image,
                        R.drawable.media_video_beach_image,R.drawable.media_video_beach_image,R.drawable.media_video_isle_image,
                        R.drawable.media_video_mountain_image,R.drawable.media_video_beach_image,R.drawable.media_video_mountain_image,
                        R.drawable.media_video_isle_image,R.drawable.media_video_mountain_image,R.drawable.media_video_isle_image)
                    mediaPhotosPictureAdapter = MediaPhotosPictureAdapter(list,clickListener)
                    binding.rvPhotos.adapter = mediaPhotosPictureAdapter
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return itemList[position].viewType
    }
}