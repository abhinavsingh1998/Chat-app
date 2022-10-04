package com.emproto.hoabl.feature.portfolio.adapters

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemPaymentReceiptBinding
import com.emproto.networklayer.response.bookingjourney.PaymentHistory

class ReceiptListAdapter(
    private var mContext: Context,
    private var accountsPaymentList: List<PaymentHistory>,
    private var mListener: OnPaymentItemClickListener

) : RecyclerView.Adapter<ReceiptListAdapter.ViewHolder>() {

    lateinit var binding: ItemPaymentReceiptBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemPaymentReceiptBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    interface OnPaymentItemClickListener {
        fun onAccountsPaymentItemClick(
            path: String
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            tvPaidAmount.text = "₹${Utility.convertTo(accountsPaymentList[position].paidAmount)}"
            if (!accountsPaymentList[position].paymentDate.isNullOrEmpty()) {
                tvPaymentDate.text =
                    Utility.parseDateFromUtc(accountsPaymentList[position].paymentDate)
            } else {
                tvPaymentDate.text = ""
            }

            tvSeeReceipt.text = showHTMLText(
                String.format(
                    mContext.getString(R.string.tv_receipt),
                    "See Receipt"
                )
            )
            tvSeeReceipt.setOnClickListener {
                if (accountsPaymentList[position].document != null &&
                    accountsPaymentList[position].document.path != null
                )
                    mListener.onAccountsPaymentItemClick(accountsPaymentList[position].document.path)
            }
        }
    }

    override fun getItemCount(): Int {
        return accountsPaymentList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvProjectName: TextView = itemView.findViewById(R.id.tvProjectName)
        val tvPaidAmount: TextView = itemView.findViewById(R.id.tvPaidAmount)
        val tvPaymentMilestone: TextView = itemView.findViewById(R.id.tvPaymentMilestone)
        val tvPaymentDate: TextView = itemView.findViewById(R.id.tvPaymentDate)
        val tvSeeReceipt: TextView = itemView.findViewById(R.id.tvSeeReceipt)

    }

    fun showHTMLText(message: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(message)
        }
    }
}