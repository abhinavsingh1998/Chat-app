package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.CompletePaymentCardBinding


class PendingPaymentsAdapter(context: Context, list: List<String>) : RecyclerView.Adapter<PendingPaymentsAdapter.MyViewHolder>() {

    var list: List<String>
    var mcontext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding:CompletePaymentCardBinding =
            CompletePaymentCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(binding: CompletePaymentCardBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: CompletePaymentCardBinding

        init {
            this.binding = binding
        }
    }

    init {
        this.list = list
        this.mcontext=context
    }
}