package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.ItemCurrentInfraStoryLayoutBinding
import com.emproto.hoabl.databinding.ItemSmartDealsBinding
import com.emproto.hoabl.databinding.ItemUpcomingInfraStoryLayoutBinding
import com.emproto.hoabl.utils.Extensions.showHTMLText
import com.emproto.networklayer.response.investment.Story

class UpcomingInfraStoryAdapter(val context: Context, val list: List<Story>) :
    RecyclerView.Adapter<UpcomingInfraStoryAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemUpcomingInfraStoryLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        holder.binding.apply {
            tvImageName.text = element.title
            tvImageInfo.text = context.showHTMLText(element.description)
            Glide.with(context)
                .load(element.media.value.url)
                .into(ivStoryImage)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: ItemUpcomingInfraStoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
}
