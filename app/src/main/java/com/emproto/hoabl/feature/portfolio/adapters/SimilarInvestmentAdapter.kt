package com.emproto.hoabl.feature.portfolio.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.core.Utility
import com.emproto.hoabl.databinding.ItemSmartDealsBinding
import com.emproto.networklayer.response.portfolio.ivdetails.SimilarInvestment

class SimilarInvestmentAdapter(
    val context: Context,
    val list: List<SimilarInvestment>,
    val ivInterface: PortfolioSpecificViewAdapter.InvestmentScreenInterface
) :
    RecyclerView.Adapter<SimilarInvestmentAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemSmartDealsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        holder.binding.apply {
            Glide.with(context)
                .load(element.projectIcon.value.url)
                .into(holder.binding.ivItemImage)
            holder.binding.tvItemLocationName.text = element.launchName
            holder.binding.tvItemLocation.text =
                element.address.city + " " + element.address.state
            holder.binding.tvItemAmount.text = element.priceStartingFrom + " Onwards"
            holder.binding.tvItemArea.text = element.areaStartingFrom + " Onwards"
            holder.binding.tvNoViews.text =
                Utility.coolFormat(element.fomoContent.noOfViews.toDouble(), 0)
            holder.binding.tvItemLocationInfo.text = element.shortDescription

            holder.binding.cvMainOuterCard.setOnClickListener {
                ivInterface.onClickSimilarInvestment(element.id)
            }
            holder.binding.tvApplyNow.setOnClickListener {
                ivInterface.onApplySinvestment(element.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: ItemSmartDealsBinding) :
        RecyclerView.ViewHolder(binding.root)

}
