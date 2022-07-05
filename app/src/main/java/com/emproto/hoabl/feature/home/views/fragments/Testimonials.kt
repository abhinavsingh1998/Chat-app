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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentTestimonialsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.adapters.AllInsightsAdapter
import com.emproto.hoabl.feature.home.adapters.TestimonialsAdapter
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.home.HomeResponse
import com.emproto.networklayer.response.testimonials.TestimonialsResponse
import java.util.*
import javax.inject.Inject


class Testimonials : BaseFragment() {


    private lateinit var mBinding: FragmentTestimonialsBinding
    lateinit var testimonialsAdapter: TestimonialsAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    val appURL= "https://hoabl.in/"
    var testimonialsItem= 0
    var testimonilalsHeading:String = ""
    var testimonilalsSubHeading:String = ""


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
            testimonialsItem = it.getInt("testimonials", 0)
        }

        arguments?.let {
            testimonilalsHeading= it.getString("testimonialsHeading", "")
        }
        arguments?.let {
            testimonilalsSubHeading= it.getString("testimonialsSubHeading", "")
        }

        initObserver(false)
        initClickListner()
        return mBinding.root
    }

    private fun initObserver(refresh:Boolean) {

        homeViewModel.getTestimonialsData(refresh).observe(viewLifecycleOwner, object:Observer<BaseResponse<TestimonialsResponse>> {
            override fun onChanged(it: BaseResponse<TestimonialsResponse>?) {
                when (it?.status){
                   Status.LOADING ->{
                       mBinding.rootView.hide()
                       mBinding.loader.show()
                   }
                    Status.SUCCESS ->{
                        mBinding.rootView.show()
                        mBinding.loader.hide()

                        mBinding.headerText.text= testimonilalsHeading
                        mBinding.subHeaderTxt.text= testimonilalsSubHeading

                        it.data.let {

                            if (it!= null){
                                homeViewModel.setTestimonials(it)


                                //loading List
                                testimonialsAdapter = TestimonialsAdapter(requireActivity(),
                                    it.data,
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
                    Status.ERROR ->{
                        mBinding.loader.hide()
                        (requireActivity() as HomeActivity).showErrorToast(
                            it.message!!
                        )
                        mBinding.rootView.show()
                    }

                }
            }
        })
        }

    private fun initClickListner() {

        mBinding.referralLayout.appShareBtn.setOnClickListener(View.OnClickListener {
            share_app()
        })

        mBinding.referralLayout.btnReferNow.setOnClickListener(View.OnClickListener {
            referNow()
        })

        mBinding.refressLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            mBinding.loader.show()
            initObserver(refresh = true)

            mBinding.refressLayout.isRefreshing= false

        })
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
            dialog.show(parentFragmentManager, "Refrral card")

        }
}