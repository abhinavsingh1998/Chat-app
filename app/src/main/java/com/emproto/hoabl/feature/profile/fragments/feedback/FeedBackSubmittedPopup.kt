package com.emproto.hoabl.feature.profile.fragments.feedback

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FeedbackSubmittedPopupBinding

class FeedBackSubmittedPopup : DialogFragment(), View.OnClickListener {

    lateinit var binding: FeedbackSubmittedPopupBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FeedbackSubmittedPopupBinding.inflate(inflater, container, false)
        binding.tcClose.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

}