package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.ItemSmartDealsBinding
import com.emproto.networklayer.response.investment.PageManagementsOrCollectionOneModel

class SmartDealsAdapter(val context: Context, val list: List<PageManagementsOrCollectionOneModel>) : RecyclerView.Adapter<SmartDealsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemSmartDealsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        holder.binding.apply {
            tvItemLocationName.text = element.launchName
//            tvItemLocation.text = ""
            tvItemLocationInfo.text = element.shortDescription
            tvItemAmount.text = element.priceRange.from + " Onwards"
            tvNoViews.text = element.fomoContent.noOfViews.toString()
            tvItemArea.text = element.areaRange.from + " Onwards"
            Glide.with(context)
                .load(element.mediaGalleries[0].coverImage[0].mediaContent.value.url)
                .into(holder.binding.ivItemImage)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: ItemSmartDealsBinding) : RecyclerView.ViewHolder(binding.root)

}