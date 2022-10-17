package com.emproto.hoabl.feature.profile.adapter.accounts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemPortfolioDocumentsBinding
import com.emproto.networklayer.response.profile.AccountsResponse

class AllReceiptsList(
    private var mContext: Context?,
    private var paymentReceiptList: ArrayList<AccountsResponse.Data.PaymentReceipt>,
    private var mListener: OnAllDocumentLabelClickListener

) : RecyclerView.Adapter<AllReceiptsList.ViewHolder>() {

    lateinit var binding: ItemPortfolioDocumentsBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemPortfolioDocumentsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding.root)
    }

    interface OnAllDocumentLabelClickListener {
        fun onViewClick(
            paymentReceiptList: ArrayList<AccountsResponse.Data.PaymentReceipt>,
            view: View,
            position: Int,
            name: String,
            path: String?
        )


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvDocName.text = paymentReceiptList[position].name
        holder.tvViewDoc.setOnClickListener {
            if (paymentReceiptList[position].path == null) {
                Toast.makeText(mContext, "No Document available", Toast.LENGTH_SHORT).show()

            } else {
                mListener.onViewClick(
                    paymentReceiptList,
                    it,
                    position,
                    paymentReceiptList[position].name!!,
                    paymentReceiptList[position].path
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return paymentReceiptList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvDocName: TextView = itemView.findViewById(R.id.tv_doc_label)
        val tvViewDoc: TextView = itemView.findViewById(R.id.tv_view)
    }
}