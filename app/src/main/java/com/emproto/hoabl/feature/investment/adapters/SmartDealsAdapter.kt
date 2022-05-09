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
            tvItemLocation.text = "${element.address.city}, ${element.address.state}"
            tvItemLocationInfo.text = element.shortDescription
            tvItemAmount.text = element.priceStartingFrom + " Onwards"
            tvNoViews.text = element.fomoContent.noOfViews.toString()
            tvItemArea.text = element.areaStartingFrom + " Onwards"
            Glide.with(context)
                .load(element.projectCoverImages.newInvestmentPageMedia.value.url)
                .into(holder.binding.ivItemImage)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: ItemSmartDealsBinding) : RecyclerView.ViewHolder(binding.root)

}
