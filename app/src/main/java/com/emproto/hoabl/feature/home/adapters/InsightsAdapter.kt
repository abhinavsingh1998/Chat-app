package com.emproto.hoabl.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.ItemInsightsBinding
import com.emproto.hoabl.databinding.ItemProjectsUpdatesBinding
import com.emproto.hoabl.databinding.ItemPromisesBinding
import com.emproto.networklayer.response.home.HomePagesOrPromise
import com.emproto.networklayer.response.home.PageManagementOrInsight

class InsightsAdapter(val context: Context, val list: List<PageManagementOrInsight>) : RecyclerView.Adapter<InsightsAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemInsightsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list.get(holder.adapterPosition)
        holder.binding.tvVideotitle.text= item.displayTitle
        Glide.with(context)
            .load(item.insightsMedia[0].media.value.url)
            .into(holder.binding.image)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(val binding: ItemInsightsBinding) :
        RecyclerView.ViewHolder(binding.root)


}
