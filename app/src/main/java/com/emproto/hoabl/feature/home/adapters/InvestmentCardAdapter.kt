package com.emproto.hoabl.feature.home.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FinancialSummryItemsBinding
import com.emproto.hoabl.model.FinancialSummaryItems

class PortfolioInvestmentCardAdapter( context: Context, list: ArrayList<FinancialSummaryItems>): RecyclerView.Adapter<PortfolioInvestmentCardAdapter.MyViewHolder>() {

    var list: ArrayList<FinancialSummaryItems>
    var mcontext:Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PortfolioInvestmentCardAdapter.MyViewHolder {

        val binding: FinancialSummryItemsBinding =
            FinancialSummryItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: PortfolioInvestmentCardAdapter.MyViewHolder, position: Int) {


                holder.binding.cardName1.setText(list.get(position).cardName_1)
                holder.binding.cardName2.setText(list.get(position).cardName_2)
                holder.binding.cardName3.setText(list.get(position).cardName_3)
                holder.binding.cardName4.setText(list.get(position).cardName_4)

                holder.binding.contentTxt1.setText(list.get(position).cardContent_1)
                holder.binding.contentTxt2.setText(list.get(position).cardContent_2)
                holder.binding.contentTxt3.setText(list.get(position).cardContent_3)
                holder.binding.contentTxt4.setText(list.get(position).cardContent_4)

        when(holder.adapterPosition){
            0 -> {
                holder.binding.cardView1.setBackgroundResource(R.drawable.financial_summary_dark_card)
                holder.binding.cardView2.setBackgroundResource(R.drawable.financial_summary_dark_card)
                holder.binding.cardView3.setBackgroundResource(R.drawable.financial_summary_dark_card)
                holder.binding.cardView4.setBackgroundResource(R.drawable.financial_summary_dark_card)

                holder.binding.cardName1.setTextColor(Color.WHITE)
                holder.binding.cardName2.setTextColor(Color.WHITE)
                holder.binding.cardName3.setTextColor(Color.WHITE)
                holder.binding.cardName4.setTextColor(Color.WHITE)

                holder.binding.contentTxt1.setTextColor(Color.WHITE)
                holder.binding.contentTxt2.setTextColor(Color.WHITE)
                holder.binding.contentTxt3.setTextColor(Color.WHITE)
                holder.binding.contentTxt4.setTextColor(Color.WHITE)
            }
            1 -> {
                holder.binding.cardView1.setBackgroundResource(R.drawable.financial_summary_light_card)
                holder.binding.cardView2.setBackgroundResource(R.drawable.financial_summary_light_card)
                holder.binding.cardView3.setBackgroundResource(R.drawable.financial_summary_light_card)
                holder.binding.cardView4.setBackgroundResource(R.drawable.financial_summary_light_card)

                holder.binding.cardName1.setTextColor(Color.BLACK)
                holder.binding.cardName2.setTextColor(Color.BLACK)
                holder.binding.cardName3.setTextColor(Color.BLACK)
                holder.binding.cardName4.setTextColor(Color.BLACK)

                holder.binding.contentTxt1.setTextColor(Color.BLACK)
                holder.binding.contentTxt2.setTextColor(Color.BLACK)
                holder.binding.contentTxt3.setTextColor(Color.BLACK)
                holder.binding.contentTxt4.setTextColor(Color.BLACK)

            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(binding: FinancialSummryItemsBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        var binding:FinancialSummryItemsBinding

        init {
            this.binding = binding
        }
    }

    init {
        this.list = list
        this.mcontext = context
    }
}