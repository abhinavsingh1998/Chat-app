package com.emproto.hoabl.feature.home.views.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.emproto.core.BaseFragment
import com.emproto.core.Constants
import com.emproto.hoabl.databinding.FragmentInsightsDetailsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.adapters.InsightsListAdapter
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.views.mediagallery.YoutubeActivity
import com.emproto.hoabl.utils.YoutubeItemClickListener
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
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
        (requireActivity() as HomeActivity).showHeader()

        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel = ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]

        (requireActivity() as HomeActivity).showBackArrow()

        initObserver()

        return mBinding.root
    }

    private fun initObserver() {
        homeViewModel.getSelectedInsights().observe(viewLifecycleOwner, Observer {
            mBinding.title.text = it.displayTitle

            mBinding.listInsights.layoutManager = LinearLayoutManager(requireContext())
            mBinding.listInsights.adapter = InsightsListAdapter(
                requireContext(),
                it.insightsMedia, itemClickListener
            )
        }
        )
    }

    val itemClickListener = object : YoutubeItemClickListener {
        override fun onItemClicked(view: View, position: Int, url: String, title: String) {
            val intent = Intent(requireActivity() as HomeActivity, YoutubeActivity::class.java)
            intent.putExtra(Constants.YOUTUBE_VIDEO_ID, url)
            intent.putExtra(Constants.VIDEO_TITLE, title)
            startActivity(intent)

        }

    }
}