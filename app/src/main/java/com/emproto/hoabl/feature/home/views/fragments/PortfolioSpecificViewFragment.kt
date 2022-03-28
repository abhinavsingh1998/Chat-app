package com.emproto.hoabl.feature.home.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentPortfolioBinding
import com.emproto.hoabl.databinding.FragmentPortfolioSpecificviewBinding

class PortfolioSpecificViewFragment : BaseFragment() {

    lateinit var binding: FragmentPortfolioSpecificviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPortfolioSpecificviewBinding.inflate(layoutInflater)

        initView()
        initClickListeners()
        return binding.root
    }

    private fun initClickListeners() {

    }

    private fun initView() {

    }
}