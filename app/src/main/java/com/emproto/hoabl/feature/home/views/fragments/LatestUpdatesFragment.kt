package com.emproto.hoabl.feature.home.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.emproto.core.BaseFragment
import com.emproto.hoabl.adapters.InsightsAdapter
import com.emproto.hoabl.adapters.LatestUpdateAdapter
import com.emproto.hoabl.databinding.FragmentLatestUpdatesBinding

class LatestUpdatesFragment : BaseFragment() {

    lateinit var fragmentLatestUpdatesBinding: FragmentLatestUpdatesBinding
    lateinit var latestUpdateAdapter: LatestUpdateAdapter
    lateinit var gridLayoutManager: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentLatestUpdatesBinding= FragmentLatestUpdatesBinding.inflate(layoutInflater)
        initView()
        initClickListener()

        return fragmentLatestUpdatesBinding.root
    }

    private fun initView() {
        val list: ArrayList<String> = ArrayList()
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")
        latestUpdateAdapter= LatestUpdateAdapter(requireActivity(),list)
        gridLayoutManager= GridLayoutManager(requireContext(),2,)
        fragmentLatestUpdatesBinding.recyclerLatestUpdates.layoutManager=gridLayoutManager
        fragmentLatestUpdatesBinding.recyclerLatestUpdates.adapter=latestUpdateAdapter
    }

    private fun initClickListener() {

    }

}
