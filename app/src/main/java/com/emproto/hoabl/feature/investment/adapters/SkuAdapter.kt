package com.emproto.hoabl.feature.investment.adapters

import android.graphics.Color
import android.content.Context
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
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
        val element = list!![position]
        holder.binding.apply {
            if(element.isApplied && (element.isSoldOut || !element.isSoldOut)){
                    tvApply.visibility = View.GONE
                    tvApplied.visibility = View.VISIBLE
                    ivTick.visibility = View.VISIBLE
            }
            else if((!element.isApplied ||element.isApplied)&& element.isSoldOut){
                tvApply.visibility = View.VISIBLE
                tvApplied.visibility = View.GONE
                ivTick.visibility = View.GONE
                tvApply.isClickable=false
                tvApply.isEnabled=false
                tvApply.text="Sold Out"
                tvProjectName.setTextColor(Color.parseColor("#ffffff"))
                tvStartingAt.setTextColor(Color.parseColor("#ffffff"))
                tvAreaSkus.setTextColor(Color.parseColor("#ffffff"))
                tvApply.setTextColor(Color.parseColor("#000000"))
                clBase.setBackgroundColor(Color.parseColor("#8b8b8b"))
            }
            else{
                tvApply.visibility = View.VISIBLE
                    tvApplied.visibility = View.GONE
                    ivTick.visibility = View.GONE
            }

            tvProjectName.text = element.name
            val amount = element.priceRange?.from!!.toDouble() / 100000
            val convertedAmount = String.format("%.0f", amount)
            tvStartingAt.text = SpannableStringBuilder()
                .append("Starting at")
                .bold { append(" ₹${convertedAmount} L") }
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