package com.emproto.hoabl.feature.portfolio.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemPortfolioDocumentsBinding
import com.emproto.networklayer.response.documents.Data

class DocumentsAdapter(
    private val list: List<Data>,
    private val docImageVisibility: Boolean,
    val ivInterface: DocumentInterface
) : RecyclerView.Adapter<DocumentsAdapter.DocumentsViewHolder>() {

    inner class DocumentsViewHolder(var binding: ItemPortfolioDocumentsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentsViewHolder {
        val view = ItemPortfolioDocumentsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DocumentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentsViewHolder, position: Int) {
        holder.binding.tvDocLabel.text = list[position].documentType
        when (docImageVisibility) {
            true -> holder.binding.ivDocsImage.visibility = View.VISIBLE
        }
        holder.binding.tvView.setOnClickListener {
            ivInterface.onclickDocument(position)
        }
    }

    override fun getItemCount(): Int = list.size


}

interface DocumentInterface {
    fun onclickDocument(position: Int)
}