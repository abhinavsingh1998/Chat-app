package com.emproto.hoabl.feature.portfolio.adapters

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
import com.emproto.networklayer.response.portfolio.ivdetails.SimilarInvestment
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

class SimilarInvestmentAdapter(
    val context: Context,
    val list: List<SimilarInvestment>,
    val ivInterface: PortfolioSpecificViewAdapter.InvestmentScreenInterface,
    val toShow: Int
) :
    RecyclerView.Adapter<SimilarInvestmentAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemSmartDealsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        holder.binding.apply {
            if (element.projectIcon != null) {
                Glide.with(context)
                    .load(element.projectCoverImages.homePageMedia.value.url)
                    .into(holder.binding.ivItemImage)
            }
            tvItemLocationName.text = element.launchName
            tvItemLocation.text = "${element.address.city},${element.address.state}"

            val amount = element.priceStartingFrom.toDouble() / 100000
            val convertedAmount = amount.toString().replace(".0", "")
            tvItemAmount.text = SpannableStringBuilder()
                .bold { append("₹${convertedAmount} L") }
                .append(Constants.ONWARDS)
            tvItemArea.text = SpannableStringBuilder()
                .bold { append("${element.areaStartingFrom} Sqft") }
                .append(Constants.ONWARDS)

            tvNoViews.text = element.fomoContent.noOfViews.toString()
//                Utility.coolFormat(element.fomoContent.noOfViews.toDouble(), 0)
            tvItemLocationInfo.text = element.shortDescription
            tvRating.text = "${Utility.convertTo(element.estimatedAppreciation)}%"

            when(element.fomoContent.isTargetTimeActive){
                false -> holder.binding.tvDuration.visibility = View.GONE
                true -> holder.binding.tvDuration.visibility = View.VISIBLE
            }

            val timeCounter = object : CountDownTimer(Utility.conversionForTimer(element.fomoContent.targetTime.hours.toString(),element.fomoContent.targetTime.minutes.toString(),
                element.fomoContent.targetTime.seconds.toString()), 1000) {
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

            cvMainOuterCard.setOnClickListener {
                ivInterface.onClickSimilarInvestment(element.id)
            }
            tvApplyNow.setOnClickListener {
                ivInterface.onApplySinvestment(element.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (list.size < toShow)
            list.size
        else
            toShow
    }

    inner class MyViewHolder(var binding: ItemSmartDealsBinding) :
        RecyclerView.ViewHolder(binding.root)

}
