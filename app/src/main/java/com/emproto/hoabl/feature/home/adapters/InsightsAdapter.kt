package com.emproto.hoabl.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemInsightsBinding
import com.emproto.hoabl.databinding.ItemProjectsUpdatesBinding

class InsightsAdapter(context: Context, list: List<String>) : RecyclerView.Adapter<InsightsAdapter.MyViewHolder>() {

    var list: List<String>
    var mcontext:Context
    lateinit var monInsightsClickItem: OnInsightsClickItem

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemInsightsBinding =
            ItemInsightsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(binding: ItemInsightsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemInsightsBinding

        init {
            this.binding = binding
        }
    }


    interface OnInsightsClickItem {
        fun onclickDetails()
    }


    init {
        this.list = list
        this.mcontext=context
    }
}
