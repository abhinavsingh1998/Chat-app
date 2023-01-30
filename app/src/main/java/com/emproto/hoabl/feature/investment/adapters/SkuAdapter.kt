package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.Constants
import com.emproto.core.Utility
import com.emproto.hoabl.databinding.ItemSkusBinding
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.response.investment.Inventory
import javax.inject.Inject

class SkuAdapter(
//    var context: Context,
    val context: Context,
    private val list: List<Inventory>?,
    private val itemClickListener: ItemClickListener,
    private val investmentViewModel: InvestmentViewModel
) : RecyclerView.Adapter<SkuAdapter.MyViewHolder>() {
    @Inject
    lateinit var appPreference: AppPreference

    inner class MyViewHolder(var binding: ItemSkusBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemSkusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list!![holder.adapterPosition]
        holder.binding.apply {

            tvProjectName.text = element.name

            if (element.priceRange != null) {
                val price = element.priceRange?.from!!.toDouble()
                val value = Utility.currencyConversion(price)
                tvStartingAt.text = SpannableStringBuilder()
                    .append("Starting at" + "  ")
                    .bold { append(value.toString() + Constants.ONWARDS) }
                tvApply.visibility = View.VISIBLE
            } else {
                tvStartingAt.text = "undefined"
                tvApply.visibility = View.GONE
            }

            if (element.isApplied) {
                tvApply.visibility = View.INVISIBLE
                tvApplied.visibility = View.VISIBLE
                ivTick.visibility = View.VISIBLE
            } else if (element.isSoldOut) {
                tvApply.visibility = View.VISIBLE
                tvApplied.visibility = View.GONE
                ivTick.visibility = View.GONE
                tvApply.isClickable = false
                tvApply.isEnabled = false
                tvApply.text = "Sold Out"
                tvApply.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                tvApply.setTextColor(Color.parseColor("#ffffff"))
                tvProjectName.setTextColor(Color.parseColor("#ffffff"))
                tvStartingAt.setTextColor(Color.parseColor("#ffffff"))
                tvAreaSkus.setTextColor(Color.parseColor("#ffffff"))
                tvApply.setTextColor(Color.parseColor("#ffffff"))
                clBase.setBackgroundColor(Color.parseColor("#8b8b8b"))
            } else {
                tvApply.visibility = View.VISIBLE
                tvApplied.visibility = View.GONE
                ivTick.visibility = View.GONE
            }



            val areaSkus = "${element.areaRange?.from} - ${element.areaRange?.to} Sqft"
            tvAreaSkus.text = areaSkus
            //Changing UI corresponding to application
//            when (element.isApplied) {
//                true -> {
//                    tvApply.visibility = View.GONE
//                    tvApplied.visibility = View.VISIBLE
//                    ivTick.visibility = View.VISIBLE
//                }
//                false -> {
//                    tvApply.visibility = View.VISIBLE
//                    tvApplied.visibility = View.GONE
//                    ivTick.visibility = View.GONE
//                }
//                else -> {}
//            }
        }



        holder.binding.tvApply.setOnClickListener {
            investmentViewModel.setSku(element)
            itemClickListener.onItemClicked(it, position, element.id.toString())
        }
    }

    override fun getItemCount(): Int = list!!.size

}