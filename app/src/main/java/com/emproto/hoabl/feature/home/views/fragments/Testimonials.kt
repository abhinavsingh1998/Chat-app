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
import javax.inject.Inject


class Testimonials : BaseFragment() {


    private lateinit var mBinding: FragmentTestimonialsBinding
    lateinit var testimonialsAdapter: TestimonialsAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    val appURL= "https://hoabl.in/"


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

        initObserver()
        initClickListner()
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
                            testimonialsAdapter = TestimonialsAdapter(requireActivity(),
                                it.data!!.data.pageManagementsOrTestimonials
                            )
                            linearLayoutManager = LinearLayoutManager(
                                requireContext(),
                                RecyclerView.VERTICAL,
                                false
                            )
                            mBinding.recyclerTestimonilas.layoutManager = linearLayoutManager
                            mBinding.recyclerTestimonilas.adapter = testimonialsAdapter

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