package com.emproto.hoabl.feature.portfolio.adapters

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.core.Constants
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.*
import com.emproto.hoabl.feature.home.adapters.HoABLPromisesAdapter
import com.emproto.hoabl.model.MediaViewItem
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.networklayer.response.documents.Data
import com.emproto.networklayer.response.portfolio.dashboard.GeneralInfoEscalationGraph
import com.emproto.networklayer.response.portfolio.dashboard.InvestmentHeadingDetails
import com.emproto.networklayer.response.portfolio.ivdetails.*
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.google.android.material.tabs.TabLayoutMediator
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.createBalloon
import java.math.BigDecimal
import kotlin.collections.ArrayList


class PortfolioSpecificViewAdapter(
    private val context: Context,
    private val list: List<RecyclerViewItem>,
    private val ivInterface: InvestmentScreenInterface,
    private val allMediaList: ArrayList<MediaViewItem>,
    private val headingDetails: InvestmentHeadingDetails
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
        const val MEDIA_ACTIVE = "1001"
        const val MEDIA_INACTIVE = "1002"
    }

    private lateinit var specificViewPagerAdapter: PortfolioSpecificViewPagerAdapter
    private lateinit var documentsAdapter: DocumentsAdapter
    private lateinit var latestImagesVideosAdapter: VideoAdapter
    private lateinit var promisesAdapter: HoABLPromisesAdapter
    private lateinit var faqAdapter: ProjectFaqAdapter
    private lateinit var similarInvestmentsAdapter: SimilarInvestmentAdapter
    private lateinit var onItemClickListener: View.OnClickListener

    //for graph
    private var graphType = ""
    private var xaxisList = ArrayList<String>()

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
                    FaqPortfolioLayoutBinding.inflate(
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

    @RequiresApi(Build.VERSION_CODES.M)
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
        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(position: Int) {
            //binding.btnApplyNow.setOnClickListener(onItemClickListener)
            binding.tvViewMore.setOnClickListener {

                showHideSubSection()

            }
            binding.ivViewMoreDropDown.setOnClickListener {
                showHideSubSection()
            }
            //set data to view
            val data =
                list[position].data as com.emproto.networklayer.response.portfolio.ivdetails.Data
            if (data != null) {
                binding.tvProjectName.text = data.projectInformation.launchName
                binding.tvProjectLocation.text =
                    data.projectExtraDetails.address.city + "," + data.projectExtraDetails.address.state
                if (data.investmentInformation != null) {
                    if (data.projectExtraDetails != null) {
                        binding.tvPaidAmount.text =
                            Utility.formatAmount(data?.projectExtraDetails.paidAmount)
                    }
                    binding.tvAreaUnit.text =
                        "${Utility.convertTo(data?.investmentInformation.crmInventory.areaSqFt)} sqft"
                    if (data.projectInformation.fullDescription != null)
                        binding.tvProjectInfo.text = data.projectInformation.fullDescription
                    else binding.tvProjectInfo.text = "-"
                    var reraNumber = "-"
                    if (data.investmentInformation.crmInventory.crmReraPhase != null) {
                        reraNumber = data.investmentInformation.crmInventory.crmReraPhase.reraNumber
                    }

//                    val mSize = data.projectInformation.reraDetails.reraNumbers.size
//                    for ((index, item) in data.projectInformation.reraDetails.reraNumbers.withIndex()) {
//                        reraNumber += item
//                        if (index + 1 != mSize) {
//                            reraNumber += "\n"
//                        }
//                    }
                    if (data.investmentInformation.allocationDate != null)
                        binding.tvAllocationDate.text =
                            Utility.parseDateFromUtc(
                                data.investmentInformation.allocationDate,
                                null
                            )
                    if (data.investmentInformation.possesionDate != null) {
                        binding.tvPossessionDate.text =
                            Utility.parseDateFromUtcToMMYYYY(
                                data.investmentInformation.possesionDate,
                                null
                            )
                    } else {
                        binding.tvPossessionDate.text = "-"
                    }
                    //view more
                    binding.tvLandId.text = "Hoabl/" + data.investmentInformation.crmInventory.name
                    if (data.investmentInformation.crmInventoryBucket != null) {
                        binding.tvSkuType.text = data.investmentInformation.crmInventoryBucket.name
                    }

                    if (data.investmentInformation != null) {

                    }
                    binding.tvInvestmentAmount.text =
                        Utility.formatAmount(data.investmentInformation.amountInvested)

                    if (data.investmentInformation != null) {
                        binding.tvAmountPending.text =
                            Utility.formatAmount(data.projectExtraDetails.amountPending)
                    }

                    binding.tvRegistryAmount.text =
                        Utility.formatAmount(data.investmentInformation.sdrCharges)
                    binding.tvOtherExpenses.text =
                        Utility.formatAmount(data.investmentInformation.otherExpenses)

                    binding.tvInvestmentAmountTitle.setOnClickListener {
                        getToolTip("₹${data.investmentInformation.amountInvested}").showAlignTop(
                            binding.ivInvestedAmount
                        )
                    }
                    binding.ivInvestedAmount.setOnClickListener {
                        getToolTip("₹${data.investmentInformation.amountInvested}").showAlignTop(
                            binding.ivInvestedAmount
                        )
                    }

                    binding.ivAmountPending.setOnClickListener {
                        getToolTip("₹${data.investmentInformation.sdrCharges}").showAlignTop(
                            binding.ivAmountPending
                        )
                    }
                    binding.tvRegistryAmountTitle.setOnClickListener {
                        getToolTip("₹${data.investmentInformation.sdrCharges}").showAlignTop(
                            binding.ivAmountPending
                        )
                    }
                    binding.tvOtherExpensesTitle.setOnClickListener {
                        getToolTip("₹${data.investmentInformation.otherExpenses}").showAlignTop(
                            binding.ivAmountPending1
                        )
                    }
                    binding.ivAmountPending1.setOnClickListener {
                        getToolTip("₹${data.investmentInformation.otherExpenses}").showAlignTop(
                            binding.ivAmountPending1
                        )
                    }

                    binding.registrationNo.text = reraNumber
                    binding.tvLatitude.text = data.projectInformation.crmProject.lattitude
                    binding.tvLongitude.text = data.projectInformation.crmProject.longitude
                    binding.tvAltitude.text = data.projectInformation.crmProject.altitude + "m"
                    binding.ownersName.text = data.investmentInformation.owners[0]
                    Glide.with(context).load(data.projectExtraDetails.projectIco.value.url)
                        .into(binding.ivProjectImage)
                    //project status based configuration
                    if (data.projectExtraDetails.isBookingComplete) {
                        binding.tvPending.text = "IEA"
                        binding.tvPendingAmount.text = "${list[position].iea}%"
                        binding.tvPendingAmount.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_trending,
                            0,
                            0,
                            0
                        )
                        binding.tvPending.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        binding.tvPendingAmount.setTextColor(context.getColor(R.color.app_color))
                        binding.tvPaid.text = "Invested"
                        binding.tvPaidAmount.text =
                                //NumberFormat.getCurrencyInstance(Locale("en", "in")).format(data.investmentInformation.bookingJourney.paidAmount)
                            Utility.formatAmount(data.investmentInformation.amountInvested)
                        getToolTip("₹${data.investmentInformation.amountInvested}")
                        binding.tvPaid.setOnClickListener {
                            getToolTip("₹${data.investmentInformation.amountInvested}").showAlignTop(
                                binding.tvPaid
                            )
                        }

                        binding.tvAmountPaid.visibility = View.GONE
                        binding.tvAmountPaidTitle.visibility = View.GONE
                        binding.tvAmountPending.visibility = View.GONE
                        binding.tvAmountPendingTitle.visibility = View.GONE
                        binding.tvAllocationDateText.text = "Owned Since"
                    } else {
                        binding.tvPendingAmount.text =
                            Utility.formatAmount(data.projectExtraDetails.amountPending)
                        binding.tvAmountPaid.text =
                            Utility.formatAmount(data.projectExtraDetails.paidAmount)
                        binding.tvPaid.setOnClickListener {
                            getToolTip("₹${data.projectExtraDetails.paidAmount}").showAlignTop(
                                binding.tvPaid
                            )
                        }
                        binding.tvPending.setOnClickListener {
                            getToolTip("₹${BigDecimal(data.projectExtraDetails.amountPending)}").showAlignTop(
                                binding.tvPending
                            )
                        }
                    }
                }


            }

            binding.tvViewTimeline.setOnClickListener {
                ivInterface.seeProjectTimeline(data.projectInformation.id)

            }
            binding.tvViewBookingJourney.setOnClickListener {
                ivInterface.seeBookingJourney(data.investmentInformation.id)
            }
            binding.tvSeeProjectDetails.setOnClickListener {
                ivInterface.seeProjectDetails(data.projectInformation.id)
            }
            binding.tvSeeOnMap.setOnClickListener {
                ivInterface.seeOnMap(
                    data.projectInformation.crmProject.lattitude,
                    data.projectInformation.crmProject.longitude
                )
            }
        }

        private fun showHideSubSection() {
            if (binding.cvMoreInfoCard.visibility == View.VISIBLE) {
                binding.cvMoreInfoCard.visibility = View.GONE
                binding.tvViewMore.text = "View More"
                binding.ivViewMoreDropDown.setImageDrawable(context.getDrawable(R.drawable.ic_drop_down))

            } else {
                binding.cvMoreInfoCard.visibility = View.VISIBLE
                binding.tvViewMore.text = "View Less"
                binding.ivViewMoreDropDown.setImageDrawable(context.getDrawable(R.drawable.ic_arrow_upward))
            }
        }
    }

    private inner class PendingViewHolder(private val binding: PendingItemsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

            val allPayments = list[position].data as List<PaymentSchedulesItem>
            val investmentid = list[position].iid

            specificViewPagerAdapter = PortfolioSpecificViewPagerAdapter(allPayments,
                object : PortfolioSpecificViewPagerAdapter.PendingCardInterface {
                    override fun onclickCard() {
                        ivInterface.seeBookingJourney(investmentid)
                    }

                })
            binding.vpAttention.adapter = specificViewPagerAdapter
            binding.tvSeeallAttention.setOnClickListener {
                ivInterface.seeBookingJourney(investmentid)
            }

            TabLayoutMediator(binding.tabDotLayout, binding.vpAttention) { _, _ ->
            }.attach()
        }
    }

    private inner class FacilityManagementViewHolder(private val binding: FacilityManagementLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val fmCardUrl = list[position].data as String
            Glide.with(context)
                .load(fmCardUrl)
                .into(binding.cvFacilityManagementCard)
            binding.cvFacilityManagementCard.setOnClickListener { ivInterface.onClickFacilityCard() }
        }
    }

    private inner class DocumentsViewHolder(private val binding: DocumentsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            if (list[position].data != null) {
                val docList = list[position].data as List<Data>
                documentsAdapter = DocumentsAdapter(docList, false, object : DocumentInterface {
                    override fun onclickDocument(name: String, path: String) {
                        ivInterface.onDocumentView(name, path)
                    }

                })
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
            binding.tvLatestImagesVideos.text = headingDetails.latestMediaGallerySectionHeading
            val imagesList = ArrayList<MediaViewItem>()
            var itemId = 0
            val allMediasList = ArrayList<MediaViewItem>()
            val imagesData = list[position].data as LatestMediaGalleryOrProjectContent
            for (item in imagesData.droneShoots) {
                if (item.status == MEDIA_ACTIVE) {
                    itemId++
                    allMediasList.add(
                        MediaViewItem(
                            item.mediaContentType,
                            item.mediaContent.value.url,
                            title = Constants.DRONE_SHOOT,
                            id = itemId,
                            name = item.name
                        )
                    )
                }
            }
            for (item in imagesData.images) {
                if (item.status == MEDIA_ACTIVE) {
                    itemId++
                    allMediasList.add(
                        MediaViewItem(
                            item.mediaContentType,
                            item.mediaContent.value.url,
                            title = "Images",
                            id = itemId,
                            name = item.name
                        )
                    )
                }
            }
            for (item in imagesData.videos) {
                if (item.status == MEDIA_ACTIVE) {
                    itemId++
                    allMediasList.add(
                        MediaViewItem(
                            item.mediaContentType,
                            item.mediaContent.value.url,
                            title = Constants.VIDEOS,
                            id = itemId,
                            name = item.name
                        )
                    )
                }
            }
            for (item in imagesData.threeSixtyImages) {
                if (item.status == MEDIA_ACTIVE) {
                    itemId++
                    allMediasList.add(
                        MediaViewItem(
                            item.mediaContentType,
                            item.mediaContent.value.url,
                            title = "ThreeSixtyImages",
                            id = itemId,
                            name = item.name
                        )
                    )
                }
            }
            for (item in allMediasList) {
                when (item.title) {
                    "Images" -> {
                        imagesList.add(item)
                    }
                    "ThreeSixtyImages" -> {
                        imagesList.add(item)
                    }
                }
            }
            Log.d("allmedia", allMediaList.toString())
            latestImagesVideosAdapter = VideoAdapter(allMediaList, ivInterface)
            val layoutManager =
                GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)

//            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//                override fun getSpanSize(position: Int): Int {
//                    return if (position == 0) 2 else 1
//                }
//            }
            binding.rvLatestImagesVideos.layoutManager = layoutManager
            binding.rvLatestImagesVideos.adapter = latestImagesVideosAdapter
            binding.tvLastUpdatedDate.text =
                Utility.parseDateFromUtc(imagesData.updatedAt, null)

            binding.tvSeeAll.setOnClickListener {
                ivInterface.seeAllImages(allMediasList)
            }
        }
    }

    private inner class ApplicablePromisesViewHolder(private val binding: PortfolioApplicablePromisesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvApplicablePromises.text =
                headingDetails.otherSectionHeadings.promises.sectionHeading
            val promisesData = list[position].data as ProjectPromises
            promisesAdapter =
                HoABLPromisesAdapter(context, promisesData.data, ivInterface, list[position].count)
            binding.rvApplicablePromises.adapter = promisesAdapter
            binding.btnMoreAboutPromises.setOnClickListener {
                ivInterface.moreAboutPromises()
            }
        }
    }

    private inner class PriceTrendsViewHolder(private val binding: PriceTrendsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvRating.text = "${Utility.convertTo(list[position].ea)} %"
            val graphData = list[position].data as GeneralInfoEscalationGraph
            binding.tvPriceTrendsTitle.text = graphData.title
            binding.tvXAxisLabel.text = graphData.yAxisDisplayName
            binding.tvYAxisLabel.text = graphData.xAxisDisplayName
            val linevalues = ArrayList<Entry>()

            when (graphData.dataPoints.dataPointType) {
                Constants.YEARLY -> {
                    graphType = Constants.YEARLY
                    for (item in graphData.dataPoints.points) {
                        linevalues.add(Entry(item.year.toFloat(), item.value.toFloat()))
                    }
                }
                Constants.HALF_YEARLY -> {
                    graphType = Constants.HALF_YEARLY
                    for (i in 0..graphData.dataPoints.points.size - 1) {
                        val fmString = graphData.dataPoints.points[i].halfYear.substring(0, 3)
                        val yearString = graphData.dataPoints.points[i].year.substring(2, 4)
                        val str = "$fmString-$yearString"
                        xaxisList.add(str)
                    }
                    var index = 0
                    for (item in graphData.dataPoints.points) {
                        linevalues.add(Entry(index.toFloat(), item.value.toFloat()))
                        index++
                    }
                }
                Constants.QUATERLY -> {
                    graphType = Constants.QUATERLY
                    for (i in 0..graphData.dataPoints.points.size - 1) {
                        val fmString = graphData.dataPoints.points[i].quater.substring(0, 2)
                        val yearString = graphData.dataPoints.points[i].year.substring(2, 4)
                        val str = "$fmString-$yearString"
                        xaxisList.add(str)
                    }
                    var index = 0
                    for (item in graphData.dataPoints.points) {
                        linevalues.add(Entry(index.toFloat(), item.value.toFloat()))
                        index++
                    }
                }
                Constants.MONTHLY -> {
                    graphType = Constants.MONTHLY
                    for (i in 0..graphData.dataPoints.points.size - 1) {
                        val fmString = graphData.dataPoints.points[i].month.substring(0, 3)
                        val yearString = graphData.dataPoints.points[i].year.substring(2, 4)
                        val str = "$fmString-$yearString"
                        xaxisList.add(str)
                    }
                    var index = 0
                    for (item in graphData.dataPoints.points) {
                        linevalues.add(Entry(index.toFloat(), item.value.toFloat()))
                        index++
                    }
                }
            }

//            for (item in graphData.dataPoints.points) {
//                linevalues.add(Entry(item.year.toFloat(), item.value.toFloat()))
//            }
            if (linevalues.isNotEmpty()) {
                val linedataset = LineDataSet(linevalues, "")
                //We add features to our chart
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    linedataset.color = context.getColor(R.color.green)
                }

                linedataset.valueTextSize = 12F
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    linedataset.fillColor = context.getColor(R.color.green)
                }
                linedataset.mode = LineDataSet.Mode.HORIZONTAL_BEZIER;
                linedataset.setDrawCircles(false)
                linedataset.setDrawValues(false)
                val data = LineData(linedataset)

                //binding.ivPriceTrendsGraph.setDrawBorders(false);
                //binding.ivPriceTrendsGraph.setDrawGridBackground(false);
                binding.ivPriceTrendsGraph.getDescription().setEnabled(false);
                binding.ivPriceTrendsGraph.getLegend().setEnabled(false);
                binding.ivPriceTrendsGraph.getAxisLeft().setDrawGridLines(false)
                binding.ivPriceTrendsGraph.setTouchEnabled(false)
                binding.ivPriceTrendsGraph.setPinchZoom(false)
                binding.ivPriceTrendsGraph.isDoubleTapToZoomEnabled = false
                //binding.ivPriceTrendsGraph.getAxisLeft().setDrawLabels(false);
                //binding.ivPriceTrendsGraph.getAxisLeft().setDrawAxisLine(false);
                binding.ivPriceTrendsGraph.getXAxis().setDrawGridLines(false)
                binding.ivPriceTrendsGraph.xAxis.typeface
                binding.ivPriceTrendsGraph.xAxis.granularity = 1f
                binding.ivPriceTrendsGraph.getXAxis().position = XAxis.XAxisPosition.BOTTOM
                //binding.ivPriceTrendsGraph.getXAxis().setDrawAxisLine(false);
                binding.ivPriceTrendsGraph.getAxisRight().setDrawGridLines(false)
                binding.ivPriceTrendsGraph.getAxisRight().setDrawLabels(false)
                binding.ivPriceTrendsGraph.getAxisRight().setDrawAxisLine(false)
                binding.ivPriceTrendsGraph.getAxisLeft().valueFormatter = Xaxisformatter()
                binding.ivPriceTrendsGraph.xAxis.valueFormatter = Xaxisformatter()
                //binding.ivPriceTrendsGraph.axisLeft.isEnabled = false
                //binding.ivPriceTrendsGraph.axisRight.isEnabled = false
                binding.ivPriceTrendsGraph.data = data
                binding.ivPriceTrendsGraph.animateXY(2000, 2000)
            }
        }
    }

    private inner class ReferViewHolder(private val binding: PortfolioSepcificViewReferLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.btnReferNow.setOnClickListener {
                ivInterface.referNow()
            }
            binding.shareTxtCard.setOnClickListener {
                ivInterface.shareApp()
            }
        }
    }

    private inner class FaqViewHolder(private val binding: FaqPortfolioLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            if (headingDetails.otherSectionHeadings != null && headingDetails.otherSectionHeadings.faqSection != null)
                binding.tvFaqTitle.text =
                    headingDetails.otherSectionHeadings.faqSection.sectionHeading

            val faqList = list[position].data as List<ProjectContentsAndFaq>
            val showList = ArrayList<ProjectContentsAndFaq>()
            when {
                faqList.size > 2 -> {
                    for (i in 0..2) {
                        showList.add(faqList[i])
                    }
                }
                else -> {
                    for (item in faqList) {
                        showList.add(item)
                    }
                }
            }

            faqAdapter = ProjectFaqAdapter(context, showList, ivInterface)
            binding.rvFaq.adapter = faqAdapter
            binding.tvFaqReadAll.visibility = View.VISIBLE
            binding.tvFaqReadAll.setOnClickListener {
                ivInterface.readAllFaq(-1, 0)
            }
            binding.bnAskHere.setOnClickListener {
                ivInterface.onClickAsk()
            }
        }
    }

    private inner class SimilarInvestmentsViewHolder(private val binding: TrendingProjectsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvTrendingProjectsTitle.text = headingDetails.similarInvestmentSectionHeading
            if (list[position].data != null) {
                val itemList = list[position].data as List<SimilarInvestment>
                similarInvestmentsAdapter =
                    SimilarInvestmentAdapter(
                        context,
                        itemList,
                        ivInterface,
                        headingDetails.numberOfSimilarInvestmentsToShow
                    )
                binding.rvTrendingProjects.adapter = similarInvestmentsAdapter
                //binding.tvTrendingProjectsTitle.text = "Similar Investments"
                binding.tvTrendingProjectsSubtitle.visibility = View.GONE
            }
            binding.tvTrendingProjectsSeeAll.setOnClickListener {
                ivInterface.seeAllSimilarInvestment()
            }
        }
    }

    inner class Xaxisformatter : IAxisValueFormatter {
        override fun getFormattedValue(p0: Float, p1: AxisBase?): String {
            var floatValue = 0.0f
            when{
                p0<0.0f -> {
                    floatValue = 0.0f
                }
                else -> {
                    floatValue = p0
                }
            }
            return when (graphType) {
                Constants.QUATERLY -> returnFormattedValue(Math.abs(floatValue))
               Constants.MONTHLY -> returnFormattedValue(Math.abs(floatValue))
                Constants.HALF_YEARLY -> returnFormattedValue(Math.abs(floatValue))
                else -> {
                    String.format("%.0f", Math.abs(floatValue).toDouble())
                }
            }
        }
    }

    private fun returnFormattedValue(floatValue: Float): String {
        return when {
            floatValue.toInt() < 10 -> xaxisList[floatValue.toInt()]
            else -> {
                String.format("%.0f", floatValue.toDouble())
            }
        }
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


    interface InvestmentScreenInterface {
        fun onClickFacilityCard()
        fun seeAllCard()
        fun seeProjectTimeline(id: Int)
        fun seeBookingJourney(id: Int)
        fun referNow()
        fun seeAllSimilarInvestment()
        fun onClickSimilarInvestment(project: Int)
        fun onApplySinvestment(projectId: Int)
        fun readAllFaq(position: Int, faqId: Int)
        fun seePromisesDetails(position: Int)
        fun moreAboutPromises()
        fun seeProjectDetails(projectId: Int)
        fun seeOnMap(latitude: String, longitude: String)
        fun onClickImage(mediaViewItem: MediaViewItem, position: Int)
        fun seeAllImages(imagesList: ArrayList<MediaViewItem>)
        fun shareApp()
        fun onClickAsk()
        fun onDocumentView(name: String, path: String)
    }

    private fun setScaleAnimation(view: View) {
        val anim = ScaleAnimation(
            0.0f, 1.0f, 0.0f, 1.0f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        anim.duration = 300
        view.startAnimation(anim)
    }

}