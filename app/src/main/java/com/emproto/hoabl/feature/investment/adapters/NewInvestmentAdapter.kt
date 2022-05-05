package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.*
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.networklayer.response.investment.Data

class NewInvestmentAdapter(private val activity:HomeActivity, private val context: Context, val list:List<RecyclerViewItem>, private val data:Data):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val INVESTMENT_VIEW_TYPE_ONE = 1
        const val INVESTMENT_VIEW_TYPE_TWO = 2
        const val INVESTMENT_VIEW_TYPE_THREE = 3
        const val INVESTMENT_VIEW_TYPE_FOUR = 4
    }

    private lateinit var adapter: InvestmentViewPagerAdapter
    private lateinit var smartDealsAdapter: SmartDealsAdapter
    private lateinit var smartDealsLinearLayoutManager: LinearLayoutManager
    private lateinit var trendingProjectsLinearLayoutManager: LinearLayoutManager
    private lateinit var onItemClickListener : View.OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            INVESTMENT_VIEW_TYPE_ONE -> { InvestmentTopViewHolder(NewInvestmentTopLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)) }
            INVESTMENT_VIEW_TYPE_TWO -> { DontMissViewHolder(DontMissLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            INVESTMENT_VIEW_TYPE_THREE -> { SmartDealsViewHolder(SmartDealsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            else -> { TrendingProjectsViewHolder(TrendingProjectsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(list[position].viewType){
            INVESTMENT_VIEW_TYPE_ONE -> { (holder as InvestmentTopViewHolder).bind(position)}
            INVESTMENT_VIEW_TYPE_TWO -> { (holder as DontMissViewHolder).bind(position)}
            INVESTMENT_VIEW_TYPE_THREE -> { (holder as SmartDealsViewHolder).bind(position)}
            INVESTMENT_VIEW_TYPE_FOUR -> { (holder as TrendingProjectsViewHolder).bind(position)}
        }
    }

    override fun getItemCount(): Int = list.size

    private inner class InvestmentTopViewHolder(private val binding: NewInvestmentTopLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val listViews = ArrayList<String>()
            listViews.add(data.pageManagementsOrNewInvestments[0].mediaGalleries[0].coverImage[0].mediaContent.value.url)
            listViews.add(data.pageManagementsOrNewInvestments[0].mediaGalleries[0].coverImage[0].mediaContent.value.url)
            listViews.add(data.pageManagementsOrNewInvestments[0].mediaGalleries[0].coverImage[0].mediaContent.value.url)
            listViews.add(data.pageManagementsOrNewInvestments[0].mediaGalleries[0].coverImage[0].mediaContent.value.url)
            listViews.add(data.pageManagementsOrNewInvestments[0].mediaGalleries[0].coverImage[0].mediaContent.value.url)
            adapter = InvestmentViewPagerAdapter(listViews)
            binding.viewPager.adapter = adapter
            binding.ivSmallImage.setImageResource(R.drawable.new_investment_page_image)

            binding.tvNewLaunch.text = data.newInvestments.displayName
            binding.tvComingSoon.text = data.newInvestments.subHeading
            binding.tvInvestmentProjectName.text = data.pageManagementsOrNewInvestments[0].launchName
            binding.tvAmount.text = data.pageManagementsOrNewInvestments[0].priceRange.from + " Onwards"
            binding.tvArea.text = data.pageManagementsOrNewInvestments[0].areaRange.from + " Onwards"
            binding.tvBackgroundGrey.text = data.pageManagementsOrNewInvestments[0].shortDescription
            binding.tvViewInfo.text = "${data.pageManagementsOrNewInvestments[0].fomoContent.noOfViews} People saw this project in ${data.pageManagementsOrNewInvestments[0].fomoContent.days} days"
            Glide.with(context)
                .load(data.promotionAndOffersMedia.value.url)
                .into(binding.ivDontMissImage)
        }
    }

    private inner class DontMissViewHolder(private val binding: DontMissLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){

        }
    }

    private inner class SmartDealsViewHolder(private val binding: SmartDealsLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.tvSmartDealsTitle.text = data.collectionOne.displayName
            binding.tvSmartDealsSubtitle.text = data.collectionOne.subHeading
            binding.tvSmartDealsSeeAll.setOnClickListener(onItemClickListener)

            val list = data.pageManagementsOrCollectionOneModels
            smartDealsAdapter = SmartDealsAdapter(context, list)
            smartDealsLinearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            binding.rvSmartDealsNv.layoutManager = smartDealsLinearLayoutManager
            binding.rvSmartDealsNv.adapter = smartDealsAdapter
        }
    }

    private inner class TrendingProjectsViewHolder(private val binding: TrendingProjectsLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.tvTrendingProjectsTitle.text = data.collectionTwo.displayName
            binding.tvTrendingProjectsSubtitle.text = data.collectionTwo.subHeading

            val list = data.pageManagementsOrCollectionTwoModels
            smartDealsAdapter = SmartDealsAdapter(context, list)
            trendingProjectsLinearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            binding.rvTrendingProjects.layoutManager= trendingProjectsLinearLayoutManager
            binding.rvTrendingProjects.adapter= smartDealsAdapter
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