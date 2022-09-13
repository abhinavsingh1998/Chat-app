package com.emproto.hoabl.feature.investment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.ItemLatestImagesVideosBinding
import com.emproto.hoabl.model.YoutubeModel
import com.emproto.hoabl.utils.YoutubeItemClickListener

class VideoDroneAdapter(
    private val list: ArrayList<YoutubeModel>,
    private val itemClickListener: YoutubeItemClickListener
) : RecyclerView.Adapter<VideoDroneAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: ItemLatestImagesVideosBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemLatestImagesVideosBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        val id = element.url.replace("https://www.youtube.com/embed/", "")
        val youtubeUrl = "https://img.youtube.com/vi/${id}/hqdefault.jpg"
        Glide.with(holder.itemView.context)
            .load(youtubeUrl)
            .into(holder.binding.ivLatestImage)
        holder.binding.ivLatestImage.setOnClickListener {
            itemClickListener.onItemClicked(it, position, id, element.title)
        }
    }

    override fun getItemCount(): Int = list.size
}