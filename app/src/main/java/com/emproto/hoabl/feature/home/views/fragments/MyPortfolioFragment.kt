package com.emproto.hoabl.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentMyPortfolioBinding

class MyPortfolioFragment : BaseFragment() {

    lateinit var fragmentMyPortfolioBinding: FragmentMyPortfolioBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentMyPortfolioBinding= FragmentMyPortfolioBinding.inflate(layoutInflater)
        initView()
        return fragmentMyPortfolioBinding.root
    }

    private fun initView() {
        
    }

}
