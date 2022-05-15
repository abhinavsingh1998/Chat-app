package com.emproto.hoabl.feature.home.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.DetailViewItemBinding
import com.emproto.networklayer.response.home.DetailedInfo
import com.emproto.networklayer.response.home.InsightsMedia


class InsightsListAdapter(
    val context: Context,
    private val list: List<InsightsMedia>,

    ) : RecyclerView.Adapter<InsightsListAdapter.InsightsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):InsightsHolder {
        val view =
            DetailViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InsightsHolder(view)
    }

    override fun onBindViewHolder(holder: InsightsHolder, position: Int) {
        val item = list[position]
        holder.binding.firstDetails.text= item.description
        holder.binding.imageDesc.text= item.media.mediaDescription
        Glide.with(context)
            .load(item.media.value.url)
            .into(holder.binding.image1)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class InsightsHolder(var binding: DetailViewItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}
