package com.emproto.hoabl.feature.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.emproto.hoabl.databinding.FragmentSecurityBinding
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.profile.adapter.SecurityAdapter
import com.emproto.hoabl.feature.profile.adapter.SettingsAdapter
import com.emproto.hoabl.model.RecyclerViewItem


class SecurityFragment : Fragment() ,SecurityAdapter.HelpItemInterface{
    lateinit var binding: FragmentSecurityBinding
    lateinit var adapter: SettingsAdapter

    val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible=false
        binding = FragmentSecurityBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fun initData(): ArrayList<RecyclerViewItem> {
            val dataList: ArrayList<RecyclerViewItem> = ArrayList<RecyclerViewItem>()
            dataList.add(RecyclerViewItem(SecurityAdapter.VIEW_SECURITY_AUTHENTICATE))
            dataList.add(RecyclerViewItem(SecurityAdapter.VIEW_SECURITY_WHATSAPP_COMMUNICATION))
            dataList.add(RecyclerViewItem(SecurityAdapter.VIEW_SECURITY_LOCATION))
            dataList.add(RecyclerViewItem(SecurityAdapter.VIEW_SETTINGS_ALL_OPTIONS))
            return dataList
        }

        val adapter = SecurityAdapter(this.requireContext(), initData(),this)
        binding.rvHelpCenter.adapter = adapter

        binding.backAction.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onClickItem(layoutPosition: Int) {
        when(layoutPosition){
            0->{

            }
            1->{

            }
            2->{
                val securityTipsFragment = SecurityTipsFragment()
                (requireActivity() as HomeActivity).addFragment(securityTipsFragment, false)
            }
            3->{
                val securityTipsFragment=SecurityTipsFragment()
                (requireActivity() as HomeActivity).addFragment(securityTipsFragment, false)
            }
        }
    }
}

