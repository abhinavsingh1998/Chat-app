package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.core.text.color
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.Constants
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.CompletePaymentCardBinding
import com.emproto.hoabl.utils.Extensions.makeClickable
import com.emproto.hoabl.utils.Extensions.makeLinks
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.response.actionItem.Data


class PendingPaymentsAdapter(
    val context: Context,
    val list: List<Data>?,
    val itemIntrface: ItemClickListener
) : RecyclerView.Adapter<PendingPaymentsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PendingPaymentsAdapter.MyViewHolder {
        val view =
            CompletePaymentCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list!![holder.adapterPosition]

        holder.binding.kycCard.setOnClickListener(View.OnClickListener {
            itemIntrface.onItemClicked(it, position, holder.itemId.toString())
        })

        holder.binding.apply {
            actionRequired.text = item!!.cardTitle

            pendingKyc.text = item!!.displayTitle

            uploadKycStatement.text = SpannableStringBuilder()
                .append(item!!.displayText+" ")
                .color(Color.parseColor("#676ac0")){bold{append(Constants.CLICKTOKNOWMORE)}
                }

            uploadKycStatement.makeClickable(
                Pair("Click to know more", View.OnClickListener {
                    itemIntrface.onItemClicked(it, position, holder.itemId.toString())
                }),
            )

            seeAllPendingPayment.setOnClickListener(View.OnClickListener {
                itemIntrface.onItemClicked(it, position, holder.itemId.toString())
            })


        }

    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class MyViewHolder(val binding: CompletePaymentCardBinding) :
        RecyclerView.ViewHolder(binding.root)

}