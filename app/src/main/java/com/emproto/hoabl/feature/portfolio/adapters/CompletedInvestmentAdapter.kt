package com.emproto.hoabl.feature.portfolio.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemCompletedInvestmentsBinding
import com.emproto.networklayer.response.portfolio.Project
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

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //set data to view
        val project = list[position]
        Glide.with(context)
            .load(project.projectIcon.value.url)
            .into(holder.binding.ivCompletedInvestmentImage)

        holder.binding.tvCompletedInvestmentName.text = project.projectName
        holder.binding.tvCompletedInvestmentProjectText.text = project.skuType
        holder.binding.tvCompletedInvestmentLocation.text =
            project.projectAddress.city + "," + project.projectAddress.state
        holder.binding.tvCompletedInvestmentPrice.text = project.priceRange.to
        holder.binding.tvCompletedInvestmentArea.text = project.areaRange.to

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
        holder.binding.tvManageProjects.setOnClickListener {
            onCLickInterface.manageProject(position)
        }
        holder.binding.tvAppreciationRating.text =
            "" + project.projectGraph.estimatedAppreciation + "%"
        if (type == COMPLETED) {
            holder.binding.cvProjectEstimatedAppreciation.visibility = View.VISIBLE
        } else {
            holder.binding.cvProjectEstimatedAppreciation.visibility = View.GONE
        }

        //setting chart data
        val linevalues = ArrayList<Entry>()
        linevalues.add(Entry(10f, 0.0F))
        linevalues.add(Entry(20f, 3.0F))
        linevalues.add(Entry(40f, 2.0F))
        linevalues.add(Entry(50F,5.0F))
        linevalues.add(Entry(60F,6.0F))
        val linedataset = LineDataSet(linevalues, "First")
        //We add features to our chart
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            linedataset.color = context.getColor(R.color.app_color)
        }

        linedataset.valueTextSize = 12F
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            linedataset.fillColor = context.getColor(R.color.light_app_color)
        }
        linedataset.mode = LineDataSet.Mode.HORIZONTAL_BEZIER;

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
        holder.binding.ivCompletedInvestmentGraphImage.getXAxis().position = XAxis.XAxisPosition.BOTTOM;
        //binding.ivPriceTrendsGraph.getXAxis().setDrawAxisLine(false);
        holder.binding.ivCompletedInvestmentGraphImage.getAxisRight().setDrawGridLines(false);
        holder.binding.ivCompletedInvestmentGraphImage.getAxisRight().setDrawLabels(false);
        holder.binding.ivCompletedInvestmentGraphImage.getAxisRight().setDrawAxisLine(false);
        //binding.ivPriceTrendsGraph.axisLeft.isEnabled = false
        //binding.ivPriceTrendsGraph.axisRight.isEnabled = false
        holder.binding.ivCompletedInvestmentGraphImage.data = data
        holder.binding.ivCompletedInvestmentGraphImage.animateXY(2000, 2000)

    }

    override fun getItemCount(): Int = list.size

}