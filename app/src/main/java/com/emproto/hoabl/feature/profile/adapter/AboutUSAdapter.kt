package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.CorporatePhilosphyAboutUsBinding
import com.emproto.hoabl.databinding.ProductCategoriesBinding
import com.emproto.hoabl.databinding.StatsAboutusBinding
import com.emproto.hoabl.feature.profile.data.AboutusData
import com.emproto.hoabl.feature.profile.data.CorporateAboutusData
import com.emproto.hoabl.feature.profile.data.StatsAboutusData
import com.emproto.hoabl.model.RecyclerViewItem

class AboutUSAdapter(private val context: Context, private val list: ArrayList<RecyclerViewItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_CORPORATE_PHILOSOPHY = 1
        const val VIEW_PRODUCT_CATEGORY = 2
        const val VIEW_LIFESTYLE = 3
    }

    private lateinit var productAdapter: ProductAdapter
    private lateinit var corporatePhilosphyAdapter: CorporatePhilosphyAdapter
    private lateinit var statsOverViewAboutUsAdapter: StatsOverViewAboutUsAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            AboutUSAdapter.VIEW_CORPORATE_PHILOSOPHY -> {
                CorporatePhilosphyViewHolder(
                    CorporatePhilosphyAboutUsBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            AboutUSAdapter.VIEW_PRODUCT_CATEGORY -> {
                ProductCategoryViewHolder(
                    ProductCategoriesBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                StatsOverviewHolder(
                    StatsAboutusBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (list[position].viewType) {
            AboutUSAdapter.VIEW_CORPORATE_PHILOSOPHY -> {
         //       (holder as AboutUSAdapter.CorporatePhilosphyViewHolder).bind(position)
            }
            AboutUSAdapter.VIEW_LIFESTYLE -> {
                (holder as AboutUSAdapter.StatsOverviewHolder).bind(position)
            }
            AboutUSAdapter.VIEW_PRODUCT_CATEGORY -> {
                (holder as AboutUSAdapter.ProductCategoryViewHolder).bind(position)
            }

        }
    }

    private inner class CorporatePhilosphyViewHolder(private val binding: CorporatePhilosphyAboutUsBinding) :
        RecyclerView.ViewHolder(binding.root) {
//        fun bind(position: Int) {
//            binding.corporatePhillosophyRecyclerList.layoutManager =
//                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        //    corporatePhilosphyAdapter = CorporatePhilosphyAdapter(context, init())
//            binding.corporatePhillosophyRecyclerList.adapter = corporatePhilosphyAdapter
//
//        }
    }

//    private fun init(): ArrayList<CorporateAboutusData> {
////        val corporateList: ArrayList<CorporateAboutusData> = ArrayList<CorporateAboutusData>()
////        corporateList.add(
////            CorporateAboutusData(
////                R.drawable.management,
////                "Peace of Land",
////                "For HoABL, it is not just a piece of land, we believe in the Peace of Land.It is our aim to give back to land more than we take from it.",
////            )
//        )
////        corporateList.add(
////            CorporateAboutusData(
////                R.drawable.isustainable_image,
////                "Sustainability",
////                "For HoABL, it is not just a piece of land, we believe in the Peace of Land.It is our aim to give back to land more than we take from it.",
////            )
////        )
//
//
//        return corporateList
//    }

    private inner class StatsOverviewHolder(private val binding: StatsAboutusBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.statsOverviewRecyclerList.layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            statsOverViewAboutUsAdapter = StatsOverViewAboutUsAdapter(context, initialize())
            binding.statsOverviewRecyclerList.adapter = statsOverViewAboutUsAdapter
        }
    }

    private fun initialize(): ArrayList<StatsAboutusData> {
        val statsList: ArrayList<StatsAboutusData> = ArrayList<StatsAboutusData>()
        statsList.add(
            StatsAboutusData(
                "₹4,86,00,000 Cr",
                "Land Transacted",
                "Neque porro quisquam estp qui dolorem ipsu porro quisqua m estp qui ipsu.Neque porro quisquam estp qui dolorem ip"
            )
        )
        statsList.add(
            StatsAboutusData(
                "₹4,86,00,000 Cr",
                "Land Transacted",
                "Neque porro quisquam estp qui dolorem ipsu porro quisqua m estp qui ipsu.Neque porro quisquam estp qui dolorem ip"
            )
        )
        statsList.add(
            StatsAboutusData(
                "₹4,86,00,000 Cr",
                "Gross w.avg Appreciation",
                "Neque porro quisquam estp qui dolorem ipsu porro quisqua m estp qui ipsu.Neque porro quisquam estp qui dolorem ip"
            )
        )
        statsList.add(
            StatsAboutusData(
                "₹4,86,00,000 Cr",
                "Number of Investors",
                "Neque porro quisquam estp qui dolorem ipsu porro quisqua m estp qui ipsu.Neque porro quisquam estp qui dolorem ip"
            )
        )


        return statsList
    }

    private fun initData(): ArrayList<AboutusData> {
        val aboutusList: ArrayList<AboutusData> = ArrayList<AboutusData>()
        aboutusList.add(
            AboutusData(
                R.drawable.location_image,
                "Lifestyle",
                "For HoABL, it is not just a piece of land, we believe in the Peace of Land.It is our aim to give back to land more than we take from it.",
            )
        )
        aboutusList.add(
            AboutusData(
                R.drawable.location_image,
                "Investment",
                "For HoABL, it is not just a piece of land, we believe in the Peace of Land.It is our aim to give back to land more than we take from it.",
            )
        )
        aboutusList.add(
            AboutusData(
                R.drawable.location_image,
                "Consumption",
                "For HoABL, it is not just a piece of land, we believe in the Peace of Land.It is our aim to give back to land more than we take from it.",
            )
        )

        return aboutusList
    }

    private inner class ProductCategoryViewHolder(private val binding: ProductCategoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.productRecyclerList.layoutManager = LinearLayoutManager(context)
       //     productAdapter = ProductAdapter(context, initData())
            binding.productRecyclerList.adapter = productAdapter

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

}