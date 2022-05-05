package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemChatReceiverBinding
import com.emproto.hoabl.databinding.ItemChatReceiverMessageBinding
import com.emproto.hoabl.databinding.ItemChatSenderMessageBinding
import com.emproto.hoabl.model.ChatsDetailModel
import com.emproto.hoabl.model.TypeOfMessage

class ChatsDetailAdapter(private var mContext: Context?,
                   private var chatsDetailModel: ArrayList<ChatsDetailModel>,
                   private var mListener: OnItemClickListener

) : RecyclerView.Adapter<ChatsDetailAdapter.ViewHolder>() {

    lateinit var receiverBinding: ItemChatReceiverBinding
    lateinit var senderMessageBinding: ItemChatSenderMessageBinding

    val Received=1;
    val Sent=2;
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        if(viewType==1){
        receiverBinding = ItemChatReceiverBinding.inflate( LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(receiverBinding.root)
        }else{
            senderMessageBinding = ItemChatSenderMessageBinding.inflate( LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(senderMessageBinding.root)
        }
    }

    interface OnItemClickListener{
        fun onChatItemClick(chatsDetailModel: ChatsDetailModel, view: View, position: Int)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val  currentMessage=chatsDetailModel[position]
        if(holder.javaClass==SentViewHolder::class.java){
            val viewHolder=holder as SentViewHolder
            holder.tvSentMessage.text=currentMessage.msgReply
        }
        else{
            val viewHolder=holder as ReceiveViewHolder
        }
//        binding.ivThumb.setImageResource(chatsModel[position].image)
//        binding.tvTitle.text = chatsModel[position].topic
//        binding.tvMsg.text = chatsModel[position].desc
//        binding.tvTime.text = chatsModel[position].time
//
//        binding.clChat.setOnClickListener {
//            mListener.onChatItemClick(chatsModel[position], it, position)
//        }

    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage=chatsDetailModel[position]
        if(currentMessage.typeOfMessage==TypeOfMessage.SENDER){
            return Sent
        }else{
            return Received
        }
        return super.getItemViewType(position)
    }
    override fun getItemCount(): Int {
        return chatsDetailModel.size
    }

    class SentViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvSentMessage: TextView = itemView.findViewById(R.id.tvSentMessage)
        val time: TextView = itemView.findViewById(R.id.tvChatSendTime)

    }
    class ReceiveViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {


    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

    }


}