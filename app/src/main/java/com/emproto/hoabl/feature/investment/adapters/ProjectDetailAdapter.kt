package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.os.Build
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.core.text.color
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.*
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.model.YoutubeModel
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.utils.SimilarInvItemClickListener
import com.emproto.hoabl.utils.YoutubeItemClickListener
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.networklayer.response.investment.Inventory
import com.emproto.networklayer.response.investment.PdData
import com.emproto.networklayer.response.investment.PmData
import com.emproto.networklayer.response.investment.ProjectAminity
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textview.MaterialTextView
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.createBalloon


class ProjectDetailAdapter(
    private val context: Context,
    private val list: List<RecyclerViewItem>,
    private val data: PdData,
    private val promisesData: List<PmData>,
    private val itemClickListener: ItemClickListener,
    private val isBookmarked: Boolean,
    private val investmentViewModel: InvestmentViewModel,
    private val videoItemClickListener: YoutubeItemClickListener,
    private val similarInvItemClickListener: SimilarInvItemClickListener
):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
        const val VIEW_TYPE_THREE = 3
        const val VIEW_TYPE_FOUR = 4
        const val VIEW_TYPE_FIVE = 5
        const val VIEW_TYPE_SIX = 6
        const val VIEW_TYPE_SEVEN = 7
        const val VIEW_TYPE_EIGHT = 8
        const val VIEW_TYPE_NINE = 9
        const val VIEW_TYPE_TEN = 10
        const val VIEW_TYPE_ELEVEN = 11
        const val VIEW_TYPE_TWELVE = 12
        const val VIEW_TYPE_THIRTEEN = 13
        const val VIEW_TYPE_FOURTEEN = 14
        const val TWO_SPACES = " "
    }

    private lateinit var projectDetailViewPagerAdapter: ProjectDetailViewPagerAdapter
    private lateinit var keyPillarAdapter: KeyPillarAdapter
    private lateinit var videoDroneAdapter: VideoDroneAdapter
    private lateinit var skuAdapter: SkuAdapter
    private lateinit var locationInfrastructureAdapter: LocationInfrastructureAdapter
    private lateinit var promisesAdapter: PromisesAdapter
    private lateinit var faqAdapter: FaqQuestionAdapter
    private lateinit var similarInvestmentsAdapter: InvestmentAdapter
    private lateinit var onItemClickListener : View.OnClickListener

    private var isCollapsed = true
    private var isClicked = true
    private var isReadMoreClicked = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            VIEW_TYPE_ONE -> { ProjectTopCardViewHolder(ProjectDetailTopLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)) }
            VIEW_TYPE_TWO -> { ProjectMapViewHolder(ViewMapLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            VIEW_TYPE_THREE -> { ProjectPriceTrendsViewHolder(PriceTrendsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            VIEW_TYPE_FOUR -> { ProjectKeyPillarsViewHolder(KeyPillarsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            VIEW_TYPE_FIVE -> { ProjectVideosDroneViewHolder(VideoDroneLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            VIEW_TYPE_SIX -> { ProjectDontMissViewHolder(DontMissLayoutPdBinding.inflate(LayoutInflater.from(parent.context),parent,false)) }
            VIEW_TYPE_SEVEN -> { ProjectSkusViewHolder(SkusLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            VIEW_TYPE_EIGHT -> { ProjectAmenitiesViewHolder(ProjectAmenitiesLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            VIEW_TYPE_NINE -> { ProjectLocationInfrastructureViewHolder(LocationInfrastructureLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            VIEW_TYPE_TEN -> { ProjectPromisesViewHolder(PromisesLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            VIEW_TYPE_ELEVEN -> { ProjectFaqViewHolder(FaqLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            VIEW_TYPE_TWELVE -> { ProjectTestimonialsViewHolder(NewInvestmentTestimonialsCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            VIEW_TYPE_THIRTEEN -> { ProjectNotConvincedViewHolder(NotConvincedLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            else -> { ProjectSimilarInvestmentsViewHolder(SimilarInvestmentsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(list[position].viewType){
            VIEW_TYPE_ONE -> { (holder as ProjectTopCardViewHolder).bind(position)}
            VIEW_TYPE_TWO -> { (holder as ProjectMapViewHolder).bind(position)}
            VIEW_TYPE_THREE -> { (holder as ProjectPriceTrendsViewHolder).bind(position)}
            VIEW_TYPE_FOUR -> { (holder as ProjectKeyPillarsViewHolder).bind(position)}
            VIEW_TYPE_FIVE -> { (holder as ProjectVideosDroneViewHolder).bind(position)}
            VIEW_TYPE_SIX -> { (holder as ProjectDontMissViewHolder).bind(position)}
            VIEW_TYPE_SEVEN -> { (holder as ProjectSkusViewHolder).bind(position)}
            VIEW_TYPE_EIGHT -> { (holder as ProjectAmenitiesViewHolder).bind(position)}
            VIEW_TYPE_NINE -> { (holder as ProjectLocationInfrastructureViewHolder).bind(position)}
            VIEW_TYPE_TEN -> { (holder as ProjectPromisesViewHolder).bind(position)}
            VIEW_TYPE_ELEVEN -> { (holder as ProjectFaqViewHolder).bind(position)}
            VIEW_TYPE_TWELVE -> { (holder as ProjectTestimonialsViewHolder).bind(position)}
            VIEW_TYPE_THIRTEEN -> { (holder as ProjectNotConvincedViewHolder).bind(position)}
            VIEW_TYPE_FOURTEEN -> { (holder as ProjectSimilarInvestmentsViewHolder).bind(position)}
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

    private inner class ProjectTopCardViewHolder(val binding: ProjectDetailTopLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val listViews = ArrayList<String>()
            listViews.add(data.projectCoverImages.newInvestmentPageMedia.value.url)
            listViews.add(data.projectCoverImages.newInvestmentPageMedia.value.url)
            listViews.add(data.projectCoverImages.newInvestmentPageMedia.value.url)
            listViews.add(data.projectCoverImages.newInvestmentPageMedia.value.url)

            projectDetailViewPagerAdapter = ProjectDetailViewPagerAdapter(listViews)
            binding.projectDetailViewPager.adapter = projectDetailViewPagerAdapter
            binding.projectDetailViewPager.clipToPadding = false
            binding.projectDetailViewPager.clipChildren = false
            binding.projectDetailViewPager.offscreenPageLimit = 3
            binding.projectDetailViewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            val comPosPageTarn = CompositePageTransformer()
            comPosPageTarn.addTransformer(MarginPageTransformer(40))
            comPosPageTarn.addTransformer { page, pos ->
                val r: Float = 1 - kotlin.math.abs(pos)
                page.scaleY = 0.85f + r * 0.20f
            }
            binding.projectDetailViewPager.setPageTransformer(comPosPageTarn)
            TabLayoutMediator(binding.tabDotLayout,binding.projectDetailViewPager){ _, _ ->
            }.attach()
            itemView.tag = this

            binding.projectDetailViewPager.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when(position){
                        3 -> binding.clSeeAll.visibility = View.VISIBLE
                        else -> binding.clSeeAll.visibility = View.GONE
                    }
                }
            })
            binding.clSeeAll.setOnClickListener(onItemClickListener)
            binding.whyInvestCard.clOuterWhyInvest.setOnClickListener(onItemClickListener)

            binding.apply {
                tvProjectName.text = data.launchName
                tvProjectLocation.text = "${data.address.city}, ${data.address.state}"
                tvViewCount.text = Utility.coolFormat(data.fomoContent.noOfViews.toDouble(),0)
                tvDuration.text = "${data.fomoContent.targetTime.hours}:${data.fomoContent.targetTime.minutes}:${data.fomoContent.targetTime.seconds} Hrs Left"
                val amount = data.priceStartingFrom.toDouble() / 100000
                val convertedAmount = amount.toString().replace(".0","")
                tvPriceRange.text = "₹${convertedAmount} L"
                tvAreaRange.text = "${data.areaStartingFrom} Sqft"
                tvProjectViewInfo.text = SpannableStringBuilder()
                    .bold { append("${Utility.coolFormat(data.fomoContent.noOfViews.toDouble(),0)} People") }
                    .append( " saw this project in ${data.fomoContent.days} days" )
                var regString = ""
                for(item in data.reraDetails.reraNumbers){
                    when (regString) {
                        "" -> regString += item
                        else -> regString = regString + "\n" + item
                    }
                }
                tvRegistrationNumber.text = regString
                Glide.with(context)
                    .load(data.projectCoverImages.newInvestmentPageMedia.value.url)
                    .into(ivSmallTopImage)
                tvLocationInformationText.text = data.shortDescription + data.shortDescription
                btnReadMore.setOnClickListener {
                    when(isReadMoreClicked){
                        true -> {
                            btnReadMore.visibility = View.GONE
                            tvLocationInformationText.text = SpannableStringBuilder()
                                .append(data.shortDescription + data.shortDescription + " ")
                                .bold { color(context.resources.getColor(R.color.app_color)) {
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
                    when(isReadMoreClicked){
                        false -> {
                            btnReadMore.visibility = View.VISIBLE
                            tvLocationInformationText.text = data.shortDescription + data.shortDescription
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
                balloon.getContentView().findViewById<MaterialTextView>(R.id.tv_tooltip_info).text = data.reraDetails.companyNameAndAddress
                ivRegInfo.setOnClickListener {
                    balloon.showAlignBottom(ivRegInfo)
                }
                binding.ivShareIcon.setOnClickListener(onItemClickListener)
                when(isBookmarked){
                    true -> {
                        ivBookmarkIcon.setImageResource(R.drawable.ic_favourite_dark)
                        isClicked = false
                    }
                    false -> {
                        ivBookmarkIcon.setImageResource(R.drawable.ic_favourite)
                        isClicked = true
                    }
                }
                binding.ivBookmarkIcon.setOnClickListener{
                    when(isClicked){
                        true -> {
                            ivBookmarkIcon.setImageResource(R.drawable.ic_favourite_dark)
                            itemClickListener.onItemClicked(it,position,isClicked.toString())
                            isClicked = false
                        }
                        false -> {
                            ivBookmarkIcon.setImageResource(R.drawable.ic_favourite)
                            itemClickListener.onItemClicked(it,position,isClicked.toString())
                            isClicked = true
                        }
                    }
                }
                binding.whyInvestCard.clWhyInvest.setOnClickListener(onItemClickListener)
                binding.tvApplyNow.setOnClickListener(onItemClickListener)
                binding.tvRating.text = "${data.generalInfoEscalationGraph.estimatedAppreciation.toString()}%"
            }
        }
    }

    private inner class ProjectMapViewHolder(private val binding: ViewMapLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.projectDetailMap.setOnClickListener(onItemClickListener)
        }
    }

    private inner class ProjectPriceTrendsViewHolder(private val binding: PriceTrendsLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.tvRating.text = data.generalInfoEscalationGraph.estimatedAppreciation.toString()
            binding.tvXAxisLabel.text = data.generalInfoEscalationGraph.yAxisDisplayName
            binding.tvYAxisLabel.text = data.generalInfoEscalationGraph.xAxisDisplayName
            val graphData = data.generalInfoEscalationGraph.dataPoints.points

            val linevalues = ArrayList<Entry>()
            for(item in graphData){
                linevalues.add(Entry(item.year.toFloat(),item.value.toFloat()))
            }
            val linedataset = LineDataSet(linevalues, "")
            //We add features to our chart
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                linedataset.color = context.getColor(R.color.green)
            }

            linedataset.valueTextSize = 12F
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                linedataset.fillColor = context.getColor(R.color.green)
            }
            linedataset.mode = LineDataSet.Mode.LINEAR;
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
            //binding.ivPriceTrendsGraph.axisLeft.isEnabled = false
            //binding.ivPriceTrendsGraph.axisRight.isEnabled = false
            binding.ivPriceTrendsGraph.data = data
            binding.ivPriceTrendsGraph.animateXY(2000, 2000)
            binding.textView10.visibility = View.GONE
        }
    }

    private inner class ProjectKeyPillarsViewHolder(private val binding: KeyPillarsLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            keyPillarAdapter = KeyPillarAdapter(context,data.keyPillars.values)
            binding.rvKeyPillars.adapter = keyPillarAdapter
        }
    }

    private inner class ProjectVideosDroneViewHolder(private val binding: VideoDroneLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val itemList = ArrayList<YoutubeModel>()
            for(item in data.mediaGalleryOrProjectContent[0].videos){
                itemList.add(YoutubeModel(title = item.name, url = item.mediaContent.value.url))
            }
            for(item in data.mediaGalleryOrProjectContent[0].droneShoots){
                itemList.add(YoutubeModel(title = item.name, url = item.mediaContent.value.url))
            }
            videoDroneAdapter = VideoDroneAdapter(itemList,videoItemClickListener)
            binding.rvVideoDrone.adapter = videoDroneAdapter
            binding.tvVideoDroneSeeAll.setOnClickListener(onItemClickListener)
        }
    }

    private inner class ProjectDontMissViewHolder(private val binding: DontMissLayoutPdBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val image = data.offersAndPromotions.value.url
            Glide
                .with(context)
                .load(data.offersAndPromotions.value.url)
                .into(binding.ivDontMissStaticImage)
        }
    }

    private inner class ProjectSkusViewHolder(private val binding: SkusLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val notAppliedList = ArrayList<Inventory>()
            for(item in data.inventoriesList.data){
                when(item.isApplied){
                    false -> notAppliedList.add(item)
                }
            }
            skuAdapter = SkuAdapter(notAppliedList,itemClickListener, investmentViewModel)
            binding.rvSkus.adapter = skuAdapter
            itemView.tag = this
            binding.tvSkusSeeAll.setOnClickListener(onItemClickListener)
        }
    }

    private inner class ProjectAmenitiesViewHolder(private val binding: ProjectAmenitiesLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.apply {
                val list = ArrayList<ProjectAminity>()
                if(data.opprotunityDocs[0].projectAminities.size > 4){
                    for(i in 0..3){
                        list.add(data.opprotunityDocs[0].projectAminities[i])
                    }
                }else{
                    for(item in data.opprotunityDocs[0].projectAminities){
                        list.add(item)
                    }
                }
                val adapter = ProjectAmenitiesAdapter(context,list)
                rvProjectAmenitiesItemRecycler.adapter = adapter
                tvProjectAmenitiesAll.setOnClickListener(onItemClickListener)
            }
        }
    }

    private inner class ProjectLocationInfrastructureViewHolder(private val binding: LocationInfrastructureLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            locationInfrastructureAdapter = LocationInfrastructureAdapter(
                context,
                data.locationInfrastructure.values,
                itemClickListener
            )
            binding.rvLocationInfrastructure.adapter = locationInfrastructureAdapter
            binding.tvLocationInfrastructureAll.setOnClickListener(onItemClickListener)
        }
    }

    private inner class ProjectPromisesViewHolder(private val binding: PromisesLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val itemList = promisesData
            promisesAdapter = PromisesAdapter(itemList,itemClickListener,context)
            binding.rvPromises.adapter = promisesAdapter
            binding.clNotConvincedPromises.setOnClickListener(onItemClickListener)
            binding.tvPromisesSeeAll.setOnClickListener(onItemClickListener)
        }
    }

    private inner class ProjectFaqViewHolder(private val binding: FaqLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val itemList = data.projectContentsAndFaqs
            faqAdapter = FaqQuestionAdapter(itemList,itemClickListener)
            binding.rvFaq.adapter = faqAdapter
            binding.tvFaqReadAll.setOnClickListener(onItemClickListener)
        }
    }

    private inner class ProjectTestimonialsViewHolder(private val binding: NewInvestmentTestimonialsCardBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.apply {
                for(item in data.testimonials){
                    when(item.priority){
                        1.0 -> {
                            tvProfileName.text = item.firstName + " " + item.lastName
                            tvProfileDesignation.text = item.designation
                            tvProfileInfoText.text = item.testimonialContent
                        }
                    }
                }

                tvHearSpeakSeeAll.setOnClickListener(onItemClickListener)
            }
        }
    }

    private inner class ProjectNotConvincedViewHolder(private val binding: NotConvincedLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val list = list[position]
        }
    }

    private inner class ProjectSimilarInvestmentsViewHolder(private val binding: SimilarInvestmentsLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val itemList = data.similarInvestments
            similarInvestmentsAdapter = InvestmentAdapter(context, itemList, similarInvItemClickListener)
            binding.rvSimilarInvestment.adapter = similarInvestmentsAdapter
            binding.tvSimilarInvestmentSeeAll.setOnClickListener(onItemClickListener)
        }
    }

    fun setItemClickListener(clickListener: View.OnClickListener) {
        onItemClickListener = clickListener
    }

}