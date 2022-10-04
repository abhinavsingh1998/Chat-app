package com.emproto.hoabl.feature.profile.adapter.accounts


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemAccountsPaymentBinding
import com.emproto.networklayer.response.profile.AccountsResponse
import java.text.SimpleDateFormat

class AllPaymentHistoryAdapter(
    private var mContext: Context?,
    private var accountsPaymentList: ArrayList<AccountsResponse.Data.PaymentHistory>,
    private var mListener: OnAllPaymentItemClickListener


) : RecyclerView.Adapter<AllPaymentHistoryAdapter.ViewHolder>() {

    lateinit var outputFormat: SimpleDateFormat
    lateinit var binding: ItemAccountsPaymentBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemAccountsPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    interface OnAllPaymentItemClickListener {
        fun onAccountsAllPaymentItemClick(
            accountsPaymentList: ArrayList<AccountsResponse.Data.PaymentHistory>,
            view: View,
            position: Int,
            name: String,
            path: String,
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (!accountsPaymentList[position].paidAmount.toString().isNullOrEmpty()) {
            "₹${accountsPaymentList[position].paidAmount}".also { holder.tvPaidAmount.text = it }
        }
        if (!accountsPaymentList[position].launchName.isNullOrEmpty()) {
            holder.tvProjectName.text = accountsPaymentList[position].launchName
        }
        if (accountsPaymentList[position].crmInventory != null) {
            "Land id:${accountsPaymentList[position].crmInventory}".also {
                holder.tvLandId.text = it
            }
        }
        if (!accountsPaymentList[position].paymentDate.isNullOrEmpty()) {
            val paymentDate = Utility.dateInWords(accountsPaymentList[position].paymentDate)
            holder.tvPaymentDate.text = paymentDate
        } else {
            holder.tvPaymentDate.text = ""
        }
        holder.tvSeeReceipt.setOnClickListener {
            if (accountsPaymentList[position].document == null) {
                Toast.makeText(
                    mContext,
                    "Connect with your relationship manager\nfor the receipt",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                mListener.onAccountsAllPaymentItemClick(
                    accountsPaymentList,
                    it,
                    position,
                    accountsPaymentList[position].document.name,
                    accountsPaymentList[position].document.path
                )
            }

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
