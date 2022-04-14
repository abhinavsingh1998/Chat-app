package com.emproto.hoabl.feature.investment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemLandSkusAppliedBinding
import com.emproto.hoabl.databinding.ItemLandSkusBinding

class SkusListAppliedAdapter(private val list:List<String>):RecyclerView.Adapter<SkusListAppliedAdapter.SkusListAppliedViewHolder>() {

    private lateinit var onItemClickListener : View.OnClickListener

    inner class SkusListAppliedViewHolder(var binding: ItemLandSkusAppliedBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkusListAppliedViewHolder {
        val view = ItemLandSkusAppliedBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SkusListAppliedViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkusListAppliedViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = list.size

    fun setSkusListItemClickListener(clickListener: View.OnClickListener) {
        onItemClickListener = clickListener
    }


}