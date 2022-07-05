package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemVideoLayoutBinding

class VideosAdapter(context: Context, list: List<String>) :
    RecyclerView.Adapter<VideosAdapter.MyViewHolder>() {
    val context: Context
    val list: List<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemVideoLayoutBinding =
            ItemVideoLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(binding: ItemVideoLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val binding: ItemVideoLayoutBinding

        init {
            this.binding = binding
        }
    }

    init {
        this.context = context
        this.list = list
    }
}