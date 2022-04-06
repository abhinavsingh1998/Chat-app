package com.emproto.hoabl.feature.home.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseFragment
import com.emproto.hoabl.HomeActivity
import com.emproto.hoabl.MVVM.home.HomeViewModel
import com.emproto.hoabl.adapters.HoABLPromisesAdapter
import com.emproto.hoabl.adapters.InsightsAdapter
import com.emproto.hoabl.feature.investment.adapters.InvestmentAdapter
import com.emproto.hoabl.adapters.LatestUpdateAdapter
import com.emproto.hoabl.adapters.TestimonialAdapter
import com.emproto.hoabl.databinding.FragmentHomeBinding


class HomeFragment : BaseFragment() {

    lateinit var binding: FragmentHomeBinding
    private lateinit var investmentAdapter: InvestmentAdapter
    private lateinit var insightsAdapter: InsightsAdapter
    private lateinit var testimonialAdapter: TestimonialAdapter
    private lateinit var latestUpdateAdapter: LatestUpdateAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var hoABLPromisesAdapter: HoABLPromisesAdapter

    lateinit var homeViewModel: HomeViewModel

    /*@Inject
    lateinit var factory: HomeFactory*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        //homeViewModel= ViewModelProvider(this,factory)[HomeViewModel::class.java]
        initView()
        initClickListener()
        return binding.root
    }


    private fun initView() {
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible=true

        val list: ArrayList<String> = ArrayList()
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")

        val listPromises: ArrayList<String> = arrayListOf("Security", "Transparency", "wealth")

        investmentAdapter = InvestmentAdapter(requireActivity(), list)
        linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.investmentRecyclerview.layoutManager = linearLayoutManager
        binding.investmentRecyclerview.adapter = investmentAdapter


        insightsAdapter = InsightsAdapter(requireActivity(), list)
        linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.insightsRecyclerview.layoutManager = linearLayoutManager
        binding.insightsRecyclerview.adapter = insightsAdapter

        testimonialAdapter = TestimonialAdapter(requireActivity(), list)
        linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.testimonialsRecyclerview.layoutManager = linearLayoutManager
        binding.testimonialsRecyclerview.adapter = testimonialAdapter

        latestUpdateAdapter = LatestUpdateAdapter(requireActivity(), list)
        linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.latesUpdatesRecyclerview.layoutManager = linearLayoutManager
        binding.latesUpdatesRecyclerview.adapter = latestUpdateAdapter

        hoABLPromisesAdapter = HoABLPromisesAdapter(requireActivity(), listPromises)
        linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.hoablPromisesRecyclerview.layoutManager = linearLayoutManager
        binding.hoablPromisesRecyclerview.adapter = hoABLPromisesAdapter
    }

    fun initClickListener() {
        binding.tvSeeallInsights.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                (requireActivity() as HomeActivity).addFragment(
                    InsightsAndUpdatesFragment.newInstance(),
                    true
                )
            }

        })

        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.search.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
//                (requireActivity() as HomeActivity).addFragment(SearchResultFragment.newInstance(), true)
            }
        })

    }


}