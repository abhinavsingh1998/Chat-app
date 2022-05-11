package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.FaqCategoryLayoutBinding
import com.emproto.hoabl.databinding.ItemPopularCategoryBinding


class PopularCategoryAdapter (val context: Context, private val list: List<String>):
    RecyclerView.Adapter<PopularCategoryAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: ItemPopularCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemPopularCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        holder.binding.apply {
            tvCategoryName.text = element
        }
    }

    override fun getItemCount(): Int = list.size
}