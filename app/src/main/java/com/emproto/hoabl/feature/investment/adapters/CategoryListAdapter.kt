package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.ItemCategoryListBinding
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.response.investment.PageManagementsOrCollectionOneModel
import com.emproto.networklayer.response.investment.PageManagementsOrCollectionTwoModel
import com.emproto.networklayer.response.portfolio.ivdetails.SimilarInvestment
import com.emproto.networklayer.response.watchlist.Data
import java.util.ArrayList

class CategoryListAdapter(
    private val context: Context,
    val list: List<Any>,
    private val itemClickListener: ItemClickListener?,
    val type: Int = -1
) : RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder>() {

    val TYPE_WATCHLIST = 0
    val TYPE_SIMILAR = 1
    val TYPE_OTHER = -1

    inner class CategoryViewHolder(var binding: ItemCategoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            view: View,
            position: Int,
            item: Any,
            clickListener: ItemClickListener
        ) {
            if (type == TYPE_OTHER) {
                val element = list[position] as PageManagementsOrCollectionOneModel
                itemView.setOnClickListener {
                    clickListener.onItemClicked(view, position, element.id.toString())
                }
                binding.apply {
                    tvProjectName.text = element.launchName
                    tvCategoryPrice.text = element.priceStartingFrom + " Onwards"
                    tvCategoryArea.text = element.areaStartingFrom + " Onwards"
                    tvCategoryItemInfo.text = element.shortDescription
                    Glide.with(context)
                        .load(element.projectCoverImages.newInvestmentPageMedia.value.url)
                        .into(ivCategoryImage)
                }
            } else if (type == TYPE_SIMILAR) {
                val element = list[position] as SimilarInvestment
                itemView.setOnClickListener {
                    clickListener.onItemClicked(view, position, element.id.toString())
                }
                binding.apply {
                    tvProjectName.text = element.launchName
                    tvCategoryPrice.text = element.priceStartingFrom + " Onwards"
                    tvCategoryArea.text = element.areaStartingFrom + " Onwards"
                    tvCategoryItemInfo.text = element.shortDescription
                    Glide.with(context)
                        .load(element.projectIcon.value.url)
                        .into(ivCategoryImage)
                }
            } else {
                val element = list[position] as Data
                itemView.setOnClickListener {
                    clickListener.onItemClicked(view, position, element.project.id.toString())
                }
                binding.apply {
                    tvProjectName.text = element.project.launchName
                    tvCategoryPrice.text = element.project.priceStartingFrom + " Onwards"
                    tvCategoryArea.text = element.project.areaStartingFrom + " Onwards"
                    tvCategoryItemInfo.text = element.project.shortDescription
                    Glide.with(context)
                        .load(element.project.projectIcon.value.url)
                        .into(ivCategoryImage)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            ItemCategoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val list = list[position]
        holder.bind(holder.itemView, position, list, itemClickListener!!)

    }

    override fun getItemCount(): Int = list.size

}