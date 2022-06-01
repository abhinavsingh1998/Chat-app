package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.InsightsListItemBinding
import com.emproto.networklayer.response.home.PageManagementOrInsight
import com.emproto.networklayer.response.insights.Data

class AllInsightsAdapter(
    val context: Context,
    val list: List<Data>,
    val itemInterface: InsightsItemsInterface
    ) : RecyclerView.Adapter<AllInsightsAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = InsightsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list.get(holder.adapterPosition)
        holder.binding.title.text= item.displayTitle
        Glide.with(context)
            .load(item.insightsMedia[0].media.value.url)
            .into(holder.binding.locationImage)
        if (item.insightsMedia[0].description.isNullOrEmpty()){
            holder.binding.btnReadMore.isVisible= false
            holder.binding.deatils.text= item.insightsMedia[0].description
        }else{
            holder.binding.deatils.text= item.insightsMedia[0].description
            holder.binding.btnReadMore.isVisible= true
        }


        //holder.binding.arrowImage.setImageResource(item.arrowImage)
        holder.binding.rootView.setOnClickListener {
            itemInterface.onClickItem(holder.adapterPosition)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(val binding: InsightsListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface InsightsItemsInterface {
        fun onClickItem(position: Int)
    }
}