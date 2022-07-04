package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.CompletePaymentCardBinding
import com.emproto.hoabl.databinding.ItemLatestUpdatesBinding
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.response.HomeActionItemResponse
import com.emproto.networklayer.response.actionItem.Data


class PendingPaymentsAdapter(
    val context: Context,
    val list: List<com.emproto.networklayer.response.Data>?,
    val itemIntrface: ItemClickListener
) : RecyclerView.Adapter<PendingPaymentsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingPaymentsAdapter.MyViewHolder {
        val view = CompletePaymentCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list!!.get(holder.adapterPosition)

        holder.binding.apply{
            actionRequired.text= item!!.cardTitle

            pendingKyc.text= item!!.displayTitle
            uploadKycStatement.text= item!!.displayText

            seeAllPendingPayment.setOnClickListener(View.OnClickListener {
                itemIntrface.onItemClicked(it,position,holder.itemId.toString())
            })

        }

    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class MyViewHolder(val binding: CompletePaymentCardBinding) :
        RecyclerView.ViewHolder(binding.root)

}