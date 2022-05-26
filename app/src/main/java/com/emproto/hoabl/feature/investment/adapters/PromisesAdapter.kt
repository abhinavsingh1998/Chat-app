package com.emproto.hoabl.feature.investment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemPromisesBinding
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.networklayer.response.investment.PmData
import com.emproto.networklayer.response.promises.HomePagesOrPromise

class PromisesAdapter(
    private val list: List<PmData>,
    private val itemClickListener: ItemClickListener
):RecyclerView.Adapter<PromisesAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: ItemPromisesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemPromisesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        holder.binding.apply {
            when(element.shortDescription.isNullOrEmpty()){
                true -> { tvPromisesText.text = "No Description available"}
                false -> {
                    tvPromisesText.visibility = View.VISIBLE
                    tvPromisesText.text = element.shortDescription
                }
            }
            tvPromisesName.text = element.name
        }
        holder.binding.cvPromisesCard.setOnClickListener {
            itemClickListener.onItemClicked(it,position,element.id.toString())
        }
    }

    override fun getItemCount(): Int = list.size

}