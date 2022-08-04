package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemLocationInfrastructureBinding
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.utils.MapItemClickListener
import com.emproto.networklayer.response.investment.LocInfValues
import com.emproto.networklayer.response.investment.ValueXXX

class LocationInfrastructureAdapter(
    private val context: Context,
    private val list: List<ValueXXX>,
    private val itemClickListener: MapItemClickListener,
    private var isDistanceAvl: Boolean = false,
    private var distanceList: ArrayList<String>,
    private var selectedItemPos: Int = -1
) : RecyclerView.Adapter<LocationInfrastructureAdapter.MyViewHolder>() {

    var lastItemSelectedPos = -1

    inner class MyViewHolder(var binding: ItemLocationInfrastructureBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            view: View,
            position: Int,
            item: Any,
            clickListener: MapItemClickListener
        ) {
            val element = list[position]
            when (distanceList.size) {
                0 -> {

                }
                else -> {
                    val distanceItem = distanceList[position]
                    binding.tvLocationDistance.text = distanceItem
                }
            }

            binding.apply {
                tvLocationName.text = element.name
                when (element.hours) {
                    "0" -> tvLocationDuration.text = "${element.minutes}mins"
                    else -> tvLocationDuration.text = "${element.hours}hr ${element.minutes}mins"
                }
                Glide
                    .with(context)
                    .load(element.icon.value.url)
                    .into(ivLocationImage)
                when (isDistanceAvl) {
                    true -> tvLocationDistance.visibility = View.VISIBLE
                }
            }
            binding.cvLocationInfrastructureCard.setOnClickListener {
                lastItemSelectedPos = selectedItemPos
                selectedItemPos = adapterPosition
                if (lastItemSelectedPos == -1)
                    lastItemSelectedPos = selectedItemPos
                else {
                    notifyItemChanged(lastItemSelectedPos)
                    lastItemSelectedPos = selectedItemPos
                }
                notifyItemChanged(selectedItemPos)
                itemClickListener.onItemClicked(it, position, element.latitude, element.longitude)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemLocationInfrastructureBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position == selectedItemPos) {
            holder.binding.cvLocationInfrastructureCard.strokeWidth = 2
            holder.binding.cvLocationInfrastructureCard.strokeColor =
                ContextCompat.getColor(context, R.color.text_blue_color)
        } else {
            holder.binding.cvLocationInfrastructureCard.strokeWidth = 0
            holder.binding.cvLocationInfrastructureCard.strokeColor =
                ContextCompat.getColor(context, R.color.white)
        }
        holder.bind(holder.itemView, position, list, itemClickListener)
    }

    override fun getItemCount(): Int = list.size

}