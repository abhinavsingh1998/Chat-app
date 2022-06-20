package com.emproto.hoabl.feature.profile.adapter.accounts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemAccountsKycDocBinding
import com.emproto.hoabl.databinding.ItemPortfolioDocumentsBinding
import com.emproto.networklayer.response.profile.AccountsResponse

class AllDocumentAdapter(
        private var mContext: Context?,
        private var accountsDocumentList: ArrayList<AccountsResponse.Data.Document>,
        private var mListener: OnDocumentLabelClickListener

) : RecyclerView.Adapter<AllDocumentAdapter.ViewHolder>() {

    lateinit var binding: ItemPortfolioDocumentsBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemPortfolioDocumentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    interface OnDocumentLabelClickListener {
        fun onAccountsDocumentLabelClick(
            accountsDocumentList: ArrayList<AccountsResponse.Data.Document>,
            view: View,
            position: Int
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (accountsDocumentList[position].documentCategory != "KYC") {
            holder.tvDocName.text = accountsDocumentList[position].documentType.toString()
        }

        holder.tvViewDoc.setOnClickListener {
            mListener.onAccountsDocumentLabelClick(accountsDocumentList, it, position)
        }

    }

    override fun getItemCount(): Int {
        return accountsDocumentList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvDocName: TextView = itemView.findViewById(R.id.tv_doc_label)
        val tvViewDoc: TextView = itemView.findViewById(R.id.tv_view)
    }
}