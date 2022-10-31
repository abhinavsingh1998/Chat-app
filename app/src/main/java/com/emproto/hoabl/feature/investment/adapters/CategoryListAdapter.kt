package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.core.Constants
import com.emproto.core.Utility
import com.emproto.core.textviews.CustomTextView
import com.emproto.hoabl.databinding.ItemCategoryListBinding
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.response.investment.ApData
import com.emproto.networklayer.response.investment.PageManagementsOrCollectionOneModel
import com.emproto.networklayer.response.investment.PageManagementsOrCollectionTwoModel
import com.emproto.networklayer.response.investment.PageManagementsOrNewInvestment
import com.emproto.networklayer.response.portfolio.ivdetails.SimilarInvestment
import com.emproto.networklayer.response.watchlist.Data
import com.google.android.material.textview.MaterialTextView

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
        const val TYPE_DISCOVER = 5
        const val SIMILAR_INVESTMENT_NI = 6
    }

    inner class CategoryViewHolder(var binding: ItemCategoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            view: View,
            position: Int,
            clickListener: ItemClickListener
        ) {
            when (type) {
                TYPE_NEW_LAUNCH -> {
                    val element = list[position] as PageManagementsOrNewInvestment
//                    binding.cvCategoryOuterCard.setOnClickListener {
//                        clickListener.onItemClicked(view, 0, element.id.toString())
//                    }
//                    binding.tvApplyNowCategory.setOnClickListener {
//                        clickListener.onItemClicked(view, 1, element.id.toString())
//                    }

                    binding.apply {
                        if (element != null) {
                            if(element.isSoldOut){
                                isSoldUI(tvApplyNowCategory,tvSoldOut,clCardLayout,categoryBottomViewBg,clCategoryImage,tvProjectName,tvProjectLocation,tvCategoryItemInfo,tvRating)
                            }
                            cvCategoryOuterCard.setOnClickListener {
                                clickListener.onItemClicked(view, 0, element.id.toString())
                            }
                            tvApplyNowCategory.setOnClickListener {
                                clickListener.onItemClicked(view, 1, element.id.toString())
                            }
                            tvProjectName.text = element.launchName

                            val price = element.priceStartingFrom.toDouble()
                            val value = Utility.currencyConversion(price)
                            tvCategoryPrice.text = value.toString()+Constants.ONWARDS

                            val areaPrice = element.areaStartingFrom + Constants.SQFT_ONWARDS
                            val locationName = element.address.city + "\n" + element.address.state
                            val rating = "${
                                String.format(
                                    "%.0f",
                                    element.generalInfoEscalationGraph.estimatedAppreciation
                                )
                            }%"
                            tvCategoryArea.text = areaPrice
                            tvCategoryItemInfo.text = element.shortDescription
                            tvProjectLocation.text = locationName
                            Glide.with(context)
                                .load(element.projectCoverImages.collectionListViewPageMedia.value.url)
                                .into(ivCategoryImage)
                            tvRating.text = rating
                        }
                    }

                }
                TYPE_FEW_PLOTS -> {
                    val element = list[position] as PageManagementsOrCollectionOneModel
//                    binding.cvCategoryOuterCard.setOnClickListener {
//                        clickListener.onItemClicked(view, 0, element.id.toString())
//                    }
//                    binding.tvApplyNowCategory.setOnClickListener {
//                        clickListener.onItemClicked(view, 1, element.id.toString())
//                    }
                    binding.apply {
                        if (element != null) {
                            if(element.isSoldOut){
                                isSoldUI(
                                    tvApplyNowCategory,
                                    tvSoldOut,
                                    clCardLayout,
                                    categoryBottomViewBg,
                                    clCategoryImage,
                                    tvProjectName,
                                    tvProjectLocation,
                                    tvCategoryItemInfo,
                                    tvRating
                                )
                            }
                            cvCategoryOuterCard.setOnClickListener {
                                clickListener.onItemClicked(view, 0, element.id.toString())}
                                tvApplyNowCategory.setOnClickListener {
                                    clickListener.onItemClicked(view, 1, element.id.toString())}
                                    tvProjectName.text = element.launchName
                            val price = element.priceStartingFrom.toDouble()
                            val value = Utility.currencyConversion(price)
                            tvCategoryPrice.text = value.toString()+Constants.ONWARDS

                                    val categoryArea =
                                        element.areaStartingFrom + Constants.SQFT_ONWARDS
                                    val locationName =
                                        element.address.city + "\n" + element.address.state
                                    val tRating = "${
                                        String.format(
                                            "%.0f",
                                            element.generalInfoEscalationGraph.estimatedAppreciation
                                        )
                                    }%"
                                    tvCategoryArea.text = categoryArea
                                    tvCategoryItemInfo.text = element.shortDescription
                                    tvRating.text = tRating
                                    tvProjectLocation.text = locationName

                                    Glide.with(context)
                                        .load(element.projectCoverImages.collectionListViewPageMedia.value.url)
                                        .into(ivCategoryImage)
                                }
                            }
                        }
                        TYPE_TRENDING_PROJECTS -> {
                            val element = list[position] as PageManagementsOrCollectionTwoModel
//                            binding.cvCategoryOuterCard.setOnClickListener {
//                                clickListener.onItemClicked(view, 0, element.id.toString())
//                            }
//                            binding.tvApplyNowCategory.setOnClickListener {
//                                clickListener.onItemClicked(view, 1, element.id.toString())
//                            }

                            binding.apply {
                                if (element != null) {
                                    if(element.isSoldOut){
                                        isSoldUI(tvApplyNowCategory,tvSoldOut,clCardLayout,categoryBottomViewBg,clCategoryImage,tvProjectName,tvProjectLocation,tvCategoryItemInfo,tvRating)
                                    }
                                    cvCategoryOuterCard.setOnClickListener {
                                        clickListener.onItemClicked(view, 0, element.id.toString())
                                    }
                                    tvApplyNowCategory.setOnClickListener {
                                        clickListener.onItemClicked(view, 1, element.id.toString())
                                    }
                                    tvProjectName.text = element.launchName
                                    val amount = element.priceStartingFrom.toDouble() / 100000
                                    val convertedAmount = amount.toString().replace(".0", "")
                                    val categoryPrice = "₹${convertedAmount} L" + Constants.ONWARDS
                                    val categoryArea =
                                        element.areaStartingFrom + Constants.SQFT_ONWARDS
                                    val locationName =
                                        element.address.city + "\n" + element.address.state
                                    val tRating = "${
                                        String.format(
                                            "%.0f",
                                            element.generalInfoEscalationGraph.estimatedAppreciation
                                        )
                                    }%"
                                    tvCategoryPrice.text = categoryPrice
                                    tvCategoryArea.text = categoryArea
                                    tvCategoryItemInfo.text = element.shortDescription
                                    tvRating.text = tRating
                                    tvProjectLocation.text = locationName

                                    Glide.with(context)
                                        .load(element.projectCoverImages.collectionListViewPageMedia.value.url)
                                        .into(ivCategoryImage)
                                }
                            }
                        }

                        TYPE_ALL_INVESTMENTS -> {
                            val element = list[position] as ApData
//                            binding.cvCategoryOuterCard.setOnClickListener {
//                                clickListener.onItemClicked(view, 0, element.id.toString())
//                            }
//                            binding.tvApplyNowCategory.setOnClickListener {
//                                clickListener.onItemClicked(view, 1, element.id.toString())
//                            }

                            binding.apply {
                                if (element != null) {
                                    if(element.isSoldOut){
                                        isSoldUI(tvApplyNowCategory,tvSoldOut,clCardLayout,categoryBottomViewBg,clCategoryImage,tvProjectName,tvProjectLocation,tvCategoryItemInfo,tvRating)
                                    }
                                    cvCategoryOuterCard.setOnClickListener {
                                        clickListener.onItemClicked(view, 0, element.id.toString())
                                    }
                                    tvApplyNowCategory.setOnClickListener {
                                        clickListener.onItemClicked(view, 1, element.id.toString())
                                    }
                                    tvProjectName.text = element.launchName
                                    val amount = element.priceStartingFrom.toDouble() / 100000
                                    val convertedAmount = amount.toString().replace(".0", "")
                                    val categoryPrice = "₹${convertedAmount} L" + Constants.ONWARDS
                                    val categoryIemInfo =
                                        element.areaStartingFrom + Constants.SQFT_ONWARDS
                                    val locationName =
                                        element.address.city + "\n" + element.address.state

                                    val rating = "${
                                        String.format(
                                            "%.0f",
                                            element.generalInfoEscalationGraph.estimatedAppreciation
                                        )
                                    }%"
                                    tvCategoryPrice.text = categoryPrice
                                    tvCategoryArea.text = categoryIemInfo
                                    tvCategoryItemInfo.text = element.shortDescription
                                    tvRating.text = rating
                                    tvProjectLocation.text = locationName

                                    Glide.with(context)
                                        .load(element.projectCoverImages.collectionListViewPageMedia.value.url)
                                        .into(ivCategoryImage)
                                }
                            }
                        }
                        TYPE_WATCHLIST -> {
                            val element = list[position] as Data
//                            binding.cvCategoryOuterCard.setOnClickListener {
//                                clickListener.onItemClicked(view, 0, element.project.id.toString())
//                            }
//                            binding.tvApplyNowCategory.setOnClickListener {
//                                clickListener.onItemClicked(view, 1, element.project.id.toString())
//                            }

                            binding.apply {
                                if (element != null) {
                                    if(element.project.isSoldOut){
                                        isSoldUI(tvApplyNowCategory,tvSoldOut,clCardLayout,categoryBottomViewBg,clCategoryImage,tvProjectName,tvProjectLocation,tvCategoryItemInfo,tvRating)
                                    }

                                    cvCategoryOuterCard.setOnClickListener {
                                        clickListener.onItemClicked(view, 0, element.project.id.toString())
                                    }
                                    tvApplyNowCategory.setOnClickListener {
                                        clickListener.onItemClicked(view, 1, element.project.id.toString())
                                    }
                                    tvProjectName.text = element.project.launchName
                                    val amount =
                                        element.project.priceStartingFrom.toDouble() / 100000
                                    val convertedAmount = amount.toString().replace(".0", "")
                                    val categoryPrice = "₹${convertedAmount} L" + Constants.ONWARDS
                                    val categoryArea =
                                        element.project.areaStartingFrom + Constants.SQFT_ONWARDS
                                    val locationName =
                                        element.project.address.city + "\n" + element.project.address.state

                                    tvCategoryPrice.text = categoryPrice
                                    tvCategoryArea.text = categoryArea
                                    tvCategoryItemInfo.text = element.project.shortDescription
                                    tvProjectLocation.text = locationName
                                    Glide.with(context)
                                        .load(element.project.projectCoverImages.collectionListViewPageMedia.value.url)
                                        .into(ivCategoryImage)
                                }
                            }
                        }

                        TYPE_DISCOVER -> {
                            val element = list[position] as ApData
//                            binding.cvCategoryOuterCard.setOnClickListener {
//                                clickListener.onItemClicked(view, 0, element.id.toString())
//                            }
//                            binding.tvApplyNowCategory.setOnClickListener {
//                                clickListener.onItemClicked(view, 1, element.id.toString())
//                            }

                            binding.apply {
                                if (element != null) {
                                    if(element.isSoldOut){
                                        isSoldUI(tvApplyNowCategory,tvSoldOut,clCardLayout,categoryBottomViewBg,clCategoryImage,tvProjectName,tvProjectLocation,tvCategoryItemInfo,tvRating)
                                    }
                                    cvCategoryOuterCard.setOnClickListener {
                                        clickListener.onItemClicked(view, 0, element.id.toString())
                                    }
                                    tvApplyNowCategory.setOnClickListener {
                                        clickListener.onItemClicked(view, 1, element.id.toString())
                                    }

                                    tvProjectName.text = element.launchName
                                    val amount = element.priceStartingFrom.toDouble() / 100000
                                    val convertedAmount = amount.toString().replace(".0", "")
                                    val categoryPrice = "₹${convertedAmount} L" + Constants.ONWARDS
                                    val categoryArea =
                                        element.areaStartingFrom + Constants.SQFT_ONWARDS
                                    val locationName =
                                        element.address.city + "\n" + element.address.state

                                    val rating = "${
                                        String.format(
                                            "%.0f",
                                            element.generalInfoEscalationGraph.estimatedAppreciation
                                        )
                                    }%"
                                    tvCategoryPrice.text = categoryPrice
                                    tvCategoryArea.text = categoryArea
                                    tvProjectLocation.text = locationName

                                    tvCategoryItemInfo.text = element.shortDescription
                                    tvRating.text = rating
                                    Glide.with(context)
                                        .load(element.projectCoverImages.collectionListViewPageMedia.value.url)
                                        .into(ivCategoryImage)
                                }
                            }
                        }
                        SIMILAR_INVESTMENT_NI -> {
                            val element =
                                list[position] as com.emproto.networklayer.response.investment.SimilarInvestment
//                            binding.cvCategoryOuterCard.setOnClickListener {
//                                clickListener.onItemClicked(view, 0, element.id.toString())
//                            }
//                            binding.tvApplyNowCategory.setOnClickListener {
//                                clickListener.onItemClicked(view, 1, element.id.toString())
//                            }

                            binding.apply {
                                if (element != null) {
                                    if(element.isSoldOut){
                                        isSoldUI(tvApplyNowCategory,tvSoldOut,clCardLayout,categoryBottomViewBg,clCategoryImage,tvProjectName,tvProjectLocation,tvCategoryItemInfo,tvRating)
                                    }
                                    cvCategoryOuterCard.setOnClickListener {
                                        clickListener.onItemClicked(view, 0, element.id.toString())
                                    }
                                    tvApplyNowCategory.setOnClickListener {
                                        clickListener.onItemClicked(view, 1, element.id.toString())
                                    }
                                    tvProjectName.text = element.launchName
                                    val amount = element.priceStartingFrom!!.toDouble() / 100000
                                    val convertedAmount = amount.toString().replace(".0", "")
                                    val categoryPrice = "₹${convertedAmount} L" + Constants.ONWARDS
                                    val locationName =
                                        element.address.city + "\n" + element.address.state

                                    val rating = "${
                                        String.format(
                                            "%.0f",
                                            element.generalInfoEscalationGraph?.estimatedAppreciation!!.toDouble()
                                        )
                                    }%"
                                    tvCategoryPrice.text = categoryPrice
                                    val categoryArea =
                                        element.areaStartingFrom + Constants.SQFT_ONWARDS
                                    tvCategoryArea.text = categoryArea
                                    tvProjectLocation.text = locationName
                                    tvCategoryItemInfo.text = element.shortDescription
                                    tvRating.text = rating
                                    Glide.with(context)
                                        .load(element.projectCoverImages?.collectionListViewPageMedia?.value?.url)
                                        .into(ivCategoryImage)
                                }
                            }
                        }

                        else -> {
                        val element = list[position] as SimilarInvestment
//                        binding.cvCategoryOuterCard.setOnClickListener {
//                            clickListener.onItemClicked(view, 0, element.id.toString())
//                        }
//                        binding.tvApplyNowCategory.setOnClickListener {
//                            clickListener.onItemClicked(view, 1, element.id.toString())
//                        }
                        binding.apply {
                            if (element != null) {
                                cvCategoryOuterCard.setOnClickListener {
                                    clickListener.onItemClicked(view, 0, element.id.toString())
                                }
                                tvApplyNowCategory.setOnClickListener {
                                    clickListener.onItemClicked(view, 1, element.id.toString())
                                }
                                tvProjectName.text = element.launchName
                                val amount = element.priceStartingFrom.toDouble() / 100000
                                val convertedAmount = amount.toString().replace(".0", "")
                                val categoryPrice = "₹${convertedAmount} L" + Constants.ONWARDS
                                val categoryArea = element.areaStartingFrom + " Sqft Onwards"
                                val rating =
                                    "${String.format("%.0f", element.estimatedAppreciation)}%"
                                val locationName =
                                    element.address.city + "\n" + element.address.state

                                tvCategoryPrice.text = categoryPrice
                                tvCategoryArea.text = categoryArea
                                tvCategoryItemInfo.text = element.shortDescription
                                tvProjectLocation.text = locationName

                                tvRating.text = rating

                                Glide.with(context)
                                    .load(element.projectCoverImages.collectionListViewPageMedia.value.url)
                                    .into(ivCategoryImage)
                            }
                        }
                    }
            }
        }

        private fun isSoldUI(
            tvApplyNowCategory: CustomTextView,
            tvSoldOut: TextView,
            clCardLayout: ConstraintLayout,
            categoryBottomViewBg: ImageView,
            clCategoryImage: ConstraintLayout,
            tvProjectName: MaterialTextView,
            tvProjectLocation: MaterialTextView,
            tvCategoryItemInfo: MaterialTextView,
            tvRating: MaterialTextView
        ) {
            tvApplyNowCategory.visibility=View.INVISIBLE
            tvSoldOut.visibility=View.VISIBLE
            clCardLayout.setBackgroundColor(Color.parseColor("#8b8b8b"))
            categoryBottomViewBg.setBackgroundColor(Color.parseColor("#8b8b8b"))
            clCategoryImage.setBackgroundColor(Color.parseColor("#99000000"))

            tvProjectName.setTextColor(Color.parseColor("#ffffff"))
            tvProjectLocation.setTextColor(Color.parseColor("#ffffff"))
            tvCategoryItemInfo.setTextColor(Color.parseColor("#ffffff"))
            tvRating.setTextColor(Color.parseColor("#C6E8CF"))

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            ItemCategoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        list[position]
        holder.bind(holder.itemView, position, itemClickListener!!)

    }

    override fun getItemCount(): Int = list.size

}