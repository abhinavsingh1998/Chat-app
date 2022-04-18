package com.emproto.hoabl.feature.profileui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.databinding.FragmentAboutUsBinding
import com.emproto.hoabl.databinding.FragmentProfileMainBinding


class AboutUsFragment : Fragment() {
    lateinit var binding: FragmentAboutUsBinding
    lateinit var ivarrow:ImageView

    val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        FragmentProfileMainBinding.inflate(inflater, container, false)
        binding= FragmentAboutUsBinding.inflate(inflater,container,false)
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible=true
        initClickListener()
        return binding.root
    }
    private fun initClickListener() {

        binding.ivarrow.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val healthCenterFragment = HealthCenterFragment()
                (requireActivity()as HomeActivity).replaceFragment(healthCenterFragment.javaClass, "", true, bundle, null, 0, false)}
        })

    }
}



