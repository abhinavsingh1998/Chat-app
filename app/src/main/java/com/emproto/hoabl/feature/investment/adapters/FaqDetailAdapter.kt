package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FaqCategoryLayoutBinding
import com.emproto.hoabl.databinding.FaqInvestmentFragmentBinding
import com.emproto.hoabl.model.RecyclerViewFaqItem
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.networklayer.response.investment.CgData
import com.google.android.material.textview.MaterialTextView

class FaqDetailAdapter(
    private val context: Context,
    private val list: List<RecyclerViewFaqItem>,
    private val faqData: List<CgData>,
    private val faqId: Int?,
    private val itemClickListener: ItemClickListener
):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }

    private lateinit var categoryAdapter:PopularCategoryAdapter
    private lateinit var faqAdapter: FaqAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            VIEW_TYPE_ONE -> {
                TopViewHolder(
                        LayoutInflater.from(parent.context).inflate(R.layout.faq_investment_fragment,parent,false)
                )
            }
            else -> {
                CategoryViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.faq_category_layout,parent,false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(list[position].viewType) {
            VIEW_TYPE_ONE -> {
                (holder as TopViewHolder).bind(position)
            }
            VIEW_TYPE_TWO -> {
                (holder as CategoryViewHolder).bind(position,list[position].data)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

    private inner class TopViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val rvPopCategory = itemView.findViewById<RecyclerView>(R.id.rv_pop_category)
        fun bind(position: Int){
            val list = arrayListOf<String>()
            for(item in faqData){
                list.add(item.name)
            }
            categoryAdapter = PopularCategoryAdapter(context,list,itemClickListener)
            rvPopCategory.adapter = categoryAdapter
        }
    }

    inner class CategoryViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val tvCategoryTitle = itemView.findViewById<MaterialTextView>(R.id.tv_category_title)
        val rvFaq = itemView.findViewById<RecyclerView>(R.id.rv_faq)
        fun bind(position: Int,data:CgData){
            tvCategoryTitle.text = data.name
            faqAdapter = FaqAdapter(data.faqs,context,faqId)
            rvFaq.adapter = faqAdapter
        }
    }

}