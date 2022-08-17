package com.emproto.hoabl.feature.chat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemChatBinding
import com.emproto.networklayer.response.chats.CData
import com.emproto.networklayer.response.chats.ChatResponse
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

class ChatsAdapter(
    private var mContext: Context,
    private var chatList: List<CData>,
    private var mListener: OnItemClickListener

) : RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {

    lateinit var binding: ItemChatBinding
    var timePattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    interface OnItemClickListener {
        fun onChatItemClick(chatList: List<CData>, view: View, position: Int)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.tvChatTitle.text = chatList[position].project.projectContent.launchName.toString()
        if(chatList[position].lastMessage!=null){
            binding.tvChatDesc.text = chatList[position].lastMessage.message.toString()
            binding.tvChatTime.text = Utility.convertUTCtoTime(chatList[position].lastMessage.createdAt)
        }else{
            binding.tvChatDesc.text=""
            binding.tvChatTime.text = ""
        }

        Glide.with(mContext)
            .load(chatList[position].project.projectContent.projectCoverImages.chatListViewPageMedia.value.url)
            .placeholder(R.drawable.ic_baseline_image_24)
            .into(binding.ivChatThumb)

        binding.clChat.setOnClickListener {
            mListener.onChatItemClick(chatList, it, position)
        }

    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

    }


}