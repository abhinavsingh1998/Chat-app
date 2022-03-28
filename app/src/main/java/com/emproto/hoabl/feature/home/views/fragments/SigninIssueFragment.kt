package com.emproto.hoabl.feature.home.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.hoabl.databinding.FragmentSigninIssueBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SigninIssueFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentSigninIssueBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSigninIssueBinding.inflate(layoutInflater)

        return binding.root

    }

//    @SuppressLint("ResourceAsColor")
//    private fun radio_event(){
//
//        binding.issueList.setOnClickListener(View.OnClickListener {
//
//            binding.issueOne.setBackgroundColor(R.color.black)
//        })
//    }


}








