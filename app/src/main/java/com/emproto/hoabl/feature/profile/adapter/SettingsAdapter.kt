package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.feature.profile.data.SettingsData
import com.emproto.hoabl.utils.Extensions


class SettingsAdapter (private val context: Context, private val settingsList:ArrayList<SettingsData>):RecyclerView.Adapter<SettingsAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.setting_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem= settingsList[position]
        holder.tvHeading.text= currentItem.heading
        holder.desc.text= currentItem.desc
//        holder.switch.setOnCheckedChangeListener { buttonView, isChecked ->
//            when (isChecked) {
//                true -> {
//
//                }
//                false -> {
//
//                }
//            }
//        }
    }

    override fun getItemCount(): Int {
        return settingsList.size
    }

    class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val tvHeading:TextView= itemView.findViewById(R.id.tvHeading)
        val desc:TextView= itemView.findViewById(R.id.desc)
        val switch:SwitchCompat = itemView.findViewById(R.id.switch1)
    }

}