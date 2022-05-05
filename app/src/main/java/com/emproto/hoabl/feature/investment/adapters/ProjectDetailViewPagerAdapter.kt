package com.emproto.hoabl.feature.investment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.CustomImageLayoutBinding
import com.emproto.hoabl.model.ViewItem

class ProjectDetailViewPagerAdapter(private val imageList: List<String>) : RecyclerView.Adapter<ProjectDetailViewPagerAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: CustomImageLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = CustomImageLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = imageList[position]
        Glide.with(holder.itemView.context)
            .load(element)
            .into(holder.binding.image)
    }

    override fun getItemCount(): Int = imageList.size

}