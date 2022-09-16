package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.ItemKeyPillarBinding
import com.emproto.networklayer.response.investment.ProjectValue
import com.emproto.networklayer.response.investment.ValueX

class KeyPillarAdapter(val context: Context, private val list: List<ValueX>) :
    RecyclerView.Adapter<KeyPillarAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: ItemKeyPillarBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemKeyPillarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        holder.binding.tvPremiumLocation.text = element.name

        if (element.points.isNotEmpty())
            holder.binding.tvPointOne.text = element.points[0]
        Glide
            .with(context)
            .load(element.icon.value.url)
            .into(holder.binding.ivPremiumLocation)
    }

    override fun getItemCount(): Int = list.size
}