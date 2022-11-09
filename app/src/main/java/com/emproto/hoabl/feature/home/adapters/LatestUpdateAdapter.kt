package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.core.Utility
import com.emproto.hoabl.databinding.ItemLatestUpdatesBinding
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.response.home.Data
import com.emproto.networklayer.response.home.PageManagementOrLatestUpdate
import javax.inject.Inject

class LatestUpdateAdapter(
    val context: Context,
    val itemCount: Data,
    val list: List<PageManagementOrLatestUpdate>,
    val itemIntrface: ItemClickListener
) : RecyclerView.Adapter<LatestUpdateAdapter.MyViewHolder>() {

    @Inject
    lateinit var appPreference: AppPreference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            ItemLatestUpdatesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list.get(holder.adapterPosition)
        holder.binding.title.text = item.displayTitle


        if (!item.subTitle.isNullOrEmpty()) {
            holder.binding.tvName.text = item.subTitle
        } else {
            holder.binding.tvName.isVisible = false
        }


        if (item.detailedInfo[0].media != null) {

            if (!item.detailedInfo[0].media.value.url.isNullOrEmpty()) {
                holder.binding.imageCard.isVisible = true
                Glide.with(context).load(item.detailedInfo[0]?.media?.value.url)
                    .into(holder.binding.image)
            } else {
                holder.binding.imageCard.isVisible = false
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Utility.convertString(
                    holder.binding.description,
                    context,
                    showHTMLText(item.detailedInfo[0].description).toString(),
                    3
                )
            }
        }

        holder.binding.homeLatestUpdateCard.setOnClickListener {
            itemIntrface.onItemClicked(
                it,
                position,
                holder.itemId.toString())
        }
    }



    override fun getItemCount(): Int {
        val itemList = if (itemCount.page.totalUpdatesOnHomeScreen < list.size) {
            itemCount.page.totalUpdatesOnHomeScreen
        } else {
            list.size
        }
        return itemList
    }

    inner class MyViewHolder(val binding: ItemLatestUpdatesBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun showHTMLText(message: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(message)
        }
    }
}
