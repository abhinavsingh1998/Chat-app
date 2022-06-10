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
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemCompletedInvestmentsBinding
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
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler

class CompletedInvestmentAdapter(
    val context: Context,
    val list: List<Project>,
    val onCLickInterface: ExistingUsersPortfolioAdapter.ExistingUserInterface,
    val type: Int
) :
    RecyclerView.Adapter<CompletedInvestmentAdapter.MyViewHolder>() {

    val COMPLETED = 0
    val ONGOING = 1

    //for dropdown graph
    private var graphType = ""
    private var xaxisList = ArrayList<String>()

    inner class MyViewHolder(var binding: ItemCompletedInvestmentsBinding) :
        RecyclerView.ViewHolder(binding.root)

    private lateinit var onItemClickListener: View.OnClickListener

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
            val projectExtraDetails =
                ProjectExtraDetails(
                    project.project.address,
                    project.project.projectIcon,
//                    project.project.latitude,
//                    project.project.longitude,
//                    project.project.altitude,
                    project.project.generalInfoEscalationGraph
                )
            onCLickInterface.manageProject(
                project.investment.id,
                project.project.id,
                projectExtraDetails
            )
        }
        if (project.project != null) {
            Glide.with(context)
                .load(project.project.projectIcon.value.url)
                .into(holder.binding.ivCompletedInvestmentImage)

            holder.binding.tvCompletedInvestmentName.text = project.project.launchName
            holder.binding.tvCompletedInvestmentProjectText.text =
                project.investment.inventoryBucket
            holder.binding.tvCompletedInvestmentLocation.text =
                project.project.address.city + "," + project.project.address.state
            val amount = (project.project.priceStartingFrom.toDouble()) / 100000
            holder.binding.tvCompletedInvestmentPrice.text = "₹$amount"
            holder.binding.tvCompletedInvestmentArea.text =
                "" + project.project.areaStartingFrom.split(" ")[0]

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

            holder.binding.tvInventoryId.text = "Hoabl/${project.investment.inventoryId}"
            holder.binding.tvEstimatedAppreciationRating.text =
                "" + project.project.generalInfoEscalationGraph.estimatedAppreciation + "%"
            if (type == COMPLETED) {
                holder.binding.cvInvesterAppreciation.visibility = View.VISIBLE
            } else {
                holder.binding.cvInvesterAppreciation.visibility = View.GONE
            }

            setFirstGraph(
                holder.binding.ivCompletedInvestmentGraphImage,
                project.project.generalInfoEscalationGraph.dataPoints.points
            )
            //setting chart data
            setDropDownGraph(
                holder.binding.ivCompletedInvestmentGraph,
                project.project.generalInfoEscalationGraph.dataPoints.points,
                project.project.generalInfoEscalationGraph.dataPoints.dataPointType
            )

            if (type == ONGOING) {
                holder.binding.tvCompletedInvestmentRatingUnit.text = "Actions"
                holder.binding.tvCompletedInvestmentRating.text =
                    "" + project.investment.actionItemCount
                holder.binding.tvCompletedInvestmentRating.setTextColor(context.getColor(R.color.text_red_color))
                holder.binding.tvCompletedInvestmentRatingUnit.setTextColor(context.getColor(R.color.text_red_color))
            }

        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setDropDownGraph(
        ivCompletedInvestmentGraph: LineChart,
        points: List<Point>,
        dataPointType: String
    ) {
        val linevalues = ArrayList<Entry>()
//        for (item in points) {
//            linevalues.add(Entry(item.year.toFloat(), item.value.toFloat()))
//        }
        when(dataPointType){
            "Yearly" -> {
                graphType = "Yearly"
                for(item in points){
                    linevalues.add(Entry(item.year.toFloat(),item.value.toFloat()))
                }
            }
            "Half Yearly" -> {
                graphType = "Half Yearly"
                for(i in 0..points.size-1){
                    val fmString = points[i].halfYear.substring(0,3)
                    val yearString = points[i].year.substring(2,4)
                    val str = "$fmString-$yearString"
                    xaxisList.add(str)
                }
                var index = 0
                for(item in points){
                    linevalues.add(Entry(index.toFloat(),item.value.toFloat()))
                    index++
                }
            }
            "Quaterly" -> {
                graphType = "Quaterly"
                for(i in 0..points.size-1){
                    val fmString = points[i].quater.substring(0,2)
                    val yearString = points[i].year.substring(2,4)
                    val str = "$fmString-$yearString"
                    xaxisList.add(str)
                }
                var index = 0
                for(item in points){
                    linevalues.add(Entry(index.toFloat(),item.value.toFloat()))
                    index++
                }
            }
            "Monthly" -> {
                graphType = "Monthly"
                for(i in 0..points.size-1){
                    val fmString = points[i].month.substring(0,3)
                    val yearString = points[i].year.substring(2,4)
                    val str = "$fmString-$yearString"
                    xaxisList.add(str)
                }
                var index = 0
                for(item in points){
                    linevalues.add(Entry(index.toFloat(),item.value.toFloat()))
                    index++
                }
            }
        }
        val linedataset1 = LineDataSet(linevalues, "First")
        //We add features to our chart
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            linedataset1.color = context.getColor(R.color.app_color)
        }

        linedataset1.valueTextSize = 12F
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            linedataset1.fillColor = context.getColor(R.color.app_color)
        }
        linedataset1.mode = LineDataSet.Mode.LINEAR
        linedataset1.setDrawCircles(false)
        linedataset1.setDrawValues(false)
        //linedataset1.setDrawFilled(true)
        //linedataset1.fillDrawable = context.getDrawable(R.drawable.why_invest_blue_bg)

        //We connect our data to the UI Screen
        val data1 = LineData(linedataset1)
        if (type == COMPLETED) {
            val limitLine = LimitLine(2018F, "My Investment")
            limitLine.lineColor = context.getColor(R.color.app_color)
            limitLine.lineWidth = 1F
            limitLine.enableDashedLine(10F, 10F, 10F)
            limitLine.textSize = 14F
            //binding.ivPriceTrendsGraph.setDrawBorders(false);
            //binding.ivPriceTrendsGraph.setDrawGridBackground(false);
            ivCompletedInvestmentGraph.xAxis.addLimitLine(limitLine)
        }
        ivCompletedInvestmentGraph.getDescription().setEnabled(false)
        ivCompletedInvestmentGraph.getLegend().setEnabled(false)
        ivCompletedInvestmentGraph.getAxisLeft().setDrawGridLines(false)
        //binding.ivPriceTrendsGraph.getAxisLeft().setDrawLabels(false);
        //binding.ivPriceTrendsGraph.getAxisLeft().setDrawAxisLine(false);
        ivCompletedInvestmentGraph.setTouchEnabled(false)
        ivCompletedInvestmentGraph.setPinchZoom(false)
        ivCompletedInvestmentGraph.isDoubleTapToZoomEnabled = false
        ivCompletedInvestmentGraph.getXAxis().setDrawGridLines(false)
        ivCompletedInvestmentGraph.xAxis.granularity = 1f
        ivCompletedInvestmentGraph.getXAxis().position =
            XAxis.XAxisPosition.BOTTOM;
        ivCompletedInvestmentGraph.xAxis.typeface = ResourcesCompat.getFont(
            context,
            R.font.jost_regular
        )
        //holder.binding.ivCompletedInvestmentGraph.getXAxis().setDrawAxisLine(false);
        ivCompletedInvestmentGraph.getAxisRight().setDrawGridLines(false)
        ivCompletedInvestmentGraph.getAxisRight().setDrawLabels(false)
        ivCompletedInvestmentGraph.getAxisRight().setDrawAxisLine(false)
        ivCompletedInvestmentGraph.axisLeft.typeface = ResourcesCompat.getFont(
            context,
            R.font.jost_regular
        )
        ivCompletedInvestmentGraph.getAxisLeft().valueFormatter = Xaxisformatter()
        ivCompletedInvestmentGraph.xAxis.valueFormatter = Xaxisformatter()
        //binding.ivPriceTrendsGraph.axisLeft.isEnabled = false
        //binding.ivPriceTrendsGraph.axisRight.isEnabled = false
        ivCompletedInvestmentGraph.data = data1
        ivCompletedInvestmentGraph.animateXY(2000, 2000)
    }

    private fun setFirstGraph(ivCompletedInvestmentGraphImage: LineChart, points: List<Point>) {
        val linevalues = ArrayList<Entry>()
        for (item in points) {
            linevalues.add(Entry(item.year.toFloat(), item.value.toFloat()))
        }
        val linedataset = LineDataSet(linevalues, "First")
        //We add features to our chart
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            linedataset.color = context.getColor(R.color.green)
        }

        linedataset.valueTextSize = 0F
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            linedataset.fillColor = context.getColor(R.color.green)
        }
        linedataset.mode = LineDataSet.Mode.LINEAR
        linedataset.setDrawCircles(false)
        val data = LineData(linedataset)
        ivCompletedInvestmentGraphImage.getDescription().setEnabled(false)
        ivCompletedInvestmentGraphImage.getLegend().setEnabled(false)
        ivCompletedInvestmentGraphImage.getAxisLeft().setDrawGridLines(false)
        ivCompletedInvestmentGraphImage.setTouchEnabled(false)
        ivCompletedInvestmentGraphImage.setPinchZoom(false)
        ivCompletedInvestmentGraphImage.isDoubleTapToZoomEnabled = false
        //binding.ivPriceTrendsGraph.getAxisLeft().setDrawLabels(false);
        //binding.ivPriceTrendsGraph.getAxisLeft().setDrawAxisLine(false);
        ivCompletedInvestmentGraphImage.getXAxis().setDrawGridLines(false)
        ivCompletedInvestmentGraphImage.getXAxis().position =
            XAxis.XAxisPosition.BOTTOM
        ivCompletedInvestmentGraphImage.getXAxis().setDrawLabels(false)
        ivCompletedInvestmentGraphImage.getAxisRight().setDrawGridLines(false)
        ivCompletedInvestmentGraphImage.getAxisRight().setDrawLabels(false)
        ivCompletedInvestmentGraphImage.getAxisRight().setDrawAxisLine(false)
        //binding.ivPriceTrendsGraph.axisLeft.isEnabled = false
        ivCompletedInvestmentGraphImage.axisLeft.setDrawLabels(false)
        //binding.ivPriceTrendsGraph.axisRight.isEnabled = false
        ivCompletedInvestmentGraphImage.data = data
        ivCompletedInvestmentGraphImage.animateXY(2000, 2000)

    }

    inner class Xaxisformatter :  IAxisValueFormatter {
        override fun getFormattedValue(p0: Float, p1: AxisBase?): String {
            return when(graphType){
                "Quaterly" -> returnFormattedValue(p0)
                "Monthly" -> returnFormattedValue(p0)
                "Half Yearly" -> returnFormattedValue(p0)
                else -> { String.format("%.0f", p0.toDouble()) }
            }
        }
    }

    private fun returnFormattedValue(floatValue:Float):String{
        return when {
            floatValue.toInt() < 10 -> xaxisList[floatValue.toInt()]
            else -> { String.format("%.0f", floatValue.toDouble()) }
        }
    }

    override fun getItemCount(): Int = list.size

}