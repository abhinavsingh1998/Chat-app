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
import com.emproto.hoabl.databinding.FragmentInsightsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.adapters.AllInsightsAdapter
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import javax.inject.Inject

class InsightsFragment : BaseFragment() {

    lateinit var mBinding: FragmentInsightsBinding
    lateinit var insightsAdapter: AllInsightsAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    val appURL= "https://hoabl.in/"


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

        initObserver()
        initClickListner()
        return mBinding.root
    }

    private fun initObserver() {

        homeViewModel.gethomeData().observe(viewLifecycleOwner , Observer {
            mBinding.rootView.show()
            mBinding.loader.hide()

            it.let {
                insightsAdapter = AllInsightsAdapter(requireActivity(),
                    it.data!!.pageManagementOrInsights,
                    object : AllInsightsAdapter.InsightsItemsInterface {
                        override fun onClickItem(position: Int) {
                            homeViewModel.setSeLectedInsights(it.data!!.pageManagementOrInsights[position])
                            (requireActivity() as HomeActivity).addFragment(
                                InsightsDetailsFragment(),
                                false
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
        })
    }

    private fun initClickListner() {

        mBinding.appShareBtn.setOnClickListener(View.OnClickListener {
            share_app()
        })
    }

    private fun share_app() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "The House Of Abhinandan Lodha $appURL")
        startActivity(shareIntent)
    }
}