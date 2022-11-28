package com.emproto.hoabl.feature.profile.fragments.securtiyandsettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentSecurityTipsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.profile.adapter.SecurityTipsAdapter
import com.emproto.hoabl.viewmodels.ProfileViewModel
import com.emproto.hoabl.viewmodels.factory.ProfileFactory
import com.emproto.networklayer.response.enums.Status
import java.util.*
import javax.inject.Inject

class SecurityTipsFragment : BaseFragment() {

    @Inject
    lateinit var profileFactory: ProfileFactory
    private lateinit var profileViewModel: ProfileViewModel
    lateinit var binding: FragmentSecurityTipsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSecurityTipsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        initClicklistener()
        callApi()
    }

    private fun initClicklistener() {
        binding.imgArrow.setOnClickListener {
            (requireActivity() as HomeActivity).onBackPressed()
        }
    }

    private fun setUpViewModel() {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        profileViewModel =
            ViewModelProvider(requireActivity(), profileFactory)[ProfileViewModel::class.java]
    }

    private fun callApi() {
        profileViewModel.getSecurityTips(5005).observe(viewLifecycleOwner) { it ->
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBar.hide()
                    binding.nestedScrollView.visibility = View.VISIBLE
                    it.data?.let { data ->
                        binding.tvPageHeading.text =
                            data.data.page.securityTips.sectionHeading.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.ROOT
                                ) else it.toString()
                            }
                        val stAdapter = SecurityTipsAdapter(
                            requireContext(),
                            data.data.page.securityTips.detailedInformation
                        )
                        binding.rvSecurityTips.adapter = stAdapter
                    }
                }
                Status.ERROR -> {
                    binding.progressBar.hide()
                    (requireActivity() as HomeActivity).showErrorToast(
                        it.message!!
                    )
                }
            }
        }
    }
}