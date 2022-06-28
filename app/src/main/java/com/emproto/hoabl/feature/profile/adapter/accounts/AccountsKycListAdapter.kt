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
import com.emproto.networklayer.response.profile.KycUpload

class AccountsKycListAdapter(
    private var mContext: Context?,
    private var accountsKycList: ArrayList<AccountsResponse.Data.Document>,
    private var mListener: OnKycItemClickListener

) : RecyclerView.Adapter<AccountsKycListAdapter.ViewHolder>() {
    val kycUploadList = ArrayList<KycUpload>()

    lateinit var binding: ItemAccountsKycDocBinding
    lateinit var kycUploadAdapter: AccountKycUploadAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemAccountsKycDocBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    interface OnKycItemClickListener {
        fun onAccountsKycItemClick(
            accountsDocumentList: ArrayList<AccountsResponse.Data.Document>,
            view: View,
            position: Int,
            name: String,
            path: String?
        )

        fun onUploadClickItem(
            accountsDocumentList: ArrayList<AccountsResponse.Data.Document>,
            view: View,
            position: Int
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(accountsKycList[position].documentType=="200100"){
            holder.tvDocName.text="PAN Card"
        }
        else if(accountsKycList[position].documentType=="200101"){
            holder.tvDocName.text="Address Proof"
        }
        if (accountsKycList[position].status == "Upload") {
            binding.tvDocName.text == accountsKycList[position].documentType
            holder.tvViewDoc.text = "Upload"
        }
        if (holder.tvViewDoc.text == "Upload") {
            holder.tvViewDoc.setOnClickListener {
                mListener.onUploadClickItem(
                    accountsKycList,
                    it,
                    position,

                    )
            }

        }

        when (accountsKycList[position].documentType) {
            "Unverified Address Proof" -> {
                binding.tvDocName.text = "Address Proof"
                binding.tvViewDoc.text = "Verification Pending"
                holder.tvViewDoc.isEnabled = false
            }
            "Unverified PAN Card" -> {
                binding.tvDocName.text = "PAN Card"
                binding.tvViewDoc.text = "Verification Pending"
                holder.tvViewDoc.isEnabled = false
            }


            else -> {
                binding.tvViewDoc.text = "View"
                holder.tvViewDoc.setOnClickListener {
                    if (accountsKycList[position] == null) {
                        Toast.makeText(mContext, "No Document available", Toast.LENGTH_SHORT).show()
                    } else {
                        mListener.onAccountsKycItemClick(
                            accountsKycList,
                            it,
                            position,
                            accountsKycList[position].name,
                            accountsKycList[position].path
                        )

                    }
                }

            }
        }


    }

    override fun getItemCount(): Int {
        return accountsKycList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvDocName: TextView = itemView.findViewById(R.id.tvDocName)
        val tvViewDoc: TextView = itemView.findViewById(R.id.tvViewDoc)
    }
}