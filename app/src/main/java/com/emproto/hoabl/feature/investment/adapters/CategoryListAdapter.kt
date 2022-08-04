package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.util.Log
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
        const val TYPE_DISCOVERALL = 5
        const val SIMILAR_INVESTMENT_NI = 6
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
                    binding.cvCategoryOuterCard.setOnClickListener {
                        clickListener.onItemClicked(view, 0, element.id.toString())
                    }
                    binding.tvApplyNowCategory.setOnClickListener {
                        clickListener.onItemClicked(view, 1, element.id.toString())
                    }

                    binding.apply {
                        tvProjectName.text = element.launchName
                        val amount = element.priceStartingFrom.toDouble() / 100000
                        val convertedAmount = amount.toString().replace(".0", "")
                        tvCategoryPrice.text = "₹${convertedAmount} L" + " Onwards"
                        tvCategoryArea.text = element.areaStartingFrom + " Sqft Onwards"
                        tvCategoryItemInfo.text = element.shortDescription
                        Glide.with(context)
                            .load(element.projectCoverImages.collectionListViewPageMedia.value.url)
                            .into(ivCategoryImage)
                        tvRating.text = "${
                            String.format(
                                "%.0f",
                                element.generalInfoEscalationGraph.estimatedAppreciation.toDouble()
                            )
                        }%"
                    }
                }
                TYPE_FEW_PLOTS -> {
                    val element = list[position] as PageManagementsOrCollectionOneModel
                    binding.cvCategoryOuterCard.setOnClickListener {
                        clickListener.onItemClicked(view, 0, element.id.toString())
                    }
                    binding.tvApplyNowCategory.setOnClickListener {
                        clickListener.onItemClicked(view, 1, element.id.toString())
                    }

                    binding.apply {
                        tvProjectName.text = element.launchName
                        val amount = element.priceStartingFrom.toDouble() / 100000
                        val convertedAmount = amount.toString().replace(".0", "")
                        tvCategoryPrice.text = "₹${convertedAmount} L" + " Onwards"
                        tvCategoryArea.text = element.areaStartingFrom + " Sqft Onwards"
                        tvCategoryItemInfo.text = element.shortDescription
                        tvRating.text = "${
                            String.format(
                                "%.0f",
                                element.generalInfoEscalationGraph.estimatedAppreciation.toDouble()
                            )
                        }%"
                        Glide.with(context)
                            .load(element.projectCoverImages.collectionListViewPageMedia.value.url)
                            .into(ivCategoryImage)
                    }
                }
                TYPE_TRENDING_PROJECTS -> {
                    val element = list[position] as PageManagementsOrCollectionTwoModel
                    binding.cvCategoryOuterCard.setOnClickListener {
                        clickListener.onItemClicked(view, 0, element.id.toString())
                    }
                    binding.tvApplyNowCategory.setOnClickListener {
                        clickListener.onItemClicked(view, 1, element.id.toString())
                    }

                    binding.apply {
                        tvProjectName.text = element.launchName
                        val amount = element.priceStartingFrom.toDouble() / 100000
                        val convertedAmount = amount.toString().replace(".0", "")
                        tvCategoryPrice.text = "₹${convertedAmount} L" + " Onwards"
                        tvCategoryArea.text = element.areaStartingFrom + " Sqft Onwards"
                        tvCategoryItemInfo.text = element.shortDescription
                        tvRating.text = "${
                            String.format(
                                "%.0f",
                                element.generalInfoEscalationGraph.estimatedAppreciation.toDouble()
                            )
                        }%"
                        Glide.with(context)
                            .load(element.projectCoverImages.collectionListViewPageMedia.value.url)
                            .into(ivCategoryImage)
                    }
                }
                TYPE_ALL_INVESTMENTS -> {
                    val element = list[position] as ApData
                    binding.cvCategoryOuterCard.setOnClickListener {
                        clickListener.onItemClicked(view, 0, element.id.toString())
                    }
                    binding.tvApplyNowCategory.setOnClickListener {
                        clickListener.onItemClicked(view, 1, element.id.toString())
                    }

                    binding.apply {
                        tvProjectName.text = element.launchName
                        Log.d("dessrt", element.priceStartingFrom.toString())
                        val amount = element.priceStartingFrom.toDouble() / 100000
                        val convertedAmount = amount.toString().replace(".0", "")
                        tvCategoryPrice.text = "₹${convertedAmount} L" + " Onwards"
                        tvCategoryArea.text = element.areaStartingFrom + " Sqft Onwards"
                        tvCategoryItemInfo.text = element.shortDescription
                        tvRating.text = "${
                            String.format(
                                "%.0f",
                                element.generalInfoEscalationGraph.estimatedAppreciation.toDouble()
                            )
                        }%"
                        Glide.with(context)
                            .load(element.projectCoverImages.collectionListViewPageMedia.value.url)
                            .into(ivCategoryImage)
                    }
                }
                TYPE_ALL_INVESTMENTS -> {
                    val element = list[position] as ApData
                    binding.cvCategoryOuterCard.setOnClickListener {
                        clickListener.onItemClicked(view, 0, element.id.toString())
                    }
                    binding.tvApplyNowCategory.setOnClickListener {
                        clickListener.onItemClicked(view, 1, element.id.toString())
                    }

                    binding.apply {
                        tvProjectName.text = element.launchName
                        val amount = element.priceStartingFrom.toDouble() / 100000
                        val convertedAmount = amount.toString().replace(".0", "")
                        tvCategoryPrice.text = "₹${convertedAmount} L" + " Onwards"
                        tvCategoryArea.text = element.areaStartingFrom + " Sqft Onwards"
                        tvCategoryItemInfo.text = element.shortDescription
                        tvRating.text = "${
                            String.format(
                                "%.0f",
                                element.generalInfoEscalationGraph.estimatedAppreciation.toDouble()
                            )
                        }%"
                        Glide.with(context)
                            .load(element.projectCoverImages.collectionListViewPageMedia.value.url)
                            .into(ivCategoryImage)
                    }
                }
                TYPE_WATCHLIST -> {
                    val element = list[position] as Data
                    binding.cvCategoryOuterCard.setOnClickListener {
                        clickListener.onItemClicked(view, 0, element.project.id.toString())
                    }
                    binding.tvApplyNowCategory.setOnClickListener {
                        clickListener.onItemClicked(view, 1, element.project.id.toString())
                    }

                    binding.apply {
                        tvProjectName.text = element.project.launchName
                        val amount = element.project.priceStartingFrom.toDouble() / 100000
                        val convertedAmount = amount.toString().replace(".0", "")
                        tvCategoryPrice.text = "₹${convertedAmount} L" + " Onwards"
                        tvCategoryArea.text = element.project.areaStartingFrom + " Sqft Onwards"
                        tvCategoryItemInfo.text = element.project.shortDescription
                        Glide.with(context)
                            .load(element.project.projectCoverImages.collectionListViewPageMedia.value.url)
                            .into(ivCategoryImage)
                    }
                }

                TYPE_DISCOVERALL -> {
                    val element = list[position] as ApData
                    binding.cvCategoryOuterCard.setOnClickListener {
                        clickListener.onItemClicked(view, 0, element.id.toString())
                    }
                    binding.tvApplyNowCategory.setOnClickListener {
                        clickListener.onItemClicked(view, 1, element.id.toString())
                    }

                    binding.apply {
                        tvProjectName.text = element.launchName
                        val amount = element.priceStartingFrom.toDouble() / 100000
                        val convertedAmount = amount.toString().replace(".0", "")
                        tvCategoryPrice.text = "₹${convertedAmount} L" + " Onwards"
                        tvCategoryArea.text = element.areaStartingFrom + " Sqft Onwards"
                        tvCategoryItemInfo.text = element.shortDescription
                        tvRating.text = "${
                            String.format(
                                "%.0f",
                                element.generalInfoEscalationGraph.estimatedAppreciation.toDouble()
                            )
                        }%"
                        Glide.with(context)
                            .load(element.projectCoverImages.collectionListViewPageMedia.value.url)
                            .into(ivCategoryImage)
                    }
                }

                SIMILAR_INVESTMENT_NI -> {
                    val element =
                        list[position] as com.emproto.networklayer.response.investment.SimilarInvestment
                    binding.cvCategoryOuterCard.setOnClickListener {
                        clickListener.onItemClicked(view, 0, element.id.toString())
                    }
                    binding.tvApplyNowCategory.setOnClickListener {
                        clickListener.onItemClicked(view, 1, element.id.toString())
                    }

                    binding.apply {
                        tvProjectName.text = element.launchName
                        val amount = element.priceStartingFrom!!.toDouble() / 100000
                        val convertedAmount = amount.toString().replace(".0", "")
                        tvCategoryPrice.text = "₹${convertedAmount} L" + " Onwards"
                        tvCategoryArea.text = element.areaStartingFrom + " Sqft Onwards"
                        tvCategoryItemInfo.text = element.shortDescription
                        tvRating.text = "${
                            String.format(
                                "%.0f",
                                element.generalInfoEscalationGraph?.estimatedAppreciation!!.toDouble()
                            )
                        }%"
                        Glide.with(context)
                            .load(element.projectCoverImages?.collectionListViewPageMedia?.value?.url)
                            .into(ivCategoryImage)
                    }
                }

                SIMILAR_INVESTMENT_NI -> {
                    val element =
                        list[position] as com.emproto.networklayer.response.investment.SimilarInvestment
                    binding.cvCategoryOuterCard.setOnClickListener {
                        clickListener.onItemClicked(view, 0, element.id.toString())
                    }
                    binding.tvApplyNowCategory.setOnClickListener {
                        clickListener.onItemClicked(view, 1, element.id.toString())
                    }

                    binding.apply {
                        tvProjectName.text = element.launchName
                        tvCategoryPrice.text = element.priceStartingFrom + " Onwards"
                        tvCategoryArea.text = element.areaStartingFrom + " Onwards"
                        tvCategoryItemInfo.text = element.shortDescription
                        tvRating.text = "${
                            String.format(
                                "%.0f",
                                element.generalInfoEscalationGraph?.estimatedAppreciation!!.toDouble()
                            )
                        }%"
                        Glide.with(context)
                            .load(element.projectCoverImages?.collectionListViewPageMedia?.value?.url)
                            .into(ivCategoryImage)
                    }
                }

                else -> {
                    val element = list[position] as SimilarInvestment
                    binding.cvCategoryOuterCard.setOnClickListener {
                        clickListener.onItemClicked(view, 0, element.id.toString())
                    }
                    binding.tvApplyNowCategory.setOnClickListener {
                        clickListener.onItemClicked(view, 1, element.id.toString())
                    }
                    binding.apply {
                        tvProjectName.text = element.launchName
                        val amount = element.priceStartingFrom.toDouble() / 100000
                        val convertedAmount = amount.toString().replace(".0", "")
                        tvCategoryPrice.text = "₹${convertedAmount} L" + " Onwards"
                        tvCategoryArea.text = element.areaStartingFrom + " Sqft Onwards"
                        tvCategoryItemInfo.text = element.shortDescription
                        tvRating.text =
                            "${String.format("%.0f", element.estimatedAppreciation.toDouble())}%"
                        Glide.with(context)
                            .load(element.projectCoverImages.collectionListViewPageMedia.value.url)
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