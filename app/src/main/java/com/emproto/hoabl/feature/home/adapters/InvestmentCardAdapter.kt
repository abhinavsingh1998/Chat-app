package com.emproto.hoabl.feature.home.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.ItemSmartDealsBinding
import com.emproto.hoabl.feature.investment.adapters.InvestmentAdapter
import com.emproto.networklayer.response.home.PageManagementsOrNewInvestment

class InvestmentCardAdapter(val context: Context, val list: List<PageManagementsOrNewInvestment>) :
    RecyclerView.Adapter<InvestmentCardAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemSmartDealsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list.get(holder.adapterPosition)
        holder.binding.tvItemLocationName.text = item.launchName
        holder.binding.tvItemLocation.text = item.address.city +","+item.address.state
        holder.binding.tvItemAmount.text = item.priceStartingFrom + " Onwards"
        holder.binding.tvItemArea.text = item.areaStartingFrom + " Onwards"
        holder.binding.tvItemLocationInfo.text = item.shortDescription
        Glide.with(context)
            .load(item.projectCoverImages.homePageMedia.value.url)
            .into(holder.binding.ivItemImage)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(val binding: ItemSmartDealsBinding) :
        RecyclerView.ViewHolder(binding.root)


}