package com.emproto.hoabl.feature.investment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.KeyAttractionsLayoutBinding

class KeyAttractionsAdapter(private val itemList:List<String>):RecyclerView.Adapter<KeyAttractionsAdapter.KeyAttractionsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeyAttractionsViewHolder {
        val view = KeyAttractionsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KeyAttractionsViewHolder(view)
    }

    override fun onBindViewHolder(holder: KeyAttractionsViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = itemList.size

    inner class KeyAttractionsViewHolder(var binding: KeyAttractionsLayoutBinding) : RecyclerView.ViewHolder(binding.getRoot())

}