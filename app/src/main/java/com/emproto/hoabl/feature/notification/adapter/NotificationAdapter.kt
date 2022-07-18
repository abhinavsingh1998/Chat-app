package com.emproto.hoabl.feature.notification.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.InsightsListItemBinding
import com.emproto.hoabl.databinding.NotificationItemBinding
import com.emproto.hoabl.feature.home.adapters.AllInsightsAdapter
import com.emproto.hoabl.feature.notification.data.NotificationDataModel
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.response.notification.Data
import com.emproto.networklayer.response.notification.NotificationResponse

class NotificationAdapter(
    val mContext: Context,
    val list: List<Data>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  ViewHolder{
        val view = NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }
     override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         val item = list.get(holder.adapterPosition)



             if (item.notification.notificationDescription.media!=null){
                 Glide.with(mContext)
                     .load(item.notification.notificationDescription.media.value.url)
                     .into(holder.binding.ivImage)
             } else{
                 holder.binding.ivImage.isVisible= false
             }

         holder.binding.tvChatDesc.text= item.notification.notificationDescription.body
         holder.binding.tvTopic.text= item.notification.notificationDescription.title


     }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding:NotificationItemBinding ) :
        RecyclerView.ViewHolder(binding.root)


}
