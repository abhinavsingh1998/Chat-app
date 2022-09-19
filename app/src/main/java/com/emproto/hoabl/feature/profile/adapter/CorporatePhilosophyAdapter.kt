package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.CorporatePhilosphyAboutUsViewBinding
import com.emproto.networklayer.response.resourceManagment.DetailedInformation

@Suppress("DEPRECATION")
class CorporatePhilosophyAdapter(
    val context: Context,
    private val corporateList: List<DetailedInformation>):
    RecyclerView.Adapter<CorporatePhilosophyAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CorporatePhilosophyAdapter.MyViewHolder {
        val view = CorporatePhilosphyAboutUsViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CorporatePhilosophyAdapter.MyViewHolder, position: Int) {
        val currentItem= corporateList[position]

        holder.binding.tvPieceOfLand.text= currentItem.displayName
        holder.binding.fullDescTv.text= showHTMLText(currentItem.description)
        Glide.with(context).load(currentItem.media.value.url)
            .into(holder.binding.ivImage)
    }

    override fun getItemCount(): Int {
        return corporateList.size
    }
    inner class MyViewHolder(val binding: CorporatePhilosphyAboutUsViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun showHTMLText(message: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(message)
        }
    }
}
