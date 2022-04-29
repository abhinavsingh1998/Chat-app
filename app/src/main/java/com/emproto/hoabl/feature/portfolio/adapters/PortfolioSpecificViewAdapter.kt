package com.emproto.hoabl.feature.portfolio.adapters

import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.view.animation.TranslateAnimation
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.*
import com.emproto.hoabl.feature.home.adapters.HoABLPromisesAdapter
import com.emproto.hoabl.feature.investment.adapters.FaqAdapter
import com.emproto.hoabl.feature.investment.adapters.InvestmentAdapter
import com.emproto.hoabl.feature.investment.adapters.VideoDroneAdapter
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.model.ViewItem
import com.google.android.material.tabs.TabLayoutMediator


class PortfolioSpecificViewAdapter(private val context: Context, private val list:List<RecyclerViewItem>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val PORTFOLIO_SPECIFIC_VIEW_TYPE_ONE = 1
        const val PORTFOLIO_SPECIFIC_VIEW_TYPE_TWO = 2
        const val PORTFOLIO_SPECIFIC_VIEW_TYPE_THREE = 3
        const val PORTFOLIO_SPECIFIC_VIEW_TYPE_FOUR = 4
        const val PORTFOLIO_SPECIFIC_VIEW_TYPE_FIVE = 5
        const val PORTFOLIO_SPECIFIC_VIEW_TYPE_SIX = 6
        const val PORTFOLIO_SPECIFIC_VIEW_TYPE_SEVEN = 7
        const val PORTFOLIO_SPECIFIC_VIEW_TYPE_EIGHT = 8
        const val PORTFOLIO_SPECIFIC_VIEW_TYPE_NINE = 9
        const val PORTFOLIO_SPECIFIC_VIEW_TYPE_TEN = 10
    }

    private lateinit var specificViewPagerAdapter: PortfolioSpecificViewPagerAdapter
    private lateinit var documentsAdapter: DocumentsAdapter
    private lateinit var latestImagesVideosAdapter: VideoDroneAdapter
    private lateinit var promisesAdapter: HoABLPromisesAdapter
    private lateinit var faqAdapter: FaqAdapter
    private lateinit var similarInvestmentsAdapter: InvestmentAdapter
    private lateinit var onItemClickListener: View.OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            PORTFOLIO_SPECIFIC_VIEW_TYPE_ONE -> { ProjectSpecificTopViewHolder(PortfolioSpecificViewTopLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)) }
            PORTFOLIO_SPECIFIC_VIEW_TYPE_TWO -> { PendingViewHolder(PendingItemsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            PORTFOLIO_SPECIFIC_VIEW_TYPE_THREE -> { FacilityManagementViewHolder(FacilityManagementLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            PORTFOLIO_SPECIFIC_VIEW_TYPE_FOUR -> { DocumentsViewHolder(DocumentsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            PORTFOLIO_SPECIFIC_VIEW_TYPE_FIVE -> { LatestImagesVideosViewHolder(LatestImagesVideosLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            PORTFOLIO_SPECIFIC_VIEW_TYPE_SIX -> { ApplicablePromisesViewHolder(PortfolioApplicablePromisesBinding.inflate(LayoutInflater.from(parent.context),parent,false)) }
            PORTFOLIO_SPECIFIC_VIEW_TYPE_SEVEN -> { PriceTrendsViewHolder(PriceTrendsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            PORTFOLIO_SPECIFIC_VIEW_TYPE_EIGHT -> { ReferViewHolder(PortfolioSepcificViewReferLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            PORTFOLIO_SPECIFIC_VIEW_TYPE_NINE -> { FaqViewHolder(FaqLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
            else -> { SimilarInvestmentsViewHolder(TrendingProjectsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(list[position].viewType){
            PORTFOLIO_SPECIFIC_VIEW_TYPE_ONE -> { (holder as ProjectSpecificTopViewHolder).bind(position)}
            PORTFOLIO_SPECIFIC_VIEW_TYPE_TWO -> { (holder as PendingViewHolder).bind(position)}
            PORTFOLIO_SPECIFIC_VIEW_TYPE_THREE -> { (holder as FacilityManagementViewHolder).bind(position)}
            PORTFOLIO_SPECIFIC_VIEW_TYPE_FOUR -> { (holder as DocumentsViewHolder).bind(position)}
            PORTFOLIO_SPECIFIC_VIEW_TYPE_FIVE -> { (holder as LatestImagesVideosViewHolder).bind(position)}
            PORTFOLIO_SPECIFIC_VIEW_TYPE_SIX -> { (holder as ApplicablePromisesViewHolder).bind(position)}
            PORTFOLIO_SPECIFIC_VIEW_TYPE_SEVEN -> { (holder as PriceTrendsViewHolder).bind(position)}
            PORTFOLIO_SPECIFIC_VIEW_TYPE_EIGHT -> { (holder as ReferViewHolder).bind(position)}
            PORTFOLIO_SPECIFIC_VIEW_TYPE_NINE -> { (holder as FaqViewHolder).bind(position)}
            PORTFOLIO_SPECIFIC_VIEW_TYPE_TEN -> { (holder as SimilarInvestmentsViewHolder).bind(position)}
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int = list[position].viewType

    private inner class ProjectSpecificTopViewHolder(private val binding: PortfolioSpecificViewTopLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.btnApplyNow.setOnClickListener(onItemClickListener)
            binding.tvViewMore.setOnClickListener{
                binding.tvViewLess.visibility = View.VISIBLE
                binding.ivViewMoreArrowUpward.visibility = View.VISIBLE
                binding.cvMoreInfoCard.visibility = View.VISIBLE
//                moveBottom(binding.cvMoreInfoCard)

                binding.tvViewMore.visibility = View.GONE
                binding.ivViewMoreDropDown.visibility = View.GONE
            }
            binding.tvViewLess.setOnClickListener{
                binding.tvViewLess.visibility = View.GONE
                binding.ivViewMoreArrowUpward.visibility = View.GONE
                binding.cvMoreInfoCard.visibility = View.GONE

                binding.tvViewMore.visibility = View.VISIBLE
                binding.ivViewMoreDropDown.visibility = View.VISIBLE
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

    fun moveBottom(view:View){
        val moveAnim: ObjectAnimator = ObjectAnimator.ofFloat(view, "Y", 1000f)
        moveAnim.duration = 2000
        moveAnim.interpolator = BounceInterpolator()
        moveAnim.start()
        view.visibility = View.VISIBLE
    }


    private inner class PendingViewHolder(private val binding: PendingItemsLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){

            val listViews = ArrayList<ViewItem>()
            listViews.add(ViewItem(1, R.drawable.new_investment_page_image))
            listViews.add(ViewItem(2, R.drawable.new_investment_page_image))
            listViews.add(ViewItem(3, R.drawable.new_investment_page_image))
            listViews.add(ViewItem(4, R.drawable.new_investment_page_image))
            listViews.add(ViewItem(5, R.drawable.new_investment_page_image))
            specificViewPagerAdapter = PortfolioSpecificViewPagerAdapter(listViews)
            binding.vpAttention.adapter = specificViewPagerAdapter

            TabLayoutMediator(binding.tabDotLayout,binding.vpAttention){ _, _ ->
            }.attach()
        }
    }

    private inner class FacilityManagementViewHolder(private val binding: FacilityManagementLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
        }
    }

    private inner class DocumentsViewHolder(private val binding: DocumentsLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val list = arrayListOf<String>("1","2","3")
            documentsAdapter = DocumentsAdapter(list)
            binding.rvDocuments.adapter = documentsAdapter
            binding.tvDocumentsSeeAll.setOnClickListener(onItemClickListener)
        }
    }

    private inner class LatestImagesVideosViewHolder(private val binding: LatestImagesVideosLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val itemList = ArrayList<ViewItem>()
            itemList.add(ViewItem(1, R.drawable.new_investment_page_image))
            itemList.add(ViewItem(2, R.drawable.new_investment_page_image))
            itemList.add(ViewItem(3, R.drawable.new_investment_page_image))
            itemList.add(ViewItem(4, R.drawable.new_investment_page_image))
            itemList.add(ViewItem(5, R.drawable.new_investment_page_image))
            latestImagesVideosAdapter = VideoDroneAdapter(itemList)
            binding.rvLatestImagesVideos.adapter = latestImagesVideosAdapter
        }
    }

    private inner class ApplicablePromisesViewHolder(private val binding: PortfolioApplicablePromisesBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val listPromises: ArrayList<String> = arrayListOf("Security", "Transparency", "Wealth")
            promisesAdapter = HoABLPromisesAdapter(context,listPromises)
            binding.rvApplicablePromises.adapter = promisesAdapter
        }
    }

    private inner class PriceTrendsViewHolder(private val binding: PriceTrendsLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
        }
    }

    private inner class ReferViewHolder(private val binding: PortfolioSepcificViewReferLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
        }
    }

    private inner class FaqViewHolder(private val binding: FaqLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val itemList = arrayListOf<String>("1", "2", "3", "4", "5")
            faqAdapter = FaqAdapter(itemList)
            binding.rvFaq.adapter = faqAdapter

            binding.tvReadAll.visibility = View.VISIBLE
            binding.ivSeeAllArrow.visibility = View.VISIBLE
        }
    }

    private inner class SimilarInvestmentsViewHolder(private val binding: TrendingProjectsLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val itemList = arrayListOf<String>("1", "2", "3", "4", "5")
            similarInvestmentsAdapter = InvestmentAdapter(context, itemList)
            binding.rvTrendingProjects.adapter = similarInvestmentsAdapter
            binding.tvTrendingProjectsTitle.text = "Similar Investments"
            binding.tvTrendingProjectsSubtitle.visibility = View.GONE
        }
    }


    fun setItemClickListener(clickListener: View.OnClickListener) {
        onItemClickListener = clickListener
    }

}