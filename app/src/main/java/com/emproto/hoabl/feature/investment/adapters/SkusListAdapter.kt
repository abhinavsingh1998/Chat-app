package com.emproto.hoabl.feature.investment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemLandSkusBinding

class SkusListAdapter(private val list:List<String>):RecyclerView.Adapter<SkusListAdapter.SkusListViewHolder>() {

    private lateinit var onItemClickListener : View.OnClickListener

    inner class SkusListViewHolder(var binding: ItemLandSkusBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkusListViewHolder {
        val view = ItemLandSkusBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SkusListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkusListViewHolder, position: Int) {
        holder.binding.tvItemLandSkusApplyNow.setOnClickListener(onItemClickListener)
    }

    override fun getItemCount(): Int = list.size

    fun setItemClickListener(clickListener: View.OnClickListener) {
        onItemClickListener = clickListener
    }


}