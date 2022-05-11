package com.emproto.hoabl.feature.home.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseFragment
import com.emproto.hoabl.adapters.InsightsAdapter
import com.emproto.hoabl.adapters.LatestUpdateAdapter
import com.emproto.hoabl.adapters.TestimonialAdapter
import com.emproto.hoabl.databinding.FragmentHomeBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.adapters.HoABLPromisesAdapter
import com.emproto.hoabl.feature.home.adapters.PendingPaymentsAdapter
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.adapters.InvestmentAdapter
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.response.investment.SimilarInvestment
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject


class HomeFragment : BaseFragment() {

    lateinit var binding: FragmentHomeBinding
    private lateinit var investmentAdapter: InvestmentAdapter
    private lateinit var insightsAdapter: InsightsAdapter
    private lateinit var testimonialAdapter: TestimonialAdapter
    private lateinit var latestUpdateAdapter: LatestUpdateAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var hoABLPromisesAdapter: HoABLPromisesAdapter
    private lateinit var pendingPaymentsAdapter: PendingPaymentsAdapter


    private lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var factory: HomeFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel = ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]
        initView()
        initClickListener()
        referNow()
        return binding.root
    }

    private fun initView() {
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility =
            View.VISIBLE
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =
            true

        val list: ArrayList<String> = ArrayList()
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")

        val invList = arrayListOf<SimilarInvestment>()

        val listPromises: ArrayList<String> = arrayListOf("Security", "Transparency", "wealth")
        val listTostimonials: ArrayList<String> = arrayListOf("Rajeev Kumar",
            "Mukesh Singh",
            "Rohit Roy",
            "Rajeev Kumar",
            "Mukesh Singh",
            "Rohit Roy")
        val pymentList: ArrayList<String> = arrayListOf("1", "2", "3", "4", "5")

        investmentAdapter = InvestmentAdapter(requireActivity(), invList)
        linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.investmentRecyclerview.layoutManager = linearLayoutManager
        binding.investmentRecyclerview.adapter = investmentAdapter


        insightsAdapter = InsightsAdapter(requireActivity(), list)
        linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.insightsRecyclerview.layoutManager = linearLayoutManager
        binding.insightsRecyclerview.adapter = insightsAdapter

        //Testimonial Cards
        testimonialAdapter = TestimonialAdapter(requireActivity(), listTostimonials)
        binding.testimonialsRecyclerview.adapter = testimonialAdapter
        TabLayoutMediator(binding.tabDotLayout, binding.testimonialsRecyclerview) { _, _ ->
        }.attach()


        //Pending Payment Card
        pendingPaymentsAdapter = PendingPaymentsAdapter(requireActivity(), pymentList)
        binding.kycLayoutCard.adapter = pendingPaymentsAdapter
        TabLayoutMediator(binding.tabDot, binding.kycLayoutCard) { _, _ ->
        }.attach()



        latestUpdateAdapter = LatestUpdateAdapter(requireActivity(), list)
        linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.latesUpdatesRecyclerview.layoutManager = linearLayoutManager
        binding.latesUpdatesRecyclerview.adapter = latestUpdateAdapter

//        hoABLPromisesAdapter = HoABLPromisesAdapter(requireActivity(), listPromises)
//        linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
//        binding.hoablPromisesRecyclerview.layoutManager = linearLayoutManager
//        binding.hoablPromisesRecyclerview.adapter = hoABLPromisesAdapter
    }

    fun initClickListener() {
        binding.tvSeeallInsights.setOnClickListener {
            (requireActivity() as HomeActivity).addFragment(
                InsightsAndUpdatesFragment.newInstance(),
                true
            )
        }

        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.search.setOnClickListener {
            (requireActivity() as HomeActivity).addFragment(
                SearchResultFragment.newInstance(),
                true
            )
        }

    }

    private fun referNow() {

        binding.referBtn.setOnClickListener {

            val dialog = ReferralDialog()
            dialog.isCancelable = true
            dialog.show(parentFragmentManager, "Refrral card")

        }
    }

}