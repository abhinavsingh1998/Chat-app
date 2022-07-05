package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemOptionBinding
import com.emproto.networklayer.response.chats.Option

class ChatOptionAdapter(
    private var mContext: Context?,
    private var option: ArrayList<Option>,
    private var optionListener: OnOptionClickListener
) : RecyclerView.Adapter<ChatOptionAdapter.ViewHolder>() {

    lateinit var binding: ItemOptionBinding


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        binding = ItemOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvOption.text = option[position].text

        holder.tvOption.setOnClickListener {
            optionListener.onOptionClick(option[position], it, position)
        }
    }

    override fun getItemCount(): Int {
        return option.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        var tvOption = itemView.findViewById<TextView>(R.id.tvOption)
//        var clOption = itemView.findViewById<ConstraintLayout>(R.id.clOption)

    }

}