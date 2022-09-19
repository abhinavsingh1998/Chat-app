package com.emproto.hoabl.feature.portfolio.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemAttentionBinding
import com.emproto.networklayer.response.portfolio.ivdetails.PaymentSchedulesItem

class PortfolioSpecificViewPagerAdapter(
    private val imageList: List<PaymentSchedulesItem>,
    private val cardInterface: PendingCardInterface
) :
    RecyclerView.Adapter<PortfolioSpecificViewPagerAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: ItemAttentionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemAttentionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = imageList[position]
        holder.binding.pendingKyc.text = item.paymentMilestone
        "Your payment is due. Please pay on time to maintain healthy relation.".also { holder.binding.uploadKycStatement.text = it }
        holder.binding.actionNavigate.setOnClickListener {
            cardInterface.onclickCard()
        }
    }

    override fun getItemCount(): Int = imageList.size

    interface PendingCardInterface {
        fun onclickCard()
    }

}