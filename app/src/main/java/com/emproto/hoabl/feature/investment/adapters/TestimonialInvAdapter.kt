package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemTestimonialsBinding
import com.emproto.networklayer.response.home.PageManagementsOrTestimonial

class TestimonialInvAdapter(val context: Context, val list: List<PageManagementsOrTestimonial>) :
    RecyclerView.Adapter<TestimonialInvAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            ItemTestimonialsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[holder.adapterPosition]

        val tvName = item.firstName + " " + item.lastName
        holder.binding.tvName.text = tvName
        val tvCompanyName = item.designation + " " + item.companyName
        holder.binding.tvCompanyName.text = tvCompanyName
        holder.binding.details.text = item.testimonialContent

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(val binding: ItemTestimonialsBinding) :
        RecyclerView.ViewHolder(binding.root)
}