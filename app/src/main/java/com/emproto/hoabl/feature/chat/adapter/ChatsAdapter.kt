package com.emproto.hoabl.feature.chat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.IssueListBinding
import com.emproto.hoabl.databinding.ItemChatBinding
import com.emproto.hoabl.model.ChatsModel

class ChatsAdapter(private var mContext: Context?,
                   private var chatsModel: ArrayList<ChatsModel>
) : RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {

    lateinit var binding: ItemChatBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =ItemChatBinding.inflate( LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }
    interface OnItemClickListener{
        fun onChatItemClick(chatsModel: ArrayList<ChatsModel>, view: View, position: Int)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.ivThumb.setImageResource(chatsModel[position].image)
        binding.tvTitle.text = chatsModel[position].topic
        binding.tvMsg.text = chatsModel[position].desc
        binding.tvTime.text = chatsModel[position].time

    }

    override fun getItemCount(): Int {
        return chatsModel.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
//        val imageView: ImageView = itemView.findViewById(R.id.ivThumb)
//        val topic: TextView = itemView.findViewById(R.id.tvTitle)
//        val desc: TextView = itemView.findViewById(R.id.tvMsg)
//        val time: TextView = itemView.findViewById(R.id.tvTime)

    }


}