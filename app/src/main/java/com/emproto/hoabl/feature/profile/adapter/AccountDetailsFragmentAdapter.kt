package com.emproto.hoabl.feature.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.AccountDetailsViewBinding
import com.emproto.hoabl.feature.profile.data.AccountDetailsData

class AccountDetailsFragmentAdapter(
    private val accountdetailsList: ArrayList<AccountDetailsData>
) : RecyclerView.Adapter<AccountDetailsFragmentAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = AccountDetailsViewBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return accountdetailsList.size
    }
    class MyViewHolder(var binding: AccountDetailsViewBinding) :
        RecyclerView.ViewHolder(binding.root)

}