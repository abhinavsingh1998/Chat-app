package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemCurrentInfraStoryLayoutBinding
import com.emproto.hoabl.databinding.ItemSmartDealsBinding
import com.emproto.hoabl.databinding.ItemUpcomingInfraStoryLayoutBinding

class UpcomingInfraStoryAdapter(val context: Context, list: List<String>) : RecyclerView.Adapter<UpcomingInfraStoryAdapter.MyViewHolder>() {

    var list: List<String>
    var mcontext:Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemUpcomingInfraStoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: ItemUpcomingInfraStoryLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    init {
        this.list = list
        this.mcontext = context
    }
}
