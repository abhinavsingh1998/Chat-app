package com.emproto.hoabl.feature.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.databinding.FragmentSettingsBinding
import com.emproto.hoabl.feature.profile.adapter.SettingsAdapter
import com.emproto.hoabl.feature.profile.data.SettingsData


class SettingsFragment:Fragment() {
    lateinit var binding: FragmentSettingsBinding
    lateinit var adapter: SettingsAdapter
    lateinit var ivarrowleft:ImageView
    lateinit var settings_view:View
    val bundle = Bundle()
    companion object{
        fun newInstance(): SettingsFragment {
            val fragment = SettingsFragment()
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        binding.recyclerview1.layoutManager = LinearLayoutManager(requireContext())

        val detailAdapter = SettingsAdapter(requireContext(),initData())
        binding.recyclerview1.adapter = detailAdapter
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible=true
        initClickListener()
        return binding.root
    }

    private fun initClickListener() {
        binding.ivarrowleft.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val profileFragment = ProfileFragment()
                (requireActivity()as HomeActivity).replaceFragment(profileFragment.javaClass, "", true, bundle, null, 0, false)}
        })
    }
    private fun initData(): ArrayList<SettingsData> {
        val newsList: ArrayList<SettingsData> = ArrayList<SettingsData>()
        newsList.add(SettingsData("Location", " Control location access here", ))
        newsList.add(SettingsData("Read SMS", "Control location access here", ))
        newsList.add(SettingsData("Send Push Notifications", "Control location access here", ))
        newsList.add(SettingsData("Assistance Access", "Control location access here", ))

        return newsList
    }
}




