package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.util.Log
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

import kotlin.collections.ArrayList

class ChatsDetailAdapter(
    private var mContext: Context?,
    private var chatDetailList: ArrayList<ChatDetailModel>,
    private var mListener: OnOptionClickListener

) : RecyclerView.Adapter<ChatsDetailAdapter.BaseViewHolder>() {
    lateinit var binding: ItemChatDetailBinding
    lateinit var bindingSender: ItemChatSenderMessageBinding

//    private var c: Calendar? = null
//    private var sdf: SimpleDateFormat? = null
//    private var timeT: String? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == R.layout.item_chat_detail) {
            binding =
                ItemChatDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ReceiverViewHolder(binding.root)
        } else {
            bindingSender =
                ItemChatSenderMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return SenderViewHolder(bindingSender.root)
        }

    }

    override fun getItemViewType(position: Int): Int {
        if (chatDetailList[position].messageType == MessageType.RECEIVER) {
            return R.layout.item_chat_detail
        } else {
            return R.layout.item_chat_sender_message
        }
    }

    override fun getItemCount(): Int {
        return chatDetailList.size
    }

    class ReceiverViewHolder(ItemView: View) : BaseViewHolder(ItemView) {
        var tvMessage = itemView.findViewById<TextView>(R.id.tvMessage)
        var rvOptions = itemView.findViewById<RecyclerView>(R.id.rvOptions)
        var tvTime = itemView.findViewById<TextView>(R.id.tvChatTime)
    }

    class SenderViewHolder(ItemView: View) : BaseViewHolder(ItemView) {
        var tvSentMessage = itemView.findViewById<TextView>(R.id.tvSentMessage)
        var tvChatSendTime=itemView.findViewById<TextView>(R.id.tvChatSendTime)
    }

    abstract class BaseViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {}

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder is ReceiverViewHolder) {
//            c = Calendar.getInstance()
//            sdf = SimpleDateFormat("h:mm a")
//            timeT= sdf!!.format(c!!.time)
            holder.tvMessage.text = chatDetailList[position].message
            holder.tvTime.text = chatDetailList[position].time
            Log.i("time", chatDetailList[position].time.toString())
            if (chatDetailList[position].option.isNullOrEmpty()) {
                holder.rvOptions.visibility = View.GONE
            } else {
                holder.rvOptions.visibility = View.VISIBLE
                holder.rvOptions.layoutManager =
                    LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
                holder.rvOptions.adapter =
                    ChatOptionAdapter(mContext, chatDetailList[position].option!!, mListener)

            }
        } else if (holder is SenderViewHolder) {
            holder.tvSentMessage.text = chatDetailList[position].message
            holder.tvChatSendTime.text = chatDetailList[position].time



        }
    }

}


interface OnOptionClickListener {
    fun onOptionClick(
        option: Option,
        view: View,
        position: Int
    )
}