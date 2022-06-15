package com.emproto.hoabl.feature.portfolio.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemAccountsPaymentBinding
import com.emproto.networklayer.response.profile.AccountsResponse

class ReceiptListAdapter(
    private var mContext: Context?,
    private var accountsPaymentList: ArrayList<String>,
    private var mListener: OnPaymentItemClickListener

) : RecyclerView.Adapter<ReceiptListAdapter.ViewHolder>() {

    lateinit var binding: ItemAccountsPaymentBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemAccountsPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    interface OnPaymentItemClickListener {
        fun onAccountsPaymentItemClick(
            accountsPaymentList: String,
            view: View,
            position: Int
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.tvPaidAmount.text = accountsPaymentList[position].paidAmount.toString()
//        holder.tvProjectName.text = accountsPaymentList[position].projectName
//        holder.tvPaymentDate.text=accountsPaymentList[position].paymentDate.substring(0,10)

        holder.tvSeeReceipt.setOnClickListener {
            mListener.onAccountsPaymentItemClick("", it, position)
        }
    }

    override fun getItemCount(): Int {
        return 5
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvProjectName: TextView = itemView.findViewById(R.id.tvProjectName)
        val tvPaidAmount: TextView = itemView.findViewById(R.id.tvPaidAmount)
        val tvPaymentMilestone: TextView = itemView.findViewById(R.id.tvPaymentMilestone)
        val tvPaymentDate: TextView = itemView.findViewById(R.id.tvPaymentDate)
        val tvSeeReceipt: TextView = itemView.findViewById(R.id.tvSeeReceipt)

    }
}