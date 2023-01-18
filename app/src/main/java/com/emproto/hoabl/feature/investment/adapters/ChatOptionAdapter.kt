package com.emproto.hoabl.feature.investment.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemOptionBinding
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.response.chats.Option

class ChatOptionAdapter(
    private var option: ArrayList<Option>,
    private var conversationId: Int,
    private var optionListener: OnOptionClickListener,
    private val appPreference: AppPreference

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

        if(appPreference.getMessageVisible().equals(true)){
            holder.tvOption.setTextColor(Color.parseColor("#d3d3d8"))
        }

        holder.tvOption.setOnClickListener {
            optionListener.onOptionClick(option[position], it, position, conversationId, option[position].actionType)

        }
    }

    override fun getItemCount(): Int {
        return option.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var tvOption: TextView = itemView.findViewById(R.id.tvOption)
    }

}