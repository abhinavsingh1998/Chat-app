package com.emproto.hoabl.feature.home.profileAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.feature.home.profileAdapter.data.DataHealthCenter

class HoabelHealthAdapter(context: Context, list: ArrayList<DataHealthCenter>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var onItemClickListener : View.OnClickListener
    lateinit var faqAdapter:FaqAdapter

    companion object {
        const val VIEW_HELP_CENTER_LOCATION_ACCESS = 1
        const val VIEW_HELP_CENTER_CONNECT = 2
    }

    private val context: Context = context
    var list: ArrayList<DataHealthCenter> = list


    private inner class View1ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var message: TextView = itemView.findViewById(R.id.tv_security)
        var hoabelImg:ImageView= itemView.findViewById(R.id.hoabelImgfaq)

        fun bind(position: Int) {
            val recyclerViewModel = list[position]
            message.text = recyclerViewModel.tv_security
            hoabelImg.setImageResource(recyclerViewModel.hoabelImg)
        }
    }
    private inner class View2ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var message: TextView = itemView.findViewById(R.id.tv_wants_to_connect)
        fun bind(position: Int) {
            val recyclerViewModel = list[position]
            message.text = recyclerViewModel.textData

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_HELP_CENTER_LOCATION_ACCESS) {
            return View1ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.health_center_view, parent, false)
            )
        }
        return View2ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.health_center_view2, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list[position].viewType === VIEW_HELP_CENTER_LOCATION_ACCESS) {
            (holder as View1ViewHolder).bind(position)
        } else {
            (holder as View2ViewHolder).bind(position)
        }
    }
    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

}