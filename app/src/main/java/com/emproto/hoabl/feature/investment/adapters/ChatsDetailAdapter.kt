package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemChatDetailBinding
import com.emproto.hoabl.databinding.ItemChatSenderMessageBinding
import com.emproto.hoabl.feature.chat.model.ChatDetailModel
import com.emproto.hoabl.feature.chat.model.MessageType
import com.emproto.networklayer.response.chats.Option

class ChatsDetailAdapter(
    private var mContext: Context?,
    private var chatDetailList: ArrayList<ChatDetailModel>,
    private var mListener: OnOptionClickListener

) : RecyclerView.Adapter<ChatsDetailAdapter.BaseViewHolder>() {
    lateinit var binding: ItemChatDetailBinding
    private lateinit var bindingSender: ItemChatSenderMessageBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == R.layout.item_chat_detail) {
            binding =
                ItemChatDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ReceiverViewHolder(binding.root)
        } else {
            bindingSender =
                ItemChatSenderMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            SenderViewHolder(bindingSender.root)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatDetailList[position].messageType == MessageType.RECEIVER) {
            R.layout.item_chat_detail
        } else {
            R.layout.item_chat_sender_message
        }
    }

    override fun getItemCount(): Int {
        return chatDetailList.size
    }

    class ReceiverViewHolder(ItemView: View) : BaseViewHolder(ItemView) {
        var tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        var rvOptions: RecyclerView = itemView.findViewById(R.id.rvOptions)
        var tvTime: TextView = itemView.findViewById(R.id.tvChatTime)
    }

    class SenderViewHolder(ItemView: View) : BaseViewHolder(ItemView) {
        var tvSentMessage: TextView = itemView.findViewById(R.id.tvSentMessage)
        var tvChatSendTime: TextView = itemView.findViewById(R.id.tvChatSendTime)
    }

    abstract class BaseViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView)

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder is ReceiverViewHolder) {
            holder.tvMessage.text = chatDetailList[position].message
            holder.tvTime.text = chatDetailList[position].time
            if (chatDetailList[position].option.isNullOrEmpty()) {
                holder.rvOptions.visibility = View.GONE
            } else {
                holder.rvOptions.visibility = View.VISIBLE
                holder.rvOptions.layoutManager =
                    LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
                holder.rvOptions.adapter =
                    ChatOptionAdapter(
                        chatDetailList[position].option!!,
                        chatDetailList[position].conversationId,
                        mListener,

                        )

            }
        } else if (holder is SenderViewHolder) {
            holder.tvSentMessage.text = chatDetailList[position].message?.trim()
            holder.tvChatSendTime.text = chatDetailList[position].time
        }
    }
}


interface OnOptionClickListener {
    fun onOptionClick(
        option: Option,
        view: View,
        position: Int,
        conversationId: Int
    )
}