package com.emproto.hoabl.feature.home.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
import com.emproto.hoabl.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentMyProjectsBinding
import com.emproto.hoabl.databinding.FragmentPortfolioNewUserBinding
import com.emproto.hoabl.feature.home.login.AuthActivity


class PortfolioNewUserFragment : BaseFragment() {

    lateinit var binding:FragmentPortfolioNewUserBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentPortfolioNewUserBinding.inflate(layoutInflater)

        binding.exploreBtn.setOnClickListener(View.OnClickListener {

            (requireActivity() as HomeActivity).addFragment(FinancialAndProjectFragment(), true)
        })

        return binding.root
    }


}