package com.emproto.hoabl.feature.home.promises.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.PromisesViewBinding
import com.emproto.hoabl.feature.home.promises.data.DataModel


class PromisesListAdapter(
    context: Context,
    private val data: List<DataModel>,
    val itemInterface: HoabelPromiseAdapter.PromisedItemInterface

) : RecyclerView.Adapter<PromisesListAdapter.PromisesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromisesHolder {
        val view =
            PromisesViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PromisesHolder(view)
    }

    override fun onBindViewHolder(holder: PromisesHolder, position: Int) {
        val item = data[position]
        holder.binding.title.text = item.title
        holder.binding.desc.text = item.desc
        holder.binding.image.setImageResource(item.image)
        holder.binding.arrowImage.setImageResource(item.arrowImage)
        holder.binding.itemCard.setOnClickListener {
            itemInterface.onClickItem(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class PromisesHolder(var binding: PromisesViewBinding) :
        RecyclerView.ViewHolder(binding.root)

}
