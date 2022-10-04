package com.emproto.hoabl.feature.home.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.TestimonalsListViewBinding
import com.emproto.networklayer.response.testimonials.Data

class TestimonialsAdapter(val context: Context, val list: List<Data>, private val listCount: Int) :
    RecyclerView.Adapter<TestimonialsAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            TestimonalsListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        itemList = if (list.size < listCount) {
            list.size
        } else {
            listCount
        }
        return itemList

    }

    inner class MyViewHolder(val binding: TestimonalsListViewBinding) :
        RecyclerView.ViewHolder(binding.root)
}