package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.core.Utility
import com.emproto.hoabl.databinding.ItemInsightsBinding
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.response.home.Data
import com.emproto.networklayer.response.home.PageManagementOrInsight
import javax.inject.Inject

@Suppress("DEPRECATION")
class InsightsAdapter(
    val context: Context,
    val itemCount: Data,
    val list: List<PageManagementOrInsight>,
    val itemInterface: ItemClickListener
) : RecyclerView.Adapter<InsightsAdapter.MyViewHolder>() {

    @Inject
    lateinit var appPreference: AppPreference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemInsightsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[holder.adapterPosition]
        holder.binding.tvVideotitle.text = item.displayTitle

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Utility.convertStringIns(
                holder.binding.shortDesc,
                context,
                showHTMLText(item.insightsMedia[0].description).toString(),
                2
            )
        }

        if (item.insightsMedia[0].media != null) {
            when (item.insightsMedia[0].media.value.mediaType) {
                "VIDEO" -> {
                    val url = item.insightsMedia[0].media.value.url.replace(
                        "https://www.youtube.com/embed/",
                        ""
                    )
                    val youtubeUrl = "https://img.youtube.com/vi/${url}/hqdefault.jpg"

                    holder.binding.playBtn.isVisible = true
                    Glide.with(context)
                        .load(youtubeUrl)
                        .into(holder.binding.image)
                }
                else -> {
                    Glide.with(context)
                        .load(item.insightsMedia[0].media.value.url)
                        .into(holder.binding.image)
                }
            }
        }

        when {
            item.insightsMedia[0].description.isNullOrEmpty() -> {
                holder.binding.btnReadMore.visibility = View.GONE
            }
        }
        holder.binding.tvVideotitle.text = item.displayTitle

        holder.binding.homeInsightsCard.setOnClickListener {
            itemInterface.onItemClicked(it, position, item.id.toString())
        }
    }



    override fun getItemCount(): Int {
        val itemList = if (itemCount.page.totalInsightsOnHomeScreen < list.size) {
            itemCount.page.totalInsightsOnHomeScreen
        } else {
            list.size
        }
        return itemList
    }

    inner class MyViewHolder(val binding: ItemInsightsBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun showHTMLText(message: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(message)
        }
    }
}

