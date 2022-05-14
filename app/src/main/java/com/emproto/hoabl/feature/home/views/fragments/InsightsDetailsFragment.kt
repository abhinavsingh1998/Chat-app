package com.emproto.hoabl.feature.home.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentInsightsDetailsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.adapters.InsightsListAdapter
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.response.home.InsightsMedia
import javax.inject.Inject

class InsightsDetailsFragment : BaseFragment() {

    lateinit var mBinding: FragmentInsightsDetailsBinding
    private lateinit var adapter: InsightsListAdapter
    @Inject
    lateinit var factory: HomeFactory
    lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = FragmentInsightsDetailsBinding.inflate(inflater, container, false)

        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility =
            View.GONE

        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel = ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]

        (requireActivity() as HomeActivity).showBackArrow()

        initObserver()

        return mBinding.root
    }

    private fun initObserver() {
        homeViewModel.getSelectedInsights().observe(viewLifecycleOwner, Observer {
            mBinding.title.text= it.displayTitle

            mBinding.listInsights.layoutManager = LinearLayoutManager(requireContext())
            mBinding.listInsights.adapter = InsightsListAdapter(requireContext(),
                it.insightsMedia as ArrayList<InsightsMedia>)
                }
            )

        }
}