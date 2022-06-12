package com.emproto.hoabl.feature.home.views.fragments

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemFaqBinding
import com.emproto.hoabl.feature.portfolio.adapters.PortfolioSpecificViewAdapter
import com.emproto.networklayer.response.portfolio.ivdetails.ProjectContentsAndFaq

class SearchFaqAdapter(
    val context: Context,
    private val list: List<ProjectContentsAndFaq>,
    val ivInterface: PortfolioSpecificViewAdapter.InvestmentScreenInterface
) :
    RecyclerView.Adapter<SearchFaqAdapter.FaqViewHolder>() {
    inner class FaqViewHolder(var binding: ItemFaqBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val view = ItemFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FaqViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val faqItem = list[position]
        holder.binding.ivFaqCardDropDown.setOnClickListener {
            //move to another screen flow.
            holder.binding.ivFaqCardDropDown.visibility = View.INVISIBLE
            holder.binding.tvFaqAnswer.visibility = View.VISIBLE
            holder.binding.ivFaqCardUpArrow.visibility = View.VISIBLE
        }
        holder.binding.ivFaqCardUpArrow.setOnClickListener {
            holder.binding.ivFaqCardDropDown.visibility = View.VISIBLE
            holder.binding.tvFaqAnswer.visibility = View.GONE
            holder.binding.ivFaqCardUpArrow.visibility = View.GONE
        }
        holder.binding.tvFaqQuestion.text = faqItem.frequentlyAskedQuestion.faqQuestion.question
        holder.binding.tvFaqAnswer.text = faqItem.frequentlyAskedQuestion.faqAnswer.answer
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            holder.binding.ivFaqCardDropDown.setImageDrawable(context.getDrawable(R.drawable.rightarrow))
//        }
        holder.binding.cvFaqCard.setOnClickListener {
            ivInterface.readAllFaq(position, faqItem.faqId)
        }

    }

    override fun getItemCount(): Int = 2
}