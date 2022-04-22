package com.emproto.hoabl.feature.home.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentReferralBinding


class ReferralFragment : BaseFragment() {


    lateinit var mBinding:FragmentReferralBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding= FragmentReferralBinding.inflate(inflater)

        return mBinding.root
    }

}