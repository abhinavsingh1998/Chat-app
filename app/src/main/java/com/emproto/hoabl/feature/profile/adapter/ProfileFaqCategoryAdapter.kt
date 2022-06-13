package com.emproto.hoabl.feature.profile.adapter

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
        holder.categoryNumber.text = "Category"+ faqCategoryList[position].categoryId.toString()
        holder.rvFaqList.layoutManager =
            LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        holder.rvFaqList.adapter =
            ProfileFaqListAdapter(mContext, faqCategoryList)


    }

    override fun getItemCount(): Int {
        return faqCategoryList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val categoryNumber: TextView = itemView.findViewById(R.id.tv_Category)
        var rvFaqList = itemView.findViewById<RecyclerView>(R.id.rvFaq)


    }
}

    //    companion object {
//        const val FAQ_TEXT_VIEW = 1
//        const val FAQ_CATEGORY_VIEW = 2
//        const val FAQ_TEXT_VIEW2 = 3
//    }
//
//    private val context: Context = context
//    var list: ArrayList<FaqData> = list
//
//    private inner class FaqTextViewHolder1(itemView: View) :
//        RecyclerView.ViewHolder(itemView) {
//        var message: TextView = itemView.findViewById(R.id.fulltextview)
//        var unward :ImageView= itemView.findViewById(R.id.imgDrop)
//        var downward :ImageView= itemView.findViewById(R.id.imgdropdown)
//        var answer:TextView = itemView.findViewById(R.id.tvView)
//        fun bind(position: Int) {
//            val recyclerViewModel = list[position]
//            message.text = recyclerViewModel.textData
//            downward.setOnClickListener{
//                downward.visibility = View.GONE
//                unward.visibility = View.VISIBLE
//                answer.visibility = View.VISIBLE
//            }
//            unward.setOnClickListener {
//                downward.visibility = View.VISIBLE
//                unward.visibility = View.GONE
//                answer.visibility = View.GONE
//            }
//        }
//    }
//    private inner class FaqCategoryViewHolder(itemView: View) :
//        RecyclerView.ViewHolder(itemView) {
//        var message: TextView = itemView.findViewById(R.id.category)
//        fun bind(position: Int) {
//            val recyclerViewModel = list[position]
//            message.text = recyclerViewModel.category
//
//        }
//    }
//    private inner class FaqTextViewHolder2(itemView: View) :
//        RecyclerView.ViewHolder(itemView) {
//        var message: TextView = itemView.findViewById(R.id.fulltextview)
//        var unward :ImageView= itemView.findViewById(R.id.imgDrop)
//        var downward :ImageView= itemView.findViewById(R.id.imgdropdown)
//        var answer:TextView = itemView.findViewById(R.id.tvView)
//        fun bind(position: Int) {
//            val recyclerViewModel = list[position]
//            message.text = recyclerViewModel.textData
//            downward.setOnClickListener{
//                downward.visibility = View.GONE
//                unward.visibility = View.VISIBLE
//                answer.visibility = View.VISIBLE
//            }
//            unward.setOnClickListener {
//                downward.visibility = View.VISIBLE
//                unward.visibility = View.GONE
//                answer.visibility = View.GONE
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        if (viewType == FAQ_TEXT_VIEW) {
//            return FaqTextViewHolder1(
//                LayoutInflater.from(context).inflate(R.layout.faq_view_1, parent, false)
//            )
//        }
//        else if (viewType == FAQ_CATEGORY_VIEW){
//            return FaqCategoryViewHolder(
//                LayoutInflater.from(context).inflate(R.layout.faq_view_2, parent, false)
//            )
//        }
//        return FaqTextViewHolder2(
//            LayoutInflater.from(context).inflate(R.layout.faq_view_3, parent, false)
//        )
//    }
//
//    override fun getItemCount(): Int {
//        return list.size
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        if (list[position].viewType === FAQ_TEXT_VIEW) {
//            (holder as FaqTextViewHolder1).bind(position)
//        } else if(list[position].viewType === FAQ_CATEGORY_VIEW) {
//            (holder as FaqCategoryViewHolder).bind(position)
//        }
//        else if(list[position].viewType === FAQ_TEXT_VIEW2){
//            (holder as FaqTextViewHolder2).bind(position)
//        }
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return list[position].viewType
//    }

