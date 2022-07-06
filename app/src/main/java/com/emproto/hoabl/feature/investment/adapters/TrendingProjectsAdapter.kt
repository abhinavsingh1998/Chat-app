package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
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
//            tvNoViews.text = Utility.coolFormat(element.fomoContent.noOfViews.toDouble(),0)
            tvNoViews.text = element.fomoContent.noOfViews.toString()
            val amount = element.priceStartingFrom.toDouble() / 100000
            val convertedAmount = amount.toString().replace(".0","")
            tvItemAmount.text = SpannableStringBuilder()
                .bold { append("₹${convertedAmount} L") }
                .append(" Onwards")
            tvRating.text = "${String.format(" % .0f",element.generalInfoEscalationGraph.estimatedAppreciation)}%"
            tvNoViews.text = Utility.coolFormat(element.fomoContent.noOfViews.toDouble(),0)
            tvItemArea.text = SpannableStringBuilder()
                .bold { append("${element.areaStartingFrom} Sqft") }
                .append(" Onwards")
//            Glide.with(context)
//                .load(element.mediaGalleries[0].coverImage[0].mediaContent.value.url)
//                .into(holder.binding.ivItemImage)
            Glide.with(context)
                .load(element.projectCoverImages.newInvestmentPageMedia.value.url)
                .into(holder.binding.ivItemImage)
        }
        holder.binding.cvTopView.setOnClickListener {
            itemClickListener.onItemClicked(it, 0, element.id.toString())
        }
        holder.binding.tvItemLocationInfo.setOnClickListener {
            itemClickListener.onItemClicked(it, 1, element.id.toString())
        }
        holder.binding.ivBottomArrow.setOnClickListener {
            itemClickListener.onItemClicked(it, 2, element.id.toString())
        }
        holder.binding.tvApplyNow.setOnClickListener {
            itemClickListener.onItemClicked(it, 3, element.id.toString())
        }
        holder.binding.ivBottomOuterArrow.setOnClickListener {
            itemClickListener.onItemClicked(it, 3, element.id.toString())
        }
        holder.binding.clItemInfo.setOnClickListener {
            itemClickListener.onItemClicked(it, 4, element.id.toString())
        }
//        when(holder.absoluteAdapterPosition){
//            1 -> {
//                holder.binding.cvDurationView.visibility = View.VISIBLE
//                holder.binding.tvDuration.text = "${element.fomoContent.targetTime.hours}:${element.fomoContent.targetTime.minutes}:${element.fomoContent.targetTime.seconds} Hrs Left"
//            }
//        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: ItemSmartDealsBinding) : RecyclerView.ViewHolder(binding.root)

}
