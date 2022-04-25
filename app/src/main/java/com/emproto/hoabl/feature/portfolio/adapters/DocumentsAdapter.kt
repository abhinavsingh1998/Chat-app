package com.emproto.hoabl.feature.portfolio.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemDocumentsBinding
import com.emproto.hoabl.databinding.ItemLandSkusBinding
import com.emproto.hoabl.databinding.ItemPortfolioDocumentsBinding
import com.emproto.hoabl.feature.investment.adapters.SkusListAdapter

class DocumentsAdapter (private val list:List<String>): RecyclerView.Adapter<DocumentsAdapter.DocumentsViewHolder>() {

    private lateinit var onItemClickListener : View.OnClickListener

    inner class DocumentsViewHolder(var binding: ItemPortfolioDocumentsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentsViewHolder {
        val view = ItemPortfolioDocumentsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DocumentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentsViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = list.size

    fun setSkusListItemClickListener(clickListener: View.OnClickListener) {
        onItemClickListener = clickListener
    }

}