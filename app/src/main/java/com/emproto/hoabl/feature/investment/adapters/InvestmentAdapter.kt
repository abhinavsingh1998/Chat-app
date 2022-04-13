package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemSmartDealsBinding

class InvestmentAdapter(val context: Context, list: List<String>, private val theme:String = "Home") : RecyclerView.Adapter<InvestmentAdapter.MyViewHolder>() {

    var list: List<String>
    var mcontext:Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemSmartDealsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(binding: ItemSmartDealsBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
        var binding: ItemSmartDealsBinding
        init {
            this.binding = binding
        }
    }

    init {
        this.list = list
        this.mcontext = context
    }
}
