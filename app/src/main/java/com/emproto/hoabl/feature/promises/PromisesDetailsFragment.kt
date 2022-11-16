package com.emproto.hoabl.feature.promises

import android.graphics.Paint
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.emproto.core.BaseFragment
import com.emproto.core.databinding.TermsConditionDialogBinding
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentPromiseDetailsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.home.views.Mixpanel
import com.emproto.hoabl.feature.promises.adapter.PromiseDetailsAdapter
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.preferences.AppPreference
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject


class PromisesDetailsFragment : BaseFragment() {

    @Inject
    lateinit var homeFactory: HomeFactory
    lateinit var homeViewModel: HomeViewModel

    lateinit var binding: FragmentPromiseDetailsBinding
    lateinit var adapter: PromiseDetailsAdapter

    lateinit var heading: Array<String>
    val bundle = Bundle()

    lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var termsConditionDialogBinding: TermsConditionDialogBinding

    @Inject
    lateinit var appPreference: AppPreference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        binding = FragmentPromiseDetailsBinding.inflate(inflater, container, false)
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel =
            ViewModelProvider(requireActivity(), homeFactory)[HomeViewModel::class.java]
        (requireActivity() as HomeActivity).hideBottomNavigation()

        initView()
        initObserver()

        return binding.root

    }

    private fun initView() {
        (requireActivity() as HomeActivity).showBackArrow()
        bottomSheetDialog = BottomSheetDialog(requireContext())
        termsConditionDialogBinding = TermsConditionDialogBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(termsConditionDialogBinding.root)
        binding.textViewTAndC.paintFlags =
            binding.textViewTAndC.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        binding.getPortfolioButton.setOnClickListener {
            //open 3 rd tab portfolio
            eventTrackingPorfolio()
            (requireActivity() as HomeActivity).navigate(R.id.navigation_portfolio)
        }
        binding.textViewTAndC.setOnClickListener {
            eventTrackingTermsAndConditions()
            bottomSheetDialog.show()
        }

        termsConditionDialogBinding.acitonClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
    }

    private fun eventTrackingTermsAndConditions() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(), Mixpanel.PROMISESTERMSANDCONDITIONS
        )
    }

    private fun eventTrackingPorfolio() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(), Mixpanel.PORTFOLIO
        )
    }

    private fun initObserver() {
        homeViewModel.getSelectedPromise().observe(viewLifecycleOwner) { promise ->
            binding.tvPromiseTitle.text = promise.name
            binding.tvPromiseInfo.text = promise.shortDescription
            binding.tvDescList.layoutManager = LinearLayoutManager(requireContext())
            val list = ArrayList<String>()
            list.addAll(promise.description)
            binding.tvDescList.adapter = PromiseDetailsAdapter(requireContext(), list)

            if (promise.isHowToApplyActive) {
                binding.applyView.isVisible = true
                //apply
                binding.textviewApply.text = promise.howToApply?.title
                promise.howToApply?.let {
                    binding.textviewApply1.text = it.description

                }
            } else {
                binding.applyView.isVisible = false
            }

            //termsAndConditions
            if (promise.isTermsAndConditionsActive) {
                if (promise.termsAndConditions != null) {
                    binding.textViewTAndC.visibility = View.VISIBLE
                    termsConditionDialogBinding.tvTitle.text =
                        showHTMLText(promise.termsAndConditions?.description)
                    termsConditionDialogBinding.tvTitle.movementMethod = ScrollingMovementMethod()
                    binding.textViewTAndC.text = promise.termsAndConditions?.displayName

                }
            } else {
                binding.textViewTAndC.visibility = View.GONE

            }

            if (promise.displayMedia != null)
                Glide.with(requireContext())
                    .load(promise.displayMedia?.value?.url)
                    .into(binding.imageSecurity)

        }

    }

}