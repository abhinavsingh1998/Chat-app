package com.example.portfolioui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolioui.databinding.ItemTimelineDataBinding
import com.example.portfolioui.databinding.ItemTimelineHeaderBinding
import com.example.portfolioui.models.StepsModel
import com.example.portfolioui.models.TimelineModel

class TimelineAdapter(
    var context: Context,
    val dataList: ArrayList<TimelineModel>,
    val itemInterface: TimelineInterface?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_LIST = 1
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
            }
            TYPE_LIST -> {
                val stepsList = ArrayList<StepsModel>()
                stepsList.add(StepsModel(StepsAdapter.TYPE_1, "Internal Roads "))
                stepsList.add(StepsModel(StepsAdapter.TYPE_1,"Amenities & Clubhouse"))
                stepsList.add(StepsModel(StepsAdapter.TYPE_1,"Other Infra Development"))

                val listHolder = holder as StepsListHolder
                listHolder.binding.stepsList.layoutManager = LinearLayoutManager(context)
                listHolder.binding.stepsList.adapter = StepsAdapter(context, stepsList, null)
            }

        }
    }


    override fun getItemCount() = dataList.size

    inner class HeaderHolder(var binding: ItemTimelineHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class StepsListHolder(var binding: ItemTimelineDataBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface TimelineInterface {
        fun onClickItem(position: Int)
    }

}
