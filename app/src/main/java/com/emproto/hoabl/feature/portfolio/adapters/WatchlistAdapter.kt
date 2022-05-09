package com.emproto.hoabl.feature.portfolio.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.core.Utility
import com.emproto.hoabl.databinding.ItemSmartDealsBinding
import com.emproto.networklayer.response.investment.PageManagementsOrCollectionOneModel
import com.emproto.networklayer.response.watchlist.Data
import com.emproto.networklayer.response.watchlist.Project

class WatchlistAdapter(val context: Context, val list: List<Data>) :
    RecyclerView.Adapter<WatchlistAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemSmartDealsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        holder.binding.apply {
            Glide.with(context)
                .load(element.project.projectIcon.value.url)
                .into(holder.binding.ivItemImage)
            holder.binding.tvItemLocationName.text = element.project.launchName
            holder.binding.tvItemLocation.text =
                element.project.address.city + " " + element.project.address.state
            holder.binding.tvItemAmount.text = element.project.priceStartingFrom + " Onwards"
            holder.binding.tvItemArea.text = element.project.areaStartingFrom + " Onwards"
            holder.binding.tvNoViews.text =
                Utility.coolFormat(element.project.fomoContent.noOfViews.toDouble(), 0)
            holder.binding.tvItemLocationInfo.text =element.project.shortDescription


        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: ItemSmartDealsBinding) :
        RecyclerView.ViewHolder(binding.root)

}
