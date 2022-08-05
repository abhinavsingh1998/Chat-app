package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.core.text.color
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.bumptech.glide.Glide
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.*
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.model.YoutubeModel
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.utils.MapItemClickListener
import com.emproto.hoabl.utils.SimilarInvItemClickListener
import com.emproto.hoabl.utils.YoutubeItemClickListener
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.networklayer.response.home.PageManagementsOrTestimonial
import com.emproto.networklayer.response.investment.*
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textview.MaterialTextView
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.createBalloon
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit


class ProjectDetailAdapter(
    private val context: Context,
    private val list: List<RecyclerViewItem>,
    private val data: PdData,
    private val promisesData: List<PmData>,
    private val itemClickListener: ItemClickListener,
    private val isBookmarked: Boolean,
    private val investmentViewModel: InvestmentViewModel,
    private val videoItemClickListener: YoutubeItemClickListener,
    private val similarInvItemClickListener: SimilarInvItemClickListener,
    private val mapItemClickListener: MapItemClickListener,
    private val projectContentsAndFaqs: List<ProjectContentsAndFaq>,
    private val pageManagementContent: PageManagementContent
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_PROJECT_DETAIL = 1
        const val VIEW_TYPE_MAP = 2
        const val VIEW_TYPE_PRICE_TRENDS = 3
        const val VIEW_TYPE_KEY_PILLARS = 4
        const val VIEW_TYPE_VIDEO_DRONE = 5
        const val VIEW_TYPE_DONT_MISS = 6
        const val VIEW_TYPE_SKUS = 7
        const val VIEW_TYPE_AMENITIES = 8
        const val VIEW_TYPE_LOCATION_INFRASTRUCTURE = 9
        const val VIEW_TYPE_PROMISES = 10
        const val VIEW_TYPE_FAQ = 11
        const val VIEW_TYPE_TESTIMONIALS = 12
        const val VIEW_TYPE_NOT_CONVINCED = 13
        const val VIEW_TYPE_SIMILAR_INVESTMENT = 14
        const val TWO_SPACES = " "
        const val MEDIA_ACTIVE = "1001"
        const val MEDIA_INACTIVE = "1002"
    }

    private lateinit var projectDetailViewPagerAdapter: ProjectDetailViewPagerAdapter
    private lateinit var keyPillarAdapter: KeyPillarAdapter
    private lateinit var videoDroneAdapter: VideoDroneAdapter
    private lateinit var skuAdapter: SkuAdapter
    private lateinit var locationInfrastructureAdapter: LocationInfrastructureAdapter
    private lateinit var promisesAdapter: PromisesAdapter
    private lateinit var faqAdapter: FaqQuestionAdapter
    private lateinit var similarInvestmentsAdapter: InvestmentAdapter
    private lateinit var onItemClickListener: View.OnClickListener

    private var isCollapsed = true
    private var isClicked = true
    private var isReadMoreClicked = true
    private var graphType = ""
    private var xaxisList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_PROJECT_DETAIL -> {
                ProjectTopCardViewHolder(
                    ProjectDetailTopLayoutBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
            VIEW_TYPE_MAP -> {
                ProjectMapViewHolder(
                    ViewMapLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_TYPE_PRICE_TRENDS -> {
                ProjectPriceTrendsViewHolder(
                    PriceTrendsLayoutBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
            VIEW_TYPE_KEY_PILLARS -> {
                ProjectKeyPillarsViewHolder(
                    KeyPillarsLayoutBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
            VIEW_TYPE_VIDEO_DRONE -> {
                ProjectVideosDroneViewHolder(
                    VideoDroneLayoutBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
            VIEW_TYPE_DONT_MISS -> {
                ProjectDontMissViewHolder(
                    DontMissLayoutPdBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_TYPE_SKUS -> {
                ProjectSkusViewHolder(
                    SkusLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_TYPE_AMENITIES -> {
                ProjectAmenitiesViewHolder(
                    ProjectAmenitiesLayoutBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
            VIEW_TYPE_LOCATION_INFRASTRUCTURE -> {
                ProjectLocationInfrastructureViewHolder(
                    LocationInfrastructureLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_TYPE_PROMISES -> {
                ProjectPromisesViewHolder(
                    PromisesLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_TYPE_FAQ -> {
                ProjectFaqViewHolder(
                    FaqLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_TYPE_TESTIMONIALS -> {
                ProjectTestimonialsViewHolder(
                    NewInvestmentTestimonialsCardBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_TYPE_NOT_CONVINCED -> {
                ProjectNotConvincedViewHolder(
                    NotConvincedLayoutBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
            else -> {
                ProjectSimilarInvestmentsViewHolder(
                    SimilarInvestmentsLayoutBinding.inflate(
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
            VIEW_TYPE_PROJECT_DETAIL -> {
                (holder as ProjectTopCardViewHolder).bind(position)
            }
            VIEW_TYPE_MAP -> {
                (holder as ProjectMapViewHolder).bind(position)
            }
            VIEW_TYPE_PRICE_TRENDS -> {
                (holder as ProjectPriceTrendsViewHolder).bind(position)
            }
            VIEW_TYPE_KEY_PILLARS -> {
                (holder as ProjectKeyPillarsViewHolder).bind(position)
            }
            VIEW_TYPE_VIDEO_DRONE -> {
                (holder as ProjectVideosDroneViewHolder).bind(position)
            }
            VIEW_TYPE_DONT_MISS -> {
                (holder as ProjectDontMissViewHolder).bind(position)
            }
            VIEW_TYPE_SKUS -> {
                (holder as ProjectSkusViewHolder).bind(position)
            }
            VIEW_TYPE_AMENITIES -> {
                (holder as ProjectAmenitiesViewHolder).bind(position)
            }
            VIEW_TYPE_LOCATION_INFRASTRUCTURE -> {
                (holder as ProjectLocationInfrastructureViewHolder).bind(position)
            }
            VIEW_TYPE_PROMISES -> {
                (holder as ProjectPromisesViewHolder).bind(position)
            }
            VIEW_TYPE_FAQ -> {
                (holder as ProjectFaqViewHolder).bind(position)
            }
            VIEW_TYPE_TESTIMONIALS -> {
                (holder as ProjectTestimonialsViewHolder).bind(position)
            }
            VIEW_TYPE_NOT_CONVINCED -> {
                (holder as ProjectNotConvincedViewHolder).bind(position)
            }
            VIEW_TYPE_SIMILAR_INVESTMENT -> {
                (holder as ProjectSimilarInvestmentsViewHolder).bind(position)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

    private inner class ProjectTopCardViewHolder(val binding: ProjectDetailTopLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val list = ArrayList<RecyclerViewItem>()
            val listViews = ArrayList<String>()
            listViews.add(data.projectCoverImages.newInvestmentPageMedia.value.url)
            for (item in data.mediaGalleryOrProjectContent[0].images!!) {
                listViews.add(item.mediaContent.value.url)
                list.add(RecyclerViewItem(1))
            }
            list.add(RecyclerViewItem(2))

            projectDetailViewPagerAdapter =
                ProjectDetailViewPagerAdapter(context, list, listViews, itemClickListener)
            binding.projectDetailViewPager.adapter = projectDetailViewPagerAdapter
            binding.projectDetailViewPager.clipToPadding = false
            binding.projectDetailViewPager.clipChildren = false
            binding.projectDetailViewPager.offscreenPageLimit = 3
            binding.projectDetailViewPager.getChildAt(0).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER
            val comPosPageTarn = CompositePageTransformer()
            comPosPageTarn.addTransformer(MarginPageTransformer(40))
            comPosPageTarn.addTransformer { page, pos ->
                val r: Float = 1 - kotlin.math.abs(pos)
                page.scaleY = 0.85f + r * 0.20f
            }
            binding.projectDetailViewPager.setPageTransformer(comPosPageTarn)
            TabLayoutMediator(binding.tabDotLayout, binding.projectDetailViewPager) { _, _ ->
            }.attach()
            itemView.tag = this

            binding.apply {
                tvProjectName.text = data.launchName

                tvProjectLocation.text = "${data.address.city}, ${data.address.state}"
//                tvViewCount.text = Utility.coolFormat(data.fomoContent.noOfViews.toDouble(),0)
                tvViewCount.text = data.fomoContent.noOfViews.toString()
//                tvDuration.text = "${data.fomoContent.targetTime.hours}:${data.fomoContent.targetTime.minutes}:${data.fomoContent.targetTime.seconds} Hrs Left"
                val amount = data.priceStartingFrom.toDouble() / 100000
                val convertedAmount = amount.toString().replace(".0", "")
                tvPriceRange.text = "₹${convertedAmount} L"
                tvAreaRange.text = "${data.areaStartingFrom} Sqft"
                tvProjectViewInfo.text = SpannableStringBuilder()
//                    .bold { append("${Utility.coolFormat(data.fomoContent.noOfViews.toDouble(),0)} People") }
                    .bold { append("${data.fomoContent.noOfViews} People") }
                    .append(" saw this in ${data.fomoContent.days} days")
                var regString = ""
                for (item in data.reraDetails.reraNumbers) {
                    when (regString) {
                        "" -> regString += item
                        else -> regString = regString + "\n" + item
                    }
                }
                Glide.with(context)
                    .load(data.opportunityDoc.whyToInvestMedia.value.url)
                    .into(binding.whyInvestCard)
                tvRegistrationNumber.text = regString
                Glide.with(context)
                    .load(data.projectCoverImages.newInvestmentPageMedia.value.url)
                    .into(ivSmallTopImage)
                tvLocationInformationText.text = data.fullDescription
                when {
                    data.fullDescription.isNullOrEmpty() -> {
                        btnReadMore.visibility = View.GONE
                    }
                }
                btnReadMore.setOnClickListener {
                    when (isReadMoreClicked) {
                        true -> {
                            btnReadMore.visibility = View.GONE
                            tvLocationInformationText.text = SpannableStringBuilder()
                                .append(data.fullDescription + " ")
                                .bold {
                                    color(context.resources.getColor(R.color.app_color)) {
                                        append(
                                            " ${context.resources.getString(R.string.read_less_expand)}"
                                        )
                                    }
                                }
                            tvLocationInformationText.maxLines = Integer.MAX_VALUE
                            isReadMoreClicked = false
                        }
                    }
                }
                tvLocationInformationText.setOnClickListener {
                    when (isReadMoreClicked) {
                        false -> {
                            btnReadMore.visibility = View.VISIBLE
                            tvLocationInformationText.text = data.fullDescription
                            tvLocationInformationText.maxLines = 2
                            isReadMoreClicked = true
                        }
                    }
                }

                val balloon = createBalloon(context) {
                    setLayout(R.layout.tooltip_layout)
                    setArrowSize(10)
                    setWidthRatio(1.0f)
                    setArrowPosition(0.9f)
                    setCornerRadius(4f)
                    setAlpha(0.9f)
                    setTextColorResource(R.color.white)
//                    setIconDrawable(ContextCompat.getDrawable(context, R.drawable.ic_profile))
                    setBackgroundColorResource(R.color.black)
                    setPadding(5)
//                    setOnBalloonClickListener(onBalloonClickListener)
                    setBalloonAnimation(BalloonAnimation.FADE)
                    setLifecycleOwner(lifecycleOwner)
                }
                balloon.getContentView().findViewById<MaterialTextView>(R.id.tv_tooltip_info).text =
                    data.reraDetails.companyNameAndAddress
                ivRegInfo.setOnClickListener {
                    balloon.showAlignBottom(ivRegInfo)
                }
                binding.ivShareIcon.setOnClickListener(onItemClickListener)
                when (isBookmarked) {
                    true -> {
                        ivBookmarkIcon.setImageResource(R.drawable.heart_5_filled)
                        isClicked = false
                    }
                    false -> {
                        ivBookmarkIcon.setImageResource(R.drawable.heart_5)
                        isClicked = true
                    }
                }
                binding.ivBookmarkIcon.setOnClickListener {
                    when (isClicked) {
                        true -> {
                            ivBookmarkIcon.setImageResource(R.drawable.heart_5_filled)
                            itemClickListener.onItemClicked(it, position, isClicked.toString())
                            isClicked = false
                        }
                        false -> {
                            ivBookmarkIcon.setImageResource(R.drawable.heart_5)
                            itemClickListener.onItemClicked(it, position, isClicked.toString())
                            isClicked = true
                        }
                    }
                }
                binding.cvWhyInvestCard.setOnClickListener(onItemClickListener)
                binding.tvApplyNow.setOnClickListener(onItemClickListener)
                binding.tvFullApplyNow.setOnClickListener(onItemClickListener)
                binding.tvRating.text = "${
                    String.format(
                        "%.0f",
                        data.generalInfoEscalationGraph.estimatedAppreciation.toDouble()
                    )
                }%"

                val hoursInMillis =
                    TimeUnit.HOURS.toMillis(data.fomoContent.targetTime.hours.toLong())
                val minsInMillis =
                    TimeUnit.MINUTES.toMillis(data.fomoContent.targetTime.minutes.toLong())
                val secsInMillis =
                    TimeUnit.SECONDS.toMillis(data.fomoContent.targetTime.seconds.toLong())
                val totalTimeInMillis = hoursInMillis + minsInMillis + secsInMillis

                val timeCounter = object : CountDownTimer(totalTimeInMillis, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        val f = DecimalFormat("00")
                        val fh = DecimalFormat("0")
                        val hour = millisUntilFinished / 3600000 % 24
                        val min = millisUntilFinished / 60000 % 60
                        val sec = millisUntilFinished / 1000 % 60
                        binding.tvDuration.text = "${
                            fh.format(hour).toString() + ":" + f.format(min) + ":" + f.format(sec)
                        } Hrs Left"
                    }

                    override fun onFinish() {

                    }

                }
                timeCounter.start()

                when (data.fomoContent.isDaysActive) {
                    true -> {
                        binding.tvProjectViewInfo.visibility = View.VISIBLE
                        binding.tvApplyNow.visibility = View.VISIBLE
                        binding.tvFullApplyNow.visibility = View.GONE
                    }
                    false -> {
                        binding.tvProjectViewInfo.visibility = View.INVISIBLE
                        binding.tvApplyNow.visibility = View.INVISIBLE
                        binding.tvFullApplyNow.visibility = View.VISIBLE
                    }
                }
                when (data.fomoContent.isNoOfViewsActive) {
                    true -> {
                        binding.clView.visibility = View.VISIBLE
                    }
                    false -> {
                        binding.clView.visibility = View.GONE
                    }
                }
                when (data.fomoContent.isTargetTimeActive) {
                    true -> {
                        binding.clDuration.visibility = View.VISIBLE
                    }
                    false -> {
                        binding.clDuration.visibility = View.GONE
                    }
                }
            }
        }
    }

    private inner class ProjectMapViewHolder(private val binding: ViewMapLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            Glide
                .with(context)
                .load(data.address.mapMedia.value.url)
                .into(binding.projectDetailMap)
            binding.btnViewOnMap.setOnClickListener(onItemClickListener)
        }
    }

    private inner class ProjectPriceTrendsViewHolder(private val binding: PriceTrendsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvPriceTrendsTitle.text = data.generalInfoEscalationGraph.title
            binding.tvRating.text =
                String.format("%.0f", data.generalInfoEscalationGraph.estimatedAppreciation) + "%"
            binding.tvXAxisLabel.text = data.generalInfoEscalationGraph.yAxisDisplayName
            binding.tvYAxisLabel.text = data.generalInfoEscalationGraph.xAxisDisplayName
            val graphData = data.generalInfoEscalationGraph.dataPoints.points
            val linevalues = ArrayList<Entry>()
            when (data.generalInfoEscalationGraph.dataPoints.dataPointType) {
                "Yearly" -> {
                    graphType = "Yearly"
                    for (item in graphData) {
                        linevalues.add(Entry(item.year.toFloat(), item.value.toFloat()))
                    }
                }
                "Half Yearly" -> {
                    graphType = "Half Yearly"
                    for (i in 0..data.generalInfoEscalationGraph.dataPoints.points.size - 1) {
                        val fmString =
                            data.generalInfoEscalationGraph.dataPoints.points[i].halfYear.substring(
                                0,
                                3
                            )
                        val yearString =
                            data.generalInfoEscalationGraph.dataPoints.points[i].year.substring(
                                2,
                                4
                            )
                        val str = "$fmString-$yearString"
                        xaxisList.add(str)
                    }
                    var index = 0
                    for (item in graphData) {
                        linevalues.add(Entry(index.toFloat(), item.value.toFloat()))
                        index++
                    }
                }
                "Quaterly" -> {
                    graphType = "Quaterly"
                    for (i in 0..data.generalInfoEscalationGraph.dataPoints.points.size - 1) {
                        val quarterString =
                            data.generalInfoEscalationGraph.dataPoints.points[i].quater.substring(
                                0,
                                2
                            )
                        val yearString =
                            data.generalInfoEscalationGraph.dataPoints.points[i].year.substring(
                                2,
                                4
                            )
                        val str = "$quarterString-$yearString"
                        xaxisList.add(str)
                    }
                    var index = 0
                    for (item in graphData) {
                        linevalues.add(Entry(index.toFloat(), item.value.toFloat()))
                        index++
                    }
                }
                "Monthly" -> {
                    graphType = "Monthly"
                    for (i in 0..data.generalInfoEscalationGraph.dataPoints.points.size - 1) {
                        val fmString =
                            data.generalInfoEscalationGraph.dataPoints.points[i].month.substring(
                                0,
                                3
                            )
                        val yearString =
                            data.generalInfoEscalationGraph.dataPoints.points[i].year.substring(
                                2,
                                4
                            )
                        val str = "$fmString-$yearString"
                        xaxisList.add(str)
                    }
                    var index = 0
                    for (item in graphData) {
                        linevalues.add(Entry(index.toFloat(), item.value.toFloat()))
                        index++
                    }
                }
            }
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
                linedataset.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                linedataset.setDrawCircles(false)
                linedataset.setDrawValues(false)
                val data = LineData(linedataset)

                binding.ivPriceTrendsGraph.getDescription().setEnabled(false);
                binding.ivPriceTrendsGraph.getLegend().setEnabled(false);
                binding.ivPriceTrendsGraph.getAxisLeft().setDrawGridLines(false);
                binding.ivPriceTrendsGraph.setTouchEnabled(false)
                binding.ivPriceTrendsGraph.setPinchZoom(false)
                binding.ivPriceTrendsGraph.isDoubleTapToZoomEnabled = false
                //binding.ivPriceTrendsGraph.getAxisLeft().setDrawLabels(false);
                //binding.ivPriceTrendsGraph.getAxisLeft().setDrawAxisLine(false);
                binding.ivPriceTrendsGraph.getXAxis().setDrawGridLines(false);
                binding.ivPriceTrendsGraph.getXAxis().position = XAxis.XAxisPosition.BOTTOM;
                //binding.ivPriceTrendsGraph.getXAxis().setDrawAxisLine(false);
                binding.ivPriceTrendsGraph.getAxisRight().setDrawGridLines(false);
                binding.ivPriceTrendsGraph.getAxisRight().setDrawLabels(false);
                binding.ivPriceTrendsGraph.getAxisRight().setDrawAxisLine(false);
                binding.ivPriceTrendsGraph.xAxis.granularity = 1f
                binding.ivPriceTrendsGraph.axisLeft.granularity = 1f
//            binding.ivPriceTrendsGraph.getXAxis().setAxisMaximum(data.getXMax() + 0.25f);
//            binding.ivPriceTrendsGraph.getXAxis().setAxisMinimum(data.getXMin() - 0.25f);
                //binding.ivPriceTrendsGraph.axisLeft.isEnabled = false
                //binding.ivPriceTrendsGraph.axisRight.isEnabled = false
                binding.ivPriceTrendsGraph.getAxisLeft().valueFormatter = Xaxisformatter()
                binding.ivPriceTrendsGraph.xAxis.valueFormatter = Xaxisformatter()
                binding.ivPriceTrendsGraph.data = data
                binding.ivPriceTrendsGraph.extraBottomOffset
                binding.ivPriceTrendsGraph.animateXY(2000, 2000)
                binding.textView10.visibility = View.GONE
            }
        }
    }

    inner class Xaxisformatter : IAxisValueFormatter {
        override fun getFormattedValue(p0: Float, p1: AxisBase?): String {
            return when (graphType) {
                "Quaterly" -> returnFormattedValue(p0)
                "Monthly" -> returnFormattedValue(p0)
                "Half Yearly" -> returnFormattedValue(p0)
                else -> {
                    String.format("%.0f", p0.toDouble())
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

    private inner class ProjectKeyPillarsViewHolder(private val binding: KeyPillarsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvKeyPillarsTitle.text = data.keyPillars.heading
            keyPillarAdapter = KeyPillarAdapter(context, data.keyPillars.values)
            binding.rvKeyPillars.adapter = keyPillarAdapter
        }
    }

    private inner class ProjectVideosDroneViewHolder(private val binding: VideoDroneLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvVideoTitle.text = data.mediaGallerySectionHeading
            val itemList = ArrayList<YoutubeModel>()
            for (item in data.mediaGalleryOrProjectContent[0].videos!!) {
                if (item.status == MEDIA_ACTIVE) {
                    itemList.add(YoutubeModel(title = item.name, url = item.mediaContent.value.url))
                }
            }
            for (item in data.mediaGalleryOrProjectContent[0].droneShoots!!) {
                if (item.status == MEDIA_ACTIVE) {
                    itemList.add(YoutubeModel(title = item.name, url = item.mediaContent.value.url))
                }
            }
            videoDroneAdapter = VideoDroneAdapter(itemList, videoItemClickListener)
            binding.rvVideoDrone.adapter = videoDroneAdapter
            binding.tvVideoDroneSeeAll.setOnClickListener(onItemClickListener)
        }
    }

    private inner class ProjectDontMissViewHolder(private val binding: DontMissLayoutPdBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            Glide
                .with(context)
                .load(data.offersAndPromotions.value.url)
                .into(binding.ivDontMissStaticImage)
        }
    }

    private inner class ProjectSkusViewHolder(private val binding: SkusLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvChooseSkusApplyTitle.text =
                data.otherSectionHeadings.inventoryBucketContents.sectionHeading
            skuAdapter = SkuAdapter(
                data.inventoriesList.projectContent.inventoryBucketContents,
                itemClickListener,
                investmentViewModel
            )
            binding.rvSkus.adapter = skuAdapter
            itemView.tag = this
            binding.tvSkusSeeAll.setOnClickListener(onItemClickListener)
        }
    }

    private inner class ProjectAmenitiesViewHolder(private val binding: ProjectAmenitiesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                tvProjectAmenitiesTitle.text = data.opportunityDoc.projectAminitiesSectionHeading
                val list = ArrayList<ProjectAminity>()
                if (data.opportunityDoc.projectAminities.size > 4) {
                    for (i in 0..3) {
                        list.add(data.opportunityDoc.projectAminities[i])
                    }
                } else {
                    for (item in data.opportunityDoc.projectAminities) {
                        list.add(item)
                    }
                }
                val adapter = ProjectAmenitiesAdapter(context, list)
                rvProjectAmenitiesItemRecycler.adapter = adapter
                tvProjectAmenitiesAll.setOnClickListener(onItemClickListener)
            }
        }
    }

    private inner class ProjectLocationInfrastructureViewHolder(private val binding: LocationInfrastructureLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvLocationInfrastructureTitle.text = data.locationInfrastructure.heading
            val disList = ArrayList<String>()
            locationInfrastructureAdapter = LocationInfrastructureAdapter(
                context,
                data.locationInfrastructure.values,
                mapItemClickListener,
                false,
                disList
            )
            binding.rvLocationInfrastructure.adapter = locationInfrastructureAdapter
            binding.tvLocationInfrastructureAll.setOnClickListener(onItemClickListener)
        }
    }

    private inner class ProjectPromisesViewHolder(private val binding: PromisesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvPromisesTitle.text = data.otherSectionHeadings.promises.sectionHeading
            val promisesList = ArrayList<PmData>()
            for (item in promisesData) {
                if (item.priority != null) {
                    promisesList.add(item)
                }
            }
            val itemList = promisesList.sortedBy { it.priority }
            val sortedByList = ArrayList<PmData>()
            val listSize = pageManagementContent[0].totalPromisesOnHomeScreen
            for (i in 0..listSize - 1) {
                sortedByList.add(itemList[i])
            }
            promisesAdapter = PromisesAdapter(sortedByList, itemClickListener, context)
            binding.rvPromises.adapter = promisesAdapter
            binding.clNotConvincedPromises.setOnClickListener(onItemClickListener)
            binding.tvPromisesSeeAll.setOnClickListener(onItemClickListener)
        }
    }

    private inner class ProjectFaqViewHolder(private val binding: FaqLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            if (data.otherSectionHeadings != null) {
                binding.tvFaqTitle.text = data.otherSectionHeadings.faqSection.sectionHeading
            }
            val itemList = projectContentsAndFaqs
            val list = ArrayList<ProjectContentsAndFaq>()
            when {
                itemList.size > 2 -> {
                    for (i in 0..1) {
                        list.add(projectContentsAndFaqs[i])
                        faqAdapter = FaqQuestionAdapter(list, itemClickListener)
                    }
                }
                else -> {
                    faqAdapter = FaqQuestionAdapter(itemList, itemClickListener)
                }
            }
            binding.rvFaq.adapter = faqAdapter
            binding.tvFaqReadAll.setOnClickListener(onItemClickListener)
            binding.bnAskHere.setOnClickListener(onItemClickListener)
            binding.ivSeeAllArrow.setOnClickListener(onItemClickListener)
        }
    }

    private inner class ProjectTestimonialsViewHolder(private val binding: NewInvestmentTestimonialsCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                tvVideoTitle.text = data.otherSectionHeadings.testimonials.sectionHeading
                val list = ArrayList<PageManagementsOrTestimonial>()
                for (item in data.testimonials) {
                    list.add(
                        PageManagementsOrTestimonial(
                            firstName = item.firstName,
                            lastName = item.lastName,
                            designation = item.designation,
                            testimonialContent = item.testimonialContent,
                            companyName = item.companyName,
                            createdAt = item.createdAt,
                            createdBy = item.createdBy,
                            id = item.id,
                            priority = item.priority,
                            status = item.status,
                            updatedAt = item.updatedAt,
                            updatedBy = item.updatedBy
                        )
                    )
                }
                val sortedList = list.sortedBy { it.priority }
                val showList = ArrayList<PageManagementsOrTestimonial>()
                val listSize = pageManagementContent[0].totalTestimonialsOnHomeScreen
                for (i in 0..listSize - 1) {
                    showList.add(sortedList[i])
                }
                val adapter = TestimonialInvAdapter(context, showList)
                binding.vpTestimonials.adapter = adapter
                TabLayoutMediator(
                    binding.tabDotLayout,
                    binding.vpTestimonials
                ) { _, _ ->
                }.attach()
                tvHearSpeakSeeAll.setOnClickListener {
                    itemClickListener.onItemClicked(it, position, list.size.toString())
                }
            }
        }
    }

    private inner class ProjectNotConvincedViewHolder(private val binding: NotConvincedLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val list = list[position]
        }
    }

    private inner class ProjectSimilarInvestmentsViewHolder(private val binding: SimilarInvestmentsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvSimilarInvestmentTitle.text = data.similarInvestmentSectionHeading
            val itemList = ArrayList<SimilarInvestment>()
            if(data.similarInvestments.isNotEmpty()){
                for (i in 0..data.numberOfSimilarInvestmentsToShow - 1) {
                    itemList.add(data.similarInvestments[i])
                }
                similarInvestmentsAdapter =
                    InvestmentAdapter(context, itemList, similarInvItemClickListener)
                binding.rvSimilarInvestment.adapter = similarInvestmentsAdapter
            }
            binding.tvSimilarInvestmentSeeAll.setOnClickListener(onItemClickListener)
        }
    }

    fun setItemClickListener(clickListener: View.OnClickListener) {
        onItemClickListener = clickListener
    }

}