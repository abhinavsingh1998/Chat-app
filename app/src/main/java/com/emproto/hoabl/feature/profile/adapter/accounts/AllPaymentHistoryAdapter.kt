package com.emproto.hoabl.feature.profile.adapter.accounts


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemAccountsPaymentBinding
import com.emproto.networklayer.response.profile.AccountsResponse

class AllPaymentHistoryAdapter(
    private var accountsPaymentList: ArrayList<AccountsResponse.Data.PaymentHistory>,
    private var mListener: OnAllPaymentItemClickListener
) : RecyclerView.Adapter<AllPaymentHistoryAdapter.ViewHolder>() {

    lateinit var binding: ItemAccountsPaymentBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemAccountsPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    interface OnAllPaymentItemClickListener {
        fun onAccountsAllPaymentItemClick(
            view: View,
            bookingId: String
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (accountsPaymentList[position].paidAmount.toString().isNotEmpty()) {
            "₹${accountsPaymentList[position].paidAmount}".also { holder.tvPaidAmount.text = it }
        }
        if (accountsPaymentList[position].launchName.isNotEmpty()) {
            holder.tvProjectName.text = accountsPaymentList[position].launchName
        }
        if (accountsPaymentList[position].crmInventory != null) {
            "Land id:${accountsPaymentList[position].crmInventory}".also {
                holder.tvLandId.text = it
            }
        }
        if (!(accountsPaymentList[position].paymentDate.isNullOrEmpty())) {
            val paymentDate = Utility.dateInWords(accountsPaymentList[position].paymentDate)
            holder.tvPaymentDate.text = paymentDate
        } else {
            holder.tvPaymentDate.text = ""
        }
        holder.tvSeeReceipt.setOnClickListener {
                mListener.onAccountsAllPaymentItemClick(
                    it,
                    accountsPaymentList[position].crmBookingId
                )
        }

    }

    override fun getItemCount(): Int {
        return accountsPaymentList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvProjectName: TextView = itemView.findViewById(R.id.tvProjectName)
        val tvPaidAmount: TextView = itemView.findViewById(R.id.tvPaidAmount)
        val tvPaymentDate: TextView = itemView.findViewById(R.id.tvPaymentDate)
        val tvSeeReceipt: TextView = itemView.findViewById(R.id.tvSeeReceipt)
        val tvLandId: TextView = itemView.findViewById(R.id.tvLandId)
    }
}
