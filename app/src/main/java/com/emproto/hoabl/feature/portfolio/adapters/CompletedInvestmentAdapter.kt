package com.emproto.hoabl.feature.portfolio.adapters

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemCompletedInvestmentsBinding
import com.emproto.networklayer.response.portfolio.dashboard.Project
import com.emproto.networklayer.response.portfolio.ivdetails.ProjectExtraDetails
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class CompletedInvestmentAdapter(
    val context: Context,
    val list: List<Project>,
    val onCLickInterface: ExistingUsersPortfolioAdapter.ExistingUserInterface,
    val type: Int
) :
    RecyclerView.Adapter<CompletedInvestmentAdapter.MyViewHolder>() {

    val COMPLETED = 0
    val ONGOING = 1

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
                    project.project.latitude,
                    project.project.longitude,
                    project.project.altitude
                )
            onCLickInterface.manageProject(
                project.investment.crmProjectId,
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
            holder.binding.tvCompletedInvestmentPrice.text = project.project.priceStartingFrom
            holder.binding.tvCompletedInvestmentArea.text =
                "" + project.project.areaStartingFrom.split(" ")[0]

            holder.binding.ivCompletedInvestmentDropArrow.setOnClickListener {
                holder.binding.cvCompletedInvestmentGraphCard.visibility = View.VISIBLE
                holder.binding.ivCompletedInvestmentUpwardArrow.visibility = View.VISIBLE
                holder.binding.ivCompletedInvestmentDropArrow.visibility = View.GONE
            }

            holder.binding.ivCompletedInvestmentUpwardArrow.setOnClickListener {
                holder.binding.cvCompletedInvestmentGraphCard.visibility = View.GONE
                holder.binding.ivCompletedInvestmentUpwardArrow.visibility = View.GONE
                holder.binding.ivCompletedInvestmentDropArrow.visibility = View.VISIBLE
            }

            holder.binding.tvEstimatedAppreciationRating.text =
                "" + project.project.generalInfoEscalationGraph.estimatedAppreciation + "%"
            if (type == COMPLETED) {
                holder.binding.cvInvesterAppreciation.visibility = View.VISIBLE
            } else {
                holder.binding.cvInvesterAppreciation.visibility = View.GONE
            }

            //setting chart data
            val linevalues = ArrayList<Entry>()
            for (item in project.project.generalInfoEscalationGraph.dataPoints.points) {
                linevalues.add(Entry(item.year.toFloat(), item.value.toFloat()))
            }
            val linedataset = LineDataSet(linevalues, "First")
            //We add features to our chart
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                linedataset.color = context.getColor(R.color.app_color)
            }

            linedataset.valueTextSize = 0F
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                linedataset.fillColor = context.getColor(R.color.app_color)
            }
            linedataset.mode = LineDataSet.Mode.LINEAR;
            linedataset.setCircleColor(context.getColor(R.color.app_color))

            //We connect our data to the UI Screen
            val data = LineData(linedataset)

            //binding.ivPriceTrendsGraph.setDrawBorders(false);
            //binding.ivPriceTrendsGraph.setDrawGridBackground(false);

            holder.binding.ivCompletedInvestmentGraphImage.getDescription().setEnabled(false);
            holder.binding.ivCompletedInvestmentGraphImage.getLegend().setEnabled(false);
            holder.binding.ivCompletedInvestmentGraphImage.getAxisLeft().setDrawGridLines(false);
            //binding.ivPriceTrendsGraph.getAxisLeft().setDrawLabels(false);
            //binding.ivPriceTrendsGraph.getAxisLeft().setDrawAxisLine(false);
            holder.binding.ivCompletedInvestmentGraphImage.getXAxis().setDrawGridLines(false);
            holder.binding.ivCompletedInvestmentGraphImage.getXAxis().position =
                XAxis.XAxisPosition.BOTTOM;
            holder.binding.ivCompletedInvestmentGraphImage.getXAxis().setDrawLabels(false)
            holder.binding.ivCompletedInvestmentGraphImage.getAxisRight().setDrawGridLines(false);
            holder.binding.ivCompletedInvestmentGraphImage.getAxisRight().setDrawLabels(false);
            holder.binding.ivCompletedInvestmentGraphImage.getAxisRight().setDrawAxisLine(false);
            //binding.ivPriceTrendsGraph.axisLeft.isEnabled = false
            holder.binding.ivCompletedInvestmentGraphImage.axisLeft.setDrawLabels(false)
            //binding.ivPriceTrendsGraph.axisRight.isEnabled = false
            holder.binding.ivCompletedInvestmentGraphImage.data = data
            holder.binding.ivCompletedInvestmentGraphImage.animateXY(2000, 2000)


            val linedataset1 = LineDataSet(linevalues, "First")
            linedataset1.setCircleColor(context.getColor(R.color.app_color))
            //We add features to our chart
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                linedataset1.color = context.getColor(R.color.app_color)
            }

            linedataset1.valueTextSize = 12F
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                linedataset1.fillColor = context.getColor(R.color.app_color)
            }
            linedataset1.mode = LineDataSet.Mode.LINEAR;

            //We connect our data to the UI Screen
            val data1 = LineData(linedataset1)
            val limitLine = LimitLine(2026F,"My Investment")
            limitLine.lineColor = Color.RED
            limitLine.lineWidth = 1F

            //binding.ivPriceTrendsGraph.setDrawBorders(false);
            //binding.ivPriceTrendsGraph.setDrawGridBackground(false);
            holder.binding.ivCompletedInvestmentGraph.xAxis.addLimitLine(limitLine)
            holder.binding.ivCompletedInvestmentGraph.getDescription().setEnabled(false);
            holder.binding.ivCompletedInvestmentGraph.getLegend().setEnabled(false);
            holder.binding.ivCompletedInvestmentGraph.getAxisLeft().setDrawGridLines(false);
            //binding.ivPriceTrendsGraph.getAxisLeft().setDrawLabels(false);
            //binding.ivPriceTrendsGraph.getAxisLeft().setDrawAxisLine(false);
            holder.binding.ivCompletedInvestmentGraph.getXAxis().setDrawGridLines(false);
            holder.binding.ivCompletedInvestmentGraph.getXAxis().position =
                XAxis.XAxisPosition.BOTTOM;
            //binding.ivPriceTrendsGraph.getXAxis().setDrawAxisLine(false);
            holder.binding.ivCompletedInvestmentGraph.getAxisRight().setDrawGridLines(false);
            holder.binding.ivCompletedInvestmentGraph.getAxisRight().setDrawLabels(false);
            holder.binding.ivCompletedInvestmentGraph.getAxisRight().setDrawAxisLine(false);
            //binding.ivPriceTrendsGraph.axisLeft.isEnabled = false
            //binding.ivPriceTrendsGraph.axisRight.isEnabled = false
            holder.binding.ivCompletedInvestmentGraph.data = data1
            holder.binding.ivCompletedInvestmentGraph.animateXY(2000, 2000)

        }

    }

    override fun getItemCount(): Int = list.size

}