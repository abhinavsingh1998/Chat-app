package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.*
import com.emproto.hoabl.feature.profile.data.HelpModel


class HoabelHealthAdapter(var context: Context, val dataList: ArrayList<HelpModel>, val itemInterface:HelpItemInterface) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object {
        const val VIEW_HELP_CENTER_LOCATION_ACCESS = 0
        const val TYPE_LIST = 1
    }


    override fun getItemViewType(position: Int): Int {
        return dataList[position].viewType
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
           VIEW_HELP_CENTER_LOCATION_ACCESS -> {
                val view =
                    HealthCenterViewBinding.inflate(LayoutInflater.from(parent.context), parent,
                        false
                    )
                return HoablHealthViewHolder(view)
            }
            TYPE_LIST -> {
                val view =
                    HelpCentreBinding.inflate(LayoutInflater.from(parent.context), parent,
                        false
                    )
                return HoabelHealthViewHolder2(view)
            }
            else -> {
                val view =
                   HelpCentreBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return HoabelHealthViewHolder2(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(position){
            HoabelHealthAdapter.VIEW_HELP_CENTER_LOCATION_ACCESS->{
                val header_holder= holder as HoabelHealthAdapter.HoablHealthViewHolder
            }
            HoabelHealthAdapter.TYPE_LIST->{
                val listHolder= holder as HoabelHealthAdapter.HoabelHealthViewHolder2
                listHolder.binding.helpItems.layoutManager = GridLayoutManager(context, 2)
                listHolder.binding.helpItems.adapter =
                    HelpListAdapter(context, dataList[position].data, itemInterface)
            }
        }
    }

    override fun getItemCount() = dataList.size
    inner class HoablHealthViewHolder(var binding: HealthCenterViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class HoabelHealthViewHolder2(var binding: HelpCentreBinding):
        RecyclerView.ViewHolder(binding.root)
    interface HelpItemInterface {
        fun onClickItem(position: Int)
    }
}