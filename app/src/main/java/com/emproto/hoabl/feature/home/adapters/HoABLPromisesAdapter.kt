package com.emproto.hoabl.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemHoablPromisesBinding
import com.emproto.hoabl.databinding.ItemLatestUpdatesBinding

class HoABLPromisesAdapter(context: Context, list: List<String>) : RecyclerView.Adapter<HoABLPromisesAdapter.MyViewHolder>() {

    var list: List<String> = list
    var mcontext:Context = context
    lateinit var binding: ItemHoablPromisesBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding= ItemHoablPromisesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        binding.tvName.setText(list.get(position))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(binding: ItemHoablPromisesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemHoablPromisesBinding

        init {
            this.binding = binding
        }
    }

}
