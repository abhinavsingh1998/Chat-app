package com.emproto.hoabl.feature.investment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.CustomImageLayoutBinding
import com.emproto.hoabl.databinding.ItemLatestImagesVideosBinding
import com.emproto.hoabl.model.ViewItem

class VideoDroneAdapter(private val list:List<ViewItem>):RecyclerView.Adapter<VideoDroneAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: ItemLatestImagesVideosBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemLatestImagesVideosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        Glide.with(holder.itemView.context)
            .load(element.imageDrawable)
            .into(holder.binding.ivLatestImage)
    }

    override fun getItemCount(): Int = list.size
}