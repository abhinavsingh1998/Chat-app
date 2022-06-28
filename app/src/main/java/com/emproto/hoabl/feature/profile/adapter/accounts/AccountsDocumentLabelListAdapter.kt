package com.emproto.hoabl.feature.profile.adapter.accounts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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
            position: Int,
            name: String,
            path: String?
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvDocName.text = accountsDocumentList[position].name


        holder.tvViewDoc.setOnClickListener {
            if (accountsDocumentList[position]==null) {
                Toast.makeText(mContext, "No Document available", Toast.LENGTH_SHORT).show()
            } else {
                mListener.onAccountsDocumentLabelItemClick(
                    accountsDocumentList,
                    it,
                    position,
                    accountsDocumentList[position].name,
                    accountsDocumentList[position].path
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return if (accountsDocumentList.size < 2) {
            accountsDocumentList.size
        } else {
            return 2
        }
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvDocName: TextView = itemView.findViewById(R.id.tvDocName)
        val tvViewDoc: TextView = itemView.findViewById(R.id.tvViewDoc)
    }
}