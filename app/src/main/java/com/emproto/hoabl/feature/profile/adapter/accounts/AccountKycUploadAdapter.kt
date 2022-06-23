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
    private var mListener: OnKycItemUploadClickListener

) : RecyclerView.Adapter<AccountKycUploadAdapter.ViewHolder>() {

    lateinit var binding: ItemAccountsKycDocUploadBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemAccountsKycDocUploadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    interface OnKycItemUploadClickListener {
        fun onUploadClick(
            newList: ArrayList<String>,
            view: View,
            position: Int
        )
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvDocName.text = newList[position]
        holder.tvUploadDoc.setOnClickListener {
            mListener.onUploadClick(newList, it, position)
        }
    }

    override fun getItemCount(): Int {
        return newList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvDocName: TextView = itemView.findViewById(R.id.tvDocName)
        val tvUploadDoc: TextView = itemView.findViewById(R.id.tvUploadDoc)
    }
}