package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.ItemHoablPromisesBinding
import com.emproto.hoabl.databinding.ItemPromisesBinding
import com.emproto.hoabl.databinding.ItemSmartDealsBinding
import com.emproto.networklayer.response.home.HomePagesOrPromise
import com.emproto.networklayer.response.home.PageManagementsOrNewInvestment

class HoABLPromisesAdapter1(
    val context: Context,
    val list: List<HomePagesOrPromise>,
    val itemInterface:PromisesItemInterface
) : RecyclerView.Adapter<HoABLPromisesAdapter1.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemHoablPromisesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list.get(holder.adapterPosition)

        holder.binding.title.text= item.name
        holder.binding.desc.text= item.shortDescription
        Glide.with(context)
            .load(item.displayMedia.value.url)
            .dontAnimate()
            .into(holder.binding.image)

        holder.binding.itemCard.setOnClickListener(View.OnClickListener {
            itemInterface.onClickItem(position)
        })

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(val binding: ItemHoablPromisesBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface PromisesItemInterface {
        fun onClickItem(position: Int)
    }

}
