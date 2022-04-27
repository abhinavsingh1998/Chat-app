package com.emproto.hoabl.feature.promises.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R


class PromiseDetailsAdapter(
    val context: Context,
    private val descriptions: List<String>
) : RecyclerView.Adapter<PromiseDetailsAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.promise_second_view, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = descriptions[position]
        holder.tvHeading.text = currentItem
    }

    override fun getItemCount(): Int {
        return descriptions.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHeading: TextView = itemView.findViewById(R.id.tvHeading)
    }

}
