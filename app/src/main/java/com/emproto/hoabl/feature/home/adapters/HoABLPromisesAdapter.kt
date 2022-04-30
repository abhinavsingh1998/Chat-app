package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.ItemPromisesBinding
import com.emproto.hoabl.databinding.ItemSmartDealsBinding
import com.emproto.networklayer.response.home.HomePagesOrPromise
import com.emproto.networklayer.response.home.PageManagementsOrNewInvestment

class HoABLPromisesAdapter(val context: Context, val list: List<HomePagesOrPromise>) : RecyclerView.Adapter<HoABLPromisesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemPromisesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list.get(holder.adapterPosition)

        holder.binding.tvPromisesName.text= item.name
        holder.binding.tvPromisesText.text= item.shortDescription

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(val binding: ItemPromisesBinding) :
        RecyclerView.ViewHolder(binding.root)


}
