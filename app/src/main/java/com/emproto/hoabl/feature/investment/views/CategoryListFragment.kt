package com.emproto.hoabl.feature.investment.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.emproto.core.BaseFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentCategoryListBinding
import com.emproto.hoabl.feature.investment.adapters.CategoryListAdapter
import com.emproto.hoabl.utils.ItemClickListener

class CategoryListFragment:BaseFragment(),View.OnClickListener {

    private lateinit var binding:FragmentCategoryListBinding
    private lateinit var categoryListAdapter: CategoryListAdapter

    private val priceSpinnerArray = arrayListOf<String>(">=100",">=1000",">=10000")
    private val areaSpinnerArray = arrayListOf<String>("1-100 sqft","100-1000 sqft","1000-10000 sqft")
    private val validitySpinnerArray = arrayListOf<String>("upto 10 days","upto 100 days","upto 1 year")
    private var priceCheck = 0
    private var areaCheck = 0
    private var validityCheck = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCategoryListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        setUpClickListeners()
        setUpCategoryAdapter()
    }

    private fun setUpUI() {
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility = View.GONE
    }


    private fun setUpClickListeners() {
//        binding.cvFilterPriceCard.setOnClickListener(this)
//        binding.cvFilterAreaCard.setOnClickListener(this)
//        binding.cvFilterValidityCard.setOnClickListener(this)
    }

    private fun setUpCategoryAdapter() {
        val list = arrayListOf<String>("1","2","3","4","5","6","7","8","9","10")
        categoryListAdapter = CategoryListAdapter(list,itemClickListener)
        binding.rvCategoryList.adapter = categoryListAdapter
    }

    override fun onClick(v: View) {
        when(v.id){
//            R.id.cv_filter_price_card -> binding.priceSpinner.performClick()
//            R.id.cv_filter_area_card -> binding.areaSpinner.performClick()
//            R.id.cv_filter_validity_card -> binding.validitySpinner.performClick()
        }
    }

    private val itemClickListener = object : ItemClickListener {
        override fun onItemClicked(view: View, position: Int, item: String) {
            val projectDetailFragment = ProjectDetailFragment()
            (requireActivity() as HomeActivity).replaceFragment(projectDetailFragment.javaClass, "", true, null, null, 0, false)
        }
    }

}