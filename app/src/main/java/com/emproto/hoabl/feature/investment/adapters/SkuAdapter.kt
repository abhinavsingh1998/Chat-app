package com.emproto.hoabl.feature.investment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemSkusBinding
import com.emproto.networklayer.response.investment.InventoryBucketContent

class SkuAdapter(private val list: List<InventoryBucketContent>):RecyclerView.Adapter<SkuAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: ItemSkusBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemSkusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        holder.binding.apply {
            tvProjectName.text = element.name
            tvStartingAt.text = "Starting at ${element.priceRange.from}"
            tvAreaSkus.text = "${element.areaRange.from}ft - ${element.areaRange.to}ft"
        }
    }

    override fun getItemCount(): Int = list.size

}