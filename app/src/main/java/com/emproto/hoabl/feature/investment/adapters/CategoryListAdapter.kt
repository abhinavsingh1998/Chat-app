package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.ItemCategoryListBinding
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.response.investment.*
import com.emproto.networklayer.response.portfolio.ivdetails.SimilarInvestment
import com.emproto.networklayer.response.watchlist.Data

class CategoryListAdapter(
    private val context: Context,
    val list: List<Any>,
    private val itemClickListener: ItemClickListener?,
    val type: Int = -1
) : RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder>() {

    companion object {
        const val TYPE_NEW_LAUNCH = 0
        const val TYPE_FEW_PLOTS = 1
        const val TYPE_TRENDING_PROJECTS = 2
        const val TYPE_ALL_INVESTMENTS = 3
        const val TYPE_WATCHLIST = 4
    }

    inner class CategoryViewHolder(var binding: ItemCategoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            view: View,
            position: Int,
            item: Any,
            clickListener: ItemClickListener
        ) {
            when (type) {
                TYPE_NEW_LAUNCH -> {
                    val element = list[position] as PageManagementsOrNewInvestment
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
                }
                TYPE_FEW_PLOTS -> {
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
                }
                TYPE_TRENDING_PROJECTS -> {
                    val element = list[position] as PageManagementsOrCollectionTwoModel
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
                }
                TYPE_ALL_INVESTMENTS -> {
                    val element = list[position] as ApData
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
                }
                TYPE_ALL_INVESTMENTS -> {
                    val element = list[position] as ApData
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
                }
                TYPE_WATCHLIST -> {
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

                else -> {
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