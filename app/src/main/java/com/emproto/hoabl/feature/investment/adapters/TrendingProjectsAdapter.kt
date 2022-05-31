package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.core.Utility
import com.emproto.hoabl.databinding.ItemSmartDealsBinding
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.response.investment.PageManagementsOrCollectionOneModel
import com.emproto.networklayer.response.investment.PageManagementsOrCollectionTwoModel

class TrendingProjectsAdapter(val context: Context, val list: List<PageManagementsOrCollectionTwoModel>,private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<TrendingProjectsAdapter.MyViewHolder>() {

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
            tvItemAmount.text = element.priceStartingFrom + " Onwards"
            tvNoViews.text = Utility.coolFormat(element.fomoContent.noOfViews.toDouble(),0)
            tvItemArea.text = element.areaStartingFrom + " Onwards"
//            Glide.with(context)
//                .load(element.mediaGalleries[0].coverImage[0].mediaContent.value.url)
//                .into(holder.binding.ivItemImage)
            Glide.with(context)
                .load(element.projectCoverImages.newInvestmentPageMedia.value.url)
                .into(holder.binding.ivItemImage)
        }
        holder.binding.ivBottomArrow.setOnClickListener {
            itemClickListener.onItemClicked(it, position, element.id.toString())
        }
        holder.binding.tvItemLocationInfo.setOnClickListener {
            itemClickListener.onItemClicked(it, position, element.id.toString())
        }
        holder.binding.tvApplyNow.setOnClickListener {
            itemClickListener.onItemClicked(it, position, element.id.toString())
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: ItemSmartDealsBinding) : RecyclerView.ViewHolder(binding.root)

}
