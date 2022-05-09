package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.AccountItemKycBinding

class DocumentKycAdapter(val context: Context, private val list: List<String>) : RecyclerView.Adapter<DocumentKycAdapter.DocumentHolder>() {

    private lateinit var onItemClickListener: View.OnClickListener

    inner class DocumentHolder(var binding: AccountItemKycBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentHolder {
        val view = AccountItemKycBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DocumentHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentHolder, position: Int) {
    }
    override fun getItemCount(): Int = list.size

    fun setItemClickListener(clickListener: View.OnClickListener) {
        onItemClickListener = clickListener
    }
}