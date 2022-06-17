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
import com.emproto.hoabl.databinding.ItemVideosMediaLayoutBinding
import com.emproto.hoabl.model.MediaViewItem
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.utils.MediaItemClickListener
import com.emproto.hoabl.utils.OnRecyclerViewItemClickListener
import com.emproto.hoabl.utils.YoutubeItemClickListener
import com.emproto.networklayer.response.investment.Image

class MediaItemVideosAdapter(private val context: Context, private val itemClickListener: YoutubeItemClickListener, private val itemList: List<MediaViewItem>):RecyclerView.Adapter<MediaItemVideosAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ItemVideosMediaLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        val image:ImageView = itemView.findViewById(R.id.iv_media_video)
        fun bind(view:View,position:Int,url:String,name: String){
            itemView.setOnClickListener{
                itemClickListener.onItemClicked(view,position,url,name)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemVideosAdapter.ViewHolder {
        val view = ItemVideosMediaLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaItemVideosAdapter.ViewHolder, position: Int) {
        val element = itemList[position]
        val url = element.media.replace("https://www.youtube.com/embed/","")
        val youtubeUrl = "https://img.youtube.com/vi/${url}/hqdefault.jpg"
        Glide.with(context)
            .load(youtubeUrl)
            .into(holder.image)
        holder.binding.tvVideoName.text = element.name
        holder.bind(holder.itemView,position,url,element.name)
    }

    override fun getItemCount(): Int = itemList.size

}