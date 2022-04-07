package com.emproto.hoabl.feature.home.profileAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R


class SettingsAdapter (private val newsList:ArrayList<SettingsData>):RecyclerView.Adapter<SettingsAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.setting_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem= newsList[position]
        holder.tvHeading.text= currentItem.heading
        holder.desc.text= currentItem.desc

    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val tvHeading:TextView= itemView.findViewById(R.id.tvHeading)
        val desc:TextView= itemView.findViewById(R.id.desc)
//        val Switch:SwitchCompat=itemView.findViewById(R.id.Switch)
    }

}