package com.emproto.hoabl.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.ItemLatestUpdatesBinding
import com.emproto.hoabl.databinding.ItemSmartDealsBinding
import com.emproto.networklayer.response.home.PageManagementOrLatestUpdate
import com.emproto.networklayer.response.home.PageManagementsOrNewInvestment

class LatestUpdateAdapter(val context: Context, val list: List<PageManagementOrLatestUpdate>) : RecyclerView.Adapter<LatestUpdateAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemLatestUpdatesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list.get(holder.adapterPosition)

        holder.binding.title.text = item.displayTitle
        holder.binding.tvLocation.text= item.subTitle.toString()
        holder.binding.description.text= item.detailedInfo[0].description


    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(val binding: ItemLatestUpdatesBinding) :
        RecyclerView.ViewHolder(binding.root)


}
