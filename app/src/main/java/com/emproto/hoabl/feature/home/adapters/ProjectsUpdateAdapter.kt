package com.emproto.hoabl.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemProjectsUpdatesBinding

class ProjectsUpdateAdapter(context: Context, list: List<String>) :
    RecyclerView.Adapter<ProjectsUpdateAdapter.MyViewHolder>() {

    var list: List<String>
    var mcontext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemProjectsUpdatesBinding =
            ItemProjectsUpdatesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(binding: ItemProjectsUpdatesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemProjectsUpdatesBinding

        init {
            this.binding = binding
        }
    }

    init {
        this.list = list
        this.mcontext = context
    }
}
