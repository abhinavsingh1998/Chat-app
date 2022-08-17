package com.emproto.hoabl.feature.home.views.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentLatestUpdatesDetailsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.adapters.LatestUpdateListAdapter
import com.emproto.hoabl.feature.home.data.LatesUpdatesPosition
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.response.home.DetailedInfo
import com.emproto.networklayer.response.marketingUpdates.Data
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class LatestUpdatesDetailsFragment : BaseFragment() {

    lateinit var mBinding: FragmentLatestUpdatesDetailsBinding

    @Inject
    lateinit var factory: HomeFactory
    lateinit var homeViewModel: HomeViewModel

    lateinit var bundle: Bundle
    var position: Int = 0
    var listLength: Int = 0
    lateinit var it: Data
    var updatesListCount = 0
    lateinit var data: LiveData<LatesUpdatesPosition>
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = FragmentLatestUpdatesDetailsBinding.inflate(inflater, container, false)

        (requireActivity() as HomeActivity).hideBottomNavigation()

        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel = ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]
        (requireActivity() as HomeActivity).hideHeader()
        data = homeViewModel.getSelectedPosition()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getSelectedPosition().observe(viewLifecycleOwner,Observer{
            position = it.position
            listLength = it.lisLenght
//            Log.d("RRR","${position},${listLength}")
            initObserver()
            initView(position)
            backView()
            forwardView()
            initClickListener()
        })
    }

    private fun initObserver() {
        homeViewModel.getSelectedLatestUpdates().observe(viewLifecycleOwner, Observer {
            this.it = it
            mBinding.title.text = it.displayTitle
            mBinding.cityName.text = it.subTitle

            mBinding.listInsights.layoutManager = LinearLayoutManager(requireContext())
            mBinding.listInsights.adapter = LatestUpdateListAdapter(
                requireContext(),
                it.detailedInfo
            )
        }
        )

    }

    private fun backView() {
        mBinding.backView.setOnClickListener(View.OnClickListener {
            if (position > 0) {
                --position
                mBinding.storyView.removeAllViews()
                initView(position)
                homeViewModel.getLatestUpdates().observe(viewLifecycleOwner, Observer {

                    it.let {
                        homeViewModel.setSeLectedLatestUpdates(it[position])
                    }
                })
                initObserver()
                mBinding.parentScrollView.scrollTo(0,0)
            }
        })
    }

    private fun forwardView() {
        mBinding.fowardView.setOnClickListener(View.OnClickListener {

            if (position + 1 != listLength) {
                ++position
                mBinding.storyView.removeAllViews()
                initView(position)

                homeViewModel.getLatestUpdates().observe(viewLifecycleOwner, Observer {

                    it.let {
                        homeViewModel.setSeLectedLatestUpdates(it[position])
                    }
                })
                initObserver()
                mBinding.parentScrollView.scrollTo(0,0)
            }

        })
    }

    private fun initView(position: Int) {
        val totalPosition = listLength
        val filledPosition = position
        for (i in 0 until totalPosition!!) {
            val dummyView = View(context)
            val viewParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1F)
            //EmptyViewBinding.inflate(LayoutInflater.from(context))
            viewParams.marginEnd = 4
            dummyView.layoutParams = viewParams
            if (i <= filledPosition!!) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dummyView.background = requireContext().getDrawable(R.color.app_color)
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
        mBinding.backBtnView.setOnClickListener(View.OnClickListener {
            (this.requireActivity() as HomeActivity).onBackPressed()
        })
    }

}