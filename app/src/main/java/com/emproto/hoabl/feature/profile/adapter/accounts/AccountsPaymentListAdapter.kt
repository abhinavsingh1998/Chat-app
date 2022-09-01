package com.emproto.hoabl.feature.profile.adapter.accounts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.AccNoDataBinding
import com.emproto.hoabl.databinding.ItemAccountsPaymentBinding
import com.emproto.networklayer.response.profile.AccountsResponse
import java.text.SimpleDateFormat
import java.util.*

class AccountsPaymentListAdapter(
    private var mContext: Context?,
    private var accountsPaymentList: ArrayList<AccountsResponse.Data.PaymentHistory>,
    private var mListener: OnPaymentItemClickListener,
    private  var type: String

) : RecyclerView.Adapter<AccountsPaymentListAdapter.BaseViewHolder>() {

    lateinit var outputFormat: SimpleDateFormat
    lateinit var binding: ItemAccountsPaymentBinding
    lateinit var bindingEmpty: AccNoDataBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == R.layout.item_accounts_payment) {
            binding =
                ItemAccountsPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        if (type == "not") {
            return R.layout.item_accounts_payment
        }
        else {
            return R.layout.acc_no_data
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
        var tvMsg = itemView.findViewById<TextView>(R.id.tvMsg)
        var ivIcon = itemView.findViewById<ImageView>(R.id.ivIcon)

    }
    abstract class BaseViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {}

    interface OnPaymentItemClickListener {
        fun onAccountsPaymentItemClick(
            accountsPaymentList: ArrayList<AccountsResponse.Data.PaymentHistory>,
            view: View,
            position: Int,
            name: String,
            path: String,
        )
    }
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if(holder is NotEmptyList) {
            if (!accountsPaymentList[position].paidAmount.toString().isNullOrEmpty()) {
                holder.tvPaidAmount.text = "₹" + accountsPaymentList[position].paidAmount.toString()
            }
            if (!accountsPaymentList[position].launchName.isNullOrEmpty()) {
                holder.tvProjectName.text = accountsPaymentList[position].launchName
            }
            if (!accountsPaymentList[position].paymentDate.isNullOrEmpty()) {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
//                val outputFormat = SimpleDateFormat("dd MMMM yyyy")
                if (accountsPaymentList[position].paymentDate.endsWith("1") && !accountsPaymentList[position].paymentDate.endsWith("11"))
                    outputFormat =
                    SimpleDateFormat("d'st' MMMM yyyy") else if (accountsPaymentList[position].paymentDate.endsWith("2") && !accountsPaymentList[position].paymentDate.endsWith(
                        "12"
                    )
                ) outputFormat =
                    SimpleDateFormat("d'nd' MMMM  yyyy") else if (accountsPaymentList[position].paymentDate.endsWith("3") && !accountsPaymentList[position].paymentDate.endsWith(
                        "13"
                    )
                ) outputFormat = SimpleDateFormat("d'rd' MMMM yyyy") else outputFormat =
                    SimpleDateFormat("d'th' MMMM yyyy")
                val date: Date = inputFormat.parse(accountsPaymentList[position].paymentDate)
                val formattedDate: String = outputFormat.format(date)
                holder.tvPaymentDate.text = formattedDate
            }
            if (accountsPaymentList[position].crmInventory != null) {
                holder.tvLandId.text =
                    "Land id:" + "" + accountsPaymentList[position].crmInventory
            }

            holder.tvSeeReceipt.setOnClickListener {
                if (accountsPaymentList[position].document == null) {
                    Toast.makeText(mContext, "No Receipt", Toast.LENGTH_SHORT).show()
                } else {
                    mListener.onAccountsPaymentItemClick(
                        accountsPaymentList,
                        it,
                        position,
                        accountsPaymentList[position].document?.name.toString(),
                        accountsPaymentList[position].document?.path.toString()
                    )

                }

            }
        }else if(holder is EmptyList){
            "No Payment is done yet,See your payment history after making your first payment.".also { holder.tvMsg.text = it }
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