package com.emproto.hoabl.feature.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.emproto.hoabl.databinding.FragmentSecurityTipsBinding
import com.emproto.hoabl.feature.home.views.HomeActivity

class SecurityTipsFragment : Fragment() {

    lateinit var binding: FragmentSecurityTipsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSecurityTipsBinding.inflate(inflater, container, false)
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible=false

        initClickListeners()
        return binding.root
    }

    private fun initClickListeners() {
        requireActivity().supportFragmentManager.popBackStack()
    }


}