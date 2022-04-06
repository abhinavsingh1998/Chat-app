package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.KeyPillarsLayoutBinding
import com.emproto.hoabl.databinding.PortfolioInvestmentCardBinding
import com.emproto.hoabl.databinding.ProjectDetailTopLayoutBinding
import com.emproto.hoabl.model.FinancialSummaryItems
import com.emproto.hoabl.model.FinancialSummaryList
import com.emproto.hoabl.model.ProjectDetailItem

class PortfolioFinacialSummaryAdapter (private val context: Context, private val list:List<FinancialSummaryList>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
        const val VIEW_TYPE_THREE = 3
        const val VIEW_TYPE_FOUR = 4

    }
    private lateinit var investmentAdapterAdapter: PortfolioInvestmentCardAdapter


//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//
//
////        return when(viewType) {
////            VIEW_TYPE_ONE -> { TestimonialsTopViewHolder(PortfolioInvestmentCardBinding.inflate(
////                LayoutInflater.from(parent.context),parent,false)) }
////            else -> {
////
////            }
////        }
//
//    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(list[position].viewType){
            VIEW_TYPE_ONE -> { (holder as TestimonialsTopViewHolder).bind(position)}

        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

    private inner class TestimonialsTopViewHolder(private val binding:PortfolioInvestmentCardBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(position: Int){
            fun initData(): ArrayList<FinancialSummaryItems> {
                val itemList: ArrayList<FinancialSummaryItems> = ArrayList<FinancialSummaryItems>()

                itemList.add(FinancialSummaryItems("No. of Products", "2",
                    "Area in Sqft", "3600",
                    "Amount Invested", "36,00,000",
                    "Avg Estimated Appreciation", "+4%"))
                itemList.add(FinancialSummaryItems("No. of Products", "2",
                    "Sqft Applied", "3600",
                    "Amount Invested", "36,00,000",
                    "Amount Pending", "36,00,000"))

                return itemList
            }

            investmentAdapterAdapter = PortfolioInvestmentCardAdapter(context,initData())
            binding.financialRecycler.adapter = investmentAdapterAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

}