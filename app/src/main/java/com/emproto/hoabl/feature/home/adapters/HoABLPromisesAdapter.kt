package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemHoablPromisesBinding
import com.emproto.hoabl.databinding.ItemPromisesBinding
import com.emproto.hoabl.databinding.PromisesViewBinding
import com.emproto.networklayer.response.portfolio.ivdetails.DataX

class HoABLPromisesAdapter(val context: Context, val list: List<DataX>) :
    RecyclerView.Adapter<HoABLPromisesAdapter.MyViewHolder>() {

    lateinit var binding: ItemHoablPromisesBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = ItemHoablPromisesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.binding.title.text = item.name
        holder.binding.desc.text = item.shortDescription
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.binding.image.setImageResource(R.drawable.securitylock)
        }
        Glide.with(context)
            .load(item.promiseMedia.value.url)
            .into(holder.binding.image)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(binding: ItemHoablPromisesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemHoablPromisesBinding = binding

    }

}
