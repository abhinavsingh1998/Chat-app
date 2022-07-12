package com.emproto.hoabl.feature.profile.adapter.accounts



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemAccountsPaymentBinding
import com.emproto.networklayer.response.profile.AccountsResponse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AllPaymentHistoryAdapter(
    private var mContext: Context?,
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
            accountsPaymentList: ArrayList<AccountsResponse.Data.PaymentHistory>,
            view: View,
            position: Int,
            name: String,
            path: String,
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(!accountsPaymentList[position].paidAmount.toString().isNullOrEmpty()){
            holder.tvPaidAmount.text = "₹" + accountsPaymentList[position].paidAmount.toString()
        }
        if(!accountsPaymentList[position].launchName.isNullOrEmpty()){
            holder.tvProjectName.text = accountsPaymentList[position].launchName
        }
        if (!accountsPaymentList[position].paymentDate.isNullOrEmpty()) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val outputFormat = SimpleDateFormat("dd-MM-yyyy")
            val date: Date = inputFormat.parse(accountsPaymentList[position].paymentDate)
            val formattedDate: String = outputFormat.format(date)
            holder.tvPaymentDate.text = formattedDate
        }
        if (accountsPaymentList[position].crmInventory!=null) {
            holder.tvLandId.text = "Land id:" + "" + accountsPaymentList[position].crmInventory
        }
        holder.tvSeeReceipt.setOnClickListener {
            if (accountsPaymentList[position].document==null) {
                Toast.makeText(mContext, "No Receipt", Toast.LENGTH_SHORT).show()
            } else {
                mListener.onAccountsAllPaymentItemClick(
                    accountsPaymentList,
                    it,
                    position,
                    accountsPaymentList[position].document.name,
                    accountsPaymentList[position].document.path)
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
