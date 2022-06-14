package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemFaqBinding
import com.emproto.hoabl.databinding.ItemInvFaqBinding
import com.emproto.networklayer.response.investment.CgData
import com.emproto.networklayer.response.investment.Faq

class FaqAdapter(private val list: List<Faq>, private val context:Context, private val faqId: Int?):RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {
    inner class FaqViewHolder(var binding: ItemInvFaqBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val view = ItemInvFaqBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FaqViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val element = list[position]
        holder.binding.ivFaqCardDropDown.setOnClickListener{
            holder.binding.ivFaqCardDropDown.visibility = View.INVISIBLE
            holder.binding.tvFaqAnswer.visibility = View.VISIBLE
            holder.binding.ivFaqCardUpArrow.visibility = View.VISIBLE
//            holder.binding.viewLine.visibility = View.VISIBLE
        }
        holder.binding.ivFaqCardUpArrow.setOnClickListener{
            holder.binding.ivFaqCardDropDown.visibility = View.VISIBLE
            holder.binding.tvFaqAnswer.visibility = View.GONE
            holder.binding.ivFaqCardUpArrow.visibility = View.GONE
//            holder.binding.viewLine.visibility = View.GONE
        }
        holder.binding.apply {
            tvFaqQuestion.text = element.faqQuestion.question
            tvFaqAnswer.text = element.faqAnswer.answer
        }
        if(faqId != null){
            when{
                faqId == element.id -> {
                    holder.binding.ivFaqCardDropDown.performClick()
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size
}