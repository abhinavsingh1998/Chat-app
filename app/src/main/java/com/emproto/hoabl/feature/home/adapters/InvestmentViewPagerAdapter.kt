package com.emproto.hoabl.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.CustomImageLayoutBinding
import com.emproto.hoabl.model.ViewItem

class InvestmentViewPagerAdapter( private var imageList: List<ViewItem>) :  RecyclerView.Adapter<InvestmentViewPagerAdapter.InvestmentViewHolder>() {

    inner class InvestmentViewHolder(var binding: CustomImageLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvestmentViewHolder {
        val view = CustomImageLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InvestmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: InvestmentViewHolder, position: Int) {
        val element = imageList[position]
        Glide.with(holder.itemView.context)
            .load(element.imageDrawable)
            .into(holder.binding.image)
    }

    override fun getItemCount(): Int = imageList.size

}