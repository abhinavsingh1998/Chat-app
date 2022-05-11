package com.example.portfolioui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolioui.databinding.ItemTimelineInprogressBinding
import com.example.portfolioui.databinding.ItemTimelineStepCompletedBinding
import com.example.portfolioui.databinding.ItemTimelineStepDisabledBinding
import com.example.portfolioui.models.StepsModel

class StepsAdapter(
    var context: Context,
    private val dataList: ArrayList<StepsModel>,
    val itemInterface: TimelineInterface?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_INSTART = 0
        const val TYPE_INPROGRESS = 1
        const val TYPE_COMPLETED = 2

    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {

            TYPE_INSTART -> {
                val view = ItemTimelineStepDisabledBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return InStartHolder(view)
            }

            TYPE_INPROGRESS -> {
                val view = ItemTimelineInprogressBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return InProgressHolder(view)
            }

            else -> {
                val view =
                    ItemTimelineStepCompletedBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return InCompletedHolder(view)
            }

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (dataList[position].viewType) {

            TYPE_INSTART -> {

                val type1Holder = holder as InStartHolder
                val data = dataList[position].timeline
                type1Holder.binding.tvName.text = data.heading

            }
            TYPE_COMPLETED -> {
                val type1Holder = holder as InCompletedHolder
                val data = dataList[position].timeline
                type1Holder.binding.tvName.text = data.heading
            }
            TYPE_INPROGRESS -> {
                val type1Holder = holder as InProgressHolder
                val data = dataList[position].timeline
                type1Holder.binding.tvName.text = data.heading
            }
        }
    }


    override fun getItemCount() = dataList.size

    inner class InStartHolder(var binding: ItemTimelineStepDisabledBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class InProgressHolder(var binding: ItemTimelineInprogressBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class InCompletedHolder(var binding: ItemTimelineStepCompletedBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface TimelineInterface {
        fun onClickItem(position: Int)
    }

}
