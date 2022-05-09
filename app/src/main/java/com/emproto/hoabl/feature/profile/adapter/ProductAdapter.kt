package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.feature.profile.data.AboutusData

class ProductAdapter(context: Context,private val aboutusList:ArrayList<AboutusData>):RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ProductViewHolder {

        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.product_category,parent,false)
        return ProductViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ProductAdapter.ProductViewHolder, position: Int) {
        val currentItem= aboutusList[position]
        holder.location.setImageResource(currentItem.location)
        holder.tvHeading.text= currentItem.heading
        holder.desc.text= currentItem.desc
    }

    override fun getItemCount(): Int {
        return aboutusList.size

    }
    class ProductViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val location:ImageView= itemView.findViewById(R.id.location_iv)
        val tvHeading: TextView = itemView.findViewById(R.id.lifestyle_text)
        val desc: TextView = itemView.findViewById(R.id.full_desc)

    }
}
