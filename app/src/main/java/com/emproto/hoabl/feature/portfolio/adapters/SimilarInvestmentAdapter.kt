package com.emproto.hoabl.feature.portfolio.adapters

import android.content.Context
import android.graphics.Color
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
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.response.portfolio.ivdetails.SimilarInvestment
import java.text.DecimalFormat
import javax.inject.Inject

class SimilarInvestmentAdapter(
    val context: Context,
    val list: List<SimilarInvestment>,
    private val ivInterface: PortfolioSpecificViewAdapter.InvestmentScreenInterface,
    private val toShow: Int
) :
    RecyclerView.Adapter<SimilarInvestmentAdapter.MyViewHolder>() {
    @Inject
    lateinit var appPreference: AppPreference

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
                tvApplyNow.text="Sold Out"
                tvApplyNow.setTextColor(Color.parseColor("#ffffff"))
                ivBottomOuterArrow.visibility=View.GONE
                tvApplyNow.isClickable=false
                tvApplyNow.isEnabled=false
            }
            if (element.projectIcon != null) {
                Glide.with(context)
                    .load(element.projectCoverImages.homePageMedia.value.url)
                    .into(holder.binding.ivItemImage)
            }
            tvItemLocationName.text = element.launchName
            "${element.address.city},${element.address.state}".also { tvItemLocation.text = it }

            val price = element.priceStartingFrom.toDouble()
            val value = Utility.currencyConversion(price)
            tvItemAmount.text = SpannableStringBuilder()
                .bold { append(value) }
                .append(Constants.ONWARDS)
            tvItemArea.text = SpannableStringBuilder()
                .bold { append("${element.areaStartingFrom} Sqft") }
                .append(Constants.ONWARDS)

            tvNoViews.text = element.fomoContent.noOfViews.toString()
            tvItemLocationInfo.text = element.shortDescription
            "${Utility.convertTo(element.estimatedAppreciation)}%".also { tvRating.text = it }

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
                    "${
                        fh.format(hour).toString() + ":" + f.format(min) + ":" + f.format(sec)
                    } Hrs Left".also { holder.binding.tvDuration.text = it }
                }

                override fun onFinish() {

                }

            }
            timeCounter.start()

            cvMainOuterCard.setOnClickListener {
                ivInterface.onClickSimilarInvestment(element.id)
            }
            tvApplyNow.setOnClickListener {
                ivInterface.onApplyInvestment(element.id)
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
