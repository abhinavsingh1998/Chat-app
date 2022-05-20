package com.emproto.hoabl.feature.home.views.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentLatestUpdatesDetailsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.adapters.AllLatestUpdatesAdapter
import com.emproto.hoabl.feature.home.adapters.InsightsListAdapter
import com.emproto.hoabl.feature.home.adapters.LatestUpdateListAdapter
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.response.home.DetailedInfo
import javax.inject.Inject

class LatestUpdatesDetailsFragment : BaseFragment() {

    lateinit var mBinding: FragmentLatestUpdatesDetailsBinding

    @Inject
    lateinit var factory: HomeFactory
    lateinit var homeViewModel: HomeViewModel

    lateinit var bundle: Bundle
    var position:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = FragmentLatestUpdatesDetailsBinding.inflate(inflater, container, false)

        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility =
            View.GONE

        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel = ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.isVisible =
            false
        initObserver()
        initView()
        initClickListener()
        return mBinding.root
    }

    private fun initObserver() {
        homeViewModel.getSelectedLatestUpdates().observe(viewLifecycleOwner, Observer {
            mBinding.title.text= it.displayTitle
            mBinding.cityName.text= it.subTitle

            mBinding.listInsights.layoutManager = LinearLayoutManager(requireContext())
            mBinding.listInsights.adapter = LatestUpdateListAdapter(requireContext(),
                it.detailedInfo as ArrayList<DetailedInfo>)
        }
        )

    }
    private fun initView(){
        var data= homeViewModel.getSelectedPosition()
        val totalPosition = data.value?.lisLenght
        val filledPosition = data.value?.position
        for (i in 0 until totalPosition!!) {
            val viewParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1F)
            val dummyView = View(context)//EmptyViewBinding.inflate(LayoutInflater.from(context))
            viewParams.marginEnd = 4
            dummyView.layoutParams = viewParams
            if (i <= filledPosition!!) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dummyView.background = requireContext().getDrawable(R.drawable.card_bg)
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dummyView.background = requireContext().getDrawable(R.color.text_grey_color)
                }
            }
            mBinding.storyView.addView(dummyView)
        }
    }



    fun initClickListener() {
        mBinding.backBtn.setOnClickListener(View.OnClickListener {
            (requireActivity() as HomeActivity).onBackPressed()
        })
    }

}