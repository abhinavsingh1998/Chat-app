package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemCurrentInfraStoryLayoutBinding
import com.emproto.hoabl.databinding.ItemSmartDealsBinding

class CurrentInfraStoryAdapter(val context: Context, list: List<String>) : RecyclerView.Adapter<CurrentInfraStoryAdapter.MyViewHolder>() {

    var list: List<String>
    var mcontext:Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemCurrentInfraStoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: ItemCurrentInfraStoryLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    init {
        this.list = list
        this.mcontext = context
    }
}
