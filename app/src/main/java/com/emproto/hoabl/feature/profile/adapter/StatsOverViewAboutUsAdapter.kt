package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.StatsOverviewAboutUsBinding
import com.emproto.networklayer.response.resourceManagment.DetailedInformationXX

@Suppress("DEPRECATION")
class StatsOverViewAboutUsAdapter(
    val context: Context,
    private val statsList: List<DetailedInformationXX>
) :
    RecyclerView.Adapter<StatsOverViewAboutUsAdapter.MyStatsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StatsOverViewAboutUsAdapter.MyStatsViewHolder {
        val view =
            StatsOverviewAboutUsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyStatsViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: StatsOverViewAboutUsAdapter.MyStatsViewHolder,
        position: Int
    ) {
        val currentItem = statsList[position]

        holder.binding.statsRupeeTv.text = currentItem.value
        holder.binding.trnsactedTv.text = currentItem.title
        holder.binding.descStats.text = showHTMLText(currentItem.description)
    }

    override fun getItemCount(): Int {
        return statsList.size
    }

    inner class MyStatsViewHolder(val binding: StatsOverviewAboutUsBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun showHTMLText(message: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(message)
        }
    }
}
