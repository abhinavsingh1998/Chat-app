package com.example.portfolioui.adapters

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.networklayer.response.portfolio.prtimeline.ProjectTimeline
import com.example.portfolioui.R
import com.example.portfolioui.databinding.ItemTimelineDataBinding
import com.example.portfolioui.databinding.ItemTimelineHeaderBinding
import com.example.portfolioui.databinding.ItemTimelineStepReraBinding
import com.example.portfolioui.models.StepsModel
import com.example.portfolioui.models.TimelineHeaderData
import com.example.portfolioui.models.TimelineModel

class TimelineAdapter(
    var context: Context,
    val dataList: ArrayList<TimelineModel>,
    val itemInterface: TimelineInterface
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_LIST = 1
        const val TYPE_RERA = 3
        const val TYPE_DISCLAIMER = 2
    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_HEADER -> {
                val view = ItemTimelineHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return HeaderHolder(view)
            }
            TYPE_RERA -> {
                val view = ItemTimelineStepReraBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return StepsReraViewHolder(view)

            }

            else -> {
                val view =
                    ItemTimelineDataBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return StepsListHolder(view)
            }

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (dataList[position].viewType) {
            TYPE_HEADER -> {
                val header_holder = holder as HeaderHolder
                val headerData = dataList[position].data as TimelineHeaderData
                header_holder.binding.projectName.text = headerData.projectName
                header_holder.binding.tvAddress.text = headerData.address
            }
            TYPE_RERA -> {
                val listData = dataList[position].data as ProjectTimeline
                val listHolder = holder as StepsReraViewHolder

                listHolder.binding.textView7.text = showHTMLText(
                    String.format(
                        context.getString(R.string.tv_receipt),
                        "View Details"
                    )
                )
                listHolder.binding.textView7.setOnClickListener {
                    itemInterface.onClickVDetails(listData.timeLines[0].sections[0].values.medias.value.url)
                }

            }
            TYPE_LIST -> {
                val listData = dataList[position].data as ProjectTimeline
                val listHolder = holder as StepsListHolder

                listHolder.binding.textHeader.text = listData.timeLineSectionHeading
                val stepsList = ArrayList<StepsModel>()
                for (item in listData.timeLines) {
                    when (item.sections[0].values.percentage) {
                        0.0 -> stepsList.add(StepsModel(StepsAdapter.TYPE_INSTART, item))
                        in 1.0..99.99 -> {
                            stepsList.add(StepsModel(StepsAdapter.TYPE_INPROGRESS, item))
                        }
                        else -> stepsList.add(StepsModel(StepsAdapter.TYPE_COMPLETED, item))
                    }
                }
                listHolder.binding.stepsList.layoutManager = LinearLayoutManager(context)
                listHolder.binding.stepsList.adapter =
                    StepsAdapter(context, stepsList, null)
            }


        }
    }


    override fun getItemCount() = dataList.size

    inner class HeaderHolder(var binding: ItemTimelineHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class StepsListHolder(var binding: ItemTimelineDataBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class StepsReraViewHolder(var binding: ItemTimelineStepReraBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface TimelineInterface {
        fun onClickVDetails(url: String)
    }

    fun showHTMLText(message: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(message)
        }
    }

}
