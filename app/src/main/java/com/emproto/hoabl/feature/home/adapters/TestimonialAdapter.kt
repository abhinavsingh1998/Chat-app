package com.emproto.hoabl.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemInsightsBinding
import com.emproto.hoabl.databinding.ItemProjectsUpdatesBinding
import com.emproto.hoabl.databinding.ItemTestimonialsBinding

class TestimonialAdapter(context: Context, list: List<String>) : RecyclerView.Adapter<TestimonialAdapter.MyViewHolder>() {

    var list: List<String>
    var mcontext:Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemTestimonialsBinding =
            ItemTestimonialsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(binding: ItemTestimonialsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemTestimonialsBinding

        init {
            this.binding = binding
        }
    }

    init {
        this.list = list
        this.mcontext=context
    }
}
