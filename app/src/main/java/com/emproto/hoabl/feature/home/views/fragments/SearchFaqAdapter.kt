package com.emproto.hoabl.feature.home.views.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemSearchFaqBinding
import com.emproto.hoabl.feature.portfolio.adapters.PortfolioSpecificViewAdapter
import com.emproto.networklayer.response.portfolio.ivdetails.ProjectContentsAndFaq

class SearchFaqAdapter(
    val context: Context,
    private val list: List<ProjectContentsAndFaq>,
    val ivInterface: PortfolioSpecificViewAdapter.InvestmentScreenInterface?
) :
    RecyclerView.Adapter<SearchFaqAdapter.FaqViewHolder>() {
    inner class FaqViewHolder(var binding: ItemSearchFaqBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val view = ItemSearchFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FaqViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val faqItem = list[position]
        holder.binding.ivArrowDown.setOnClickListener {
            //move to another screen flow.
            holder.binding.ivArrowDown.visibility = View.INVISIBLE
            holder.binding.tvFaqAnswer.visibility = View.VISIBLE
            holder.binding.ivArrowUp.visibility = View.VISIBLE
            holder.binding.viewLine.visibility = View.VISIBLE
        }
        holder.binding.ivArrowUp.setOnClickListener {
            holder.binding.ivArrowDown.visibility = View.VISIBLE
            holder.binding.tvFaqAnswer.visibility = View.GONE
            holder.binding.ivArrowUp.visibility = View.GONE
            holder.binding.viewLine.visibility = View.GONE
        }
        holder.binding.tvFaqQuestion.text = faqItem.frequentlyAskedQuestion.faqQuestion.question
        holder.binding.tvFaqAnswer.text = faqItem.frequentlyAskedQuestion.faqAnswer.answer
        holder.binding.cvFaqCard.setOnClickListener {
            ivInterface?.readAllFaq(position, faqItem.faqId)
        }

    }

    override fun getItemCount(): Int = list.size
}