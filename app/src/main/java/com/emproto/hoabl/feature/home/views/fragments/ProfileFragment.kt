package com.emproto.hoabl.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentProfileBinding

class ProfileFragment : BaseFragment() {

    lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentProfileBinding.inflate(layoutInflater)
        initView()
        return binding.root
    }

    private fun initView() {
        binding.verisonName.text= "Verion  "+ com.emproto.hoabl.BuildConfig.VERSION_NAME
    }
}
