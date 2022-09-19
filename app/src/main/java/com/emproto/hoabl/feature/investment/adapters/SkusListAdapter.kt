package com.emproto.hoabl.feature.investment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemLandSkusBinding
import com.emproto.hoabl.feature.investment.views.LandSkusFragment
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.response.investment.Inventory

class SkusListAdapter(
    private val fragment: LandSkusFragment,
    private val list: List<Inventory>,
    val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<SkusListAdapter.SkusListViewHolder>() {

    private lateinit var onItemClickListener: View.OnClickListener

    inner class SkusListViewHolder(var binding: ItemLandSkusBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkusListViewHolder {
        val view = ItemLandSkusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SkusListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkusListViewHolder, position: Int) {
        val element = list[position]
        holder.binding.apply {
            btnApplyNow.setOnClickListener {
                fragment.investmentViewModel.setSku(element)
                itemClickListener.onItemClicked(it, position, element.id.toString())
            }
            tvItemLandSkusName.text = element.name
            val landSkusArea = "${element.areaRange?.from} - ${element.areaRange?.to} Sqft"
            tvItemLandSkusArea.text = landSkusArea
            val amount = element.priceRange?.from!!.toDouble() / 100000
            val convertedFromAmount = String.format("%.0f", amount)
            val amountTo = element.priceRange!!.to.toDouble() / 100000
            val convertedToAmount = String.format("%.0f", amountTo)
            val itemLandSkusPrice = "₹${convertedFromAmount}L - ${convertedToAmount}L"
            tvItemLandSkusPrice.text = itemLandSkusPrice
            tvItemLandSkusDescription.text = element.shortDescription
        }
    }

    override fun getItemCount(): Int = list.size

    fun setSkusListItemClickListener(clickListener: View.OnClickListener) {
        onItemClickListener = clickListener
    }


}