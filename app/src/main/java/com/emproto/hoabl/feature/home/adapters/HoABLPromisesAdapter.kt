package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemPromisesBinding

class HoABLPromisesAdapter(context: Context, list: List<String>) : RecyclerView.Adapter<HoABLPromisesAdapter.MyViewHolder>() {

    var list: List<String> = list
    var mcontext:Context = context
    lateinit var binding: ItemPromisesBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding= ItemPromisesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        binding.tvPromisesName.setText(list.get(position))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(binding: ItemPromisesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemPromisesBinding

        init {
            this.binding = binding
        }
    }

}
