package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemOptionBinding
import com.emproto.networklayer.response.chats.Option

class ChatOptionAdapter(private var mContext: Context?, private var option: ArrayList<Option>, private var optionListener: OnOptionClickListener
) : RecyclerView.Adapter<ChatOptionAdapter.ViewHolder>() {
    lateinit var binding: ItemOptionBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatOptionAdapter.ViewHolder {
        binding = ItemOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatOptionAdapter.ViewHolder(binding.root)
    }


    override fun onBindViewHolder(p0: ChatOptionAdapter.ViewHolder, position: Int) {
        binding.tvOption.text= option[position].text
        binding.tvOption.setOnClickListener {
            optionListener.onOptionClick(option[position],it,position)
        }

    }

    override fun getItemCount(): Int {
        return option.size

    }
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

    }

}