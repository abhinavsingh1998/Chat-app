package com.emproto.hoabl.feature.portfolio.adapters

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.view.animation.TranslateAnimation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.*
import com.emproto.hoabl.feature.home.adapters.HoABLPromisesAdapter
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.networklayer.response.documents.Data
import com.emproto.networklayer.response.portfolio.dashboard.GeneralInfoEscalationGraph
import com.emproto.networklayer.response.portfolio.ivdetails.*
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.tabs.TabLayoutMediator


class PortfolioSpecificViewAdapter(
    private val context: Context,
    private val list: List<RecyclerViewItem>,
    private val ivInterface: InvestmentScreenInterface
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val PORTFOLIO_TOP_SECTION = 1
        const val PORTFOLIO_PENDINGCARD = 2
        const val PORTFOLIO_FACILITY_CARD = 3
        const val PORTFOLIO_DOCUMENTS = 4
        const val PORTFOLIO_TRENDING_IMAGES = 5
        const val PORTFOLIO_PROMISES = 6
        const val PORTFOLIO_GRAPH = 7
        const val PORTFOLIO_REFERNOW = 8
        const val PORTFOLIO_FAQ = 9
        const val PORTFOLIO_SIMILER_INVESTMENT = 10
    }

    private lateinit var specificViewPagerAdapter: PortfolioSpecificViewPagerAdapter
    private lateinit var documentsAdapter: DocumentsAdapter
    private lateinit var latestImagesVideosAdapter: VideoAdapter
    private lateinit var promisesAdapter: HoABLPromisesAdapter
    private lateinit var faqAdapter: ProjectFaqAdapter
    private lateinit var similarInvestmentsAdapter: SimilarInvestmentAdapter
    private lateinit var onItemClickListener: View.OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PORTFOLIO_TOP_SECTION -> {
                ProjectSpecificTopViewHolder(
                    PortfolioSpecificViewTopLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            PORTFOLIO_PENDINGCARD -> {
                PendingViewHolder(
                    PendingItemsLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            PORTFOLIO_FACILITY_CARD -> {
                FacilityManagementViewHolder(
                    FacilityManagementLayoutBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
            PORTFOLIO_DOCUMENTS -> {
                DocumentsViewHolder(
                    DocumentsLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            PORTFOLIO_TRENDING_IMAGES -> {
                LatestImagesVideosViewHolder(
                    LatestImagesVideosLayoutBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
            PORTFOLIO_PROMISES -> {
                ApplicablePromisesViewHolder(
                    PortfolioApplicablePromisesBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            PORTFOLIO_GRAPH -> {
                PriceTrendsViewHolder(
                    PriceTrendsLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            PORTFOLIO_REFERNOW -> {
                ReferViewHolder(
                    PortfolioSepcificViewReferLayoutBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
            PORTFOLIO_FAQ -> {
                FaqViewHolder(
                    FaqLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                SimilarInvestmentsViewHolder(
                    TrendingProjectsLayoutBinding.inflate(
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
            PORTFOLIO_TOP_SECTION -> {
                (holder as ProjectSpecificTopViewHolder).bind(position)
            }
            PORTFOLIO_PENDINGCARD -> {
                (holder as PendingViewHolder).bind(position)
            }
            PORTFOLIO_FACILITY_CARD -> {
                (holder as FacilityManagementViewHolder).bind(position)
            }
            PORTFOLIO_DOCUMENTS -> {
                (holder as DocumentsViewHolder).bind(position)
            }
            PORTFOLIO_TRENDING_IMAGES -> {
                (holder as LatestImagesVideosViewHolder).bind(position)
            }
            PORTFOLIO_PROMISES -> {
                (holder as ApplicablePromisesViewHolder).bind(position)
            }
            PORTFOLIO_GRAPH -> {
                (holder as PriceTrendsViewHolder).bind(position)
            }
            PORTFOLIO_REFERNOW -> {
                (holder as ReferViewHolder).bind(position)
            }
            PORTFOLIO_FAQ -> {
                (holder as FaqViewHolder).bind(position)
            }
            PORTFOLIO_SIMILER_INVESTMENT -> {
                (holder as SimilarInvestmentsViewHolder).bind(position)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int = list[position].viewType

    private inner class ProjectSpecificTopViewHolder(private val binding: PortfolioSpecificViewTopLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            //binding.btnApplyNow.setOnClickListener(onItemClickListener)
            binding.tvViewMore.setOnClickListener {
                binding.tvViewLess.visibility = View.VISIBLE
                binding.ivViewMoreArrowUpward.visibility = View.VISIBLE
                binding.cvMoreInfoCard.visibility = View.VISIBLE
//                moveBottom(binding.cvMoreInfoCard)

                binding.tvViewMore.visibility = View.GONE
                binding.ivViewMoreDropDown.visibility = View.GONE
            }
            binding.tvViewLess.setOnClickListener {
                binding.tvViewLess.visibility = View.GONE
                binding.ivViewMoreArrowUpward.visibility = View.GONE
                binding.cvMoreInfoCard.visibility = View.GONE

                binding.tvViewMore.visibility = View.VISIBLE
                binding.ivViewMoreDropDown.visibility = View.VISIBLE
            }
            //set data to view
            val data =
                list[position].data as com.emproto.networklayer.response.portfolio.ivdetails.Data
            if (data != null) {
                binding.tvProjectName.text = data.projectInformation.launchName
                binding.tvProjectLocation.text =
                    data.projectExtraDetails.address.city + " " + data.projectExtraDetails.address.state
                if (data.investmentInformation != null) {
                    binding.tvPaidAmount.text =
                        "₹" + data.investmentInformation.bookingJourney.paidAmount
                    binding.tvPendingAmount.text =
                        "₹" + data.investmentInformation.bookingJourney.amountPending
                    binding.tvAreaUnit.text = "" + data.investmentInformation.areaSqFt + " sqft"
                    binding.tvProjectInfo.text = data.projectInformation.shortDescription
                    var reraNumber = ""
                    for (item in data.projectInformation.reraDetails.reraNumbers) {
                        reraNumber = reraNumber + item + "\n"
                    }
                    binding.tvAllocationDate.text =
                        Utility.parseDateFromUtc(
                            data.investmentInformation.bookingJourney.allocationDate,
                            null
                        )
                    binding.tvPossessionDate.text =
                        Utility.parseDateFromUtc(
                            data.investmentInformation.bookingJourney.possesionDate,
                            null
                        )
                    //view more
                    binding.tvLandId.text = data.investmentInformation.inventoryId
                    binding.tvSkuType.text = data.investmentInformation.inventoryBucket
                    binding.tvInvestmentAmount.text =
                        "₹" + data.investmentInformation.amountInvested
                    binding.tvAmountPaid.text = "₹" + data.investmentInformation.amountInvested
                    binding.tvAmountPending.text =
                        "₹" + data.investmentInformation.bookingJourney.amountPending
                    binding.tvRegistryAmount.text = "₹" + data.investmentInformation.registryAmount
                    binding.tvOtherExpenses.text = "₹" + data.investmentInformation.otherExpenses
                    //binding.tvLatitude.text = data.projectExtraDetails.latitude
                    //binding.tvLongitude.text = data.projectExtraDetails.longitude
                    binding.ownersName.text = data.investmentInformation.owners

                    //binding.tvRegistrationNumber.text = reraNumber
                }


            }

            binding.tvViewTimeline.setOnClickListener {
                ivInterface.seeProjectTimeline()

            }
            binding.tvViewBookingJourney.setOnClickListener {
                ivInterface.seeBookingJourney()
            }
            binding.tvSeeProjectDetails.setOnClickListener {
                ivInterface.seeProjectDetails()
            }
        }
    }

    fun slideToBottom(view: View) {
        val animate = TranslateAnimation(0f, 0f, 0f, view.height.toFloat() * 0.95f)
        animate.duration = 500
        animate.fillAfter = false
        view.startAnimation(animate)
        view.visibility = View.VISIBLE
    }

    fun moveBottom(view: View) {
        val moveAnim: ObjectAnimator = ObjectAnimator.ofFloat(view, "Y", 1000f)
        moveAnim.duration = 2000
        moveAnim.interpolator = BounceInterpolator()
        moveAnim.start()
        view.visibility = View.VISIBLE
    }


    private inner class PendingViewHolder(private val binding: PendingItemsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

            val listViews = arrayListOf<String>("1", "2", "3", "4", "5")
            specificViewPagerAdapter = PortfolioSpecificViewPagerAdapter(listViews)
            binding.vpAttention.adapter = specificViewPagerAdapter

            TabLayoutMediator(binding.tabDotLayout, binding.vpAttention) { _, _ ->
            }.attach()
        }
    }

    private inner class FacilityManagementViewHolder(private val binding: FacilityManagementLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.cvFacilityManagementCard.setOnClickListener { ivInterface.onClickFacilityCard() }
        }
    }

    private inner class DocumentsViewHolder(private val binding: DocumentsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            if (list[position].data != null) {
                val docList = list[position].data as List<Data>
                documentsAdapter = DocumentsAdapter(docList)
                binding.rvDocuments.adapter = documentsAdapter
            }
            binding.tvDocumentsSeeAll.setOnClickListener {
                ivInterface.seeAllCard()
            }
        }
    }

    private inner class LatestImagesVideosViewHolder(private val binding: LatestImagesVideosLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val imagesList = ArrayList<Image>()
            val imagesData = list[position].data as LatestMediaGalleryOrProjectContent
            imagesList.addAll(imagesData.droneShoots)
            imagesList.addAll(imagesData.images)
            imagesList.addAll(imagesData.videos)
            imagesList.addAll(imagesData.threeSixtyImages)

            latestImagesVideosAdapter = VideoAdapter(imagesList)
            val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
            binding.rvLatestImagesVideos.layoutManager = layoutManager
            binding.rvLatestImagesVideos.adapter = latestImagesVideosAdapter
            binding.tvLastUpdatedDate.text = Utility.parseDateFromUtc(imagesData.updatedAt, null)
        }
    }

    private inner class ApplicablePromisesViewHolder(private val binding: PortfolioApplicablePromisesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val promisesData = list[position].data as ProjectPromises
            promisesAdapter = HoABLPromisesAdapter(context, promisesData.data, ivInterface)
            binding.rvApplicablePromises.adapter = promisesAdapter
            binding.btnMoreAboutPromises.setOnClickListener {
                ivInterface.moreAboutPromises()
            }
        }
    }

    private inner class PriceTrendsViewHolder(private val binding: PriceTrendsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val graphData = list[position].data as GeneralInfoEscalationGraph
            val linevalues = ArrayList<Entry>()

            for (item in graphData.dataPoints.points) {
                linevalues.add(Entry(item.year.toFloat(), item.value.toFloat()))
            }
            val linedataset = LineDataSet(linevalues, "")
            //We add features to our chart
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                linedataset.color = context.getColor(R.color.app_color)
            }

            linedataset.valueTextSize = 12F
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                linedataset.fillColor = context.getColor(R.color.light_app_color)
            }
            linedataset.mode = LineDataSet.Mode.LINEAR;

            //We connect our data to the UI Screen
            val data = LineData(linedataset)

            //binding.ivPriceTrendsGraph.setDrawBorders(false);
            //binding.ivPriceTrendsGraph.setDrawGridBackground(false);
            binding.ivPriceTrendsGraph.getDescription().setEnabled(false);
            binding.ivPriceTrendsGraph.getLegend().setEnabled(false);
            binding.ivPriceTrendsGraph.getAxisLeft().setDrawGridLines(false);
            //binding.ivPriceTrendsGraph.getAxisLeft().setDrawLabels(false);
            //binding.ivPriceTrendsGraph.getAxisLeft().setDrawAxisLine(false);
            binding.ivPriceTrendsGraph.getXAxis().setDrawGridLines(false);
            binding.ivPriceTrendsGraph.getXAxis().position = XAxis.XAxisPosition.BOTTOM;
            //binding.ivPriceTrendsGraph.getXAxis().setDrawAxisLine(false);
            binding.ivPriceTrendsGraph.getAxisRight().setDrawGridLines(false);
            binding.ivPriceTrendsGraph.getAxisRight().setDrawLabels(false);
            binding.ivPriceTrendsGraph.getAxisRight().setDrawAxisLine(false);
            //binding.ivPriceTrendsGraph.axisLeft.isEnabled = false
            //binding.ivPriceTrendsGraph.axisRight.isEnabled = false
            binding.ivPriceTrendsGraph.data = data
            binding.ivPriceTrendsGraph.animateXY(2000, 2000)
        }
    }

    private inner class ReferViewHolder(private val binding: PortfolioSepcificViewReferLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.btnReferNow.setOnClickListener {
                ivInterface.referNow()
            }
        }
    }

    private inner class FaqViewHolder(private val binding: FaqLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val faqList = list[position].data as List<ProjectContentsAndFaq>
            faqAdapter = ProjectFaqAdapter(faqList)
            binding.rvFaq.adapter = faqAdapter
            binding.tvFaqReadAll.visibility = View.VISIBLE
            binding.ivSeeAllArrow.visibility = View.VISIBLE
            binding.tvFaqReadAll.setOnClickListener {
                ivInterface.readAllFaq()
            }
        }
    }

    private inner class SimilarInvestmentsViewHolder(private val binding: TrendingProjectsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            if (list[position].data != null) {
                val itemList = list[position].data as List<SimilarInvestment>
                similarInvestmentsAdapter = SimilarInvestmentAdapter(context, itemList)
                binding.rvTrendingProjects.adapter = similarInvestmentsAdapter
                binding.tvTrendingProjectsTitle.text = "Similar Investments"
                binding.tvTrendingProjectsSubtitle.visibility = View.GONE
            }
            binding.tvTrendingProjectsSeeAll.setOnClickListener {
                ivInterface.seeAllSimilarInvestment()
            }
        }
    }


    interface InvestmentScreenInterface {
        fun onClickFacilityCard()
        fun seeAllCard()
        fun seeProjectTimeline()
        fun seeBookingJourney()
        fun referNow()
        fun seeAllSimilarInvestment()
        fun readAllFaq()
        fun seePromisesDetails(position: Int)
        fun moreAboutPromises()
        fun seeProjectDetails()
    }

}