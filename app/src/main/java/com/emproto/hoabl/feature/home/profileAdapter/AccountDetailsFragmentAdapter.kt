package com.emproto.hoabl.feature.home.profileAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.feature.home.profileAdapter.data.AccountDetailsData

class AccountDetailsFragmentAdapter (private val accountdetailsList:ArrayList<AccountDetailsData>): RecyclerView.Adapter<AccountDetailsFragmentAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.account_details_view,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem= accountdetailsList[position]
        holder.transaction_name.text= currentItem.transaction_name
        holder.transaction_timedate.text= currentItem.transaction_timedate
        holder.img_rupee.setImageResource(currentItem.img_rupee)
        holder.receipt.text= currentItem.receipt
        holder.payment_milestone.text= currentItem.payment_milestone
        holder.total_amount.text= currentItem.total_amount
    }

    override fun getItemCount(): Int {
        return accountdetailsList.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val transaction_name: TextView = itemView.findViewById(R.id.tv_transaction_name)
        val transaction_timedate:TextView= itemView.findViewById(R.id.tv_transaction_time_date)
        val img_rupee:ImageView= itemView.findViewById(R.id.img_rupee)
        val receipt:TextView= itemView.findViewById(R.id.receipt_tv)
        val payment_milestone:TextView= itemView.findViewById(R.id.tv_payment_milestone)
        val total_amount:TextView= itemView.findViewById(R.id.tv_total_amount)
    }

}