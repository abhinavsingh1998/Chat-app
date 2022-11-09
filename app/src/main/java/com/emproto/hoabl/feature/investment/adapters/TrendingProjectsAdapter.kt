package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.graphics.Color
import android.os.CountDownTimer
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.core.Constants
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemSmartDealsBinding
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.response.investment.PageManagementsOrCollectionTwoModel
import java.text.DecimalFormat

class TrendingProjectsAdapter(
    val context: Context,
    val list: List<PageManagementsOrCollectionTwoModel>,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<TrendingProjectsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemSmartDealsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        holder.binding.apply {
            if(element.isSoldOut){
                cvMainOuterCard.setCardBackgroundColor(Color.parseColor("#8b8b8b"))
                clTopImageView.setBackgroundColor(Color.parseColor("#99000000"))
                tvItemLocationInfo.setTextColor(Color.parseColor("#ffffff"))
                holder.binding.tvApplyNow.visibility=View.GONE
                holder.binding.ivBottomArrow.setColorFilter(ContextCompat.getColor(context, R.color.white_s), android.graphics.PorterDuff.Mode.SRC_IN);
                holder.binding.ivBottomOuterArrow.visibility = View.GONE
                holder.binding.tvSoldOut.visibility=View.VISIBLE
                holder.binding.tvSoldOut.isClickable=false
                holder.binding.tvSoldOut.isEnabled=false
            }
            tvItemLocationName.text = element.launchName
            tvItemLocationInfo.text = element.shortDescription
            tvNoViews.text = element.fomoContent.noOfViews.toString()
            val price = element.priceStartingFrom.toDouble()
            val value = Utility.currencyConversion(price)
            tvItemAmount.text = SpannableStringBuilder()
                .bold { append(value) }
                .append(Constants.ONWARDS)
            val rating = "${
                String.format(
                    " % .0f",
                    element.generalInfoEscalationGraph.estimatedAppreciation
                )
            }%"
            tvRating.text = rating
            tvNoViews.text = element.fomoContent.noOfViews.toString()
            tvItemArea.text = SpannableStringBuilder()
                .bold { append("${element.areaStartingFrom} Sqft") }
                .append(Constants.ONWARDS)
            Glide.with(context)
                .load(element.projectCoverImages.newInvestmentPageMedia.value.url)
                .into(holder.binding.ivItemImage)

            when (element.fomoContent.isTargetTimeActive) {
                false -> holder.binding.timerView.visibility = View.GONE
                true -> holder.binding.timerView.visibility = View.VISIBLE
            }

            when (element.fomoContent.isNoOfViewsActive) {
                true -> holder.binding.cvView.visibility = View.VISIBLE
                false -> holder.binding.cvView.visibility = View.GONE
            }

            val timeCounter = object : CountDownTimer(
                Utility.conversionForTimer(
                    element.fomoContent.targetTime.hours.toString(),
                    element.fomoContent.targetTime.minutes.toString(),
                    element.fomoContent.targetTime.seconds.toString()
                ), 1000
            ) {
                override fun onTick(millisUntilFinished: Long) {
                    val f = DecimalFormat("00")
                    val fh = DecimalFormat("0")
                    val hour = millisUntilFinished / 3600000 % 24
                    val min = millisUntilFinished / 60000 % 60
                    val sec = millisUntilFinished / 1000 % 60
                    val duration = "${
                        fh.format(hour).toString() + ":" + f.format(min) + ":" + f.format(sec)
                    } Hrs Left"
                    holder.binding.tvDuration.text = duration
                }

                override fun onFinish() {

                }

            }
            timeCounter.start()
        }
        holder.binding.cvTopView.setOnClickListener {
            itemClickListener.onItemClicked(
                it,
                0,
                element.id.toString())
        }
        holder.binding.tvItemLocationInfo.setOnClickListener {
            itemClickListener.onItemClicked(
                it,
                1,
                element.id.toString())
        }
        holder.binding.ivBottomArrow.setOnClickListener {
            itemClickListener.onItemClicked(
                it,
                2,
                element.id.toString())
        }
        holder.binding.tvApplyNow.setOnClickListener {
            itemClickListener.onItemClicked(
                it,
                3,
                element.id.toString())
        }
        holder.binding.ivBottomOuterArrow.setOnClickListener {
            itemClickListener.onItemClicked(
                it,
                3,
                element.id.toString())
        }
        holder.binding.clItemInfo.setOnClickListener {
            itemClickListener.onItemClicked(
                it,
                4,
                element.id.toString())
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: ItemSmartDealsBinding) :
        RecyclerView.ViewHolder(binding.root)

}
