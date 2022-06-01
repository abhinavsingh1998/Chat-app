package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.ItemInsightsBinding
import com.emproto.networklayer.response.home.PageManagementOrInsight

class InsightsAdapter(
    val context: Context,
    val list: List<PageManagementOrInsight>,
    val itemIntrface: InsightsItemInterface
) : RecyclerView.Adapter<InsightsAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemInsightsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list.get(holder.adapterPosition)
        holder.binding.tvVideotitle.text = item.displayTitle
        holder.binding.shortDesc.text = showHTMLText(item.insightsMedia[0].description)
        if(item.insightsMedia[0].media!=null){
            Glide.with(context)
                .load(item.insightsMedia[0].media.value.url)
                .into(holder.binding.image)
        }

        when{
            item.insightsMedia[0].description.isNullOrEmpty() -> {
                holder.binding.btnReadMore.visibility = View.GONE
            }
        }

        holder.binding.rootView.setOnClickListener {
            itemIntrface.onClickItem(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(val binding: ItemInsightsBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface InsightsItemInterface {
        fun onClickItem(position: Int)
    }

    public fun showHTMLText(message: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(message)
        }
    }
}

