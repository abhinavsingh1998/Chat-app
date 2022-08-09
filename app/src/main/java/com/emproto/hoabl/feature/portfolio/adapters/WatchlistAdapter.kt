package com.emproto.hoabl.feature.portfolio.adapters

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.core.Utility
import com.emproto.hoabl.databinding.ItemSmartDealsBinding
import com.emproto.networklayer.response.watchlist.Data

class WatchlistAdapter(
    val context: Context,
    val list: List<Data>,
    val onItemClickListener: ExistingUsersPortfolioAdapter.ExistingUserInterface
) :
    RecyclerView.Adapter<WatchlistAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemSmartDealsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        holder.binding.apply {
            if (element.project != null) {
                Glide.with(context)
                    .load(element.project.projectCoverImages.homePageMedia.value.url)
                    .into(ivItemImage)
                tvItemLocationName.text = element.project.launchName
                tvItemLocation.text =
                    element.project.address.city + " " + element.project.address.state
                val amount = element.project.priceStartingFrom.toDouble() / 100000
                val convertedAmount = String.format("%.0f",amount)
                tvItemAmount.text = SpannableStringBuilder()
                    .bold { append("₹${convertedAmount} L") }
                    .append(" Onwards")
                tvItemArea.text = SpannableStringBuilder()
                    .bold { append("${element.project.areaStartingFrom} Sqft") }
                    .append(" Onwards")
                tvNoViews.text = element.project.fomoContent.noOfViews.toString()
//                Utility.coolFormat(element.project.fomoContent.noOfViews.toDouble(), 0)
                tvItemLocationInfo.text = element.project.shortDescription
                tvRating.text = "${Utility.convertTo(element.project.estimatedAppreciation)}%"
            }
        }
        holder.binding.cvMainOuterCard.setOnClickListener {
            onItemClickListener.onClickofWatchlist(element.project.id)
        }
        holder.binding.tvApplyNow.setOnClickListener {
            onItemClickListener.onClickApplyNow(element.project.id)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: ItemSmartDealsBinding) :
        RecyclerView.ViewHolder(binding.root)

}
