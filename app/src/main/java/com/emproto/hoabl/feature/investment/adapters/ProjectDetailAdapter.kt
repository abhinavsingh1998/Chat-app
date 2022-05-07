package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.*
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.networklayer.response.investment.PdData
import com.emproto.networklayer.response.investment.PmData
import com.google.android.material.tabs.TabLayoutMediator

class ProjectDetailAdapter(
    private val context: Context,
    private val list: List<RecyclerViewItem>,
    private val data: PdData,
    private val promisesData: List<PmData>
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
            binding.clNotConvinced.setOnClickListener(onItemClickListener)

            binding.apply {
                tvProjectName.text = data.launchName
                tvProjectLocation.text = "${data.address.city}, ${data.address.state}"
                tvViewCount.text = data.fomoContent.noOfViews.toString()
                tvDuration.text = "${data.fomoContent.targetTime.hours}:${data.fomoContent.targetTime.minutes}:${data.fomoContent.targetTime.seconds} Hrs Left"
                tvLocationInformationText.text = data.shortDescription
                tvPriceRange.text = data.priceStartingFrom + " Onwards"
                tvAreaRange.text = data.areaStartingFrom + " Onwards"
                tvProjectViewInfo.text = "${data.fomoContent.noOfViews} People saw this project in ${data.fomoContent.days} days"
                var regString = ""
                for(item in data.reraDetails.reraNumbers){
                    when (regString) {
                        "" -> regString += item
                        else -> regString = regString + "\n" + item
                    }
                }
                tvRegistrationNumber.text = regString
//                Glide.with(context)
//                    .load(data.offersAndPromotions.value.url)
//                    .into(ivWhyInvest)
                Glide.with(context)
                    .load(data.projectCoverImages.newInvestmentPageMedia.value.url)
                    .into(ivSmallTopImage)
            }
        }
    }

    private inner class ProjectMapViewHolder(private val binding: ViewMapLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
        }
    }

    private inner class ProjectPriceTrendsViewHolder(private val binding: PriceTrendsLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
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
            val itemList = ArrayList<String>()

            itemList.add(data.projectCoverImages.newInvestmentPageMedia.value.url)

//            itemList.add(data.mediaGalleries[0].videos[0].mediaContent.value.url)
//            itemList.add(data.mediaGalleries[0].droneShoots[0].mediaContent.value.url)
            videoDroneAdapter = VideoDroneAdapter(itemList)
            binding.rvVideoDrone.adapter = videoDroneAdapter
            binding.tvVideoDroneSeeAll.setOnClickListener(onItemClickListener)
        }
    }

    private inner class ProjectDontMissViewHolder(private val binding: DontMissLayoutPdBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
        }
    }

    private inner class ProjectSkusViewHolder(private val binding: SkusLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            skuAdapter = SkuAdapter(data.inventoryBucketContents)
            binding.rvSkus.adapter = skuAdapter
            itemView.tag = this
            binding.tvSkusSeeAll.setOnClickListener(onItemClickListener)
        }
    }

    private inner class ProjectAmenitiesViewHolder(private val binding: ProjectAmenitiesLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.apply {
                tvPaFirstText.text = data.opprotunityDocs[0].projectAminities[0].name
                tvPaSecondText.text = data.opprotunityDocs[0].projectAminities[1].name
                tvPaThirdText.text = data.opprotunityDocs[0].projectAminities[2].name
                tvPaFourText.text = data.opprotunityDocs[0].projectAminities[1].name
                tvProjectAmenitiesAll.setOnClickListener(onItemClickListener)
            }
        }
    }

    private inner class ProjectLocationInfrastructureViewHolder(private val binding: LocationInfrastructureLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            locationInfrastructureAdapter = LocationInfrastructureAdapter(data.locationInfrastructure.values)
            binding.rvLocationInfrastructure.adapter = locationInfrastructureAdapter
        }
    }

    private inner class ProjectPromisesViewHolder(private val binding: PromisesLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val itemList = promisesData
            promisesAdapter = PromisesAdapter(itemList)
            binding.rvPromises.adapter = promisesAdapter
        }
    }

    private inner class ProjectFaqViewHolder(private val binding: FaqLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val itemList = data.projectContentsAndFaqs
            faqAdapter = FaqQuestionAdapter(itemList)
            binding.rvFaq.adapter = faqAdapter
            binding.tvFaqReadAll.setOnClickListener(onItemClickListener)
        }
    }

    private inner class ProjectTestimonialsViewHolder(private val binding: NewInvestmentTestimonialsCardBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
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
            similarInvestmentsAdapter = InvestmentAdapter(context, itemList)
            binding.rvSimilarInvestment.adapter = similarInvestmentsAdapter
        }
    }

    fun setItemClickListener(clickListener: View.OnClickListener) {
        onItemClickListener = clickListener
    }
}