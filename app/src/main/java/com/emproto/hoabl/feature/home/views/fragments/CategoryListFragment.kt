package com.emproto.hoabl.feature.home.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentCategoryListBinding
import com.emproto.hoabl.feature.home.adapters.CategoryListAdapter

class CategoryListFragment:BaseFragment() {

    private lateinit var binding:FragmentCategoryListBinding
    private lateinit var categoryListAdapter: CategoryListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCategoryListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpCategoryAdapter()
    }

    private fun setUpCategoryAdapter() {
        val list = arrayListOf<String>("1","2","3","4","5","6","7","8","9","10")
        categoryListAdapter = CategoryListAdapter(list)
        binding.rvCategoryList.adapter = categoryListAdapter
    }
}