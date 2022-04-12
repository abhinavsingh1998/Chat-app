package com.emproto.hoabl.feature.investment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemPhotosMediaLayoutBinding
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.utils.OnRecyclerViewItemClickListener

class MediaPhotosPictureAdapter(private val itemList:List<Int>,private val itemClickListener : ItemClickListener):RecyclerView.Adapter<MediaPhotosPictureAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ItemPhotosMediaLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(view:View,position:Int,item:String,clickListener: ItemClickListener){
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
        val list = itemList[position]
        holder.binding.ivMediaPhoto.setImageResource(itemList[position])
        holder.bind(holder.itemView,position,list.toString(),itemClickListener)
    }

    override fun getItemCount(): Int = itemList.size

//    fun setMediaPhotoItemClickListener(clickListener: View.OnClickListener) {
//        onItemClickListener = clickListener
//    }


}