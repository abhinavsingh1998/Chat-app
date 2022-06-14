package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.CorporatePhilosphyAboutUsViewBinding
import com.emproto.hoabl.databinding.ProductCategoryBinding
import com.emproto.hoabl.feature.profile.data.AboutusData
import com.emproto.networklayer.response.resourceManagment.DetailedInformation
import com.emproto.networklayer.response.resourceManagment.DetailedInformationX

class ProductAdapter(
    val context: Context,
    private val corporateList: List<DetailedInformationX>):
    RecyclerView.Adapter<ProductAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ProductCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductAdapter.MyViewHolder, position: Int) {
        val currentItem= corporateList[position]

        holder.binding.tileTxt.text= currentItem.displayName
        Glide.with(context).load(currentItem.media.value.url)
            .into(holder.binding.locationIv)

        holder.binding.fullDesc.text= showHTMLText(currentItem.description)
    }

    override fun getItemCount(): Int {
        return corporateList.size
    }
    inner class MyViewHolder(val binding: ProductCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    public fun showHTMLText(message: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(message)
        }
    }
}
