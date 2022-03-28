package com.emproto.hoabl.feature.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemCategoryListBinding

class CategoryListAdapter(val list:List<String>):RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(var binding: ItemCategoryListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = ItemCategoryListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = list.size


}