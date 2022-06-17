package com.emproto.hoabl.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemsInvestmentBinding

class MyProjectsAdapter(val context: Context, list: List<String>) : RecyclerView.Adapter<MyProjectsAdapter.MyViewHolder>() {

    var list: List<String>
    var mcontext:Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemsInvestmentBinding =
            ItemsInvestmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(binding: ItemsInvestmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemsInvestmentBinding

        init {
            this.binding = binding
        }
    }

    init {
        this.list = list
        this.mcontext = context
    }
}
