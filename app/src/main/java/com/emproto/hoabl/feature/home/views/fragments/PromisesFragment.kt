package com.emproto.hoabl.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
<<<<<<< HEAD
import com.emproto.hoabl.HomeActivity
=======
>>>>>>> 18f31f70846a8f1a1f13937359f0310e561d1e04
import com.emproto.hoabl.databinding.FragmentPromisesBinding
import com.emproto.hoabl.feature.home.views.HomeActivity

class PromisesFragment : BaseFragment() {
    lateinit var binding:FragmentPromisesBinding

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding= FragmentPromisesBinding.inflate(layoutInflater)
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility=View.VISIBLE

        initView()
        return binding.root
    }

    private fun initView() {

    }
}
