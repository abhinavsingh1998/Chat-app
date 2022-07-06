package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.*
import com.emproto.networklayer.response.home.Data
import com.emproto.networklayer.response.home.PageManagementsOrTestimonial

class TestimonialAdapter(
    val context: Context,
    val itemCount: Data,
    val list: List<PageManagementsOrTestimonial>
) : RecyclerView.Adapter<TestimonialAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            ItemTestimonialsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list.get(holder.adapterPosition)

        holder.binding.tvName.text = item.firstName + " " + item.lastName
        holder.binding.tvCompanyName.text = item.designation + " " + item.companyName
        holder.binding.details.text = item.testimonialContent
    }

    override fun getItemCount(): Int {
        var itemList = 0
        if (itemCount.page.totalTestimonialsOnHomeScreen < list.size) {
            itemList = itemCount.page.totalTestimonialsOnHomeScreen
        } else {
            itemList = list.size
        }
        return itemList
    }

    inner class MyViewHolder(val binding: ItemTestimonialsBinding) :
        RecyclerView.ViewHolder(binding.root)
}
