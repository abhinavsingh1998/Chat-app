package com.example.portfolioui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolioui.databinding.ItemBookingHeaderBinding
import com.example.portfolioui.databinding.ItemTimelineDataBinding
import com.example.portfolioui.models.BookingModel
import com.example.portfolioui.models.BookingStepsModel
import com.example.portfolioui.models.StepsModel

class BookingJourneyAdapter(
    var context: Context,
    val dataList: ArrayList<BookingModel>,
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
                val view = ItemBookingHeaderBinding.inflate(
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
                val listHolder = holder as StepsListHolder
                val list = dataList[listHolder.adapterPosition].data as List<BookingStepsModel>
                listHolder.binding.textHeader.text = "TRANSACTION"
                listHolder.binding.stepsList.layoutManager = LinearLayoutManager(context)
                listHolder.binding.stepsList.adapter =
                    BookingStepsAdapter(context, list, itemInterface)

            }

        }
    }


    override fun getItemCount() = dataList.size

    inner class HeaderHolder(var binding: ItemBookingHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class StepsListHolder(var binding: ItemTimelineDataBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface TimelineInterface {
        fun onClickItem(position: Int)
        fun viewDetails(position: Int, data: String)

    }

}
