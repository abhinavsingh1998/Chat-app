package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.LatestUpdateDetailItemBinding
import com.emproto.networklayer.response.home.DetailedInfo


class LatestUpdateListAdapter(
    val context: Context,
    private val list: List<DetailedInfo>,

    ) : RecyclerView.Adapter<LatestUpdateListAdapter.LatestUpdateHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestUpdateHolder {
        val view =
            LatestUpdateDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LatestUpdateHolder(view)
    }

    override fun onBindViewHolder(holder: LatestUpdateHolder, position: Int) {
        val item = list[position]
        holder.binding.firstDetails.text= item.description

        if(item.media!=null){
            holder.binding.imageDesc.text= item.media.mediaDescription
            Glide.with(context)
                .load(item.media.value.url)
                .into(holder.binding.image1)
        }



    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class LatestUpdateHolder(var binding: LatestUpdateDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}