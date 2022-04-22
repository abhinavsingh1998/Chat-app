package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.*
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.model.ViewItem

class NewInvestmentAdapter(private val context: Context, val list:List<RecyclerViewItem>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val INVESTMENT_VIEW_TYPE_ONE = 1
        const val INVESTMENT_VIEW_TYPE_TWO = 2
        const val INVESTMENT_VIEW_TYPE_THREE = 3
        const val INVESTMENT_VIEW_TYPE_FOUR = 4
    }

    private lateinit var adapter: InvestmentViewPagerAdapter
    private lateinit var investmentAdapter: InvestmentAdapter
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
            val listViews = ArrayList<ViewItem>()
            listViews.add(ViewItem(1, R.drawable.new_investment_page_image))
            listViews.add(ViewItem(2, R.drawable.new_investment_page_image))
            listViews.add(ViewItem(3, R.drawable.new_investment_page_image))
            listViews.add(ViewItem(4, R.drawable.new_investment_page_image))
            listViews.add(ViewItem(5, R.drawable.new_investment_page_image))
            adapter = InvestmentViewPagerAdapter(listViews)
            binding.viewPager.adapter = adapter
            binding.ivSmallImage.setImageResource(R.drawable.new_investment_page_image)
        }
    }

    private inner class DontMissViewHolder(private val binding: DontMissLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){

        }
    }

    private inner class SmartDealsViewHolder(private val binding: SmartDealsLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val list: ArrayList<String> = ArrayList()
            list.add("22L-2.5 Cr")
            list.add("22L-2.5 Cr")
            list.add("22L-2.5 Cr")
            list.add("22L-2.5 Cr")
            list.add("22L-2.5 Cr")

            investmentAdapter = InvestmentAdapter(context, list)
            smartDealsLinearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            binding.rvSmartDealsNv.layoutManager = smartDealsLinearLayoutManager
            binding.rvSmartDealsNv.adapter = investmentAdapter
            binding.tvSmartDealsSeeAll.setOnClickListener(onItemClickListener)
        }
    }

    private inner class TrendingProjectsViewHolder(private val binding: TrendingProjectsLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val list: ArrayList<String> = ArrayList()
            list.add("22L-2.5 Cr")
            list.add("22L-2.5 Cr")
            list.add("22L-2.5 Cr")
            list.add("22L-2.5 Cr")
            list.add("22L-2.5 Cr")

            investmentAdapter = InvestmentAdapter(context, list)
            trendingProjectsLinearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            binding.rvTrendingProjects.layoutManager= trendingProjectsLinearLayoutManager
            binding.rvTrendingProjects.adapter= investmentAdapter
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