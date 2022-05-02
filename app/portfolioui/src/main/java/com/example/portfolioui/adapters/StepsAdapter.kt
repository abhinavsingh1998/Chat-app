package com.example.portfolioui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolioui.databinding.ItemTimelineStepBinding
import com.example.portfolioui.models.StepsModel

class StepsAdapter(
    var context: Context,
    private val dataList: ArrayList<StepsModel>,
    val itemInterface: TimelineInterface?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_1 = 0
        const val TYPE_2 = 1

    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {

            TYPE_1 -> {
                val view = ItemTimelineStepBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return SingleStepHolder(view)
            }

            else -> {
                val view =
                    ItemTimelineStepBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return SingleStepHolder(view)
            }

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (dataList[position].viewType) {

            TYPE_1 -> {

                val type1Holder = holder as SingleStepHolder
                type1Holder.binding.tvName.text = dataList[position].name

            }
        }
    }


    override fun getItemCount() = dataList.size

    inner class SingleStepHolder(var binding: ItemTimelineStepBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface TimelineInterface {
        fun onClickItem(position: Int)
    }

}
