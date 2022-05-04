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
import com.emproto.hoabl.feature.portfolio.models.PortfolioModel
import com.emproto.hoabl.feature.portfolio.views.PortfolioExistingUsersFragment
import com.emproto.networklayer.response.portfolio.Completed
import com.emproto.networklayer.response.portfolio.Ongoing
import com.emproto.networklayer.response.portfolio.Project

class ExistingUsersAdapter(
    private val context: Context,
    private val list: List<PortfolioModel>,
    val onItemClickListener: View.OnClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_HEADER = 1
        const val TYPE_SUMMARY_COMPLETED = 2
        const val TYPE_SUMMARY_ONGOING = 3
        const val TYPE_COMPLETED_INVESTMENT = 4
        const val TYPE_ONGOING_INVESTMENT = 5
        const val TYPE_NUDGE_CARD = 6
        const val TYPE_WATCHLIST = 7
        const val TYPE_REFER = 8
    }

    private lateinit var completedInvestmentAdapter: CompletedInvestmentAdapter

    //private lateinit var onItemClickListener : View.OnClickListener
    private lateinit var investmentAdapter: InvestmentAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                TitleViewHolder(
                    PortfolioTitleCardBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            TYPE_SUMMARY_COMPLETED -> {
                SummaryCompletedInvestmentsViewHolder(
                    FinancialSummaryCard2Binding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            TYPE_SUMMARY_ONGOING -> {
                SummaryOngoingInvestmentsViewHolder(
                    FinancialSummaryCard2Binding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            TYPE_COMPLETED_INVESTMENT -> {
                CompletedInvestmentsViewHolder(
                    CompletedInvestmentsLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            TYPE_ONGOING_INVESTMENT -> {
                OngoingInvestmentsViewHolder(
                    OngoingInvestmentsLayoutBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
            TYPE_NUDGE_CARD -> {
                BlockchainViewHolder(
                    BlockchainLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            TYPE_WATCHLIST -> {
                MyWatchListViewHolder(
                    SmartDealsLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                PortfolioReferViewHolder(
                    PortfolioReferLayoutBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (list[position].viewType) {
            TYPE_HEADER -> (holder as TitleViewHolder).bind(position)
            TYPE_SUMMARY_COMPLETED -> (holder as SummaryCompletedInvestmentsViewHolder).bind(
                position
            )
            TYPE_SUMMARY_ONGOING -> (holder as SummaryOngoingInvestmentsViewHolder).bind(position)
            TYPE_COMPLETED_INVESTMENT -> (holder as CompletedInvestmentsViewHolder).bind(position)
            TYPE_ONGOING_INVESTMENT -> (holder as OngoingInvestmentsViewHolder).bind(position)
            TYPE_NUDGE_CARD -> (holder as BlockchainViewHolder).bind(position)
            TYPE_WATCHLIST -> (holder as MyWatchListViewHolder).bind(position)
            TYPE_REFER -> (holder as PortfolioReferViewHolder).bind(position)
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

    private inner class TitleViewHolder(private val binding: PortfolioTitleCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

        }
    }

    private inner class SummaryCompletedInvestmentsViewHolder(private val binding: FinancialSummaryCard2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.contentHeader.text = "Summary - Completed Investments"

            binding.cardView1.setBackgroundResource(R.drawable.financial_summary_dark_card)
            binding.cardView2.setBackgroundResource(R.drawable.financial_summary_dark_card)
            binding.cardView3.setBackgroundResource(R.drawable.financial_summary_dark_card)
            binding.cardView4.setBackgroundResource(R.drawable.financial_summary_dark_card)

            binding.cardName1.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.unselected_button_color
                )
            )
            binding.cardName2.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.unselected_button_color
                )
            )
            binding.cardName3.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.unselected_button_color
                )
            )
            binding.cardName4.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.unselected_button_color
                )
            )

            binding.contentTxt1.setTextColor(Color.WHITE)
            binding.contentTxt2.setTextColor(Color.WHITE)
            binding.contentTxt3.setTextColor(Color.WHITE)
            binding.contentTxt4.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.portfolio_blue_color
                )
            )

            binding.cardName1.text = "No. of Products"
            binding.cardName2.text = "Area in Sqft"
            binding.cardName3.text = "Amount Invested"
            binding.cardName4.text = "Avg Estimated Appreciation"

            binding.ivAmount.visibility = View.VISIBLE
            binding.ivAmountPending.visibility = View.GONE
            //getting data
            val completed = list[position].data as Completed
            binding.contentTxt1.text = "" + completed.count
            binding.contentTxt2.text = "" + completed.areaSqFt
            binding.contentTxt3.text = "₹ " + completed.amountInvested
            binding.contentTxt4.text = "+4% IEA"

        }
    }

    private inner class SummaryOngoingInvestmentsViewHolder(private val binding: FinancialSummaryCard2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.ivAmount.visibility = View.GONE
            binding.ivAmountPending.visibility = View.VISIBLE

            val ongoing = list[position].data as Ongoing
            binding.contentTxt1.text = "" + ongoing.count
            binding.contentTxt2.text = "" + ongoing.areaSqFt
            binding.contentTxt3.text = "₹ " + ongoing.amountPaid
            binding.contentTxt4.text = "₹ " + ongoing.amountPending
        }
    }

    private inner class CompletedInvestmentsViewHolder(private val binding: CompletedInvestmentsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val projectList = list[position].data as List<Project>

            completedInvestmentAdapter = CompletedInvestmentAdapter(projectList)
            binding.rvCompletedInvestment.adapter = completedInvestmentAdapter
            completedInvestmentAdapter.setItemClickListener(onItemClickListener)
        }
    }

    private inner class OngoingInvestmentsViewHolder(private val binding: OngoingInvestmentsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val projectList = list[position].data as List<Project>
            completedInvestmentAdapter = CompletedInvestmentAdapter(projectList)
            binding.rvOngoingInvestment.adapter = completedInvestmentAdapter
            completedInvestmentAdapter.setItemClickListener(onItemClickListener)
        }
    }

    private inner class BlockchainViewHolder(private val binding: BlockchainLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
        }
    }

    private inner class MyWatchListViewHolder(private val binding: SmartDealsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvSmartDealsSubtitle.visibility = View.GONE
            binding.tvSmartDealsTitle.text = "My WatchList"
            val list = arrayListOf<String>("1", "2", "3", "4", "5")
            investmentAdapter = InvestmentAdapter(context, list)
            binding.rvSmartDealsNv.adapter = investmentAdapter
        }
    }

    private inner class PortfolioReferViewHolder(private val binding: PortfolioReferLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

        }
    }

//    fun setItemClickListener(clickListener: View.OnClickListener) {
//        onItemClickListener = clickListener
//    }

}