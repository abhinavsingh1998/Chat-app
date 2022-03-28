package com.emproto.hoabl.feature.home.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.emproto.core.BaseFragment
import com.emproto.hoabl.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.adapters.InvestmentViewPagerAdapter
import com.emproto.hoabl.databinding.FragmentInvestmentBinding
import com.emproto.hoabl.feature.home.adapters.InvestmentAdapter
import com.emproto.hoabl.model.ViewItem

class InvestmentFragment : BaseFragment(),View.OnClickListener {

    private lateinit var binding: FragmentInvestmentBinding
    private lateinit var adapter: InvestmentViewPagerAdapter
    private lateinit var investmentAdapter: InvestmentAdapter
    private lateinit var smartDealsLinearLayoutManager: LinearLayoutManager
    private lateinit var trendingProjectsLinearLayoutManager: LinearLayoutManager

    private var listViews = ArrayList<ViewItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentInvestmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
        setUpViewPager()
        setUpSmartDeals()
        setUpTrendingProjects()
    }

    private fun setClickListeners() {
        binding.clOuterCard.setOnClickListener(this)
        binding.tvSmartDealSeeAll.setOnClickListener(this)
    }

    private val viewListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            val pos = position
        }
    }

    private fun setUpViewPager() {
        listViews = ArrayList()
        listViews.add(ViewItem(1, R.drawable.new_investment_page_image))
        listViews.add(ViewItem(2, R.drawable.new_investment_page_image))
        listViews.add(ViewItem(3, R.drawable.new_investment_page_image))
        listViews.add(ViewItem(4, R.drawable.new_investment_page_image))
        listViews.add(ViewItem(5, R.drawable.new_investment_page_image))

        adapter = InvestmentViewPagerAdapter(listViews)
        binding.viewPager.adapter = adapter

        binding.viewPager.registerOnPageChangeCallback(viewListener)
    }

    private fun setUpSmartDeals() {
        val list: ArrayList<String> = ArrayList()
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")

        investmentAdapter = InvestmentAdapter(requireActivity(), list, "SmartDeals")
        smartDealsLinearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvSmartDeals.layoutManager = smartDealsLinearLayoutManager
        binding.rvSmartDeals.adapter = investmentAdapter
    }

    private fun setUpTrendingProjects() {
        val list: ArrayList<String> = ArrayList()
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")

        investmentAdapter = InvestmentAdapter(requireActivity(), list)
        trendingProjectsLinearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvTrendingProjects.layoutManager= trendingProjectsLinearLayoutManager
        binding.rvTrendingProjects.adapter= investmentAdapter
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.cl_outer_card -> { (requireActivity() as HomeActivity).addFragment(ProjectDetailFragment(),false) }
            R.id.tv_smart_deal_see_all -> { (requireActivity() as HomeActivity).addFragment(CategoryListFragment(),false)}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewPager.unregisterOnPageChangeCallback(viewListener)
    }

}