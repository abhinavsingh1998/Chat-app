package com.emproto.hoabl.feature.investment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemLocationInfrastructureBinding
import com.emproto.networklayer.response.investment.LocInfValues
import com.emproto.networklayer.response.investment.ValueXXX

class LocationInfrastructureAdapter(private val list: List<ValueXXX>):RecyclerView.Adapter<LocationInfrastructureAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: ItemLocationInfrastructureBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemLocationInfrastructureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        holder.binding.apply {
            tvLocationName.text  = element.name
        }
    }

    override fun getItemCount(): Int = list.size
}