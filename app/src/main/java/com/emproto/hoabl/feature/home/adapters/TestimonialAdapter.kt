package com.emproto.hoabl.feature.home.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemTestimonialsBinding
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[holder.adapterPosition]

        holder.binding.tvName.text = item.firstName + " " + item.lastName
        holder.binding.tvCompanyName.text = item.designation + " " + item.companyName
        holder.binding.details.text = item.testimonialContent
    }

    override fun getItemCount(): Int {
        var itemList = 0
        itemList = if (itemCount.page.totalTestimonialsOnHomeScreen < list.size) {
            itemCount.page.totalTestimonialsOnHomeScreen
        } else {
            list.size
        }
        return itemList
    }

    inner class MyViewHolder(val binding: ItemTestimonialsBinding) :
        RecyclerView.ViewHolder(binding.root)
}
