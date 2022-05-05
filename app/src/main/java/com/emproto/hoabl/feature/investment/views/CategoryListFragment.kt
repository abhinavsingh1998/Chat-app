package com.emproto.hoabl.feature.investment.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.emproto.core.BaseFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentCategoryListBinding
import com.emproto.hoabl.feature.investment.adapters.CategoryListAdapter
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.response.investment.PageManagementsOrCollectionOneModel
import com.emproto.networklayer.response.investment.PageManagementsOrCollectionTwoModel

class CategoryListFragment() :BaseFragment() {

    private lateinit var binding:FragmentCategoryListBinding
    private lateinit var categoryListAdapter: CategoryListAdapter

    private var type : String? = "Smart Deals"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCategoryListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        type = arguments?.getString("Category")
        val data = when (type) {
            "Smart Deals" -> { arguments?.getSerializable("SmartDealsData") as List<PageManagementsOrCollectionOneModel> }
            else -> { arguments?.getSerializable("TrendingProjectsData") as List<PageManagementsOrCollectionOneModel> }
        }
        setUpUI()
        setUpClickListeners()
        setUpCategoryAdapter(data)
    }

    private fun setUpUI() {
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility = View.GONE
        binding.tvCategoryHeading.text = type
    }


    private fun setUpClickListeners() {

    }

    private fun setUpCategoryAdapter(list: List<PageManagementsOrCollectionOneModel>) {
        categoryListAdapter = CategoryListAdapter(this.requireContext(),list,itemClickListener)
        binding.rvCategoryList.adapter = categoryListAdapter
    }

    private val itemClickListener = object : ItemClickListener {
        override fun onItemClicked(view: View, position: Int, item: String) {
            val projectDetailBundle = Bundle()
            projectDetailBundle.putInt("ProjectId",item.toInt())
            val projectDetailFragment = ProjectDetailFragment()
            (requireActivity() as HomeActivity).replaceFragment(projectDetailFragment.javaClass, "", true, projectDetailBundle, null, 0, false)
        }
    }

}