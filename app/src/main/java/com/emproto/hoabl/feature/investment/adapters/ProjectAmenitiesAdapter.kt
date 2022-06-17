package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.ItemSmartDealsBinding
import com.emproto.hoabl.databinding.ProjectAmenitiesItemLayoutBinding
import com.emproto.networklayer.response.investment.ProjectAminity

class ProjectAmenitiesAdapter(val context: Context,val list: List<ProjectAminity>) : RecyclerView.Adapter<ProjectAmenitiesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ProjectAmenitiesItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        holder.binding.apply {
            tvPaFirstText.text = element.name
            Glide
                .with(context)
                .load(element.icon.value.url)
                .into(ivPaFirstImage)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: ProjectAmenitiesItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

}
