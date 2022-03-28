package com.emproto.hoabl.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentPromisesBinding

class PromisesFragment : BaseFragment() {
    lateinit var binding:FragmentPromisesBinding

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding= FragmentPromisesBinding.inflate(layoutInflater)

        initView()
        return binding.root
    }

    private fun initView() {

    }
}
