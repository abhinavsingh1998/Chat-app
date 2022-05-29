package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.ItemSmartDealsBinding
import com.emproto.networklayer.response.investment.PageManagementsOrCollectionOneModel
import com.emproto.networklayer.response.investment.SimilarInvestment

class InvestmentAdapter(val context: Context, val list: List<SimilarInvestment>) : RecyclerView.Adapter<InvestmentAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemSmartDealsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        holder.binding.apply {
            tvNoViews.text = element.fomoContent.noOfViews.toString()
            tvItemLocationName.text = element.launchName
            tvItemLocation.text = "${element.address.city}, ${element.address.state}"
            tvItemAmount.text = "${element.priceStartingFrom} Onwards"
            tvItemArea.text = "${element.areaStartingFrom} Onwards"
            tvItemLocationInfo.text = element.shortDescription
            Glide
                .with(context)
                .load(element.projectCoverImages.newInvestmentPageMedia.value.url)
                .into(ivItemImage)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: ItemSmartDealsBinding) : RecyclerView.ViewHolder(binding.root)

}
