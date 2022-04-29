package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.feature.profile.data.CorporateAboutusData

class CorporatePhilosphyAdapter(context: Context, private val corporateList:ArrayList<CorporateAboutusData>):
    RecyclerView.Adapter<CorporatePhilosphyAdapter.CorporateViewHolder>(){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CorporatePhilosphyAdapter.CorporateViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.corporate_philosphy_about_us_view,parent,false)
        return CorporateViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CorporatePhilosphyAdapter.CorporateViewHolder, position: Int) {
        val currentItem= corporateList[position]
        holder.location.setImageResource(currentItem.location)
        holder.tvHeading.text= currentItem.heading
        holder.desc.text= currentItem.desc
    }

    override fun getItemCount(): Int {
        return corporateList.size
    }
    class CorporateViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val location:ImageView= itemView.findViewById(R.id.ivImage)
        val tvHeading: TextView = itemView.findViewById(R.id.tv_Piece_of_land)
        val desc: TextView = itemView.findViewById(R.id.full_desc_tv)

    }
}
