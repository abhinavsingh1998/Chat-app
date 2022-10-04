package com.emproto.hoabl.feature.profile.adapter.accounts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.AccNoDataBinding
import com.emproto.hoabl.databinding.ItemAccountsKycDocBinding
import com.emproto.networklayer.response.profile.AccountsResponse

class AccountsDocumentLabelListAdapter(
    private var mContext: Context?,
    private var accountsDocumentList: ArrayList<AccountsResponse.Data.Document>,
    private var mListener: OnDocumentLabelItemClickListener,
    private var type: String

) : RecyclerView.Adapter<AccountsDocumentLabelListAdapter.BaseViewHolder>() {

    lateinit var binding: ItemAccountsKycDocBinding
    private lateinit var bindingEmpty: AccNoDataBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        when (viewType) {
            R.layout.item_accounts_kyc_doc -> {
                binding =
                    ItemAccountsKycDocBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return NotEmptyList(binding.root)
            }
            else -> {
                bindingEmpty =
                    AccNoDataBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return EmptyList(bindingEmpty.root)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (type == "not") {
            return R.layout.item_accounts_kyc_doc
        } else {
            return R.layout.acc_no_data
        }
    }

    class NotEmptyList(ItemView: View) : AccountsDocumentLabelListAdapter.BaseViewHolder(ItemView) {
        val tvDocName: TextView = itemView.findViewById(R.id.tvDocName)
        val tvViewDoc: TextView = itemView.findViewById(R.id.tvViewDoc)
    }

    class EmptyList(ItemView: View) : AccountsDocumentLabelListAdapter.BaseViewHolder(ItemView) {
        var tvMsg = itemView.findViewById<TextView>(R.id.tvMsg)!!
    }

    abstract class BaseViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {}

    interface OnDocumentLabelItemClickListener {
        fun onAccountsDocumentLabelItemClick(
            accountsDocumentList: ArrayList<AccountsResponse.Data.Document>,
            view: View,
            position: Int,
            name: String,
            path: String?
        )
    }

    override fun getItemCount(): Int {
        return if (accountsDocumentList.size < 2) {
            accountsDocumentList.size
        } else {
            return 2
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder is NotEmptyList) {
            holder.tvDocName.text = accountsDocumentList[position].name
            holder.tvViewDoc.setOnClickListener {
                if (accountsDocumentList[position].path == null) {
                    Toast.makeText(mContext, "No Document available", Toast.LENGTH_SHORT).show()
                } else {
                    mListener.onAccountsDocumentLabelItemClick(
                        accountsDocumentList,
                        it,
                        position,
                        accountsDocumentList[position].name!!,
                        accountsDocumentList[position].path
                    )
                }
            }
        } else if (holder is EmptyList) {
            "The Uploaded Documents will be shown here.You can view them after uploading.".also {
                holder.tvMsg.text = it
            }
        }
    }
}