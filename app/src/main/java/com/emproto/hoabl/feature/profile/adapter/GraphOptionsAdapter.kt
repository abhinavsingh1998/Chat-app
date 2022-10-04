package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R

class GraphOptionsAdapter(
    context: Context,
    private val data: ArrayList<String>,
    val graphItemClicks: GraphItemClicks
) : RecyclerView.Adapter<GraphOptionsAdapter.GraphViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GraphViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_graph_options, parent, false)
        return GraphViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return data.size
    }

    class GraphViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.project_image)
        val tvHeading: TextView = itemView.findViewById(R.id.tv_projectName)
        val line: View = itemView.findViewById(R.id.viewLine)
    }

    override fun onBindViewHolder(holder: GraphViewHolder, position: Int) {
        holder.tvHeading.text = data[position]

        holder.itemView.setOnClickListener {
            graphItemClicks.graphItemClicked(
                holder.layoutPosition,
                holder.itemView,
                holder.line
            )
        }
    }

    interface GraphItemClicks {
        fun graphItemClicked(position: Int, itemView: View, line: View)
    }

}
