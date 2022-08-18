package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.os.CountDownTimer
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.core.text.color
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.core.Constants
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemSmartDealsBinding
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.utils.SimilarInvItemClickListener
import com.emproto.networklayer.response.investment.PageManagementsOrCollectionOneModel
import com.emproto.networklayer.response.investment.SimilarInvestment
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

class SimlInvestmentAdapter(
    val context: Context,
    val list: List<SimilarInvestment>,
    val itemClickListener: SimilarInvItemClickListener
) : RecyclerView.Adapter<SimlInvestmentAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemSmartDealsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        holder.binding.apply {
            tvNoViews.text = element.fomoContent.noOfViews.toString()
            tvItemLocationName.text = element.launchName
            tvItemLocation.text = "${element.address?.city}, ${element.address?.state}"
            val amount = element.priceStartingFrom!!.toDouble() / 100000
            val convertedAmount = amount.toString().replace(".0", "")
            tvItemAmount.text = SpannableStringBuilder()
                .bold { append("₹${convertedAmount} L") }
                .append(Constants.ONWARDS)
            tvItemArea.text = SpannableStringBuilder()
                .bold { append("${element.areaStartingFrom} Sqft") }
                .append(Constants.ONWARDS)
            tvItemLocationInfo.text = element.shortDescription
            Glide
                .with(context)
                .load(element.projectCoverImages?.newInvestmentPageMedia?.value?.url)
                .into(ivItemImage)

            when(element.fomoContent.isTargetTimeActive){
                false -> holder.binding.tvDuration.visibility = View.GONE
                true -> holder.binding.tvDuration.visibility = View.VISIBLE
            }

            val timeCounter = object : CountDownTimer(Utility.conversionForTimer(element.fomoContent.targetTime.hours.toString(),
                element.fomoContent.targetTime.minutes.toString(), element.fomoContent.targetTime.seconds.toString()), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val f = DecimalFormat("00")
                    val fh = DecimalFormat("0")
                    val hour = millisUntilFinished / 3600000 % 24
                    val min = millisUntilFinished / 60000 % 60
                    val sec = millisUntilFinished / 1000 % 60
                    Log.d("hello","${fh.format(hour)}, ${f.format(min)}, ${f.format(sec)}")
                    holder.binding.tvDuration.text = "${
                        fh.format(hour).toString() + ":" + f.format(min) + ":" + f.format(sec)
                    } Hrs Left"
                }

                override fun onFinish() {

                }

            }
            timeCounter.start()

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

    inner class MyViewHolder(var binding: ItemSmartDealsBinding) :
        RecyclerView.ViewHolder(binding.root)

}
