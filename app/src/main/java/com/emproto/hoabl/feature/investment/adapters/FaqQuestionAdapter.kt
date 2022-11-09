package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemFaqQuestionBinding
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.response.investment.ProjectContentsAndFaq
import javax.inject.Inject

class FaqQuestionAdapter(
    var context: Context,
    private val list: List<ProjectContentsAndFaq>,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<FaqQuestionAdapter.FaqViewHolder>() {
    @Inject
    lateinit var appPreference: AppPreference
    inner class FaqViewHolder(var binding: ItemFaqQuestionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val view =
            ItemFaqQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FaqViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val element = list[position]
        holder.binding.apply {
            tvFaqQuestion.text = element.frequentlyAskedQuestion.faqQuestion.question
            cvFaqCard.setOnClickListener {
                itemClickListener.onItemClicked(
                    it,
                    position,
                    element.faqId.toString())
            }
        }
    }


    override fun getItemCount(): Int = list.size
}