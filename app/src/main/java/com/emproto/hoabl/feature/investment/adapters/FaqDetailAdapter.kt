package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.FaqCategoryLayoutBinding
import com.emproto.hoabl.databinding.FaqInvestmentFragmentBinding
import com.emproto.hoabl.model.RecyclerViewFaqItem
import com.emproto.networklayer.response.investment.CgData
import com.emproto.networklayer.response.investment.Faq

class FaqDetailAdapter(private val context: Context, private val list: List<RecyclerViewFaqItem>,private val faqData:List<CgData>):
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
                    FaqInvestmentFragmentBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                CategoryViewHolder(
                    FaqCategoryLayoutBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
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

    private inner class TopViewHolder(private val binding: FaqInvestmentFragmentBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val list = arrayListOf<String>()
            for(item in faqData){
                list.add(item.name)
            }
            categoryAdapter = PopularCategoryAdapter(context,list)
            binding.rvPopCategory.adapter = categoryAdapter
        }
    }

    private inner class CategoryViewHolder(private val binding: FaqCategoryLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int,data:CgData){
            binding.tvCategoryTitle.text = data.name
            faqAdapter = FaqAdapter(data.faqs,context)
            binding.rvFaq.adapter = faqAdapter
        }
    }

}