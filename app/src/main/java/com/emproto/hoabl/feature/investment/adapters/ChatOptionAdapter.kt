package com.emproto.hoabl.feature.investment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemOptionBinding
import com.emproto.networklayer.response.chats.Option

class ChatOptionAdapter(
    private var option: ArrayList<Option>,
    private var conversationId: Int,
    private var optionListener: OnOptionClickListener,
    private var selectedItemPos: Int = -1

) : RecyclerView.Adapter<ChatOptionAdapter.ViewHolder>() {

    lateinit var binding: ItemOptionBinding
    var buttonbackground = -1


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        binding = ItemOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvOption.text = option[position].text

//        if (position == selectedItemPos) {
//            holder.tvOption.setTextColor(Color.parseColor("#676ac0"))
//        }else{
//            holder.tvOption.setTextColor(Color.parseColor("#494a67"))
//        }

        holder.tvOption.setOnClickListener {
//            buttonbackground = selectedItemPos
//            selectedItemPos = holder.adapterPosition
//            buttonbackground = if (buttonbackground == -1)
//                selectedItemPos
//            else {
//                notifyItemChanged(buttonbackground)
//                selectedItemPos
//            }
//            notifyItemChanged(selectedItemPos)
            optionListener.onOptionClick(option[position], it, position, conversationId)
            //buttonbackground=2

        }
    }

    override fun getItemCount(): Int {
        return option.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var tvOption: TextView = itemView.findViewById(R.id.tvOption)
    }

}