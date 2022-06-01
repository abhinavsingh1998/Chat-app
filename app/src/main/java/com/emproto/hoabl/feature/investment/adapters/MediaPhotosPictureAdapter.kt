package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemPhotosMediaLayoutBinding
import com.emproto.hoabl.model.MediaViewItem
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.utils.MediaItemClickListener
import com.emproto.hoabl.utils.OnRecyclerViewItemClickListener
import com.emproto.networklayer.response.investment.Image

class MediaPhotosPictureAdapter(private val context: Context, private val itemClickListener: MediaItemClickListener, private val itemList: List<MediaViewItem>):RecyclerView.Adapter<MediaPhotosPictureAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ItemPhotosMediaLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        val image:ImageView = itemView.findViewById(R.id.iv_media_photo)
        fun bind(view:View,position:Int,item:MediaViewItem,clickListener: MediaItemClickListener){
            itemView.setOnClickListener{
                clickListener.onItemClicked(view,position,item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaPhotosPictureAdapter.ViewHolder {
        val view = ItemPhotosMediaLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaPhotosPictureAdapter.ViewHolder, position: Int) {
        val element = itemList[position]
        Glide.with(context)
            .load(element.media)
            .into(holder.image)
        holder.bind(holder.itemView,position,element,itemClickListener)
    }

    override fun getItemCount(): Int = itemList.size

//    fun setMediaPhotoItemClickListener(clickListener: View.OnClickListener) {
//        onItemClickListener = clickListener
//    }


}