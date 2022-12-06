package com.emproto.hoabl.feature.profile.adapter.accounts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.AccNoDataBinding
import com.emproto.hoabl.databinding.ItemAccountsPaymentBinding
import com.emproto.networklayer.response.profile.AccountsResponse

class AccountsPaymentListAdapter(
    private var accountsPaymentList: ArrayList<AccountsResponse.Data.PaymentHistory>,
    private var mListener: OnPaymentItemClickListener,
    private var type: String

) : RecyclerView.Adapter<AccountsPaymentListAdapter.BaseViewHolder>() {

    lateinit var binding: ItemAccountsPaymentBinding
    private lateinit var bindingEmpty: AccNoDataBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == R.layout.item_accounts_payment) {
            binding =
                ItemAccountsPaymentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return NotEmptyList(binding.root)
        } else {
            bindingEmpty =
                AccNoDataBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

            return EmptyList(bindingEmpty.root)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (type == "not") {
            R.layout.item_accounts_payment
        } else {
            R.layout.acc_no_data
        }
    }

    class NotEmptyList(ItemView: View) : AccountsPaymentListAdapter.BaseViewHolder(ItemView) {
        val tvProjectName: TextView = itemView.findViewById(R.id.tvProjectName)
        val tvPaidAmount: TextView = itemView.findViewById(R.id.tvPaidAmount)
        val tvPaymentDate: TextView = itemView.findViewById(R.id.tvPaymentDate)
        val tvSeeReceipt: TextView = itemView.findViewById(R.id.tvSeeReceipt)
        val tvLandId: TextView = itemView.findViewById(R.id.tvLandId)
    }

    class EmptyList(ItemView: View) : AccountsPaymentListAdapter.BaseViewHolder(ItemView) {
        var tvMsg: TextView = itemView.findViewById(R.id.tvMsg)
        var ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)

    }

    abstract class BaseViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView)

    interface OnPaymentItemClickListener {
        fun onAccountsPaymentItemClick(
            view: View,
            bookingId: String
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder is NotEmptyList) {
            if (accountsPaymentList[position].paidAmount.toString().isNotEmpty()) {
                "₹${accountsPaymentList[position].paidAmount}".also {
                    holder.tvPaidAmount.text = it
                }
            }

            if (accountsPaymentList[position].launchName.isNotEmpty()) {
                holder.tvProjectName.text = accountsPaymentList[position].launchName
            }
            if (accountsPaymentList[position].crmInventory != null) {
                "Land id:${accountsPaymentList[position].crmInventory}".also {
                    holder.tvLandId.text = it
                }
            }
            if (!(accountsPaymentList[position]?.paymentDate.isNullOrEmpty())) {
                val paymentDate = Utility.dateInWords(accountsPaymentList[position].paymentDate)
                holder.tvPaymentDate.text = paymentDate
            } else {
                holder.tvPaymentDate.text = ""
            }


            holder.tvSeeReceipt.setOnClickListener {
                    mListener.onAccountsPaymentItemClick(
                        it,
                        accountsPaymentList[position].crmBookingId
                    )
            }
        } else if (holder is EmptyList) {
            "No Payment is done yet,See your payment history after making your first payment.".also {
                holder.tvMsg.text = it
            }
            holder.ivIcon.setImageResource(R.drawable.payment)

        }
    }

    override fun getItemCount(): Int {
        return if (accountsPaymentList.size < 4) {
            accountsPaymentList.size
        } else {
            return 4

        }
    }


}