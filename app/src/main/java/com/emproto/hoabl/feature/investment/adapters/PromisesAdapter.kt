package com.emproto.hoabl.feature.investment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemPromisesBinding
import com.emproto.networklayer.response.investment.PmData

class PromisesAdapter(private val list: List<PmData>):RecyclerView.Adapter<PromisesAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: ItemPromisesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemPromisesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        holder.binding.apply {
            tvPromisesName.text = element.name
            tvPromisesText.text = element.shortDescription
        }
    }

    override fun getItemCount(): Int = list.size

}