package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.*
import com.emproto.hoabl.model.RecyclerViewItem

class OpportunityDocsAdapter(private val context: Context, private val itemList:List<RecyclerViewItem>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val OPP_DOCS_VIEW_TYPE_ONE = 1
        const val OPP_DOCS_VIEW_TYPE_TWO = 2
        const val OPP_DOCS_VIEW_TYPE_THREE = 3
        const val OPP_DOCS_VIEW_TYPE_FOUR = 4
        const val OPP_DOCS_VIEW_TYPE_FIVE = 5
        const val OPP_DOCS_VIEW_TYPE_SIX = 6
        const val OPP_DOCS_VIEW_TYPE_SEVEN = 7
        const val OPP_DOCS_VIEW_TYPE_EIGHT = 8
    }

    private lateinit var keyAttractionsAdapter: KeyAttractionsAdapter
    private lateinit var destinationAdapter: DestinationAdapter
    private lateinit var currentInfraStoryAdapter: CurrentInfraStoryAdapter
    private lateinit var upcomingInfraStoryAdapter: UpcomingInfraStoryAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
                OPP_DOCS_VIEW_TYPE_ONE -> OppDocsTopViewHolder(OppDocsTopLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
                OPP_DOCS_VIEW_TYPE_TWO -> OppDocsExpGrowthViewHolder(OppDocExpectedGrowthLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
                OPP_DOCS_VIEW_TYPE_THREE -> CurrentInfraStoryViewHolder(CurrentInfraStoryLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
                OPP_DOCS_VIEW_TYPE_FOUR -> UpcomingInfraStoryViewHolder(UpcomingInfraStoryLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
                OPP_DOCS_VIEW_TYPE_FIVE -> OppDocsTourismViewHolder(OppDocsDestinationLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
                OPP_DOCS_VIEW_TYPE_SIX -> AboutProjectViewHolder(AboutProjectLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
                OPP_DOCS_VIEW_TYPE_SEVEN -> ProjectAmenitiesViewHolder(ProjectAmenitiesLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
                else -> ApplyViewHolder(ApplyLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(itemList[position].viewType) {
            OPP_DOCS_VIEW_TYPE_ONE -> (holder as OppDocsTopViewHolder).bind(position)
            OPP_DOCS_VIEW_TYPE_TWO -> (holder as OppDocsExpGrowthViewHolder).bind(position)
            OPP_DOCS_VIEW_TYPE_THREE -> (holder as CurrentInfraStoryViewHolder).bind(position)
            OPP_DOCS_VIEW_TYPE_FOUR -> (holder as UpcomingInfraStoryViewHolder).bind(position)
            OPP_DOCS_VIEW_TYPE_FIVE -> (holder as OppDocsTourismViewHolder).bind(position)
            OPP_DOCS_VIEW_TYPE_SIX -> (holder as AboutProjectViewHolder).bind(position)
            OPP_DOCS_VIEW_TYPE_SEVEN -> (holder as ProjectAmenitiesViewHolder).bind(position)
            OPP_DOCS_VIEW_TYPE_EIGHT -> (holder as ApplyViewHolder).bind(position)
        }
    }

    override fun getItemCount(): Int = itemList.size

    private inner class OppDocsTopViewHolder(private val binding: OppDocsTopLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
        }
    }

    private inner class OppDocsExpGrowthViewHolder(private val binding: OppDocExpectedGrowthLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){

        }
    }

    private inner class CurrentInfraStoryViewHolder(private val binding: CurrentInfraStoryLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val list = arrayListOf<String>("1","2","3","4","5")
            currentInfraStoryAdapter = CurrentInfraStoryAdapter(context,list)
            binding.rvCurrentInfraRecycler.adapter = currentInfraStoryAdapter
        }
    }

    private inner class UpcomingInfraStoryViewHolder(private val binding: UpcomingInfraStoryLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val list = arrayListOf<String>("1","2","3","4","5")
            upcomingInfraStoryAdapter = UpcomingInfraStoryAdapter(context,list)
            binding.rvUpcomingInfraRecycler.adapter = upcomingInfraStoryAdapter
        }
    }

    private inner class OppDocsTourismViewHolder(private val binding: OppDocsDestinationLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val list = arrayListOf<String>("1","2","3","4","5","6")
            destinationAdapter = DestinationAdapter(list)
            binding.rvDestination.adapter = destinationAdapter
        }
    }

    private inner class AboutProjectViewHolder(private val binding: AboutProjectLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){

        }
    }

    private inner class ProjectAmenitiesViewHolder(private val binding: ProjectAmenitiesLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){

        }
    }

    private inner class ApplyViewHolder(private val binding: ApplyLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){

        }
    }

    private inner class OppDocsKeyAttractionsViewHolder(private val binding: OppDocKeyAttractionsLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val list = arrayListOf<String>("1","2","3","4","5","6")
            keyAttractionsAdapter = KeyAttractionsAdapter(list)
            binding.rvKeyAttractions.adapter = keyAttractionsAdapter
        }
    }

    override fun getItemViewType(position: Int): Int {
        return itemList[position].viewType
    }

}