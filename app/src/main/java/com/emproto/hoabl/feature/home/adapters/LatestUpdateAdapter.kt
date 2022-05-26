package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.ItemLatestUpdatesBinding
import com.emproto.networklayer.response.home.PageManagementOrLatestUpdate

class LatestUpdateAdapter(
    val context: Context,
    val list: List<PageManagementOrLatestUpdate>,
    val itemInterface: ItemInterface) : RecyclerView.Adapter<LatestUpdateAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemLatestUpdatesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list.get(holder.adapterPosition)
        holder.binding.title.text = item.displayTitle
        holder.binding.tvName.text= item.subTitle
        holder.binding.description.text= item.detailedInfo[0].description

        if (item.detailedInfo[0].media!= null){
            Glide.with(context).load(item.detailedInfo[0]?.media?.value.url)
                .into(holder.binding.image)

        }

        holder.binding.rootView.setOnClickListener {
            itemInterface.onClickItem(holder.adapterPosition)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(val binding: ItemLatestUpdatesBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface ItemInterface {
        fun onClickItem(position: Int)
    }
}
