package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.ItemOppDocDestinationBinding
import com.emproto.hoabl.utils.Extensions.showHTMLText
import com.emproto.networklayer.response.investment.Story

class DestinationAdapter(private val context:Context,private val itemList: List<Story>):RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder>() {

    inner class DestinationViewHolder(var binding: ItemOppDocDestinationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val view = ItemOppDocDestinationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DestinationViewHolder(view)
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        val element = itemList[position]
        holder.binding.apply {
            tvItemDestName.text = element.title
            tvItemDestDescription.text = context.showHTMLText(element.description)
            Glide.with(context)
                .load(element.media.value.url)
                .into(ivDestImage)
        }
    }

    override fun getItemCount(): Int = itemList.size
}