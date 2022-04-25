package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.*
import com.emproto.hoabl.feature.profile.data.HelpModel


class HoabelHealthAdapter(
    var context: Context,
    val dataList: ArrayList<HelpModel>,
    val itemInterface: HelpItemInterface
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object {
        const val VIEW_ITEM = 0
        const val VIEW_FOOTER = 1
    }


    override fun getItemViewType(position: Int): Int {
        return dataList[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_ITEM -> {
                val view =
                    HealthCenterViewBinding.inflate(
                        LayoutInflater.from(parent.context), parent,
                        false
                    )
                return HoablHealthViewHolder(view)
            }

            else -> {
                val view =
                    HealthCenterFooterBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return HoablHealthFooterHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_ITEM -> {
                val item = dataList[holder.layoutPosition].data
                val header_holder = holder as HoabelHealthAdapter.HoablHealthViewHolder
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    header_holder.binding.ivIcon.setImageResource(item.img)
                }
                header_holder.binding.tvTitle.text = item.title
                header_holder.binding.tvDescirption.text = item.description
                header_holder.binding.hoabelView.setOnClickListener {
                    itemInterface.onClickItem(holder.layoutPosition)
                }

            }
            HoabelHealthAdapter.VIEW_FOOTER -> {
                val listHolder = holder as HoabelHealthAdapter.HoablHealthFooterHolder
//                listHolder.binding.helpItems.layoutManager = GridLayoutManager(context, 2)
//                listHolder.binding.helpItems.adapter =
//                    HelpListAdapter(context, dataList[position].data, itemInterface)
            }
        }
    }

    override fun getItemCount() = dataList.size
    inner class HoablHealthViewHolder(var binding: HealthCenterViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class HoablHealthFooterHolder(var binding: HealthCenterFooterBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface HelpItemInterface {
        fun onClickItem(position: Int)
    }
}