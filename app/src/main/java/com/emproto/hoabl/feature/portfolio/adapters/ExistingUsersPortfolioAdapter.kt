package com.emproto.hoabl.feature.portfolio.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.*
import com.emproto.hoabl.feature.portfolio.models.PortfolioModel
import com.emproto.networklayer.response.portfolio.dashboard.Completed
import com.emproto.networklayer.response.portfolio.dashboard.Ongoing
import com.emproto.networklayer.response.portfolio.dashboard.Project
import com.emproto.networklayer.response.watchlist.Data
import com.emproto.networklayer.response.investment.SimilarInvestment
import com.emproto.networklayer.response.portfolio.dashboard.Address
import com.emproto.networklayer.response.portfolio.ivdetails.ProjectExtraDetails
import com.google.android.material.textview.MaterialTextView
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.createBalloon

class ExistingUsersPortfolioAdapter(
    private val context: Context,
    private val list: List<PortfolioModel>,
    val onItemClickListener: ExistingUserInterface
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
    private lateinit var watchlistAdapter: WatchlistAdapter

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
            if (list[position].data != null) {
                val completed = list[position].data as Completed
                binding.contentTxt1.text = "" + completed.count
                binding.contentTxt2.text = "" + completed.areaSqFt
                binding.contentTxt3.text = "₹ " + completed.amountInvested
                binding.contentTxt4.text = "+4% IEA"
            }

            val balloon = createBalloon(context) {
                setArrowSize(6)
                setWidth(200)
                setTextSize(12F)
                setArrowPosition(0.6f)
                setCornerRadius(4f)
                setAlpha(0.9f)
                setText("Investor Estimated Appreciation")
                setTextColorResource(R.color.white)
                setBackgroundColorResource(R.color.black)
                setPadding(5)
                setTextTypeface(ResourcesCompat.getFont(context, R.font.jost_medium)!!)
                setBalloonAnimation(BalloonAnimation.FADE)
                setLifecycleOwner(lifecycleOwner)
            }

            binding.ivAmount.setOnClickListener {
                balloon.showAlignBottom(binding.ivAmount)
            }

        }
    }

    private inner class SummaryOngoingInvestmentsViewHolder(private val binding: FinancialSummaryCard2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.ivAmount.visibility = View.GONE
            binding.ivAmountPending.visibility = View.VISIBLE

            if (list[position].data != null) {
                val ongoing = list[position].data as Ongoing
                binding.contentTxt1.text = "" + ongoing.count
                binding.contentTxt2.text = "" + ongoing.areaSqFt
                binding.contentTxt3.text = "₹ " + ongoing.amountPaid
                binding.contentTxt4.text = "₹ " + ongoing.amountPending
            }

            val balloon = createBalloon(context) {
                setArrowSize(6)
                setWidth(BalloonSizeSpec.WRAP)
                setTextSize(12F)
                setArrowPosition(0.8f)
                setCornerRadius(4f)
                setAlpha(0.9f)
                setText("Excluding taxes & other charges")
                setTextColorResource(R.color.white)
                setBackgroundColorResource(R.color.black)
                setPadding(5)
                setTextTypeface(ResourcesCompat.getFont(context, R.font.jost_medium)!!)
                setBalloonAnimation(BalloonAnimation.FADE)
                setLifecycleOwner(lifecycleOwner)
            }

            binding.ivAmountPending.setOnClickListener {
                balloon.showAlignTop(binding.ivAmountPending)
            }
        }
    }

    private inner class CompletedInvestmentsViewHolder(private val binding: CompletedInvestmentsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val projectList = list[position].data as List<Project>

            if (projectList.isEmpty()) {
                binding.cvCompletedInvestment.visibility = View.VISIBLE
                binding.rvCompletedInvestment.visibility = View.GONE
                binding.cvCompletedInvestment.setOnClickListener {

                }
            } else {
                completedInvestmentAdapter =
                    CompletedInvestmentAdapter(context, projectList, onItemClickListener, 0)
                binding.rvCompletedInvestment.adapter = completedInvestmentAdapter
            }
        }
    }

    private inner class OngoingInvestmentsViewHolder(private val binding: OngoingInvestmentsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val projectList = list[position].data as List<Project>
            if (projectList.isEmpty()) {
                binding.cvOngoingInvestment.visibility = View.VISIBLE
                binding.rvOngoingInvestment.visibility = View.GONE
                binding.cvOngoingInvestment.setOnClickListener {
                    onItemClickListener.investNow()
                }
            } else {
                completedInvestmentAdapter =
                    CompletedInvestmentAdapter(context, projectList, onItemClickListener, 1)
                binding.rvOngoingInvestment.adapter = completedInvestmentAdapter
            }
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
            val watchList =
                list[position].data as List<Data>
            binding.tvSmartDealsSubtitle.visibility = View.GONE
            binding.tvSmartDealsTitle.text = "My WatchList"
            watchlistAdapter = WatchlistAdapter(context, watchList)
            binding.rvSmartDealsNv.adapter = watchlistAdapter
            binding.rvSmartDealsNv.setHasFixedSize(true)
            binding.tvSmartDealsSeeAll.setOnClickListener {
                onItemClickListener.seeAllWatchlist()
            }

        }
    }

    private inner class PortfolioReferViewHolder(private val binding: PortfolioReferLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.btnReferNow.setOnClickListener {
                onItemClickListener.referNow()
            }
        }
    }

    interface ExistingUserInterface {
        fun manageProject(crmId: Int, projectId: Int, otherDetails: ProjectExtraDetails)
        fun referNow()
        fun seeAllWatchlist()
        fun investNow()
        fun onGoingDetails()
    }

}