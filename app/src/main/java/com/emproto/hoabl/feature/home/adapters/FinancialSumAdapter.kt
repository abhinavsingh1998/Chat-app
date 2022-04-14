package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.*
import com.emproto.hoabl.model.RecyclerViewItem

class FinancialSumAdapter(private val context: Context, private val list:List<RecyclerViewItem>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val FINANCIAL_SUMMARY_VIEW_TYPE_ONE = 1
        const val FINANCIAL_SUMMARY_VIEW_TYPE_TWO = 2
        const val FINANCIAL_SUMMARY_VIEW_TYPE_THREE = 3
        const val FINANCIAL_SUMMARY_VIEW_TYPE_FOUR = 4
    }

    private lateinit var completedInvestmentAdapter: CompletedInvestmentAdapter
    private lateinit var onItemClickListener : View.OnClickListener
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            FINANCIAL_SUMMARY_VIEW_TYPE_ONE -> { SummaryCompletedInvestmentsViewHolder(FinancialSummaryCard2Binding.inflate(LayoutInflater.from(parent.context),parent,false)) }
            FINANCIAL_SUMMARY_VIEW_TYPE_TWO -> { SummaryOngoingInvestmentsViewHolder(FinancialSummaryCard2Binding.inflate(LayoutInflater.from(parent.context),parent,false))}
            FINANCIAL_SUMMARY_VIEW_TYPE_THREE -> { CompletedInvestmentsViewHolder(CompletedInvestmentsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            else -> { OngoingInvestmentsViewHolder(OngoingInvestmentsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(list[position].viewType) {
            FINANCIAL_SUMMARY_VIEW_TYPE_ONE -> (holder as SummaryCompletedInvestmentsViewHolder).bind(position)
            FINANCIAL_SUMMARY_VIEW_TYPE_TWO -> (holder as SummaryOngoingInvestmentsViewHolder).bind(position)
            FINANCIAL_SUMMARY_VIEW_TYPE_THREE -> (holder as CompletedInvestmentsViewHolder).bind(position)
            FINANCIAL_SUMMARY_VIEW_TYPE_FOUR -> (holder as OngoingInvestmentsViewHolder).bind(position)
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

    private inner class SummaryCompletedInvestmentsViewHolder(private val binding: FinancialSummaryCard2Binding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.contentHeader.text = "Summary - Completed Investments"

            binding.cardView1.setBackgroundResource(R.drawable.financial_summary_dark_card)
            binding.cardView2.setBackgroundResource(R.drawable.financial_summary_dark_card)
            binding.cardView3.setBackgroundResource(R.drawable.financial_summary_dark_card)
            binding.cardView4.setBackgroundResource(R.drawable.financial_summary_dark_card)

            binding.cardName1.setTextColor(ContextCompat.getColor(context,R.color.unselected_button_color))
            binding.cardName2.setTextColor(ContextCompat.getColor(context,R.color.unselected_button_color))
            binding.cardName3.setTextColor(ContextCompat.getColor(context,R.color.unselected_button_color))
            binding.cardName4.setTextColor(ContextCompat.getColor(context,R.color.unselected_button_color))

            binding.contentTxt1.setTextColor(Color.WHITE)
            binding.contentTxt2.setTextColor(Color.WHITE)
            binding.contentTxt3.setTextColor(Color.WHITE)
            binding.contentTxt4.setTextColor(Color.GREEN)

            binding.cardName1.text = "No. of Products"
            binding.cardName2.text = "Area in Sqft"
            binding.cardName3.text = "Amount Invested"
            binding.cardName4.text = "Avg Estimated Appreciation"

            binding.contentTxt1.text = "2"
            binding.contentTxt2.text = "3600"
            binding.contentTxt3.text = "₹ 36,00,000"
            binding.contentTxt4.text = "+4%"

        }
    }

    private inner class SummaryOngoingInvestmentsViewHolder(private val binding: FinancialSummaryCard2Binding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
        }
    }

    private inner class CompletedInvestmentsViewHolder(private val binding: CompletedInvestmentsLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val list = arrayListOf<String>("1","2")
            completedInvestmentAdapter = CompletedInvestmentAdapter(list)
            binding.rvCompletedInvestment.adapter = completedInvestmentAdapter
        }
    }

    private inner class OngoingInvestmentsViewHolder(private val binding: OngoingInvestmentsLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val list = arrayListOf<String>("1")
            completedInvestmentAdapter = CompletedInvestmentAdapter(list)
            binding.rvOngoingInvestment.adapter = completedInvestmentAdapter
            binding.btnManageProjects.setOnClickListener(onItemClickListener)
        }
    }

    fun setItemClickListener(clickListener: View.OnClickListener) {
        onItemClickListener = clickListener
    }

}