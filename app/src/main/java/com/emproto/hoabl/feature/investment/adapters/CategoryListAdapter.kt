package com.emproto.hoabl.feature.investment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemCategoryListBinding
import com.emproto.hoabl.utils.ItemClickListener

class CategoryListAdapter(val list:List<String>, private val itemClickListener:ItemClickListener):RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(var binding: ItemCategoryListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(view: View, position:Int, item:String, clickListener: ItemClickListener){
            itemView.setOnClickListener{
                clickListener.onItemClicked(view,position,item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = ItemCategoryListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val list = list[position]
        holder.bind(holder.itemView,position,list,itemClickListener)

    }

    override fun getItemCount(): Int = list.size

}