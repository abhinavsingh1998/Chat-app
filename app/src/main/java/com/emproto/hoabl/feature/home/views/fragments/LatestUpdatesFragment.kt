package com.emproto.hoabl.feature.home.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseFragment
import com.emproto.core.Constants
import com.emproto.hoabl.databinding.FragmentLatestUpdatesBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.adapters.AllLatestUpdatesAdapter
import com.emproto.hoabl.feature.home.data.LatesUpdatesPosition
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.marketingUpdates.LatestUpdatesResponse
import javax.inject.Inject

class LatestUpdatesFragment : BaseFragment() {

    lateinit var mBinding: FragmentLatestUpdatesBinding
    private lateinit var latestUpatesAdapter: AllLatestUpdatesAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val appURL = Constants.PLAY_STORE
    var updatesListCount = 0
    private lateinit var latestHeading: String
    private lateinit var latestSubHeading: String

    @Inject
    lateinit var factory: HomeFactory
    lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = FragmentLatestUpdatesBinding.inflate(layoutInflater)

        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)

        (requireActivity() as HomeActivity).hideBottomNavigation()

        homeViewModel = ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]

        (requireActivity() as HomeActivity).showHeader()

        (requireActivity() as HomeActivity).showBackArrow()
        initView()
        initObserver(false)
        initClickListner()
        return mBinding.root
    }

    private fun initView(){
        homeViewModel.getHeaderAndList().observe(viewLifecycleOwner) {
            updatesListCount = it.totalUpdatesOnListView
            latestHeading = it.latestUpdates.heading
            latestSubHeading = it.latestUpdates.subHeading

        }
    }

    private fun initObserver(refresh: Boolean) {
        if (isNetworkAvailable()){
            homeViewModel.getLatestUpdatesData(refresh, true)
                .observe(viewLifecycleOwner
                ) { it ->
                    when (it?.status) {
                        Status.LOADING -> {
                            mBinding.parentScroll.hide()
                            mBinding.loader.show()
                            mBinding.noInternetView.mainContainer.hide()
                        }
                        Status.SUCCESS -> {
                            mBinding.parentScroll.show()
                            mBinding.loader.hide()

                            mBinding.headerText.text = latestHeading
                            mBinding.subHeaderTxt.text = latestSubHeading

                            initAdapter(it!!.data!!)

                        }
                        Status.ERROR -> {
                            mBinding.loader.hide()
                            (requireActivity() as HomeActivity).showErrorToast(
                                it.message!!
                            )
                            mBinding.rootView.show()
                        }
                        else -> {}
                    }
                }
        } else{
            noInternetState()
        }

    }

    private fun initAdapter(it: LatestUpdatesResponse){

        it?.data.let {
            if (it != null) {
                homeViewModel.setLatestUpdatesData(it)

            }

            //loading List
            it.size
            latestUpatesAdapter = AllLatestUpdatesAdapter(requireActivity(),
                it,
                updatesListCount,
                object : AllLatestUpdatesAdapter.UpdatesItemsInterface {
                    override fun onClickItem(position: Int) {
                        homeViewModel.setSeLectedLatestUpdates(it[position])
                        homeViewModel.setSelectedPosition(
                            LatesUpdatesPosition(
                                position,
                                updatesListCount
                            )
                        )

                        (requireActivity() as HomeActivity).addFragment(
                            LatestUpdatesDetailsFragment(),
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
            mBinding.recyclerLatestUpdates.layoutManager = linearLayoutManager
            mBinding.recyclerLatestUpdates.adapter = latestUpatesAdapter
        }


    }

    private fun noInternetState(){
        mBinding.refressLayout.isRefreshing = false
        mBinding.loader.hide()
        mBinding.parentScroll.hide()
        mBinding.noInternetView.mainContainer.show()
        mBinding.noInternetView.textView6.setOnClickListener {
            initObserver(true)
            (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.rotateText.hide()
        }
    }

    private fun initClickListner() {

        mBinding.appShareBtn.setOnClickListener {
            shareApp()
        }

        mBinding.refressLayout.setOnRefreshListener {
            mBinding.loader.show()
            initObserver(true)

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
