package com.emproto.hoabl.feature.portfolio.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.networklayer.response.portfolio.ivdetails.Image
import com.example.portfolioui.databinding.ProjectMediaBinding

class VideoAdapter(
    private val list: List<Image>,
    val ivInterface: PortfolioSpecificViewAdapter.InvestmentScreenInterface
) :
    RecyclerView.Adapter<VideoAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: ProjectMediaBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ProjectMediaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        Glide.with(holder.itemView.context)
            .load(element.mediaContent.value.url)
            .into(holder.binding.ivLatestImage)
        holder.binding.ivLatestImage.setOnClickListener {
            ivInterface.onClickImage(element.mediaContent.value.url)
        }
    }

    override fun getItemCount(): Int = list.size
}