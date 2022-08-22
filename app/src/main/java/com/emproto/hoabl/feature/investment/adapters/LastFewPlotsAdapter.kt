package com.emproto.hoabl.feature.investment.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.core.Constants
import com.emproto.core.Utility
import com.emproto.hoabl.databinding.ItemSmartDealsBinding
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.response.investment.PageManagementsOrCollectionOneModel
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

class LastFewPlotsAdapter(
    val context: Context,
    val list: List<PageManagementsOrCollectionOneModel>,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<LastFewPlotsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemSmartDealsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        holder.binding.apply {
            tvItemLocationName.text = element.launchName
            tvItemLocation.text = "${element.address.city}, ${element.address.state}"
            tvItemLocationInfo.text = element.shortDescription
            val amount = element.priceStartingFrom.toDouble() / 100000
            val convertedAmount = String.format("%.0f",amount)
            tvItemAmount.text = SpannableStringBuilder()
                .bold { append("₹${convertedAmount} L") }
                .append(Constants.ONWARDS)
            tvRating.text = "${String.format(" % .0f", element.generalInfoEscalationGraph.estimatedAppreciation)}%"
            tvNoViews.text = element.fomoContent.noOfViews.toString()
            tvItemArea.text = SpannableStringBuilder()
                .bold { append("${element.areaStartingFrom} Sqft") }
                .append(Constants.ONWARDS)
            Glide.with(context)
                .load(element.projectCoverImages.newInvestmentPageMedia.value.url)
                .into(holder.binding.ivItemImage)

            when(element.fomoContent.isTargetTimeActive){
                false -> holder.binding.timerView.visibility = View.GONE
                true -> holder.binding.timerView.visibility = View.VISIBLE
            }

            val timeCounter = object : CountDownTimer(Utility.conversionForTimer(element.fomoContent.targetTime.hours.toString(),element.fomoContent.targetTime.minutes.toString(),
                element.fomoContent.targetTime.seconds.toString()), 1000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {
                    val f = DecimalFormat("00")
                    val fh = DecimalFormat("0")
                    val hour = millisUntilFinished / 3600000 % 24
                    val min = millisUntilFinished / 60000 % 60
                    val sec = millisUntilFinished / 1000 % 60
                    holder.binding.tvDuration.text = "${
                        fh.format(hour).toString() + ":" + f.format(min) + ":" + f.format(sec)
                    } Hrs Left"
                }

                override fun onFinish() {

                }

            }
            timeCounter.start()
        }
        holder.binding.cvTopView.setOnClickListener {
            itemClickListener.onItemClicked(it, 0, element.id.toString())
        }
        holder.binding.tvItemLocationInfo.setOnClickListener {
            itemClickListener.onItemClicked(it, 1, element.id.toString())
        }
        holder.binding.ivBottomArrow.setOnClickListener {
            itemClickListener.onItemClicked(it, 2, element.id.toString())
        }
        holder.binding.tvApplyNow.setOnClickListener {
            itemClickListener.onItemClicked(it, 3, element.id.toString())
        }
        holder.binding.ivBottomOuterArrow.setOnClickListener {
            itemClickListener.onItemClicked(it, 3, element.id.toString())
        }
        holder.binding.clItemInfo.setOnClickListener {
            itemClickListener.onItemClicked(it, 4, element.id.toString())
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: ItemSmartDealsBinding) :
        RecyclerView.ViewHolder(binding.root)

}
