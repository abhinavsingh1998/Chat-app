package com.emproto.hoabl.feature.profile.fragments.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.emproto.hoabl.databinding.FragmentAccountKycStatusPopUpBinding

class AccountKycStatusPopUpFragment : DialogFragment(), View.OnClickListener {

    lateinit var binding: FragmentAccountKycStatusPopUpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAccountKycStatusPopUpBinding.inflate(inflater, container, false)
        binding.tcClose.setOnClickListener { dismiss() }

        return binding.root
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

}
