package com.emproto.hoabl.feature.portfolio.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.*
import com.emproto.hoabl.feature.investment.adapters.InvestmentAdapter
import com.emproto.hoabl.feature.portfolio.views.PortfolioExistingUsersFragment
import com.emproto.hoabl.model.RecyclerViewItem

class ExistingUsersAdapter(private val fragment:PortfolioExistingUsersFragment,private val context: Context, private val list:List<RecyclerViewItem>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val PORTFOLIO_EXISTING_USERS_VIEW_TYPE_ONE = 1
        const val PORTFOLIO_EXISTING_USERS_VIEW_TYPE_TWO = 2
        const val PORTFOLIO_EXISTING_USERS_VIEW_TYPE_THREE = 3
        const val PORTFOLIO_EXISTING_USERS_VIEW_TYPE_FOUR = 4
        const val PORTFOLIO_EXISTING_USERS_VIEW_TYPE_FIVE = 5
        const val PORTFOLIO_EXISTING_USERS_VIEW_TYPE_SIX = 6
        const val PORTFOLIO_EXISTING_USERS_VIEW_TYPE_SEVEN = 7
        const val PORTFOLIO_EXISTING_USERS_VIEW_TYPE_EIGHT = 8
    }

    private lateinit var completedInvestmentAdapter: CompletedInvestmentAdapter
    private lateinit var onItemClickListener : View.OnClickListener
    private lateinit var investmentAdapter: InvestmentAdapter
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            PORTFOLIO_EXISTING_USERS_VIEW_TYPE_ONE -> { TitleViewHolder(PortfolioTitleCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)) }
            PORTFOLIO_EXISTING_USERS_VIEW_TYPE_TWO -> { SummaryCompletedInvestmentsViewHolder(FinancialSummaryCard2Binding.inflate(LayoutInflater.from(parent.context),parent,false)) }
            PORTFOLIO_EXISTING_USERS_VIEW_TYPE_THREE -> { SummaryOngoingInvestmentsViewHolder(FinancialSummaryCard2Binding.inflate(LayoutInflater.from(parent.context),parent,false))}
            PORTFOLIO_EXISTING_USERS_VIEW_TYPE_FOUR -> { CompletedInvestmentsViewHolder(CompletedInvestmentsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            PORTFOLIO_EXISTING_USERS_VIEW_TYPE_FIVE -> { OngoingInvestmentsViewHolder(OngoingInvestmentsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            PORTFOLIO_EXISTING_USERS_VIEW_TYPE_SIX -> { BlockchainViewHolder(BlockchainLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            PORTFOLIO_EXISTING_USERS_VIEW_TYPE_SEVEN -> { MyWatchListViewHolder(SmartDealsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            else -> { PortfolioReferViewHolder(PortfolioReferLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(list[position].viewType) {
            PORTFOLIO_EXISTING_USERS_VIEW_TYPE_ONE -> (holder as TitleViewHolder).bind(position)
            PORTFOLIO_EXISTING_USERS_VIEW_TYPE_TWO -> (holder as SummaryCompletedInvestmentsViewHolder).bind(position)
            PORTFOLIO_EXISTING_USERS_VIEW_TYPE_THREE -> (holder as SummaryOngoingInvestmentsViewHolder).bind(position)
            PORTFOLIO_EXISTING_USERS_VIEW_TYPE_FOUR -> (holder as CompletedInvestmentsViewHolder).bind(position)
            PORTFOLIO_EXISTING_USERS_VIEW_TYPE_FIVE -> (holder as OngoingInvestmentsViewHolder).bind(position)
            PORTFOLIO_EXISTING_USERS_VIEW_TYPE_SIX -> (holder as BlockchainViewHolder).bind(position)
            PORTFOLIO_EXISTING_USERS_VIEW_TYPE_SEVEN -> (holder as MyWatchListViewHolder).bind(position)
            PORTFOLIO_EXISTING_USERS_VIEW_TYPE_EIGHT -> (holder as PortfolioReferViewHolder).bind(position)
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

    private inner class TitleViewHolder(private val binding: PortfolioTitleCardBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){

        }
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
            binding.contentTxt4.setTextColor(ContextCompat.getColor(context,R.color.portfolio_blue_color))

            binding.cardName1.text = "No. of Products"
            binding.cardName2.text = "Area in Sqft"
            binding.cardName3.text = "Amount Invested"
            binding.cardName4.text = "Avg Estimated Appreciation"

            binding.contentTxt1.text = "2"
            binding.contentTxt2.text = "3600"
            binding.contentTxt3.text = "₹ 36,00,000"
            binding.contentTxt4.text = "+4% IEA"

            binding.ivAmount.visibility = View.VISIBLE
            binding.ivAmountPending.visibility = View.GONE

        }
    }

    private inner class SummaryOngoingInvestmentsViewHolder(private val binding: FinancialSummaryCard2Binding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.ivAmount.visibility = View.GONE
            binding.ivAmountPending.visibility = View.VISIBLE
        }
    }

    private inner class CompletedInvestmentsViewHolder(private val binding: CompletedInvestmentsLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val list = arrayListOf<String>("1","2")
            completedInvestmentAdapter = CompletedInvestmentAdapter(list)
            binding.rvCompletedInvestment.adapter = completedInvestmentAdapter
            completedInvestmentAdapter.setItemClickListener(fragment.onItemClickListener)
        }
    }

    private inner class OngoingInvestmentsViewHolder(private val binding: OngoingInvestmentsLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val list = arrayListOf<String>("1")
            completedInvestmentAdapter = CompletedInvestmentAdapter(list)
            binding.rvOngoingInvestment.adapter = completedInvestmentAdapter
            completedInvestmentAdapter.setItemClickListener(fragment.onItemClickListener)
        }
    }

    private inner class BlockchainViewHolder(private val binding: BlockchainLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
        }
    }

    private inner class MyWatchListViewHolder(private val binding: SmartDealsLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.tvSmartDealsSubtitle.visibility = View.GONE
            binding.tvSmartDealsTitle.text = "My WatchList"
            val list = arrayListOf<String>("1","2","3","4","5")
            investmentAdapter = InvestmentAdapter(context,list)
            binding.rvSmartDealsNv.adapter = investmentAdapter
        }
    }

    private inner class PortfolioReferViewHolder(private val binding: PortfolioReferLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){

        }
    }

    fun setItemClickListener(clickListener: View.OnClickListener) {
        onItemClickListener = clickListener
    }

}