package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.feature.profile.data.StatsAboutusData

class StatsOverViewAboutUsAdapter(context: Context, private val statsList:ArrayList<StatsAboutusData>):
    RecyclerView.Adapter<StatsOverViewAboutUsAdapter.MyStatsViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StatsOverViewAboutUsAdapter.MyStatsViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.stats_overview_about_us, parent, false)
        return MyStatsViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: StatsOverViewAboutUsAdapter.MyStatsViewHolder, position: Int) {
        val currentItem = statsList[position]
        holder.rupeestats.text= currentItem.rupeestats
        holder.tvHeading.text = currentItem.heading
        holder.desc.text = currentItem.desc
    }

    override fun getItemCount(): Int {
        return statsList.size

    }

    class MyStatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rupeestats: TextView = itemView.findViewById(R.id.stats_rupee_tv)
        val tvHeading: TextView = itemView.findViewById(R.id.trnsacted_tv)
        val desc: TextView = itemView.findViewById(R.id.desc_stats)

    }
}
