package com.emproto.hoabl.feature.investment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemLandSkusAppliedBinding
import com.emproto.hoabl.databinding.ItemLandSkusBinding
import com.emproto.hoabl.feature.investment.views.LandSkusFragment
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.response.investment.Inventory

class SkusListAppliedAdapter(private val fragment: LandSkusFragment, private val list: List<Inventory>,
                             val itemClickListener: ItemClickListener
):RecyclerView.Adapter<SkusListAppliedAdapter.SkusListAppliedViewHolder>() {

    private lateinit var onItemClickListener : View.OnClickListener

    inner class SkusListAppliedViewHolder(var binding: ItemLandSkusAppliedBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkusListAppliedViewHolder {
        val view = ItemLandSkusAppliedBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SkusListAppliedViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkusListAppliedViewHolder, position: Int) {
        val element = list[position]
        holder.binding.apply {
            tvItemLandSkusName.text = element.inventoryBucketName
            tvItemLandSkusArea.text = "${element.areaRange.from}ft - ${element.areaRange.to}ft"
            tvItemLandSkusPrice.text = "${element.priceRange.from} - ${element.priceRange.to}"
            tvItemLandSkusDescription.text = element.inventoryBucketDescription
        }
    }

    override fun getItemCount(): Int = list.size

    fun setSkusListItemClickListener(clickListener: View.OnClickListener) {
        onItemClickListener = clickListener
    }


}