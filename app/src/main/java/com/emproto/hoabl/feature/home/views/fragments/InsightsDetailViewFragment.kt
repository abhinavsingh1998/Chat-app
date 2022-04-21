package com.emproto.hoabl.feature.home.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentInsightsDetailViewBinding

class InsightsDetailViewFragment : BaseFragment() {

    lateinit var mBinding:FragmentInsightsDetailViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = FragmentInsightsDetailViewBinding.inflate(inflater)
        return mBinding.root
    }


}