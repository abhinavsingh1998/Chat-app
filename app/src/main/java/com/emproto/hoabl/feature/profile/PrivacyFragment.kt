package com.emproto.hoabl.feature.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import com.emproto.core.BaseFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.databinding.FragmentAboutUsBinding
import com.emproto.hoabl.databinding.FragmentPrivacyBinding
import com.emproto.hoabl.databinding.FragmentProfileMainBinding


class PrivacyFragment : BaseFragment() {
    lateinit var binding: FragmentPrivacyBinding


    val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPrivacyBinding.inflate(inflater, container, false)
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =
            false
        initClickListener()
        return binding.root
    }

    private fun initClickListener() {

        binding.backAction.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }
}



