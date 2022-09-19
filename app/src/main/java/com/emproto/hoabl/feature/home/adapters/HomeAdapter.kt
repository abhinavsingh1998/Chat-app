package com.emproto.hoabl.feature.home.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.core.Utility
import com.emproto.hoabl.databinding.*
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.response.home.Data
import com.google.android.material.tabs.TabLayoutMediator
import java.text.NumberFormat
import java.util.*

@Suppress("DEPRECATION")
class HomeAdapter(
    var context: Context,
    val data: Data,
    val list: List<RecyclerViewItem>,
    val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val HOME_PORTFOLIO = 1
        const val NEW_PROJECT = 2
        const val INCOMPLETE_KYC = 3
        const val LATEST_UPDATES = 4
        const val PROMISES = 5
        const val FACILITY_MANAGEMENT = 6
        const val INSIGHTS = 7
        const val TESTIMONIALS = 8
        const val SHARE_APP = 9
    }

    private lateinit var investmentAdapter: InvestmentCardAdapter
    private lateinit var pendingPaymentsAdapter: PendingPaymentsAdapter
    private lateinit var latestUpdateAdapter: LatestUpdateAdapter
    private lateinit var projectPromisesAdapter: HoABLPromisesAdapter1
    private lateinit var insightsAdapter: InsightsAdapter
    private lateinit var testimonialAdapter: TestimonialAdapter
    private lateinit var onItemClickListener: View.OnClickListener
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var presenting: Boolean = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HOME_PORTFOLIO -> {
                PortfolioViewHolder(
                    HomePortfolioCardBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )

            }
            NEW_PROJECT -> {
                NewInvestmentViewHolder(
                    HomepageHeaderLayoutBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
            INCOMPLETE_KYC -> {
                IncompleteKycViewHolder(
                    PaymentPendingLayoutBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }

            LATEST_UPDATES -> {
                LatestUpdatesViewHolder(
                    LatestUpdateLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            PROMISES -> {
                PromisesViewHolder(
                    HomePromisesLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            FACILITY_MANAGEMENT -> {
                FacilityManagementViewHolder(
                    HomeFmCardLayoutBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }

            INSIGHTS -> {
                InsightsViewHolder(
                    HomeInsightsLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            TESTIMONIALS -> {
                TestimonialsViewHolder(
                    HomeTestimonialsLayoutBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }

            else -> {
                ShareAppViewHolder(
                    PortfolioReferLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (list[position].viewType) {
            HOME_PORTFOLIO -> {
                (holder as PortfolioViewHolder).bind(position)
            }
            NEW_PROJECT -> {
                (holder as NewInvestmentViewHolder).bind(position)
            }
            INCOMPLETE_KYC -> {
                (holder as IncompleteKycViewHolder).bind(position)
            }
            LATEST_UPDATES -> {
                (holder as LatestUpdatesViewHolder).bind(position)
            }
            PROMISES -> {
                (holder as PromisesViewHolder).bind(position)
            }
            FACILITY_MANAGEMENT -> {
                (holder as FacilityManagementViewHolder).bind(position)
            }
            INSIGHTS -> {
                (holder as InsightsViewHolder).bind(position)
            }
            TESTIMONIALS -> {
                (holder as TestimonialsViewHolder).bind(position)
            }
            SHARE_APP -> {
                (holder as ShareAppViewHolder).bind(position)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }


    private inner class PortfolioViewHolder(private val binding: HomePortfolioCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {

            if (data.portfolioData != null) {
                if (data.portfolioData.investmentCount > 0) {
                    binding.nestedCard?.visibility = View.VISIBLE
                    presenting = false
                } else {
                    binding.nestedCard?.visibility = View.GONE
                    presenting = true
                }
                binding.contentTxt1.text = data.portfolioData.investmentCount.toString()
                binding.contentTxt2.text = data.portfolioData.totalAreaSqFt.toString()
                binding.contentTxt3.text = Utility.formatAmount(data.portfolioData.amountInvested)
                binding.contentTxt4.text = NumberFormat.getCurrencyInstance(Locale("en", "in"))
                    .format(data.portfolioData.amountPending).toString()

                binding.viewPortfolioBtn.setOnClickListener {
                    itemClickListener.onItemClicked(it, position, "")
                }
            }
        }
    }

    private inner class NewInvestmentViewHolder(private val binding: HomepageHeaderLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
//            val projectList = ArrayList<PageManagementsOrNewInvestment>()
//            projectList.clear()
//
//            projectList.addAll(data.pageManagementsOrNewInvestments)

            if (!presenting) {
                binding.present.isVisible = false
            }
            investmentAdapter = InvestmentCardAdapter(
                context,
                data,
                data.pageManagementsOrNewInvestments,
                itemClickListener
            )

            if (!data.page.isNewInvestmentsActive || data.pageManagementsOrNewInvestments == null || data.page.totalProjectsOnHomeScreen == 0
            ) {
                binding.investmentLayout.isVisible = false
            }


            binding.tvViewallInvestments.setOnClickListener {
                itemClickListener.onItemClicked(it, position, "")
            }

            linearLayoutManager = LinearLayoutManager(
                context,
                RecyclerView.HORIZONTAL,
                false
            )
            binding.investmentList.layoutManager = linearLayoutManager
            binding.investmentList.adapter = investmentAdapter
            binding.investmentList.setHasFixedSize(true)
            binding.investmentList.setItemViewCacheSize(10)
            binding.investmentList.isDrawingCacheEnabled = true
        }
    }

    private inner class IncompleteKycViewHolder(private val binding: PaymentPendingLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {

            pendingPaymentsAdapter = PendingPaymentsAdapter(
                context,
                data.actionItem,
                itemClickListener
            )

            binding.kycLayoutCard.adapter = pendingPaymentsAdapter
            TabLayoutMediator(binding.tabDot, binding.kycLayoutCard) { _, _ ->
            }.attach()


        }
    }

    private inner class LatestUpdatesViewHolder(private val binding: LatestUpdateLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

            latestUpdateAdapter = LatestUpdateAdapter(
                context,
                data,
                data.pageManagementOrLatestUpdates,
                itemClickListener
            )

            binding.tvSeeAllUpdate.setOnClickListener {
                itemClickListener.onItemClicked(it, position, "")
            }


            linearLayoutManager = LinearLayoutManager(
                context,
                RecyclerView.HORIZONTAL,
                false
            )

            if (!data.page.isLatestUpdatesActive || data.pageManagementOrLatestUpdates == null || data.page.totalUpdatesOnHomeScreen == 0
            ) {
                binding.latestUpdatesLayout.isVisible = false
            }

            binding.textview2.text = data.page.latestUpdates.heading
            binding.latesUpdatesRecyclerview.layoutManager = linearLayoutManager
            binding.latesUpdatesRecyclerview.adapter = latestUpdateAdapter
            binding.latesUpdatesRecyclerview.setHasFixedSize(true)
        }
    }

    private inner class PromisesViewHolder(private val binding: HomePromisesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

            projectPromisesAdapter = HoABLPromisesAdapter1(
                context,
                data,
                data.homePagesOrPromises,
                itemClickListener
            )

            binding.textview4.text = data.page.promisesHeading
            binding.tvSeeallPromise.setOnClickListener {
                itemClickListener.onItemClicked(it, position, "")
            }

            if (!data.page.isPromisesActive || data.homePagesOrPromises == null || data.page.totalPromisesOnHomeScreen == 0
            ) {
                binding.hoablPromisesLayout.isVisible = false

            }


            linearLayoutManager = LinearLayoutManager(
                context,
                RecyclerView.HORIZONTAL,
                false
            )
            binding.hoablPromisesRecyclerview.layoutManager = linearLayoutManager
            binding.hoablPromisesRecyclerview.adapter = projectPromisesAdapter

        }
    }

    private inner class FacilityManagementViewHolder(private val binding: HomeFmCardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            if (data.isFacilityVisible) {
                binding.facilityManagementCardView.visibility = View.VISIBLE
                Glide.with(context).load(data.page.facilityManagement.value.url)
                    .into(binding.facilityManagementCard)
            }
            binding.facilityManagementCard.setOnClickListener {
                itemClickListener.onItemClicked(it, position, "")
            }
            binding.dontMissOutCard.setOnClickListener {
                itemClickListener.onItemClicked(it, position, "")
            }
            if (data.contactType == 1 || data.contactType == 225360001) {
                if (data.page.isPromotionAndOfferActive) {
                    binding.dontMissOutCardView.visibility = View.VISIBLE
                    Glide.with(context).load(data.page.promotionAndOffersMedia.value.url)
                        .into(binding.dontMissOutCard)
                }
            } else {
                binding.dontMissOutCardView.visibility = View.GONE
            }

        }
    }

    private inner class InsightsViewHolder(private val binding: HomeInsightsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            insightsAdapter = InsightsAdapter(
                context,
                data,
                data.pageManagementOrInsights,
                itemClickListener
            )
            binding.tvSeeallInsights.setOnClickListener {
                itemClickListener.onItemClicked(it, position, "")
            }
            binding.textview5.text = data.page.insightsHeading

            linearLayoutManager = LinearLayoutManager(
                context,
                RecyclerView.HORIZONTAL,
                false
            )

            if (!data.page.isInsightsActive || data.pageManagementOrInsights == null || data.page.totalInsightsOnHomeScreen == 0
            ) {
                binding.insightsLayout.isVisible = false
            }
            binding.insightsRecyclerview.layoutManager = linearLayoutManager
            binding.insightsRecyclerview.adapter = insightsAdapter
            binding.insightsRecyclerview.setHasFixedSize(true)
            binding.insightsRecyclerview.setItemViewCacheSize(10)

        }
    }

    private inner class TestimonialsViewHolder(private val binding: HomeTestimonialsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

            testimonialAdapter = TestimonialAdapter(
                context,
                data,
                data.pageManagementsOrTestimonials
            )
            binding.textview6.text = data.page.testimonialsHeading

            binding.tvSeeallTestimonial.setOnClickListener {
                itemClickListener.onItemClicked(it, position, "")
            }

            if (!data.page.isTestimonialsActive || data.pageManagementsOrTestimonials == null || data.page.totalTestimonialsOnHomeScreen == 0
            ) {
                binding.testimonialsLayout.isVisible = false
            }

            binding.testimonialsRecyclerview.adapter = testimonialAdapter
            TabLayoutMediator(
                binding.tabDotLayout,
                binding.testimonialsRecyclerview
            ) { _, _ ->
            }.attach()
        }
    }

    private inner class ShareAppViewHolder(private val binding: PortfolioReferLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

            binding.btnReferNow.setOnClickListener {
                itemClickListener.onItemClicked(it, position, "")
            }
            binding.appShareView.setOnClickListener {
                itemClickListener.onItemClicked(it, position, "")
            }
        }
    }

    fun setItemClickListener(clickListener: View.OnClickListener) {
        onItemClickListener = clickListener
    }
}