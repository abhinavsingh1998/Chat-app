package com.emproto.hoabl.feature.home.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.emproto.core.BaseFragment
import com.emproto.hoabl.adapters.InsightsAdapter
import com.emproto.hoabl.databinding.FragmentInsightsBinding

class InsightsFragment : BaseFragment() {

    lateinit var fragmentInsightsBinding: FragmentInsightsBinding
    lateinit var insightsAdapter: InsightsAdapter
    lateinit var gridLayoutManager: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentInsightsBinding = FragmentInsightsBinding.inflate(layoutInflater)
        initView()
        initClickListener()
        return fragmentInsightsBinding.root
    }

    private fun initView() {
        val list: ArrayList<String> = ArrayList()
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")
        list.add("22L-2.5 Cr")
    }

    private fun initClickListener() {

    }
}
