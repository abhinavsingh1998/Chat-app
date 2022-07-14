package com.emproto.hoabl.feature.portfolio.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemFaqBinding
import com.emproto.networklayer.response.portfolio.ivdetails.ProjectContentsAndFaq

class ProjectFaqAdapter(
    val context: Context,
    private val list: List<ProjectContentsAndFaq>,
    val ivInterface: PortfolioSpecificViewAdapter.InvestmentScreenInterface
) :
    RecyclerView.Adapter<ProjectFaqAdapter.FaqViewHolder>() {
    inner class FaqViewHolder(var binding: ItemFaqBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val view = ItemFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FaqViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val faqItem = list[position]
        holder.binding.ivArrowDown.setOnClickListener {
            //move to another screen flow.
//            holder.binding.ivFaqCardDropDown.visibility = View.INVISIBLE
//            holder.binding.tvFaqAnswer.visibility = View.VISIBLE
//            holder.binding.ivFaqCardUpArrow.visibility = View.VISIBLE
        }
        holder.binding.ivArrowUp.setOnClickListener {
            holder.binding.ivArrowDown.visibility = View.VISIBLE
            holder.binding.tvFaqAnswer.visibility = View.GONE
            holder.binding.ivArrowUp.visibility = View.GONE
        }
        holder.binding.tvFaqQuestion.text = faqItem.frequentlyAskedQuestion.faqQuestion.question
        holder.binding.tvFaqAnswer.text = faqItem.frequentlyAskedQuestion.faqAnswer.answer
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.binding.ivArrowDown.setImageDrawable(context.getDrawable(R.drawable.rightarrow))
        }
        holder.binding.cvFaqCard.setOnClickListener {
            ivInterface.readAllFaq(position, faqItem.faqId)
        }

    }

    override fun getItemCount(): Int = list.size
}