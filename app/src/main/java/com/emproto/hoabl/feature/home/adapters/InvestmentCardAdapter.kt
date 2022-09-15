package com.emproto.hoabl.feature.home.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.core.Constants
import com.emproto.core.Utility
import com.emproto.hoabl.databinding.ItemSmartDealsBinding
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.response.home.Data
import com.emproto.networklayer.response.home.PageManagementsOrNewInvestment
import java.text.DecimalFormat

class InvestmentCardAdapter(
    val context: Context,
    val itemCount: Data,
    val list: List<PageManagementsOrNewInvestment>,
    private val itemIntrface: ItemClickListener
) :
    RecyclerView.Adapter<InvestmentCardAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemSmartDealsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        val item = list[holder.adapterPosition]

        if(item!=null){
            holder.binding!!.tvItemLocationName.text = item.launchName
            holder.binding.tvItemLocation.text = item.address.city + "," + item.address.state
            holder.binding.tvRating.text = Utility.convertTo(item.generalInfoEscalationGraph.estimatedAppreciation) + "%"
            holder.binding.tvNoViews.text= item.fomoContent.noOfViews.toString()
//        holder.binding.tvNoViews.text = Utility.coolFormat(item.fomoContent.noOfViews.toDouble(), 0)

            val amount = item.priceStartingFrom.toDouble() / 100000
            val convertedAmount = amount.toString().replace(".0", "")
            holder.binding.tvItemAmount.text = "₹$convertedAmount L Onwards"
            holder.binding.tvItemArea.text = item.areaStartingFrom + Constants.SQFT_ONWARDS
            holder.binding.tvItemLocationInfo.text = item.shortDescription


            when(item.fomoContent.isTargetTimeActive){
                false -> holder.binding.timerView.visibility = View.GONE
                true -> holder.binding.timerView.visibility = View.VISIBLE
            }

            when(item.fomoContent.isNoOfViewsActive){
                true -> holder.binding.cvView.visibility = View.VISIBLE
                false -> holder.binding.cvView.visibility = View.GONE
            }

            val timeCounter = object : CountDownTimer(Utility.conversionForTimer(item.fomoContent.targetTime.hours.toString(),item.fomoContent.targetTime.minutes.toString(),
                item.fomoContent.targetTime.seconds.toString()), 1000) {
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
                itemIntrface.onItemClicked(it, position, item.id.toString())
            }
            holder.binding.tvApplyNow.setOnClickListener {
                itemIntrface.onItemClicked(it, position, item.id.toString())
            }
            holder.binding.tvItemLocationInfo.setOnClickListener {
                itemIntrface.onItemClicked(it, position, item.id.toString())
            }
            holder.binding.ivBottomArrow.setOnClickListener {
                itemIntrface.onItemClicked(it, position, item.id.toString())
            }
            Glide.with(context)
                .load(item.projectCoverImages.homePageMedia.value.url)
                .into(holder.binding.ivItemImage)
        }

    }

    override fun getItemCount(): Int {
        var itemList = 0
        itemList = if (itemCount.page.totalProjectsOnHomeScreen < list.size) {
            itemCount.page.totalProjectsOnHomeScreen
        } else {
            list.size
        }
        return itemList
        return itemCount.page.totalProjectsOnHomeScreen
    }

    inner class MyViewHolder(val binding: ItemSmartDealsBinding) :
        RecyclerView.ViewHolder(binding.root)


}