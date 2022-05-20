package com.emproto.hoabl.feature.investment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemLandSkusBinding
import com.emproto.hoabl.feature.investment.views.LandSkusFragment
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.response.investment.InventoryBucketContent

class SkusListAdapter(
    private val fragment: LandSkusFragment,
    private val list: List<InventoryBucketContent>,
    val itemClickListener: ItemClickListener
):RecyclerView.Adapter<SkusListAdapter.SkusListViewHolder>() {

    private lateinit var onItemClickListener : View.OnClickListener

    inner class SkusListViewHolder(var binding: ItemLandSkusBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkusListViewHolder {
        val view = ItemLandSkusBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SkusListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkusListViewHolder, position: Int) {
        val element = list[position]
        holder.binding.apply {
            btnApplyNow.setOnClickListener {
                fragment.investmentViewModel.setSku(element)
                itemClickListener.onItemClicked(it,position,element.id.toString())
            }
            tvItemLandSkusName.text = element.name
            tvItemLandSkusArea.text = "${element.areaRange.from}ft - ${element.areaRange.to}ft"
            tvItemLandSkusPrice.text = "${element.priceRange.from} - ${element.priceRange.to}"
            tvItemLandSkusDescription.text = element.shortDescription
        }
    }

    override fun getItemCount(): Int = list.size

    fun setSkusListItemClickListener(clickListener: View.OnClickListener) {
        onItemClickListener = clickListener
    }


}