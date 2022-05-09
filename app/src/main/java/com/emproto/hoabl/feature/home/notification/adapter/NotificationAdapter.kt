package com.emproto.hoabl.feature.home.notification.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.feature.home.notification.data.NotificationDataModel

class NotificationAdapter(  private var mContext: Context?,
                      private var dataModel: ArrayList<NotificationDataModel>) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    private lateinit var onItemClickListener: View.OnClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_item, parent, false)
        return ViewHolder(view)
    }

     override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         holder.imageView.setImageResource(dataModel[position].image)
         holder.topic.text = dataModel[position].topic
        holder.desc.text = dataModel[position].desc
        holder.time.text = dataModel[position].time
     }

    override fun getItemCount(): Int {
        return dataModel.size
        Log.i("Size", dataModel.size.toString())
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivImage)
        val topic: TextView = itemView.findViewById(R.id.tvTopic)
        val desc: TextView = itemView.findViewById(R.id.tvMsg)
        val time: TextView = itemView.findViewById(R.id.tvTime)

    }
    fun setItemClickListener(clickListener: View.OnClickListener) {
        onItemClickListener = clickListener
    }
}