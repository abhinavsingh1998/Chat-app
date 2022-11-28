package com.emproto.hoabl.feature.profile.adapter.faq

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemFaqBinding
import com.emproto.networklayer.response.profile.ProfileFaqResponse

class ProfileFaqListAdapter(
    private var faqList: ArrayList<ProfileFaqResponse.ProfileFaqData>

) : RecyclerView.Adapter<ProfileFaqListAdapter.ViewHolder>() {

    lateinit var binding: ItemFaqBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvFaqQuestion.text = faqList[position].faqQuestion.question
        holder.arrowDown.setOnClickListener {
            holder.viewLine.visibility = View.VISIBLE
            holder.arrowDown.visibility = View.GONE
            holder.arrowUp.visibility = View.VISIBLE
            holder.tvFaqAnswer.visibility = View.VISIBLE
            holder.tvFaqAnswer.text = faqList[position].faqAnswer.answer
        }
        holder.arrowUp.setOnClickListener {
            holder.viewLine.visibility = View.GONE
            holder.arrowDown.visibility = View.VISIBLE
            holder.arrowUp.visibility = View.GONE
            holder.tvFaqAnswer.visibility = View.GONE
            holder.tvFaqAnswer.text = faqList[position].faqAnswer.answer
        }


    }

    override fun getItemCount(): Int {
        return faqList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvFaqQuestion: TextView = itemView.findViewById(R.id.tvFaqQuestion)
        val arrowDown: ImageView = itemView.findViewById(R.id.ivArrowDown)
        val tvFaqAnswer: TextView = itemView.findViewById(R.id.tvFaqAnswer)
        val arrowUp: ImageView = itemView.findViewById(R.id.ivArrowUp)
        val viewLine: View = itemView.findViewById(R.id.viewLine)


    }
}