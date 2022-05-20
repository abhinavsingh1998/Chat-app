package com.emproto.hoabl.feature.home.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseFragment
import com.emproto.hoabl.feature.home.adapters.InsightsAdapter
import com.emproto.hoabl.adapters.LatestUpdateAdapter
import com.emproto.hoabl.adapters.TestimonialAdapter
import com.emproto.hoabl.databinding.FragmentHomeBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.adapters.HoABLPromisesAdapter1
import com.emproto.hoabl.feature.home.adapters.InvestmentCardAdapter
import com.emproto.hoabl.feature.home.adapters.PendingPaymentsAdapter
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.home.HomeResponse
import com.google.android.material.tabs.TabLayoutMediator
import com.emproto.hoabl.feature.chat.views.fragments.ChatsFragment
import javax.inject.Inject


class HomeFragment : BaseFragment() {

    lateinit var binding: FragmentHomeBinding
    private lateinit var investmentAdapter: InvestmentCardAdapter
    private lateinit var insightsAdapter: InsightsAdapter
    private lateinit var testimonialAdapter: TestimonialAdapter
    private lateinit var latestUpdateAdapter: LatestUpdateAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var hoABLPromisesAdapter: HoABLPromisesAdapter1
    private lateinit var pendingPaymentsAdapter: PendingPaymentsAdapter

    val appURL= "https://hoabl.in/"

    @Inject
    lateinit var factory: HomeFactory
    lateinit var homeViewModel: HomeViewModel

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
                            investmentAdapter = InvestmentCardAdapter(
                                requireActivity(),
                                it.data!!.data.page.pageManagementsOrNewInvestments
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
                            hoABLPromisesAdapter = HoABLPromisesAdapter1(
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

        (requireActivity() as HomeActivity).hideBackArrow()
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility =
            View.VISIBLE
        (requireActivity() as HomeActivity).showBottomNavigation()



        val pymentList: ArrayList<String> = arrayListOf("1", "2", "3", "4", "5")


        //Pending Payment Card
        pendingPaymentsAdapter = PendingPaymentsAdapter(requireActivity(), pymentList)
        binding.kycLayoutCard.adapter = pendingPaymentsAdapter
        TabLayoutMediator(binding.tabDot, binding.kycLayoutCard) { _, _ ->
        }.attach()
    }

    fun initClickListener() {
        binding.tvSeeallInsights.setOnClickListener {
            (requireActivity() as HomeActivity).replaceFragment(InsightsFragment::class.java,
                "",
                true,
                null,
                null,
                0,
                false)
        }
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.headset.setOnClickListener {
            val bundle = Bundle()
            val chatsFragment = ChatsFragment()
            chatsFragment.setArguments(bundle)
            (requireActivity() as HomeActivity).replaceFragment(chatsFragment.javaClass,
                "",
                true,
                bundle,
                null,
                0,
                false
            )
            Toast.makeText(context, "Chat bot", Toast.LENGTH_SHORT).show()
        }

        binding.tvSeeAllUpdate.setOnClickListener {
            (requireActivity() as HomeActivity).replaceFragment(LatestUpdatesFragment::class.java,
                "",
                true,
                null,
                null,
                0,
                false)
        }

        binding.tvSeeallTestimonial.setOnClickListener {
            (requireActivity() as HomeActivity).replaceFragment(Testimonials::class.java,
                "",
                true,
                null,
                null,
                0,
                false)
        }

        binding.appShareBtn.setOnClickListener {
            share_app()
        }


        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.headset.setOnClickListener {
            val bundle = Bundle()
            val chatsFragment = ChatsFragment()
            chatsFragment.setArguments(bundle)
            (requireActivity() as HomeActivity).replaceFragment(chatsFragment.javaClass,
                "",
                true,
                bundle,
                null,
                0,
                false
            )
            Toast.makeText(context, "Chat bot", Toast.LENGTH_SHORT).show()
        }
    }

    private fun referNow() {

        binding.btnReferNow.setOnClickListener {

            val dialog = ReferralDialog()
            dialog.isCancelable = true
            dialog.show(parentFragmentManager, "Refrral card")

        }
    }

    private fun share_app() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "The House Of Abhinandan Lodha $appURL")
        startActivity(shareIntent)
    }
}