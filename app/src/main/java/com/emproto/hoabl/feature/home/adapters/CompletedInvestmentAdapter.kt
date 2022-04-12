package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemCompletedInvestmentsBinding

class CompletedInvestmentAdapter(val list: List<String>) : RecyclerView.Adapter<CompletedInvestmentAdapter.MyViewHolder>()  {

    inner class MyViewHolder(var binding: ItemCompletedInvestmentsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemCompletedInvestmentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.ivCompletedInvestmentDropArrow.setOnClickListener{
            holder.binding.cvCompletedInvestmentGraphCard.visibility = View.VISIBLE
            holder.binding.ivCompletedInvestmentUpwardArrow.visibility = View.VISIBLE
            holder.binding.ivCompletedInvestmentDropArrow.visibility = View.GONE
        }

        holder.binding.ivCompletedInvestmentUpwardArrow.setOnClickListener{
            holder.binding.cvCompletedInvestmentGraphCard.visibility = View.GONE
            holder.binding.ivCompletedInvestmentUpwardArrow.visibility = View.GONE
            holder.binding.ivCompletedInvestmentDropArrow.visibility = View.VISIBLE
        }

    }

    override fun getItemCount(): Int = list.size
}