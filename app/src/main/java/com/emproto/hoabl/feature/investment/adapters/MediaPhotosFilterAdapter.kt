package com.emproto.hoabl.feature.investment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemPhotosFilterBinding

class MediaPhotosFilterAdapter(private val itemList: List<String>) :
    RecyclerView.Adapter<MediaPhotosFilterAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: ItemPhotosFilterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            ItemPhotosFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.itemFilterName.text = itemList[position]
    }

    override fun getItemCount(): Int = itemList.size

}