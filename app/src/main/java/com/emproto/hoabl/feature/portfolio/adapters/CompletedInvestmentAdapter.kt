package com.emproto.hoabl.feature.portfolio.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.core.Constants
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemCompletedInvestmentsBinding
import com.emproto.networklayer.response.portfolio.dashboard.InvestmentHeadingDetails
import com.emproto.networklayer.response.portfolio.dashboard.Point
import com.emproto.networklayer.response.portfolio.dashboard.Project
import com.emproto.networklayer.response.portfolio.ivdetails.ProjectExtraDetails
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import kotlin.math.abs

class CompletedInvestmentAdapter(
    val context: Context,
    val list: List<Project>,
    private val onCLickInterface: ExistingUsersPortfolioAdapter.ExistingUserInterface,
    val type: Int
) :
    RecyclerView.Adapter<CompletedInvestmentAdapter.MyViewHolder>() {

    private val COMPLETED = 0
    private val ONGOING = 1

    //for dropdown graph
    private var graphType = ""
    private var xAxisList = ArrayList<String>()

    inner class MyViewHolder(var binding: ItemCompletedInvestmentsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemCompletedInvestmentsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //set data to view

        val project = list[position]

        holder.binding.tvManageProjects.setOnClickListener {
            manageInvestmentDetails(project)
        }
        holder.binding.tvCompletedInvestmentName.setOnClickListener {
            manageInvestmentDetails(project)
        }
        holder.binding.tvCompletedInvestmentLocation.setOnClickListener {
            manageInvestmentDetails(project)
        }
        holder.binding.ivCompletedInvestmentImage.setOnClickListener {
            manageInvestmentDetails(project)
        }
        if (project.project != null) {

            holder.binding.tvCompletedInvestmentName.text = project.project.launchName
            holder.binding.tvCompletedInvestmentProjectText.text =
                project.investment.inventoryBucket
            (project.project.address.city + "," + project.project.address.state).also { holder.binding.tvCompletedInvestmentLocation.text = it }
            "₹${Utility.convertToDecimal(project.investment.amountInvested)}".also { holder.binding.tvCompletedInvestmentPrice.text = it }
            holder.binding.tvCompletedInvestmentArea.text =
                Utility.convertTo(project.investment.crmInventory.areaSqFt)

            holder.binding.viewDarkBg.setOnClickListener {
                if (holder.binding.ivCompletedInvestmentDropArrow.visibility == View.VISIBLE) {
                    holder.binding.cvCompletedInvestmentGraphCard.visibility = View.VISIBLE
                    holder.binding.ivCompletedInvestmentUpwardArrow.visibility = View.VISIBLE
                    holder.binding.ivCompletedInvestmentDropArrow.visibility = View.GONE
                } else {
                    holder.binding.cvCompletedInvestmentGraphCard.visibility = View.GONE
                    holder.binding.ivCompletedInvestmentUpwardArrow.visibility = View.GONE
                    holder.binding.ivCompletedInvestmentDropArrow.visibility = View.VISIBLE
                }
            }

            holder.binding.tvInventoryId.text = "${project.investment.crmInventory.name}"
            holder.binding.tvEstimatedAppreciationRating.text =
                "" + project.project.generalInfoEscalationGraph.estimatedAppreciation + "%"

            setFirstGraph(
                holder.binding.ivCompletedInvestmentGraphImage,
                project.project.generalInfoEscalationGraph.dataPoints.points,
                project.project.generalInfoEscalationGraph.dataPoints.dataPointType
            )
            //setting chart data
            setDropDownGraph(
                holder.binding.ivCompletedInvestmentGraph,
                project.project.generalInfoEscalationGraph.dataPoints.points,
                project.project.generalInfoEscalationGraph.dataPoints.dataPointType
            )

            holder.binding.tvXAxisLabel.text = project.project.generalInfoEscalationGraph.yAxisDisplayName
            holder.binding.tvYAxisLabel.text = project.project.generalInfoEscalationGraph.xAxisDisplayName

            if (type == ONGOING) {
                holder.binding.cvInvesterAppreciation.visibility = View.GONE
                "Actions".also { holder.binding.tvCompletedInvestmentRatingUnit.text = it }
                ("" + project.investment.actionItemCount).also { holder.binding.tvCompletedInvestmentRating.text = it }
                holder.binding.tvCompletedInvestmentRating.setTextColor(context.getColor(R.color.text_red_color))
                holder.binding.tvCompletedInvestmentRatingUnit.setTextColor(context.getColor(R.color.text_red_color))
            } else {
                "${project.investment.projectIea}% ".also { holder.binding.tvCompletedInvestmentRating.text = it }
                holder.binding.cvInvesterAppreciation.visibility = View.VISIBLE
                "${project.investment.projectIea}% ".also { holder.binding.tvInvestorAppreciationRating.text = it }

            }
            Glide.with(context)
                .load(project?.project?.projectIcon?.value?.url)
                .into(holder.binding.ivCompletedInvestmentImage)

        }

    }

    private fun manageInvestmentDetails(project: Project) {
        val projectExtraDetails =
            ProjectExtraDetails(
                project.project.address,
                project.project.projectIcon,
                project.project.generalInfoEscalationGraph,
                project.project.launchName,
                project.investment.pendingAmount,
                project.investment.isBookingComplete,
                project.investment.paidAmount
            )
        val headingDetails = InvestmentHeadingDetails(
            project.project.isSimilarInvestmentActive,
            project.project.numberOfSimilarInvestmentsToShow,
            project.project.similarInvestmentSectionHeading,
            project.project.isEscalationGraphActive,
            project.project.isLatestMediaGalleryActive,
            project.project.latestMediaGallerySectionHeading ?: "",
            project.project.otherSectionHeadings
        )
        onCLickInterface.manageProject(
            project.investment.id,
            project.project.id,
            projectExtraDetails,
            project.investment.projectIea,
            project.project.generalInfoEscalationGraph.estimatedAppreciation, headingDetails
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setDropDownGraph(
        ivCompletedInvestmentGraph: LineChart,
        points: List<Point>,
        dataPointType: String
    ) {

        val lineValues = ArrayList<Entry>()

        when (dataPointType) {
            Constants.YEARLY  -> {
                graphType = Constants.YEARLY
                for (item in points) {
                    lineValues.add(Entry(item.year.toFloat(), item.value.toFloat()))
                }
            }
            Constants.HALF_YEARLY -> {
                graphType = Constants.HALF_YEARLY
                for (i in points.indices) {
                    val fmString = points[i].halfYear.substring(0, 3)
                    val yearString = points[i].year.substring(2, 4)
                    val str = "$fmString-$yearString"
                    xAxisList.add(str)
                }
                for ((index, item) in points.withIndex()) {
                    lineValues.add(Entry(index.toFloat(), item.value.toFloat()))
                }
            }
            Constants.QUATERLY -> {
                graphType = Constants.QUATERLY
                for (i in points.indices) {
                    val fmString = points[i].quater.substring(0, 2)
                    val yearString = points[i].year.substring(2, 4)
                    val str = "$fmString-$yearString"
                    xAxisList.add(str)
                }
                for ((index, item) in points.withIndex()) {
                    lineValues.add(Entry(index.toFloat(), item.value.toFloat()))
                }
            }
            Constants.MONTHLY -> {
                graphType = Constants.MONTHLY
                for (i in points.indices) {
                    val fmString = points[i].month.substring(0, 3)
                    val yearString = points[i].year.substring(2, 4)
                    val str = "$fmString-$yearString"
                    xAxisList.add(str)
                }
                for ((index, item) in points.withIndex()) {
                    lineValues.add(Entry(index.toFloat(), item.value.toFloat()))
                }
            }
        }
        if (lineValues.isNotEmpty()) {
            val lineDataSet1 = LineDataSet(lineValues, "First")
            //We add features to our chart
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                lineDataSet1.color = context.getColor(R.color.green)
            }

            lineDataSet1.valueTextSize = 12F
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                lineDataSet1.fillColor = context.getColor(R.color.green)
            }
            lineDataSet1.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            lineDataSet1.setDrawCircles(false)
            lineDataSet1.setDrawValues(false)

            val data1 = LineData(lineDataSet1)
            if (type == COMPLETED) {
                val limitLine = LimitLine(lineValues[(lineValues.size / 2)].x, "My Investment")
                limitLine.lineColor = context.getColor(R.color.app_color)
                limitLine.lineWidth = 1F
                limitLine.enableDashedLine(10F, 10F, 10F)
                limitLine.textSize = 14F
                ivCompletedInvestmentGraph.xAxis.addLimitLine(limitLine)
            }
            ivCompletedInvestmentGraph.description.isEnabled = false
            ivCompletedInvestmentGraph.legend.isEnabled = false
            ivCompletedInvestmentGraph.axisLeft.setDrawGridLines(false)
            ivCompletedInvestmentGraph.setTouchEnabled(false)
            ivCompletedInvestmentGraph.setPinchZoom(false)
            ivCompletedInvestmentGraph.isDoubleTapToZoomEnabled = false
            ivCompletedInvestmentGraph.xAxis.setDrawGridLines(false)
            ivCompletedInvestmentGraph.xAxis.granularity = 1f
            ivCompletedInvestmentGraph.xAxis.position =
                XAxis.XAxisPosition.BOTTOM
            ivCompletedInvestmentGraph.xAxis.typeface = ResourcesCompat.getFont(
                context,
                R.font.jost_regular
            )
            ivCompletedInvestmentGraph.axisRight.setDrawGridLines(false)
            ivCompletedInvestmentGraph.axisRight.setDrawLabels(false)
            ivCompletedInvestmentGraph.axisRight.setDrawAxisLine(false)
            ivCompletedInvestmentGraph.axisLeft.typeface = ResourcesCompat.getFont(
                context,
                R.font.jost_regular
            )
            ivCompletedInvestmentGraph.xAxis.valueFormatter = XAxisFormatter()
            ivCompletedInvestmentGraph.data = data1
            ivCompletedInvestmentGraph.animateXY(2000, 2000)
        }
    }

    private fun setFirstGraph(
        ivCompletedInvestmentGraphImage: LineChart,
        points: List<Point>,
        dataPointType: String
    ) {
        val lineValues = ArrayList<Entry>()

        when (dataPointType) {
            Constants.YEARLY  -> {
                graphType = Constants.YEARLY
                for (item in points) {
                    lineValues.add(Entry(item.year.toFloat(), item.value.toFloat()))
                }
            }
            Constants.HALF_YEARLY -> {
                graphType = Constants.HALF_YEARLY
                for (i in points.indices) {
                    val fmString = points[i].halfYear.substring(0, 3)
                    val yearString = points[i].year.substring(2, 4)
                    val str = "$fmString-$yearString"
                    xAxisList.add(str)
                }
                for ((index, item) in points.withIndex()) {
                    lineValues.add(Entry(index.toFloat(), item.value.toFloat()))
                }
            }
            Constants.QUATERLY -> {
                graphType = Constants.QUATERLY
                for (i in points.indices) {
                    val fmString = points[i].quater.substring(0, 2)
                    val yearString = points[i].year.substring(2, 4)
                    val str = "$fmString-$yearString"
                    xAxisList.add(str)
                }
                for ((index, item) in points.withIndex()) {
                    lineValues.add(Entry(index.toFloat(), item.value.toFloat()))
                }
            }
            Constants.MONTHLY -> {
                graphType = Constants.MONTHLY
                for (i in points.indices) {
                    val fmString = points[i].month.substring(0, 3)
                    val yearString = points[i].year.substring(2, 4)
                    val str = "$fmString-$yearString"
                    xAxisList.add(str)
                }
                for ((index, item) in points.withIndex()) {
                    lineValues.add(Entry(index.toFloat(), item.value.toFloat()))
                }
            }
        }

        if (lineValues.isNotEmpty()) {
            val lineDataSet = LineDataSet(lineValues, "First")
            //We add features to our chart
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                lineDataSet.color = context.getColor(R.color.green)
            }

            lineDataSet.valueTextSize = 0F
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                lineDataSet.fillColor = context.getColor(R.color.green)
            }
            lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            lineDataSet.setDrawCircles(false)
            val data = LineData(lineDataSet)
            ivCompletedInvestmentGraphImage.description.isEnabled = false
            ivCompletedInvestmentGraphImage.legend.isEnabled = false
            ivCompletedInvestmentGraphImage.axisLeft.setDrawGridLines(false)
            ivCompletedInvestmentGraphImage.setTouchEnabled(false)
            ivCompletedInvestmentGraphImage.setPinchZoom(false)
            ivCompletedInvestmentGraphImage.isDoubleTapToZoomEnabled = false
            ivCompletedInvestmentGraphImage.xAxis.setDrawGridLines(false)
            ivCompletedInvestmentGraphImage.xAxis.position =
                XAxis.XAxisPosition.BOTTOM
            ivCompletedInvestmentGraphImage.xAxis.setDrawLabels(false)
            ivCompletedInvestmentGraphImage.axisRight.setDrawGridLines(false)
            ivCompletedInvestmentGraphImage.axisRight.setDrawLabels(false)
            ivCompletedInvestmentGraphImage.axisRight.setDrawAxisLine(false)
            ivCompletedInvestmentGraphImage.axisLeft.setDrawLabels(false)
            ivCompletedInvestmentGraphImage.data = data
            ivCompletedInvestmentGraphImage.animateXY(2000, 2000)
        }

    }

    inner class XAxisFormatter : IAxisValueFormatter {
        override fun getFormattedValue(p0: Float, p1: AxisBase?): String {
            return when (graphType) {
                Constants.QUATERLY -> returnFormattedValue(abs(p0))
                Constants.MONTHLY -> returnFormattedValue(abs(p0))
                Constants.HALF_YEARLY -> returnFormattedValue(abs(p0))
                else -> {
                    String.format("%.0f", abs(p0).toDouble())
                }
            }
        }
    }

    private fun returnFormattedValue(floatValue: Float): String {
        return when {
            floatValue.toInt() < 10 -> xAxisList[floatValue.toInt()]
            else -> {
                String.format("%.0f", floatValue.toDouble())
            }
        }
    }

    override fun getItemCount(): Int = list.size

}