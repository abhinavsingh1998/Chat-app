package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemFaqBinding
import com.emproto.hoabl.databinding.ItemSecurityTipsBinding
import com.emproto.hoabl.feature.portfolio.adapters.PortfolioSpecificViewAdapter
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.response.portfolio.ivdetails.ProjectContentsAndFaq
import com.emproto.networklayer.response.profile.DetailedInformationXXX
import java.util.*

class SecurityTipsAdapter(
    private val context: Context,
    private val list: List<DetailedInformationXXX>
) : RecyclerView.Adapter<SecurityTipsAdapter.SecurityTipsViewHolder>() {

    inner class SecurityTipsViewHolder(var binding: ItemSecurityTipsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecurityTipsViewHolder {
        val view = ItemSecurityTipsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SecurityTipsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SecurityTipsViewHolder, position: Int) {
        val element= list[position]
        holder.binding.apply {

            tvDesc.text = element.media.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            }
            tvFullDescription.text = showHTMLText(element.description)
            Glide
                .with(context)
                .load(element.media.value.url)
                .into(ivSecurityTips)
        }
    }

    override fun getItemCount(): Int = list.size

    fun showHTMLText(message: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(message)
        }
    }
}