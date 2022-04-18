package com.emproto.hoabl.feature.profileui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.databinding.FragmentAccountDetailsBinding
import com.emproto.hoabl.databinding.FragmentProfileMainBinding

class Account_Details_Fragment : Fragment() {
    lateinit var binding: FragmentAccountDetailsBinding
    lateinit var imgArrow:ImageView
    val bundle = Bundle()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        FragmentProfileMainBinding.inflate(inflater, container, false)
        binding= FragmentAccountDetailsBinding.inflate(inflater,container,false)
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible=true
        initClickListener()
        return binding.root
    }
    private fun initClickListener() {
        binding.imgArrow.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val profileFragment = ProfileFragment()
                (requireActivity()as HomeActivity).replaceFragment(profileFragment.javaClass, "", true, bundle, null, 0, false)}
        })

    }
}