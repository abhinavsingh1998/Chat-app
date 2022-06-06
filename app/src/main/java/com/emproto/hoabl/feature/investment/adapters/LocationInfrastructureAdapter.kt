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
    private var isDistanceAvl: Boolean = false
):RecyclerView.Adapter<LocationInfrastructureAdapter.MyViewHolder>() {

    var lastCheckedPosition = -1
    var checkedPosition = 0

    inner class MyViewHolder(var binding: ItemLocationInfrastructureBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemLocationInfrastructureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        holder.binding.apply {
            tvLocationName.text  = element.name
            Glide
                .with(context)
                .load(element.icon.value.url)
                .into(ivLocationImage)
            when(isDistanceAvl){
                true ->  tvLocationDistance.visibility = View.VISIBLE
            }
            cvLocationInfrastructureCard.setOnClickListener{
                itemClickListener.onItemClicked(it,position,element.latitude,element.longitude)
//                for(i in 0..list.size-1){
//                    if(position == i){
//                        cvLocationInfrastructureCard.strokeWidth = 2
//                        cvLocationInfrastructureCard.strokeColor = ContextCompat.getColor(context,R.color.text_blue_color)
//                    }else{
//                        cvLocationInfrastructureCard.strokeWidth = 0
//                        cvLocationInfrastructureCard.strokeColor = ContextCompat.getColor(context,R.color.white)
//                    }
//                }
//                checkedPosition = position
//
//                cvLocationInfrastructureCard.strokeWidth = 2
//                cvLocationInfrastructureCard.strokeColor = ContextCompat.getColor(context,R.color.text_blue_color)
            }
        }
    }

    override fun getItemCount(): Int = list.size

}