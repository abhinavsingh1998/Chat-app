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
import com.emproto.hoabl.databinding.FragmentTestimonialsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.adapters.TestimonialsAdapter
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.testimonials.TestimonialsResponse
import javax.inject.Inject


class Testimonials : BaseFragment() {


    private lateinit var mBinding: FragmentTestimonialsBinding
    private lateinit var testimonialsAdapter: TestimonialsAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val appURL = Constants.PLAY_STORE
    private var testimonialsItem = 0
    private var testimonilalsHeading: String = ""
    private var testimonilalsSubHeading: String = ""


    @Inject
    lateinit var factory: HomeFactory
    lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        mBinding = FragmentTestimonialsBinding.inflate(layoutInflater)

        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility =
            View.GONE
        homeViewModel = ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]
        (requireActivity() as HomeActivity).showBackArrow()
        arguments?.let {
            testimonialsItem = it.getInt(Constants.TESTIMONALS, 0)
        }

        arguments?.let {
            testimonilalsHeading = it.getString(Constants.TESTIMONALS_HEADING, "")
        }
        arguments?.let {
            testimonilalsSubHeading = it.getString(Constants.TESTIMONALS_SUB_HEADING, "")
        }

        initObserver(false)
        initClickListner()
        return mBinding.root
    }

    private fun initObserver(refresh: Boolean) {

        homeViewModel.getTestimonialsData(refresh).observe(
            viewLifecycleOwner
        ) {
            when (it?.status) {
                Status.LOADING -> {
                    mBinding.rootView.hide()
                    mBinding.loader.show()
                }
                Status.SUCCESS -> {
                    mBinding.rootView.show()
                    mBinding.loader.hide()

                    mBinding.headerText.text = testimonilalsHeading
                    mBinding.subHeaderTxt.text = testimonilalsSubHeading

                    initAdpater(it!!.data!!)
                }
                Status.ERROR -> {
                    mBinding.loader.hide()
                    (requireActivity() as HomeActivity).showErrorToast(
                        it.message!!
                    )
                    mBinding.rootView.show()
                }

            }
        }
    }

    private fun initAdpater(it: TestimonialsResponse) {
        it.let {

            if (it != null) {
                homeViewModel.setTestimonials(it)


                //loading List
                testimonialsAdapter = TestimonialsAdapter(
                    requireActivity(),
                    it?.data,
                    testimonialsItem
                )
                linearLayoutManager = LinearLayoutManager(
                    requireContext(),
                    RecyclerView.VERTICAL,
                    false
                )
                mBinding.recyclerTestimonilas.layoutManager = linearLayoutManager
                mBinding.recyclerTestimonilas.adapter = testimonialsAdapter
            }
        }

    }

    private fun initClickListner() {

        mBinding.referralLayout.appShareBtn.setOnClickListener {
            share_app()
        }

        mBinding.referralLayout.btnReferNow.setOnClickListener {
            referNow()
        }

        mBinding.refressLayout.setOnRefreshListener {
            mBinding.loader.show()
            initObserver(refresh = true)

            mBinding.refressLayout.isRefreshing = false

        }
    }


    private fun share_app() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "The House Of Abhinandan Lodha $appURL")
        startActivity(shareIntent)
    }

    private fun referNow() {
        val dialog = ReferralDialog()
        dialog.isCancelable = true
        dialog.show(parentFragmentManager, Constants.REFERRAL_CARD)

    }
}