package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemSmartDealsBinding
import com.emproto.hoabl.databinding.ProjectAmenitiesItemLayoutBinding

class ProjectAmenitiesAdapter(val context: Context, list: List<String>) : RecyclerView.Adapter<ProjectAmenitiesAdapter.MyViewHolder>() {

    var list: List<String>
    var mcontext:Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ProjectAmenitiesItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: ProjectAmenitiesItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    init {
        this.list = list
        this.mcontext = context
    }
}
