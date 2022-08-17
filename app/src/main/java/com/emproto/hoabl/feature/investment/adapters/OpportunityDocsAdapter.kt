package com.emproto.hoabl.feature.investment.adapters

import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.core.Constants
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.*
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.utils.Extensions.showHTMLText
import com.emproto.networklayer.response.investment.OpprotunityDoc
import com.emproto.networklayer.response.investment.ProjectAminity
import com.emproto.networklayer.response.investment.Story
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter

class OpportunityDocsAdapter(
    private val context: Context,
    private val itemList: List<RecyclerViewItem>,
    private val data: OpprotunityDoc,
    private val title: String,
    private val isFromProjectAmenities: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
    private lateinit var onItemClickListener: View.OnClickListener
    private lateinit var projectAmenitiesAdapter: ProjectAmenitiesAdapter

    private var isClicked = true
    private var graphType = ""
    private var xaxisList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            OPP_DOCS_VIEW_TYPE_ONE -> OppDocsTopViewHolder(
                OppDocsTopLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            OPP_DOCS_VIEW_TYPE_TWO -> OppDocsExpGrowthViewHolder(
                OppDocExpectedGrowthLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            OPP_DOCS_VIEW_TYPE_THREE -> CurrentInfraStoryViewHolder(
                CurrentInfraStoryLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            OPP_DOCS_VIEW_TYPE_FOUR -> UpcomingInfraStoryViewHolder(
                UpcomingInfraStoryLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            OPP_DOCS_VIEW_TYPE_FIVE -> OppDocsTourismViewHolder(
                OppDocsDestinationLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            OPP_DOCS_VIEW_TYPE_SIX -> AboutProjectViewHolder(
                AboutProjectLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            OPP_DOCS_VIEW_TYPE_SEVEN -> ProjectAmenitiesViewHolder(
                ProjectAmenitiesLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> ApplyViewHolder(
                ApplyLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (itemList[position].viewType) {
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

    private inner class OppDocsTopViewHolder(private val binding: OppDocsTopLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvOppDocProjectTitle.text = data.sectionHeading.heading
            binding.tvOppDocProjectThemeTitle.text = data.sectionHeading.subHeading
            Glide
                .with(context)
                .load(data.bannerImage.value.url)
                .into(binding.ivOppDocTopImage)
        }
    }

    private inner class OppDocsExpGrowthViewHolder(private val binding: OppDocExpectedGrowthLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvExpectedGrowth.text = data.escalationGraph.title
            binding.tvXAxisLabel.text = data.escalationGraph.yAxisDisplayName
            binding.tvYAxisLabel.text = data.escalationGraph.xAxisDisplayName
            val graphData = data.escalationGraph.dataPoints.points
            val linevalues = ArrayList<Entry>()
            when (data.escalationGraph.dataPoints.dataPointType) {
                Constants.YEARLY -> {
                    graphType = Constants.YEARLY
                    for (item in graphData) {
                        linevalues.add(Entry(item.year.toFloat(), item.value.toFloat()))
                    }
                }
                Constants.HALF_YEARLY -> {
                    graphType = Constants.HALF_YEARLY
                    for (i in 0..data.escalationGraph.dataPoints.points.size - 1) {
                        val fmString =
                            data.escalationGraph.dataPoints.points[i].halfYear.substring(0, 3)
                        val yearString =
                            data.escalationGraph.dataPoints.points[i].year.substring(2, 4)
                        val str = "$fmString-$yearString"
                        xaxisList.add(str)
                    }
                    var index = 0
                    for (item in graphData) {
                        linevalues.add(Entry(index.toFloat(), item.value.toFloat()))
                        index++
                    }
                }
                Constants.QUATERLY-> {
                    graphType = Constants.QUATERLY
                    for (i in 0..data.escalationGraph.dataPoints.points.size - 1) {
                        val fmString =
                            data.escalationGraph.dataPoints.points[i].quater.substring(0, 2)
                        val yearString =
                            data.escalationGraph.dataPoints.points[i].year.substring(2, 4)
                        val str = "$fmString-$yearString"
                        xaxisList.add(str)
                    }
                    var index = 0
                    for (item in graphData) {
                        linevalues.add(Entry(index.toFloat(), item.value.toFloat()))
                        index++
                    }
                }
                Constants.MONTHLY -> {
                    graphType = Constants.MONTHLY
                    for (i in 0..data.escalationGraph.dataPoints.points.size - 1) {
                        val fmString =
                            data.escalationGraph.dataPoints.points[i].month.substring(0, 3)
                        val yearString =
                            data.escalationGraph.dataPoints.points[i].year.substring(2, 4)
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
                binding.ivPriceTrendsGraph.animateXY(2000, 2000)
                binding.textView10.visibility = View.GONE
            }
        }
    }

    inner class Xaxisformatter : IAxisValueFormatter {
        override fun getFormattedValue(p0: Float, p1: AxisBase?): String {
            return when (graphType) {
                Constants.QUATERLY -> returnFormattedValue(p0)
                Constants.MONTHLY -> returnFormattedValue(p0)
                Constants.HALF_YEARLY -> returnFormattedValue(p0)
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

    private inner class CurrentInfraStoryViewHolder(private val binding: CurrentInfraStoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvCurrentInfraStory.text = data.currentInfraStory.heading
            currentInfraStoryAdapter =
                CurrentInfraStoryAdapter(context, data.currentInfraStory.stories)
            binding.rvCurrentInfraRecycler.adapter = currentInfraStoryAdapter
        }
    }

    private inner class UpcomingInfraStoryViewHolder(private val binding: UpcomingInfraStoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvUpcomingInfraStory.text = data.upcomingInfraStory.heading
            upcomingInfraStoryAdapter =
                UpcomingInfraStoryAdapter(context, data.upcomingInfraStory.stories)
            binding.rvUpcomingInfraRecycler.adapter = upcomingInfraStoryAdapter
        }
    }

    private inner class OppDocsTourismViewHolder(private val binding: OppDocsDestinationLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvTourismAround.text = data.tourismAround.heading
            val list = arrayListOf<Story>()
            for (i in 0..3) {
                list.add(data.tourismAround.stories[i])
            }
            destinationAdapter = DestinationAdapter(context, list)
            binding.rvDestination.adapter = destinationAdapter
            binding.tvViewMore.setOnClickListener {
                when (isClicked) {
                    true -> {
                        Glide
                            .with(context)
                            .load(R.drawable.path_3)
                            .into(binding.ivViewMoreArrow)
                        binding.tvViewMore.text = context.getString(R.string.view_less_caps)
                        destinationAdapter = DestinationAdapter(context, data.tourismAround.stories)
                        binding.rvDestination.adapter = destinationAdapter
                        destinationAdapter.notifyDataSetChanged()
                        isClicked = false
                    }
                    false -> {
                        binding.ivViewMoreArrow.setImageResource(R.drawable.path_3_1)
                        binding.tvViewMore.text = context.getString(R.string.view_more_caps)
                        list.clear()
                        for (i in 0..3) {
                            list.add(data.tourismAround.stories[i])
                        }
                        destinationAdapter = DestinationAdapter(context, list)
                        binding.rvDestination.adapter = destinationAdapter
                        destinationAdapter.notifyDataSetChanged()
                        isClicked = true
                    }
                }

            }
            binding.ivViewMoreArrow.setOnClickListener {
                when (isClicked) {
                    true -> {
                        Glide
                            .with(context)
                            .load(R.drawable.path_3)
                            .into(binding.ivViewMoreArrow)
                        binding.tvViewMore.text = context.getString(R.string.view_less_caps)
                        destinationAdapter = DestinationAdapter(context, data.tourismAround.stories)
                        binding.rvDestination.adapter = destinationAdapter
                        destinationAdapter.notifyDataSetChanged()
                        isClicked = false
                    }
                    false -> {
                        binding.ivViewMoreArrow.setImageResource(R.drawable.path_3_1)
                        binding.tvViewMore.text = context.getString(R.string.view_more_caps)
                        list.clear()
                        for (i in 0..3) {
                            list.add(data.tourismAround.stories[i])
                        }
                        destinationAdapter = DestinationAdapter(context, list)
                        binding.rvDestination.adapter = destinationAdapter
                        destinationAdapter.notifyDataSetChanged()
                        isClicked = true
                    }
                }

            }
        }
    }

    private inner class AboutProjectViewHolder(private val binding: AboutProjectLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                tvAboutProjectTitle.text = data.aboutProjects.heading
                tvAbtProjectInfo.text = context.showHTMLText(data.aboutProjects.description)
                Glide.with(context)
                    .load(data.aboutProjects.media.value.url)
                    .into(ivAbtProjectImage)
            }
        }
    }

    private inner class ProjectAmenitiesViewHolder(private val binding: ProjectAmenitiesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                tvProjectAmenitiesAll.visibility = View.INVISIBLE
                tvViewMore.visibility = View.VISIBLE
                ivViewMoreArrow.visibility = View.VISIBLE
                var isClicked = true
                val list = arrayListOf<ProjectAminity>()
                when (isFromProjectAmenities) {
                    true -> {
                        for (item in data.projectAminities) {
                            list.add(item)
                        }
                        binding.ivViewMoreArrow.setImageResource(R.drawable.path_3)
                        binding.tvViewMore.text = context.getString(R.string.view_less_caps)
                        isClicked = false
                    }
                    false -> {
                        for (i in 0..3) {
                            list.add(data.projectAminities[i])
                        }
                    }
                }
                projectAmenitiesAdapter = ProjectAmenitiesAdapter(context, list)
                rvProjectAmenitiesItemRecycler.adapter = projectAmenitiesAdapter
                binding.tvViewMore.setOnClickListener {
                    when (isClicked) {
                        true -> {
                            Glide
                                .with(context)
                                .load(R.drawable.path_3)
                                .into(binding.ivViewMoreArrow)
                            binding.tvViewMore.text = context.getString(R.string.view_less_caps)
                            projectAmenitiesAdapter =
                                ProjectAmenitiesAdapter(context, data.projectAminities)
                            rvProjectAmenitiesItemRecycler.adapter = projectAmenitiesAdapter
                            projectAmenitiesAdapter.notifyDataSetChanged()
                            isClicked = false
                        }
                        false -> {
                            binding.ivViewMoreArrow.setImageResource(R.drawable.path_3_1)
                            binding.tvViewMore.text = context.getString(R.string.view_all_caps)
                            list.clear()
                            for (i in 0..3) {
                                list.add(data.projectAminities[i])
                            }
                            projectAmenitiesAdapter = ProjectAmenitiesAdapter(context, list)
                            rvProjectAmenitiesItemRecycler.adapter = projectAmenitiesAdapter
                            projectAmenitiesAdapter.notifyDataSetChanged()
                            isClicked = true
                        }
                    }
                }
                binding.ivViewMoreArrow.setOnClickListener {
                    when (isClicked) {
                        true -> {
                            Glide
                                .with(context)
                                .load(R.drawable.path_3)
                                .into(binding.ivViewMoreArrow)
                            binding.tvViewMore.text = context.getString(R.string.view_less_caps)
                            projectAmenitiesAdapter =
                                ProjectAmenitiesAdapter(context, data.projectAminities)
                            rvProjectAmenitiesItemRecycler.adapter = projectAmenitiesAdapter
                            projectAmenitiesAdapter.notifyDataSetChanged()
                            isClicked = false
                        }
                        false -> {
                            binding.ivViewMoreArrow.setImageResource(R.drawable.path_3_1)
                            binding.tvViewMore.text = context.getString(R.string.view_all_caps)
                            list.clear()
                            for (i in 0..3) {
                                list.add(data.projectAminities[i])
                            }
                            projectAmenitiesAdapter = ProjectAmenitiesAdapter(context, list)
                            rvProjectAmenitiesItemRecycler.adapter = projectAmenitiesAdapter
                            projectAmenitiesAdapter.notifyDataSetChanged()
                            isClicked = true
                        }
                    }
                }
            }
        }
    }

    private inner class ApplyViewHolder(private val binding: ApplyLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            when (data.isPageFooterActive) {
                false -> {
                    binding.tvBookingStarts.visibility = View.GONE
                }
                true -> {
                    binding.tvBookingStarts.text = data.pageFooter
                    binding.tvBookingStarts.visibility = View.VISIBLE
                }
            }

            binding.tvApplyNow.setOnClickListener(onItemClickListener)
        }
    }

    private inner class OppDocsKeyAttractionsViewHolder(private val binding: OppDocKeyAttractionsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val list = arrayListOf<String>("1", "2", "3", "4", "5", "6")
            keyAttractionsAdapter = KeyAttractionsAdapter(list)
            binding.rvKeyAttractions.adapter = keyAttractionsAdapter
        }
    }

    override fun getItemViewType(position: Int): Int {
        return itemList[position].viewType
    }

    fun setItemClickListener(clickListener: View.OnClickListener) {
        onItemClickListener = clickListener
    }

}