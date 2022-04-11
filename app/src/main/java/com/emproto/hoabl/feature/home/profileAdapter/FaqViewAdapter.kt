package com.emproto.hoabl.feature.home.profileAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.feature.home.profileAdapter.data.FaqData


class FaqViewAdapter(context: Context, list: ArrayList<FaqData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
        const val VIEW_TYPE_THREE = 3
    }

    private val context: Context = context
    var list: ArrayList<FaqData> = list

    private inner class View1ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var message: TextView = itemView.findViewById(R.id.textView)
        var unward :ImageView= itemView.findViewById(R.id.imgDrop)
        var downward :ImageView= itemView.findViewById(R.id.imgdropdown)
        var answer:TextView = itemView.findViewById(R.id.tvView)
        fun bind(position: Int) {
            val recyclerViewModel = list[position]
            message.text = recyclerViewModel.textData
            downward.setOnClickListener{
                downward.visibility = View.GONE
                unward.visibility = View.VISIBLE
                answer.visibility = View.VISIBLE
            }
            unward.setOnClickListener {
                downward.visibility = View.VISIBLE
                unward.visibility = View.GONE
                answer.visibility = View.GONE
            }
        }
    }
    private inner class View2ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var message: TextView = itemView.findViewById(R.id.category)
        fun bind(position: Int) {
            val recyclerViewModel = list[position]
            message.text = recyclerViewModel.category

        }
    }
    private inner class View3ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var message: TextView = itemView.findViewById(R.id.textView)
        var unward :ImageView= itemView.findViewById(R.id.imgDrop)
        var downward :ImageView= itemView.findViewById(R.id.imgdropdown)
        var answer:TextView = itemView.findViewById(R.id.tvView)
        fun bind(position: Int) {
            val recyclerViewModel = list[position]
            message.text = recyclerViewModel.textData
            downward.setOnClickListener{
                downward.visibility = View.GONE
                unward.visibility = View.VISIBLE
                answer.visibility = View.VISIBLE
            }
            unward.setOnClickListener {
                downward.visibility = View.VISIBLE
                unward.visibility = View.GONE
                answer.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ONE) {
            return View1ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.faq_view_1, parent, false)
            )
        }
        else if (viewType == VIEW_TYPE_TWO){
            return View2ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.faq_view_2, parent, false)
            )
        }
        return View3ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.faq_view_3, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list[position].viewType === VIEW_TYPE_ONE) {
            (holder as View1ViewHolder).bind(position)
        } else if(list[position].viewType === VIEW_TYPE_TWO) {
            (holder as View2ViewHolder).bind(position)
        }
        else if(list[position].viewType === VIEW_TYPE_THREE){
            (holder as View3ViewHolder).bind(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }
}