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
import com.emproto.hoabl.databinding.DetailViewItemBinding
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.utils.YoutubeItemClickListener
import com.emproto.networklayer.response.home.DetailedInfo
import com.emproto.networklayer.response.home.InsightsMedia
import com.emproto.networklayer.response.insights.Data


class InsightsListAdapter(
    val context: Context,
    private val list: List<com.emproto.networklayer.response.insights.InsightsMedia>,
    val itemClickListener: YoutubeItemClickListener
) : RecyclerView.Adapter<InsightsListAdapter.InsightsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):InsightsHolder {
        val view =
            DetailViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InsightsHolder(view)
    }

    override fun onBindViewHolder(holder: InsightsHolder, position: Int) {
        val item = list[position]
        holder.binding.firstDetails.text= showHTMLText(item.description)

        if(item.media!=null){
            holder.binding.imageDesc.text= item.media.mediaDescription

            if (!item.media.value.url.isNullOrEmpty()){
                holder.binding.image1.isVisible= true
            } else{
                holder.binding.image1.isVisible= false
            }
        when(item.media.value.mediaType){
            "VIDEO" -> {
                val url = item.media.value.url.replace("https://www.youtube.com/embed/","")
                val youtubeUrl = "https://img.youtube.com/vi/${url}/hqdefault.jpg"
                Glide.with(context)
                    .load(youtubeUrl)
                    .into(holder.binding.image1)
                holder.binding.playBtn.isVisible= true
                holder.binding.image1.setOnClickListener{
                    itemClickListener.onItemClicked(it,position,url,item.media.mediaDescription)
                }
            }
            else -> {
                Glide.with(context)
                    .load(item.media.value.url)
                    .into(holder.binding.image1)
            }
        }}
    }



    override fun getItemCount(): Int {
        return list.size
    }

    inner class InsightsHolder(var binding: DetailViewItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    public fun showHTMLText(message: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(message)
        }
    }

}
