package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemLandSkusBinding
import com.emproto.hoabl.feature.investment.views.LandSkusFragment
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.response.investment.Inventory

class SkusListAdapter(
    val context: Context,
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
            if (element.isSoldOut) {
                btnApplyNow.text = "Sold Out"
                btnApplyNow.isClickable = false
                btnApplyNow.isEnabled = false
                tvItemLandSkusName.setTextColor(Color.parseColor("#ffffff"))
                tvItemLandSkusArea.setTextColor(Color.parseColor("#ffffff"))
                tvItemLandSkusPrice.setTextColor(Color.parseColor("#ffffff"))
                tvItemLandSkusDescription.setTextColor(Color.parseColor("#ffffff"))
                btnApplyNow.background =
                    ContextCompat.getDrawable(context, R.drawable.all_corner_radius_grey_bg_five)
                clBase.setBackgroundColor(Color.parseColor("#8b8b8b"))
            } else {
                btnApplyNow.isClickable = true
                btnApplyNow.isEnabled = true
                tvItemLandSkusName.setTextColor(Color.parseColor("#000000"))
                tvItemLandSkusArea.setTextColor(Color.parseColor("#494a67"))
                tvItemLandSkusPrice.setTextColor(Color.parseColor("#676ac0"))
                tvItemLandSkusDescription.setTextColor(Color.parseColor("#28282e"))
                btnApplyNow.background =
                    ContextCompat.getDrawable(context, R.drawable.all_corner_radius_black_bg_five)
                clBase.setBackgroundColor(Color.parseColor("#ffffff"))

            }
            btnApplyNow.setOnClickListener {
                fragment.investmentViewModel.setSku(element)
                itemClickListener.onItemClicked(it, position, element.id.toString())
            }
            tvItemLandSkusName.text = element.name
            val landSkusArea = "${element.areaRange?.from} - ${element.areaRange?.to} Sqft"
            tvItemLandSkusArea.text = landSkusArea
            // ..val amount = element.priceRange?.from!!.toDouble() / 100000
            val convertedFromAmount =
                Utility.convertToDecimal(element.priceRange?.from!!.toDouble())
            //val amountTo = element.priceRange!!.to.toDouble() / 100000
            val convertedToAmount = Utility.convertToDecimal(element.priceRange!!.to.toDouble())
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