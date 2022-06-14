package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.*
import com.emproto.hoabl.feature.profile.data.HelpModel


class HelpCenterAdapter(
    var context: Context,
    val dataList: ArrayList<HelpModel>,
    val itemInterface: HelpItemInterface,
    val footerInterface: FooterInterface

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
                    ItemHelpCenterBinding.inflate(
                        LayoutInflater.from(parent.context), parent,
                        false
                    )
                return HoablHealthViewHolder(view)
            }

            else -> {
                val view =
                    HelpCenterFooterBinding.inflate(
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
                val header_holder = holder as HelpCenterAdapter.HoablHealthViewHolder
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    header_holder.binding.ivIcon.setImageResource(item.img)
                }
                header_holder.binding.tvTitle.text = item.title
                header_holder.binding.tvDescirption.text = item.description
                header_holder.binding.hoabelView.setOnClickListener {
                    itemInterface.onClickItem(holder.layoutPosition)
                }

            }
            VIEW_FOOTER -> {
                val listHolder = holder as HelpCenterAdapter.HoablHealthFooterHolder
                listHolder.binding.actionChat.setOnClickListener {
                    footerInterface.onChatClick(holder.layoutPosition)

                }

            }
        }
    }

    override fun getItemCount() = dataList.size
    inner class HoablHealthViewHolder(var binding: ItemHelpCenterBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class HoablHealthFooterHolder(var binding: HelpCenterFooterBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface HelpItemInterface {
        fun onClickItem(position: Int)
    }
    interface FooterInterface {
        fun onChatClick(position: Int)
    }
}