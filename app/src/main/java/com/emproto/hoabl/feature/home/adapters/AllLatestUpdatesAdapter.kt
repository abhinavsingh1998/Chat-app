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
import com.emproto.hoabl.databinding.ProjectUpdatesItemBinding
import com.emproto.networklayer.response.marketingUpdates.Data

class AllLatestUpdatesAdapter(
    val context: Context,
    val list: List<Data>,
    val itemInterface: UpdatesItemsInterface)
    : RecyclerView.Adapter<AllLatestUpdatesAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ProjectUpdatesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list.get(holder.adapterPosition)
        holder.binding.title.text= item.displayTitle
        holder.binding.location.text= item.subTitle.toString()


        if (item.detailedInfo[0].media!=null){

            if(!item.detailedInfo[0].media.value.url.isNullOrEmpty()){
                holder.binding.imageCard.isVisible= true
                Glide.with(context).load(item.detailedInfo[0].media.value.url)
                    .into(holder.binding.locationImage)
            } else{
                holder.binding.imageCard.isVisible= true
            }


            if (item.detailedInfo[0].description.isNullOrEmpty()){
                holder.binding.btnReadMore.isVisible= false
            } else{
                holder.binding.btnReadMore.isVisible= true
                holder.binding.deatils.text= showHTMLText(item.detailedInfo[0].description)
            }

        }

        holder.binding.rootView.setOnClickListener {
            itemInterface.onClickItem(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(val binding: ProjectUpdatesItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface UpdatesItemsInterface {
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

