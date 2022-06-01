package com.emproto.hoabl.feature.promises

import android.graphics.Paint
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.emproto.core.BaseFragment
import com.emproto.core.databinding.TermsConditionDialogBinding
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentPromiseDetailsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.promises.adapter.PromiseDetailsAdapter

import com.emproto.hoabl.feature.promises.data.DetailsScreenData
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.response.enums.Status
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject


class PromisesDetailsFragment : BaseFragment() {

    @Inject
    lateinit var homeFactory: HomeFactory
    lateinit var homeViewModel: HomeViewModel

    lateinit var binding: FragmentPromiseDetailsBinding
    lateinit var adapter: PromiseDetailsAdapter
    lateinit var button: Button

    private lateinit var newArrayList: ArrayList<DetailsScreenData>
    lateinit var imageId: Array<Int>
    lateinit var heading: Array<String>
    val bundle = Bundle()

    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var termsConditionDialogBinding: TermsConditionDialogBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        binding = FragmentPromiseDetailsBinding.inflate(inflater, container, false)
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel =
            ViewModelProvider(requireActivity(), homeFactory)[HomeViewModel::class.java]
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =
            false

        initView()
        initObserver()

        return binding.root

    }

    private fun initView() {
        (requireActivity() as HomeActivity).showBackArrow()
        bottomSheetDialog = BottomSheetDialog(requireContext())
        termsConditionDialogBinding = TermsConditionDialogBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(termsConditionDialogBinding.root)
        binding.textViewTAndC.setPaintFlags(binding.textViewTAndC.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        binding.getPortfolioButton.setOnClickListener {
            //open 3 rd tab portfolio
            (requireActivity() as HomeActivity).navigate(R.id.navigation_portfolio)
        }
        binding.textViewTAndC.setOnClickListener {
            bottomSheetDialog.show()
        }

        termsConditionDialogBinding.acitonClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
    }

    private fun initObserver() {
        homeViewModel.getSelectedPromise().observe(viewLifecycleOwner, Observer {
            binding.tvPromiseTitle.text = it.name
            binding.tvPromiseInfo.text = it.shortDescription
            binding.tvDescList.layoutManager = LinearLayoutManager(requireContext())
            val list = ArrayList<String>()
            list.addAll(it.description)
            binding.tvDescList.adapter = PromiseDetailsAdapter(requireContext(), list)
            it.howToApply?.let {
                binding.textviewApply1.text = it.description

            }
            Glide.with(requireContext())
                .load(it.displayMedia.value.url)
                .into(binding.imageSecurity)
            //termsAndConditions
            termsConditionDialogBinding.tvTitle.text =
                showHTMLText(it.termsAndConditions.description)
            termsConditionDialogBinding.tvTitle.setMovementMethod(
                ScrollingMovementMethod()
            )
        })

//        homeViewModel.getTermsCondition(5004).observe(viewLifecycleOwner, Observer {
//            when (it.status) {
//                Status.SUCCESS -> {
//                    it.data?.let {
//                        if (it.data.page.termsAndConditions != null) {
//                            termsConditionDialogBinding.tvTitle.text =
//                                showHTMLText(it.data.page.termsAndConditions.description)
//                            termsConditionDialogBinding.tvTitle.setMovementMethod(
//                                ScrollingMovementMethod()
//                            )
//                        }
//                    }
//                }
//
//            }
//        })
    }

}