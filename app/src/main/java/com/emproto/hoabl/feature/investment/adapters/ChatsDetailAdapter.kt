package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemChatBinding
import com.emproto.hoabl.databinding.ItemChatDetailBinding
import com.emproto.hoabl.databinding.ItemChatReceiverBinding
import com.emproto.hoabl.databinding.ItemChatSenderMessageBinding
import com.emproto.hoabl.feature.chat.adapter.ChatsAdapter
import com.emproto.hoabl.feature.chat.model.ChatsDetailModel
import com.emproto.hoabl.feature.chat.model.TypeOfMessage
import com.emproto.networklayer.response.chats.ChatDetailResponse
import com.emproto.networklayer.response.chats.ChatResponse

class ChatsDetailAdapter(private var mContext: Context?,
                         private var chatDetailList: List<ChatDetailResponse.ChatDetailList>,
                         private var mListener: ChatsDetailAdapter.OnItemClickListener

) : RecyclerView.Adapter<ChatsDetailAdapter.ViewHolder>() {
    lateinit var binding: ItemChatDetailBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemChatDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatsDetailAdapter.ViewHolder(binding.root)
    }

    interface OnItemClickListener{
        fun onChatItemClick(chatDetailList: List<ChatDetailResponse.ChatDetailList>, view: View, position: Int)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position==0)
        binding.tvMessage.text = chatDetailList[position].autoChat.chatJSON.chatBody.message


    }

    override fun getItemCount(): Int {
        return chatDetailList.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

    }


}