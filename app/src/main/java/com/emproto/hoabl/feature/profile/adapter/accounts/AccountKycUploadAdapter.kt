package com.emproto.hoabl.feature.profile.adapter.accounts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemAccountsKycDocBinding
import com.emproto.hoabl.databinding.ItemAccountsKycDocUploadBinding
import com.emproto.networklayer.response.profile.AccountsResponse

class AccountKycUploadAdapter(
    private var mContext: Context?,
    private var newList: ArrayList<String>,
//    private var mListener: OnKycItemClickListener

) : RecyclerView.Adapter<AccountKycUploadAdapter.ViewHolder>() {

    lateinit var binding: ItemAccountsKycDocUploadBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemAccountsKycDocUploadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }
//
//    interface OnKycItemClickListener {
//        fun onAccountsKycItemClick(
//            accountsDocumentList: ArrayList<AccountsResponse.Data.Document>,
//            view: View,
//            position: Int
//        )
//    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvDocName.text = newList[position]

//        holder.tvViewDoc.setOnClickListener {
//            mListener.onAccountsKycItemClick(accountsKycList, it, position)
//        }
    }

    override fun getItemCount(): Int {
        return newList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvDocName: TextView = itemView.findViewById(R.id.tvDocName)
        val tvViewDoc: TextView = itemView.findViewById(R.id.tvViewDoc)
    }
}