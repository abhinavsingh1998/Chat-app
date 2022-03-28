package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemKeyPillarBinding

class KeyPillarAdapter(val context: Context, private val list: List<String>):
    RecyclerView.Adapter<KeyPillarAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: ItemKeyPillarBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemKeyPillarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = list.size
}