package com.emproto.hoabl.feature.home.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.feature.home.adapters.InsightsAdapter
import com.emproto.hoabl.feature.home.adapters.LatestUpdateAdapter
import com.emproto.hoabl.adapters.TestimonialAdapter
import com.emproto.hoabl.databinding.FragmentHomeBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.adapters.HoABLPromisesAdapter1
import com.emproto.hoabl.feature.home.adapters.InvestmentCardAdapter
import com.emproto.hoabl.feature.home.adapters.PendingPaymentsAdapter
import com.emproto.hoabl.feature.home.data.LatesUpdatesPosition
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.views.CategoryListFragment
import com.emproto.hoabl.feature.investment.views.ProjectDetailFragment
import com.emproto.hoabl.feature.portfolio.adapters.PortfolioSpecificViewAdapter
import com.emproto.hoabl.feature.promises.PromisesDetailsFragment
import com.emproto.hoabl.utils.Extensions.toHomePagesOrPromise
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import com.emproto.networklayer.enum.ModuleEnum
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.home.HomeResponse
import com.emproto.networklayer.response.home.PageManagementsOrNewInvestment
import com.google.android.material.tabs.TabLayoutMediator
import java.io.Serializable
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
    lateinit var ivInterface: PortfolioSpecificViewAdapter.InvestmentScreenInterface

    val appURL = "https://hoabl.in/"

    @Inject
    lateinit var factory: HomeFactory
    lateinit var investmentFactory: InvestmentFactory
    lateinit var homeViewModel: HomeViewModel
    val list = ArrayList<PageManagementsOrNewInvestment>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel = ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]
        initObserver()
        initView()
        initClickListener()
        referNow()

        return binding.root
    }

    private fun initObserver() {


        homeViewModel.getDashBoardData(ModuleEnum.HOME.value)
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

                            it.data.let {
                                if (it != null) {
                                    homeViewModel.setDashBoardData(it)
                                }
                            }

                            //loading investment list
                            list.clear()
                            list.addAll(it.data!!.data.page.pageManagementsOrNewInvestments)
                            investmentAdapter = InvestmentCardAdapter(
                                requireActivity(),
                                it.data!!.data.page.pageManagementsOrNewInvestments,
                                object : InvestmentCardAdapter.InvestItemInterface {
                                    override fun onClickItem(id: Int) {
                                        val fragment = ProjectDetailFragment()
                                        val bundle = Bundle()
                                        bundle.putInt("ProjectId", id)
                                        fragment.arguments = bundle
                                        (requireActivity() as HomeActivity).addFragment(
                                            fragment,
                                            false
                                        )
                                    }

                                }
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
                                it.data!!.data.pageManagementOrLatestUpdates,
                                object : LatestUpdateAdapter.ItemInterface {
                                    override fun onClickItem(position: Int) {
                                        homeViewModel.setSeLectedLatestUpdates(it.data!!.data.pageManagementOrLatestUpdates[position])
                                        homeViewModel.setSelectedPosition(
                                            LatesUpdatesPosition(
                                                position,
                                                it.data!!.data.pageManagementOrLatestUpdates.size
                                            )
                                        )
                                        (requireActivity() as HomeActivity).addFragment(
                                            LatestUpdatesFragment(),
                                            false
                                        )
                                    }

                                }
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
                                it.data!!.data.homePagesOrPromises,
                                object : HoABLPromisesAdapter1.PromisesItemInterface {
                                    override fun onClickItem(position: Int) {
                                        val data =
                                            it.data!!.data.homePagesOrPromises[position].toHomePagesOrPromise()
                                        homeViewModel.setSelectedPromise(data)
                                        (requireActivity() as HomeActivity).addFragment(
                                            PromisesDetailsFragment(),
                                            false
                                        )
                                    }

                                }
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
                                it.data!!.data.pageManagementOrInsights,
                                object : InsightsAdapter.InsightsItemInterface {
                                    override fun onClickItem(position: Int) {
                                        homeViewModel.setSeLectedInsights(it.data!!.data.pageManagementOrInsights[position])
                                        (requireActivity() as HomeActivity).addFragment(
                                            InsightsFragment(),
                                            false
                                        )
                                    }

                                }

                            )
                            linearLayoutManager = LinearLayoutManager(
                                requireContext(),
                                RecyclerView.HORIZONTAL,
                                false
                            )
                            binding.insightsRecyclerview.layoutManager = linearLayoutManager
                            binding.insightsRecyclerview.adapter = insightsAdapter


                            //loading Testimonial Cards list
                            testimonialAdapter = TestimonialAdapter(
                                requireActivity(),
                                it.data!!.data.pageManagementsOrTestimonials
                            )
                            binding.testimonialsRecyclerview.adapter = testimonialAdapter
                            TabLayoutMediator(
                                binding.tabDotLayout,
                                binding.testimonialsRecyclerview
                            ) { _, _ ->
                            }.attach()

                        }
                        Status.ERROR -> {
                            binding.loader.hide()
                            (requireActivity() as HomeActivity).showErrorToast(
                                it.message!!
                            )
                            binding.rootView.show()
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
            (requireActivity() as HomeActivity).addFragment(InsightsFragment(), false)
        }

        binding.tvSeeAllUpdate.setOnClickListener {
            (requireActivity() as HomeActivity).addFragment(LatestUpdatesDetailsFragment(), false)
        }

        binding.tvSeeallTestimonial.setOnClickListener {
            (requireActivity() as HomeActivity).addFragment(Testimonials(), false)
        }

        binding.tvSeeallPromise.setOnClickListener(View.OnClickListener {
            (requireActivity() as HomeActivity).navigate(R.id.navigation_promises)
        })

        binding.tvViewallInvestments.setOnClickListener(View.OnClickListener {
            val fragment = CategoryListFragment()
            val bundle = Bundle()
            bundle.putString("Category", "Home")
            bundle.putSerializable(
                "DiscoverAll",
                list as Serializable
            )
            fragment.arguments = bundle
            (requireActivity() as HomeActivity).addFragment(fragment, false)
        })

        binding.referralLayout.appShareBtn.setOnClickListener {
            share_app()
        }
    }

    private fun referNow() {

        binding.referralLayout.btnReferNow.setOnClickListener {
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