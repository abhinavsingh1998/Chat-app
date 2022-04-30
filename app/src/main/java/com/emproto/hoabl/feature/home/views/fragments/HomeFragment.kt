package com.emproto.hoabl.feature.home.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
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
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.home.HomeResponse
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
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel = ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]
        initView()
        initClickListener()
        referNow()
        initObserver()
        return binding.root
    }

    private fun initObserver() {
        homeViewModel.getDashboardData(5001)
            .observe(viewLifecycleOwner, object : Observer<BaseResponse<HomeResponse>> {
                override fun onChanged(it: BaseResponse<HomeResponse>?) {
                    when (it!!.status) {
                        Status.LOADING -> {
                            binding.rootView.hide()
                            binding.loader.show()

                        }
                        Status.SUCCESS -> {
                            binding.rootView.show()
                            binding.loader.hide()
                            //loading investment list
                            investmentAdapter = InvestmentAdapter(
                                requireActivity(),
                                it.data!!.data.pageManagementsOrNewInvestments
                            )
                            linearLayoutManager = LinearLayoutManager(
                                requireContext(),
                                RecyclerView.HORIZONTAL,
                                false
                            )
                            binding.investmentList.layoutManager = linearLayoutManager
                            binding.investmentList.adapter = investmentAdapter


                            //loading latestUpdate list
                            latestUpdateAdapter = LatestUpdateAdapter(
                                requireActivity(),
                                it.data!!.data.pageManagementOrLatestUpdates
                            )
                            linearLayoutManager = LinearLayoutManager(
                                requireContext(),
                                RecyclerView.HORIZONTAL,
                                false
                            )
                            binding.latesUpdatesRecyclerview.layoutManager = linearLayoutManager
                            binding.latesUpdatesRecyclerview.adapter = latestUpdateAdapter

                            //loading Promises list
                            hoABLPromisesAdapter = HoABLPromisesAdapter(
                                requireActivity(),
                                it.data!!.data.homePagesOrPromises
                            )
                            linearLayoutManager = LinearLayoutManager(
                                requireContext(),
                                RecyclerView.HORIZONTAL,
                                false
                            )
                            binding.hoablPromisesRecyclerview.layoutManager = linearLayoutManager
                            binding.hoablPromisesRecyclerview.adapter = hoABLPromisesAdapter

                            //loading insights list
                            insightsAdapter = InsightsAdapter(
                                requireActivity(),
                                it.data!!.data.pageManagementOrInsights
                            )
                            linearLayoutManager = LinearLayoutManager(
                                requireContext(),
                                RecyclerView.HORIZONTAL,
                                false
                            )
                            binding.insightsRecyclerview.layoutManager = linearLayoutManager
                            binding.insightsRecyclerview.adapter = insightsAdapter


                            //loading Testimonial Cards list
                            testimonialAdapter = TestimonialAdapter(requireActivity(), it.data!!.data.pageManagementsOrTestimonials)
                            binding.testimonialsRecyclerview.adapter = testimonialAdapter
                            TabLayoutMediator(binding.tabDotLayout, binding.testimonialsRecyclerview) { _, _ ->
                            }.attach()

                        }
                        Status.ERROR -> {
                            //binding.loader.hide()
                            (requireActivity() as HomeActivity).showErrorToast(
                                it.message!!
                            )
                        }
                    }
                }

            })
    }

    private fun initView() {
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility =
            View.VISIBLE
        (requireActivity() as HomeActivity).showBottomNavigation()


        val list: ArrayList<String> = ArrayList()
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")

        val pymentList: ArrayList<String> = arrayListOf("1", "2", "3", "4", "5")


        //Pending Payment Card
        pendingPaymentsAdapter = PendingPaymentsAdapter(requireActivity(), pymentList)
        binding.kycLayoutCard.adapter = pendingPaymentsAdapter
        TabLayoutMediator(binding.tabDot, binding.kycLayoutCard) { _, _ ->
        }.attach()
    }

    fun initClickListener() {
        binding.tvSeeallInsights.setOnClickListener {
            (requireActivity() as HomeActivity).addFragment(
                InsightsAndUpdatesFragment.newInstance(),
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