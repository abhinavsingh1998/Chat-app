package com.emproto.hoabl.feature.home.promises.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.feature.home.promises.data.SecondScreenPromiseData
import com.google.android.material.imageview.ShapeableImageView
class PromiseSecondScreenAdapter(
    context: Context, private val newsList:ArrayList<SecondScreenPromiseData>): RecyclerView.Adapter<PromiseSecondScreenAdapter.MyViewHolder>() {


    private val context: Context = context
    var list: ArrayList<SecondScreenPromiseData> = newsList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.promise_second_view,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem= newsList[position]
        holder.titleImage.setImageResource(currentItem.titleImage)
        holder.tvHeading.text=currentItem.heading
    }

    override fun getItemCount(): Int {
        return  newsList.size
    }
    class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val titleImage:ShapeableImageView= itemView.findViewById(R.id.title_image)
        val tvHeading:TextView= itemView.findViewById(R.id.tvHeading)
    }

}
