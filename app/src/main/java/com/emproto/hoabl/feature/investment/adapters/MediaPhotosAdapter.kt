package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.*
import com.emproto.hoabl.model.MediaGalleryItem
import com.emproto.hoabl.model.MediaViewItem
import com.emproto.hoabl.utils.MediaItemClickListener
import com.emproto.hoabl.utils.YoutubeItemClickListener

class MediaPhotosAdapter(
    private val context: Context,
    private val itemList: List<MediaGalleryItem>,
    private val clickListener: MediaItemClickListener,
    private val mediaData: List<MediaViewItem>,
    private val youtubeItemClickListener: YoutubeItemClickListener
):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        const val PHOTOS_VIEW_TYPE_ONE = 1
        const val PHOTOS_VIEW_TYPE_TWO = 2
    }

    private lateinit var mediaPhotosFilterAdapter: MediaPhotosFilterAdapter
    private lateinit var mediaPhotosPictureAdapter: MediaPhotosPictureAdapter
    private lateinit var mediaItemVideosAdapter: MediaItemVideosAdapter

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
                    val imageList = mediaData
                    mediaPhotosPictureAdapter = MediaPhotosPictureAdapter(context,clickListener,imageList)
                    val gridLayoutManager = GridLayoutManager(context,2)
                    binding.rvPhotos.layoutManager = gridLayoutManager
                    binding.rvPhotos.adapter = mediaPhotosPictureAdapter
                }
                "Videos" -> {
                    val videoList = mediaData
                    mediaItemVideosAdapter = MediaItemVideosAdapter(context,youtubeItemClickListener,videoList)
                    val linearLayoutManager = LinearLayoutManager(context)
                    binding.rvPhotos.layoutManager = linearLayoutManager
                    binding.rvPhotos.adapter = mediaItemVideosAdapter
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return itemList[position].viewType
    }
}