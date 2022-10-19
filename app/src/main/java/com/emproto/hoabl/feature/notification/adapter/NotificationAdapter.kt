package com.emproto.hoabl.feature.notification.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.NotificationItemBinding
import com.emproto.networklayer.response.notification.dataResponse.Data

class NotificationAdapter(
    val mContext: Context,
    val list: List<Data>,
    val itemInterface: ItemsClickInterface
) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list.get(holder.adapterPosition)


        if (item.notification.notificationDescription.media != null) {
            holder.binding.cvChatImage.isVisible = true
            Glide.with(mContext)
                .load(item.notification.notificationDescription.media.value.url)
                .into(holder.binding.ivImage)
        } else {
            holder.binding.cvChatImage.isVisible = false
        }

        holder.binding.tvChatDesc.text = item.notification.notificationDescription.body
        holder.binding.tvTopic.text = item.notification.notificationDescription.title
        holder.binding.tvChatTime.text = Utility.convertUTCtoTime(item.notification.updatedAt)

        if (item.readStatus == true) {
            holder.binding.cardView.cardElevation = 0f
            holder.binding.tvNew.isVisible = false
            readStatus(holder.binding.tvChatTime)
            readStatus(holder.binding.tvChatDesc)
            readStatus(holder.binding.tvTopic)

        } else {
            holder.binding.cardView.cardElevation = 25f
            holder.binding.tvNew.isVisible = true
            holder.binding.tvChatTime.setTextColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.text_orange_color
                )
            )
            holder.binding.tvChatDesc.setTextColor(ContextCompat.getColor(
                mContext,
                R.color.black
            ))
            holder.binding.tvTopic.setTextColor(ContextCompat.getColor(
                mContext,
                R.color.app_color
            ))
        }

        holder.binding.cardView.setOnClickListener {
            itemInterface.onClickItem(item.id, position)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: NotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface ItemsClickInterface {
        fun onClickItem(id: Int, position: Int)
    }

    fun readStatus(textView:TextView){
        textView.setTextColor(
            ContextCompat.getColor(
                mContext,
                R.color.text_grey_color
            )
        )
    }
}
