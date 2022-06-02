package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.core.text.color
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemSmartDealsBinding
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.utils.SimilarInvItemClickListener
import com.emproto.networklayer.response.investment.PageManagementsOrCollectionOneModel
import com.emproto.networklayer.response.investment.SimilarInvestment

class InvestmentAdapter(
    val context: Context,
    val list: List<SimilarInvestment>,
    val itemClickListener: SimilarInvItemClickListener
) : RecyclerView.Adapter<InvestmentAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemSmartDealsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        holder.binding.apply {
            tvNoViews.text = Utility.coolFormat(element.fomoContent.noOfViews.toDouble(),0)
            tvItemLocationName.text = element.launchName
            tvItemLocation.text = "${element.address.city}, ${element.address.state}"
            tvItemAmount.text = SpannableStringBuilder()
                .bold { append(element.priceStartingFrom) }
                .append(" Onwards")
            tvItemArea.text = SpannableStringBuilder()
                .bold { append(element.areaStartingFrom) }
                .append(" Onwards")
            tvItemLocationInfo.text = element.shortDescription
            Glide
                .with(context)
                .load(element.projectCoverImages.newInvestmentPageMedia.value.url)
                .into(ivItemImage)

            cvTopView.setOnClickListener {
                itemClickListener.onItemClicked(it, position, element.id.toString())
            }
            tvItemLocationInfo.setOnClickListener {
                itemClickListener.onItemClicked(it, position, element.id.toString())
            }
            ivBottomArrow.setOnClickListener {
                itemClickListener.onItemClicked(it, position, element.id.toString())
            }
            tvApplyNow.setOnClickListener {
                itemClickListener.onItemClicked(it, position, element.id.toString())
            }
            clItemInfo.setOnClickListener {
                itemClickListener.onItemClicked(it, position, element.id.toString())
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: ItemSmartDealsBinding) : RecyclerView.ViewHolder(binding.root)

}
