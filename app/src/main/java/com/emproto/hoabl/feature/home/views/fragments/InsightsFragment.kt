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
import com.emproto.core.Constants
import com.emproto.hoabl.databinding.FragmentInsightsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.adapters.AllInsightsAdapter
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.insights.InsightsResponse
import javax.inject.Inject

class InsightsFragment : BaseFragment() {

    lateinit var mBinding: FragmentInsightsBinding
    private lateinit var insightsAdapter: AllInsightsAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val appURL = Constants.PLAY_STORE
    private var insightsListCount = 0
    private lateinit var insightsHeading: String
    private lateinit var insightsSubHeading: String


    @Inject
    lateinit var factory: HomeFactory
    lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = FragmentInsightsBinding.inflate(layoutInflater)

        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)

        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility =
            View.GONE
        homeViewModel = ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]

        (requireActivity() as HomeActivity).showBackArrow()

        initView()
        initObserver(false)
        initClickListner()
        return mBinding.root
    }

    private fun initView() {
        homeViewModel.getHeaderAndList().observe(viewLifecycleOwner, Observer {
            insightsListCount = it.totalInsightsOnListView
            insightsHeading = it.insightsHeading
            insightsSubHeading = it.insightsSubHeading

        })
    }

    private fun initObserver(refresh: Boolean) {

        homeViewModel.getInsightsData(refresh, true)
            .observe(
                viewLifecycleOwner
            ) {
                when (it?.status) {

                    Status.ERROR -> {
                        mBinding.rootView.hide()
                        mBinding.loader.show()
                    }
                    Status.SUCCESS -> {
                        mBinding.rootView.show()
                        mBinding.loader.hide()

                        mBinding.headerText.text = insightsHeading
                        mBinding.subHeaderTxt.text = insightsSubHeading

                        initAdapeter(it.data!!)

                    }
                }
            }
    }

    private fun initAdapeter(data: InsightsResponse) {

        data.data.let {
            if (data != null) {
                homeViewModel.setInsightsData(data?.data)
            }

            data?.data!!.size
            insightsAdapter = AllInsightsAdapter(requireActivity(),
                insightsListCount,
                data?.data,
                object : AllInsightsAdapter.InsightsItemsInterface {
                    override fun onClickItem(position: Int) {
                        homeViewModel.setSeLectedInsights(data?.data[position])
                        (requireActivity() as HomeActivity).addFragment(
                            InsightsDetailsFragment(),
                            true
                        )
                    }
                }
            )
            linearLayoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL,
                false
            )
            mBinding.recyclerInsights.layoutManager = linearLayoutManager
            mBinding.recyclerInsights.adapter = insightsAdapter

        }

    }

    private fun initClickListner() {

        mBinding.appShareBtn.setOnClickListener {
            shareApp()

        }

        mBinding.refressLayout.setOnRefreshListener {
            mBinding.loader.show()
            initObserver(refresh = true)

            mBinding.refressLayout.isRefreshing = false

        }
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "The House Of Abhinandan Lodha $appURL")
        startActivity(shareIntent)
    }
}