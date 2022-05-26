package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemChatDetailBinding

import com.emproto.hoabl.feature.chat.model.ChatDetailModel
import com.emproto.hoabl.feature.chat.views.fragments.ChatsDetailFragment
import com.emproto.networklayer.response.chats.ChatDetailResponse
import com.emproto.networklayer.response.chats.Option

class ChatsDetailAdapter(
    private var mContext: Context?,
    private var chatDetailList: ArrayList<ChatDetailModel>,
    private var mListener: OnOptionClickListener

) : RecyclerView.Adapter<ChatsDetailAdapter.ViewHolder>() {
    lateinit var binding: ItemChatDetailBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemChatDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatsDetailAdapter.ViewHolder(binding.root)
    }

//    override fun getItemViewType(position: Int): Int {
//        if (chatDetailList[position].autoChat.chatJSON.chatBody.options.isNotEmpty()) {
//            return R.layout.item_chat_detail
//        } else {
//            return R.layout.item_message
//        }
//    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.tvMessage.text= chatDetailList[position].message
        if(chatDetailList[position].option.isNullOrEmpty()){
            binding.rvOptions.visibility=View.GONE
        }
        else{
            binding.rvOptions.layoutManager =
                LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
            binding.rvOptions.adapter =
                ChatOptionAdapter(mContext,chatDetailList[position].option!!, mListener)
        }
    }

    override fun getItemCount(): Int {
        return chatDetailList.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

    }


}


interface OnOptionClickListener  {
    fun onOptionClick(
        option: Option,
        view: View,
        position: Int
    )
}