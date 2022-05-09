package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.HealthCenterViewBinding
import com.emproto.hoabl.feature.profile.data.DataHealthCenter

class HelpListAdapter(context: Context, private val data: List<DataHealthCenter>, val itemInterface: HoabelHealthAdapter.HelpItemInterface

) : RecyclerView.Adapter<HelpListAdapter.ListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelpListAdapter.ListHolder {
        val view = HealthCenterViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListHolder(view)
    }

    override fun onBindViewHolder(holder: HelpListAdapter.ListHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ListHolder(var binding: HealthCenterViewBinding) :
        RecyclerView.ViewHolder(binding.root)

}