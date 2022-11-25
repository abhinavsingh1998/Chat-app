package com.emproto.hoabl.feature.portfolio.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.*
import com.emproto.hoabl.feature.portfolio.models.PortfolioModel
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.response.portfolio.dashboard.*
import com.emproto.networklayer.response.portfolio.ivdetails.ProjectExtraDetails
import com.emproto.networklayer.response.watchlist.Data
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.createBalloon

class ExistingUsersPortfolioAdapter(
    private val context: Context,
    private val list: List<PortfolioModel>,
    val onItemClickListener: ExistingUserInterface,
    private val appPreference: AppPreference
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
                    FinancialSummaryCard1Binding.inflate(
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
            TYPE_SUMMARY_COMPLETED -> (holder as SummaryCompletedInvestmentsViewHolder).bind(position)
            TYPE_SUMMARY_ONGOING -> (holder as SummaryOngoingInvestmentsViewHolder).bind(position)
            TYPE_COMPLETED_INVESTMENT -> (holder as CompletedInvestmentsViewHolder).bind(position)
            TYPE_ONGOING_INVESTMENT -> (holder as OngoingInvestmentsViewHolder).bind(position)
            TYPE_NUDGE_CARD -> (holder as BlockchainViewHolder).bind(position)
            TYPE_WATCHLIST -> (holder as MyWatchListViewHolder).bind(position)
            TYPE_REFER -> (holder as PortfolioReferViewHolder).bind()
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

    private inner class TitleViewHolder(private val binding: PortfolioTitleCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            if (list[position].data != null) {
                val pageData = list[position].data as PageData
                binding.tvPortfolio.text = pageData.data.page.pageName
                binding.tvSubheading.text = pageData.data.page.subHeading
            }

        }
    }

    private inner class SummaryCompletedInvestmentsViewHolder(private val binding: FinancialSummaryCard1Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

            binding.contentTxt4.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.portfolio_blue_color
                )
            )
            binding.ivAmount.visibility = View.VISIBLE
            binding.ivAmountPending.visibility = View.GONE
            //getting data
            if (list[position].data != null) {
                val summary = list[position].data as Summary
                val completed = summary.completed
                binding.contentTxt1.text = "${completed.count}"
                binding.contentTxt2.text = Utility.convertTo(completed.areaSqFt)
                val value = Utility.homeCurrencyConversion(completed.amountInvested)
                binding.contentTxt3.text = value.toString()

                if (summary.iea == "---") {
                    "${summary.iea} OEA".also { binding.contentTxt4.text = it }

                } else {
                    "+ ${summary.iea} OEA".also { binding.contentTxt4.text = it }

                }
            }



            binding.contentTxt4.setOnClickListener {
                getToolTip("Owner Estimated Appreciation").showAlignBottom(binding.ivAmount)
            }
            binding.cardName4.setOnClickListener {
                getToolTip("Owner  Estimated Appreciation").showAlignBottom(binding.ivAmount)
            }
            binding.ivAmount.setOnClickListener {
                getToolTip("Owner Estimated Appreciation").showAlignBottom(binding.ivAmount)
            }

            binding.cardName3.setOnClickListener {
                getToolTip("Amount Invested").showAlignBottom(binding.ivAmountpaid)
            }
            binding.ivAmountpaid.setOnClickListener {
                getToolTip("Amount Invested").showAlignBottom(binding.ivAmountpaid)
            }
            binding.contentTxt3.setOnClickListener {
                getToolTip("Amount Invested").showAlignBottom(binding.ivAmountpaid)
            }
            ImageViewCompat.setImageTintList(
                binding.ivAmountpaid,
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
            )


        }
    }

    private inner class SummaryOngoingInvestmentsViewHolder(private val binding: FinancialSummaryCard2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.ivAmount.visibility = View.GONE
            binding.ivAmountPending.visibility = View.VISIBLE

            if (list[position].data != null) {
                val ongoing = list[position].data as Ongoing
                ("" + ongoing.count).also { binding.contentTxt1.text = it }
                binding.contentTxt2.text = Utility.convertTo(ongoing.areaSqFt)
                val value1 = Utility.homeCurrencyConversion(ongoing.amountPaid)
                binding.contentTxt3.text = value1.toString()
                val value2 = Utility.homeCurrencyConversion(ongoing.amountPending)
                binding.contentTxt4.text = value2.toString()
            }
            binding.ivAmountPending.setOnClickListener {
                getToolTip("Excluding taxes & other charges").showAlignTop(binding.ivAmountPending)
            }
            binding.contentTxt4.setOnClickListener {
                getToolTip("Excluding taxes & other charges").showAlignTop(binding.ivAmountPending)
            }
            binding.cardName4.setOnClickListener {
                getToolTip("Excluding taxes & other charges").showAlignTop(binding.ivAmountPending)
            }
            binding.cardName3.setOnClickListener {
                getToolTip("Amount Paid").showAlignBottom(binding.ivAmountpaid)
            }
            binding.ivAmountpaid.setOnClickListener {
                getToolTip("Amount Paid").showAlignBottom(binding.ivAmountpaid)
            }
            binding.contentTxt3.setOnClickListener {
                getToolTip("Amount Invested").showAlignBottom(binding.ivAmountpaid)
            }
        }
    }

    private inner class CompletedInvestmentsViewHolder(private val binding: CompletedInvestmentsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val projectList = list[position].data as List<Project>
            Log.i("projectList", projectList.toString())
            if (projectList.isEmpty()) {
                binding.cvCompletedInvestment.visibility = View.VISIBLE
                binding.rvCompletedInvestment.visibility = View.GONE
                binding.cvCompletedInvestment.setOnClickListener {
                    onItemClickListener.onGoingDetails()
                }
            } else {
                completedInvestmentAdapter =
                    CompletedInvestmentAdapter(
                        context,
                        projectList,
                        onItemClickListener,
                        0,
                        appPreference
                    )
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
                    CompletedInvestmentAdapter(
                        context,
                        projectList,
                        onItemClickListener,
                        1,
                        appPreference
                    )
                binding.rvOngoingInvestment.adapter = completedInvestmentAdapter
            }
        }
    }

    private inner class BlockchainViewHolder(private val binding: BlockchainLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val url = list[position].data as String
            Glide.with(context).load(url).placeholder(R.drawable.dont_miss_new)
                .into(binding.ivDontMissImage)
            binding.ivDontMissImage.setOnClickListener {
                onItemClickListener.doNotMissOutCard()
            }
        }
    }

    private inner class MyWatchListViewHolder(private val binding: SmartDealsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val watchList =
                list[position].data as List<Data>
            binding.tvSmartDealsSubtitle.visibility = View.GONE
            "My WatchList".also { binding.tvSmartDealsTitle.text = it }
            watchlistAdapter = WatchlistAdapter(context, watchList, onItemClickListener)
            binding.rvSmartDealsNv.adapter = watchlistAdapter
            binding.rvSmartDealsNv.setHasFixedSize(true)
            binding.rvSmartDealsNv.setItemViewCacheSize(10)
            binding.tvSmartDealsSeeAll.setOnClickListener {
                onItemClickListener.seeAllWatchlist()
            }

        }
    }

    private inner class PortfolioReferViewHolder(private val binding: PortfolioReferLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.btnReferNow.setOnClickListener {
                onItemClickListener.referNow()
            }
            binding.appShareView.setOnClickListener {
                onItemClickListener.onClickShare()
            }
        }
    }

    interface ExistingUserInterface {
        fun manageProject(
            crmId: Int,
            projectId: Int,
            otherDetails: ProjectExtraDetails,
            iea: String?,
            ea: Double,
            headingDetails: InvestmentHeadingDetails,
            customerGuideLinesValueUrl: String?
        )

        fun referNow()
        fun seeAllWatchlist()
        fun investNow()
        fun onGoingDetails()
        fun onClickOfWatchlist(projectId: Int)
        fun onClickApplyNow(projectId: Int)
        fun onClickShare()
        fun doNotMissOutCard()
    }

    fun getToolTip(text: String): Balloon {
        val balloon = createBalloon(context) {
            setArrowSize(6)
            setWidth(BalloonSizeSpec.WRAP)
            setTextSize(12F)
            setCornerRadius(4f)
            setAlpha(0.9f)
            setText(text)
            setTextColorResource(R.color.white)
            setBackgroundColorResource(R.color.black)
            setPadding(5)
            setTextTypeface(ResourcesCompat.getFont(context, R.font.jost_medium)!!)
            setBalloonAnimation(BalloonAnimation.FADE)
            setLifecycleOwner(lifecycleOwner)
        }
        return balloon
    }


}