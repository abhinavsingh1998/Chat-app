package com.emproto.hoabl.feature.investment.adapters

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemSkusBinding
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.networklayer.response.investment.Inventory
import com.emproto.networklayer.response.investment.InventoryBucketContent

class SkuAdapter(
    private val list: List<Inventory>,
    private val itemClickListener: ItemClickListener,
    private val investmentViewModel: InvestmentViewModel
):RecyclerView.Adapter<SkuAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: ItemSkusBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemSkusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        holder.binding.apply {
            tvProjectName.text = element.inventoryBucketName
            val amount = element.priceRange.from.toDouble() / 100000
            val convertedAmount = String.format("%.0f",amount)
            tvStartingAt.text = SpannableStringBuilder()
                .append("Starting at")
                .bold { append(" ₹${convertedAmount} L") }
            tvAreaSkus.text = "${element.areaRange.from} Sqft - ${element.areaRange.to} Sqft"
            //Changing UI corresponding to application
            when (element.isApplied) {
                true -> {
                    tvApply.visibility = View.GONE
                    tvApplied.visibility = View.VISIBLE
                    ivTick.visibility = View.VISIBLE
                }
                false -> {
                    tvApply.visibility = View.VISIBLE
                    tvApplied.visibility = View.GONE
                    ivTick.visibility = View.GONE
                }
            }
        }

        holder.binding.tvApply.setOnClickListener {
            investmentViewModel.setSku(element)
            itemClickListener.onItemClicked(it,position,element.id.toString())
        }
    }

    override fun getItemCount(): Int = list.size

}