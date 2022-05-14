package com.emproto.hoabl.feature.home.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentLatestUpdatesBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.adapters.AllLatestUpdatesAdapter
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.home.HomeResponse
import javax.inject.Inject

class LatestUpdatesFragment : BaseFragment() {

    lateinit var mBinding: FragmentLatestUpdatesBinding
    lateinit var latestUpatesAdapter: AllLatestUpdatesAdapter
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
        mBinding = FragmentLatestUpdatesBinding.inflate(inflater, container, false)

        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility =
            View.GONE

        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel = ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]

        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.isVisible =
            true

        (requireActivity() as HomeActivity).showBackArrow()
        initClickListner()
        initObserver()
        return mBinding.root
    }

    private fun initObserver() {

        homeViewModel.getDashboardData(5001)
            .observe(viewLifecycleOwner, object : Observer<BaseResponse<HomeResponse>> {
                override fun onChanged(it: BaseResponse<HomeResponse>?) {
                    when (it!!.status) {
                        Status.LOADING -> {
                            mBinding.rootView.hide()
                            mBinding.loader.show()
                        }
                        Status.SUCCESS -> {
                            mBinding.rootView.show()
                            mBinding.loader.hide()

                            //loading List
                            latestUpatesAdapter = AllLatestUpdatesAdapter(requireActivity(),
                                it.data!!.data.pageManagementOrLatestUpdates,
                                object : AllLatestUpdatesAdapter.UpdatesItemsInterface {
                                    override fun onClickItem(position: Int) {
                                        homeViewModel.setSeLectedLatestUpdates(it.data!!.data.pageManagementOrLatestUpdates[position])
                                        (requireActivity() as HomeActivity).replaceFragment(
                                            LatestUpdatesDetailsFragment()::class.java,
                                            "",
                                            true,
                                            null,
                                            null,
                                            0,
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
                            mBinding.recyclerLatestUpdates.layoutManager = linearLayoutManager
                            mBinding.recyclerLatestUpdates.adapter = latestUpatesAdapter

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


