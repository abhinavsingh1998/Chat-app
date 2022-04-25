package com.emproto.hoabl.feature.portfolio.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.CustomImageLayoutBinding
import com.emproto.hoabl.databinding.ItemAttentionBinding
import com.emproto.hoabl.feature.investment.adapters.ProjectDetailViewPagerAdapter
import com.emproto.hoabl.model.ViewItem

class PortfolioSpecificViewPagerAdapter(private val imageList: List<ViewItem>) : RecyclerView.Adapter<PortfolioSpecificViewPagerAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: ItemAttentionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemAttentionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = imageList.size

}