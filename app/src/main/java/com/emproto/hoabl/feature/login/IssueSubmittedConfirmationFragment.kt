package com.emproto.hoabl.feature.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.emproto.hoabl.databinding.FragmentIssueSubmittedConfirmationBinding


class IssueSubmittedConfirmationFragment : DialogFragment(), View.OnClickListener {

    lateinit var binding: FragmentIssueSubmittedConfirmationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentIssueSubmittedConfirmationBinding.inflate(inflater, container, false)
        binding.tcClose.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

}