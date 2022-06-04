package com.emproto.hoabl.feature.investment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            tvStartingAt.text = "Starting at ₹${convertedAmount} L"
            tvAreaSkus.text = "${element.areaRange.from} Sqft - ${element.areaRange.to} Sqft"
        }
        holder.binding.clOuterItemSkus.setOnClickListener {
            investmentViewModel.setSku(element)
            itemClickListener.onItemClicked(it,position,element.id.toString())
        }
    }

    override fun getItemCount(): Int = list.size

}