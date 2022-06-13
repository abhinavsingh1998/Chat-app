package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemFaqBinding
import com.emproto.hoabl.databinding.ItemFaqCategoryBinding
import com.emproto.networklayer.response.profile.ProfileFaqResponse

class ProfileFaqListAdapter(
    private var mContext: Context?,
    private var faqList: ArrayList<ProfileFaqResponse.ProfileFaqData>

) : RecyclerView.Adapter<ProfileFaqListAdapter.ViewHolder>() {

    lateinit var binding: ItemFaqBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvFaqQuestion.text = faqList[position].faqQuestion.toString()


    }

    override fun getItemCount(): Int {
        return faqList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvFaqQuestion: TextView = itemView.findViewById(R.id.tv_faq_question)


    }
}