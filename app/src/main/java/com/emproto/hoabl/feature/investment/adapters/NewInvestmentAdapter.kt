package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.core.Utility
import com.emproto.hoabl.databinding.*
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.response.investment.Data

class NewInvestmentAdapter(private val activity:HomeActivity, private val context: Context, val list:List<RecyclerViewItem>, private val data:Data,private val itemClickListener: ItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_NEW_LAUNCH = 1
        const val TYPE_LAST_PLOTS = 2
        const val TYPE_TRENDING_PROJECTS = 3
    }

    private lateinit var adapter: InvestmentViewPagerAdapter
    private lateinit var lastFewPlotsAdapter: LastFewPlotsAdapter
    private lateinit var trendingProjectsAdapter: TrendingProjectsAdapter
    private lateinit var onItemClickListener : View.OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            TYPE_NEW_LAUNCH -> { NewLaunchViewHolder(NewInvestmentTopLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)) }
            TYPE_LAST_PLOTS -> { LastFewPlotsViewHolder(LastFewPlotsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            else -> { TrendingProjectsViewHolder(TrendingProjectsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)) }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(list[position].viewType){
            TYPE_NEW_LAUNCH -> { (holder as NewLaunchViewHolder).bind(position)}
            TYPE_LAST_PLOTS -> { (holder as LastFewPlotsViewHolder).bind(position)}
            TYPE_TRENDING_PROJECTS -> { (holder as TrendingProjectsViewHolder).bind(position)}
        }
    }

    override fun getItemCount(): Int = list.size

    private inner class NewLaunchViewHolder(private val binding: NewInvestmentTopLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.tvNewLaunch.text = data.page.newInvestments.displayName
            binding.tvComingSoon.text = data.page.newInvestments.subHeading
            binding.tvInvestmentProjectName.text = data.page.pageManagementsOrNewInvestments[0].launchName
            binding.tvAmount.text = data.page.pageManagementsOrNewInvestments[0].priceStartingFrom + " Onwards"
            binding.tvArea.text = data.page.pageManagementsOrNewInvestments[0].areaStartingFrom + " Onwards"
            binding.tvBackgroundGrey.text = data.page.pageManagementsOrNewInvestments[0].shortDescription
            binding.tvViewInfo.text = SpannableStringBuilder()
                .bold { append("${Utility.coolFormat(data.page.pageManagementsOrNewInvestments[0].fomoContent.noOfViews.toDouble(),0)} People") }
                .append( " saw this project in ${data.page.pageManagementsOrNewInvestments[0].fomoContent.days} days" )

            val listViews = ArrayList<String>()
            listViews.add(data.page.pageManagementsOrNewInvestments[0].projectCoverImages.newInvestmentPageMedia.value.url)
            adapter = InvestmentViewPagerAdapter(listViews)
            binding.viewPager.adapter = adapter

            Glide.with(context)
                .load(data.page.pageManagementsOrNewInvestments[0].projectCoverImages.newInvestmentPageMedia.value.url)
                .into(binding.ivSmallImage)

            binding.tvNewLaunchSeeAll.setOnClickListener(onItemClickListener)
            binding.clPlaceInfo.setOnClickListener(onItemClickListener)
            binding.tvApplyNow.setOnClickListener(onItemClickListener)
            binding.ivDontMissImage.setOnClickListener(onItemClickListener)
            binding.btnDiscover.setOnClickListener(onItemClickListener)
        }
    }

    private inner class LastFewPlotsViewHolder(private val binding: LastFewPlotsLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.tvSmartDealsTitle.text = data.page.collectionOne.displayName
            binding.tvSmartDealsSubtitle.text = data.page.collectionOne.subHeading

            val list = data.pageManagementsOrCollectionOneModels
            lastFewPlotsAdapter = LastFewPlotsAdapter(context, list,itemClickListener)
            binding.rvSmartDealsNv.adapter = lastFewPlotsAdapter

            binding.tvSmartDealsSeeAll.setOnClickListener(onItemClickListener)
        }
    }

//    private inner class SmartDealsViewHolder(private val binding: SmartDealsLayoutBinding):RecyclerView.ViewHolder(binding.root){
//        fun bind(position: Int){
//            binding.tvSmartDealsTitle.text = data.page.collectionOne.displayName
//            binding.tvSmartDealsSubtitle.text = data.page.collectionOne.subHeading
//            binding.tvSmartDealsSeeAll.setOnClickListener(onItemClickListener)
//
//            val list = data.pageManagementsOrCollectionOneModels
//            smartDealsAdapter = LastFewPlotsAdapter(context, list)
//            smartDealsLinearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
//            binding.rvSmartDealsNv.layoutManager = smartDealsLinearLayoutManager
//            binding.rvSmartDealsNv.adapter = smartDealsAdapter
//        }
//    }

    private inner class TrendingProjectsViewHolder(private val binding: TrendingProjectsLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.tvTrendingProjectsTitle.text = data.page.collectionTwo.displayName
            binding.tvTrendingProjectsSubtitle.text = data.page.collectionTwo.subHeading

            val list = data.pageManagementsOrCollectionTwoModels
            trendingProjectsAdapter = TrendingProjectsAdapter(context, list,itemClickListener)
            binding.rvTrendingProjects.adapter= trendingProjectsAdapter
            binding.tvTrendingProjectsSeeAll.setOnClickListener(onItemClickListener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

    fun setItemClickListener(clickListener: View.OnClickListener) {
        onItemClickListener = clickListener
    }

}