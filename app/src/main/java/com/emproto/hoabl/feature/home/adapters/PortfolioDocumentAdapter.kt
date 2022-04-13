package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemDocumentsBinding

class PortfolioDocumentAdapter (context: Context, list: List<String>) : RecyclerView.Adapter<PortfolioDocumentAdapter.MyViewHolder>() {

    var list: List<String> = list
    var mcontext: Context = context
    lateinit var binding: ItemDocumentsBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding= ItemDocumentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        binding.tvDocumentName.setText(list.get(position))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(binding: ItemDocumentsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemDocumentsBinding

        init {
            this.binding = binding
        }
    }

}