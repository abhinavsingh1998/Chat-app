package com.emproto.hoabl.feature.profileui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentSecurityBinding
import com.emproto.hoabl.feature.home.profileAdapter.SecurityAdapter
import com.emproto.hoabl.feature.home.profileAdapter.data.SecurityData



class SecurityFragment : Fragment() {
    lateinit var binding: FragmentSecurityBinding
    lateinit var leftarrowimage:ImageView
    val bundle = Bundle()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSecurityBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager= LinearLayoutManager(requireContext())
        val detailAdapter= SecurityAdapter(requireContext(),initData())
        binding.recyclerView.adapter= detailAdapter
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible=true
        initClickListener()
        return binding.root
    }
    private fun initClickListener() {
        binding.leftarrowimage.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val profileFragment = ProfileFragment()
                (requireActivity()as HomeActivity).replaceFragment(profileFragment.javaClass, "", true, bundle, null, 0, false)}
        })
    }
    private fun initData(): ArrayList<SecurityData> {
        val dataList: ArrayList<SecurityData> = ArrayList<SecurityData>()
        dataList.add(SecurityData(SecurityAdapter.VIEW_SECURITY_ONE, "Control location access here","Mobile Pin Authentication",
            R.drawable.arrow))
        dataList.add(SecurityData(SecurityAdapter.VIEW_SECURITY_TWO, "Control location access here","Whatsapp Communication",
            R.drawable.arrow))
        dataList.add(SecurityData(SecurityAdapter.VIEW_SECURITY_THREE, "Control location access here","Read Security",
            R.drawable.arrow))


      return dataList
    }

}