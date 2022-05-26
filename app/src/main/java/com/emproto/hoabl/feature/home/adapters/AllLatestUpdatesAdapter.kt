package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.InsightsListItemBinding
import com.emproto.hoabl.databinding.ItemLatestUpdatesBinding
import com.emproto.hoabl.databinding.ProjectUpdatesItemBinding
import com.emproto.hoabl.feature.promises.adapter.HoabelPromiseAdapter
import com.emproto.networklayer.response.home.PageManagementOrInsight
import com.emproto.networklayer.response.home.PageManagementOrLatestUpdate

class AllLatestUpdatesAdapter(
    val context: Context,
    val list: List<PageManagementOrLatestUpdate>,
    val itemInterface: UpdatesItemsInterface)
    : RecyclerView.Adapter<AllLatestUpdatesAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ProjectUpdatesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list.get(holder.adapterPosition)
        holder.binding.title.text= item.displayTitle
        holder.binding.location.text= item.subTitle.toString()

        if (item.detailedInfo[0].media!=null){
            Glide.with(context).load(item.detailedInfo[0].media.value.url)
                .into(holder.binding.locationImage)

        }

        holder.binding.btnReadMore.setOnClickListener {
            itemInterface.onClickItem(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(val binding: ProjectUpdatesItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface UpdatesItemsInterface {
        fun onClickItem(position: Int)
    }
}

