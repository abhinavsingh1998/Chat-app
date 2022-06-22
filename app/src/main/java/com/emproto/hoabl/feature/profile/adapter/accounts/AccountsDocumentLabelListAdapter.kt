package com.emproto.hoabl.feature.profile.adapter.accounts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemAccountsKycDocBinding
import com.emproto.networklayer.response.profile.AccountsResponse

class AccountsDocumentLabelListAdapter(
    private var mContext: Context?,
    private var accountsDocumentList: ArrayList<AccountsResponse.Data.Document>,
    private var mListener: OnDocumentLabelItemClickListener

) : RecyclerView.Adapter<AccountsDocumentLabelListAdapter.ViewHolder>() {

    lateinit var binding: ItemAccountsKycDocBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemAccountsKycDocBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    interface OnDocumentLabelItemClickListener {
        fun onAccountsDocumentLabelItemClick(
            accountsDocumentList: ArrayList<AccountsResponse.Data.Document>,
            view: View,
            position: Int
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(accountsDocumentList[position].documentCategory!="KYC"&& !(accountsDocumentList[position].documentCategory.isNullOrEmpty())){
            holder.tvDocName.text = accountsDocumentList[position].documentType.toString()
        }

        holder.tvViewDoc.setOnClickListener {
            mListener.onAccountsDocumentLabelItemClick(accountsDocumentList, it, position)
        }
    }

    override fun getItemCount(): Int {
        return 2
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvDocName: TextView = itemView.findViewById(R.id.tvDocName)
        val tvViewDoc: TextView = itemView.findViewById(R.id.tvViewDoc)
    }
}