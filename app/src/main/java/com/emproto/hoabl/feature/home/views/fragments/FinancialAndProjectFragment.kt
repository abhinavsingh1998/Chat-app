package com.emproto.hoabl.feature.home.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentFinancialAndProjectBinding
import com.emproto.hoabl.feature.home.adapters.PortfolioPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class FinancialAndProjectFragment : BaseFragment(){

    val tabNames = arrayOf(
        "Financial Summary",
        "Manage Projects"
    )

    lateinit var binding: FragmentFinancialAndProjectBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFinancialAndProjectBinding.inflate(layoutInflater)


       tabLayout=  binding.testimonialsTab
       viewPager= binding.viewPager2

        val adapter = PortfolioPagerAdapter(fragmentManager, lifecycle)
        viewPager.adapter = adapter

        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabNames[position]
        }.attach()

        return binding.root

    }

}