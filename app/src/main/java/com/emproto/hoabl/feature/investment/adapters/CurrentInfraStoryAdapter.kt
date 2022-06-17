package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.ItemCurrentInfraStoryLayoutBinding
import com.emproto.hoabl.databinding.ItemSmartDealsBinding
import com.emproto.networklayer.response.investment.Story

class CurrentInfraStoryAdapter(val context: Context,val list: List<Story>) : RecyclerView.Adapter<CurrentInfraStoryAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemCurrentInfraStoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        holder.apply {
            binding.tvImageName.text = element.title
            binding.tvImageInfo.text = element.description
            Glide.with(context)
                .load(element.media.value.url)
                .into(binding.ivStoryImage)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: ItemCurrentInfraStoryLayoutBinding) : RecyclerView.ViewHolder(binding.root)

}
