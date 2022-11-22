package com.emproto.hoabl.feature.profile.adapter.faq

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemFaqCategoryBinding
import com.emproto.networklayer.response.profile.ProfileFaqResponse


class ProfileFaqCategoryAdapter(
    private var mContext: Context?,
    private var faqCategoryList: ArrayList<ProfileFaqResponse.ProfileFaqData>

) : RecyclerView.Adapter<ProfileFaqCategoryAdapter.ViewHolder>() {

    lateinit var binding: ItemFaqCategoryBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemFaqCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (faqCategoryList[position].categoryId.toString().isNotEmpty())
            holder.categoryNumber.text =
                "Category" + " " + faqCategoryList[position].categoryId.toString()
        if (faqCategoryList.isNotEmpty())
            holder.rvFaqList.layoutManager =
                LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        holder.rvFaqList.adapter =
            ProfileFaqListAdapter(faqCategoryList)

    }

    override fun getItemCount(): Int {
        return faqCategoryList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val categoryNumber: TextView = itemView.findViewById(R.id.tv_Category)
        var rvFaqList = itemView.findViewById<RecyclerView>(R.id.rvFaq)
    }
}
