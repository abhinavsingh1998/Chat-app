package com.emproto.hoabl.feature.home.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.core.Constants
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemSmartDealsBinding
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.response.home.Data
import com.emproto.networklayer.response.home.PageManagementsOrNewInvestment
import java.text.DecimalFormat

class InvestmentCardAdapter(
    val context: Context,
    val itemCount: Data,
    val list: List<PageManagementsOrNewInvestment>,
    val itemInterface: ItemClickListener

) :
    RecyclerView.Adapter<InvestmentCardAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemSmartDealsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        val item = list[holder.adapterPosition]

        if (item != null) {
            if(item.isSoldOut){
                holder.binding.cvMainOuterCard.setCardBackgroundColor(Color.parseColor("#8b8b8b"))
                holder.binding.clTopImageView.setBackgroundColor(Color.parseColor("#99000000"))
                holder.binding.tvItemLocationInfo.setTextColor(Color.parseColor("#ffffff"))
                holder.binding.tvApplyNow.visibility=View.GONE
                holder.binding.ivBottomOuterArrow.visibility = View.GONE
                holder.binding.tvSoldOut.visibility=View.VISIBLE
                holder.binding.tvSoldOut.isClickable=false
                holder.binding.tvSoldOut.isEnabled=false
                holder.binding.ivBottomArrow.setColorFilter(ContextCompat.getColor(context, R.color.white_s), android.graphics.PorterDuff.Mode.SRC_IN);

            }
            holder.binding!!.tvItemLocationName.text = item.launchName
            holder.binding.tvItemLocation.text = item.address.city + "," + item.address.state
            holder.binding.tvRating.text = Utility.convertTo(item.generalInfoEscalationGraph.estimatedAppreciation) + "%"
            holder.binding.tvNoViews.text = item.fomoContent.noOfViews.toString()
            val price = item.priceStartingFrom.toDouble()
            val value = Utility.currencyConversion(price)
            holder.binding.tvItemAmount.text = value.toString()+Constants.ONWARDS

            holder.binding.tvItemArea.text = item.areaStartingFrom + Constants.SQFT_ONWARDS
            holder.binding.tvItemLocationInfo.text = item.shortDescription


            when (item.fomoContent.isTargetTimeActive) {
                false -> holder.binding.timerView.visibility = View.GONE
                true -> holder.binding.timerView.visibility = View.VISIBLE
            }

            when (item.fomoContent.isNoOfViewsActive) {
                true -> holder.binding.cvView.visibility = View.VISIBLE
                false -> holder.binding.cvView.visibility = View.GONE
            }

            val timeCounter = object : CountDownTimer(
                Utility.conversionForTimer(
                    item.fomoContent.targetTime.hours.toString(),
                    item.fomoContent.targetTime.minutes.toString(),
                    item.fomoContent.targetTime.seconds.toString()
                ), 1000
            ) {
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

            holder.binding.cvTopView.setOnClickListener {
                itemInterface.onItemClicked(
                    it,
                    position,
                    item.id.toString()
                )
            }
            holder.binding.tvApplyNow.setOnClickListener {
                itemInterface.onItemClicked(
                    it,
                    position,
                    item.id.toString())
            }
            holder.binding.tvItemLocationInfo.setOnClickListener {
                itemInterface.onItemClicked(
                    it,
                    position,
                    item.id.toString()
                )
            }
            holder.binding.ivBottomArrow.setOnClickListener {
                itemInterface.onItemClicked(
                    it,
                    position,
                    item.id.toString())
            }
            Glide.with(context)
                .load(item.projectCoverImages.homePageMedia.value.url)
                .into(holder.binding.ivItemImage)
        }

    }




    override fun getItemCount(): Int {
        val itemList = if (itemCount.page.totalProjectsOnHomeScreen < list.size) {
            itemCount.page.totalProjectsOnHomeScreen
        } else {
            list.size
        }
        return itemList
    }

    inner class MyViewHolder(val binding: ItemSmartDealsBinding) :
        RecyclerView.ViewHolder(binding.root)


}