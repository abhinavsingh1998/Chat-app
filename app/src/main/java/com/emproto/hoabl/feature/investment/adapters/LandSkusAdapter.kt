package com.emproto.hoabl.feature.investment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.LandSkusTopLayoutBinding
import com.emproto.hoabl.databinding.NotConvincedLayoutBinding
import com.emproto.hoabl.model.RecyclerViewItem

class LandSkusAdapter(private val list:List<RecyclerViewItem>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val LAND_SKUS_VIEW_TYPE_ONE = 1
        const val LAND_SKUS_VIEW_TYPE_TWO = 2
    }

    private lateinit var skusListAdapter: SkusListAdapter
    private lateinit var onItemClickListener : View.OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            LAND_SKUS_VIEW_TYPE_ONE -> LandSkusTopViewHolder(LandSkusTopLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            else -> { LandSkusNotConvincedViewHolder(NotConvincedLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(list[position].viewType){
            LAND_SKUS_VIEW_TYPE_ONE -> { (holder as LandSkusTopViewHolder).bind(position)}
            LAND_SKUS_VIEW_TYPE_TWO -> { (holder as LandSkusNotConvincedViewHolder).bind(position)}
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

    private inner class LandSkusTopViewHolder(val binding: LandSkusTopLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val list = arrayListOf<String>("1","2","3","4","5")

            skusListAdapter = SkusListAdapter(list)
            binding.rvLandSkusItems.adapter = skusListAdapter
            skusListAdapter.setItemClickListener(onItemClickListener)
        }
    }

    private inner class LandSkusNotConvincedViewHolder(val binding: NotConvincedLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){

        }
    }

    fun setItemClickListener(clickListener: View.OnClickListener) {
        onItemClickListener = clickListener
    }


}