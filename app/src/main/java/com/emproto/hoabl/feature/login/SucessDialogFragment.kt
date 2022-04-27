package com.emproto.hoabl.feature.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.databinding.SuccessLayoutBinding


class SucessDialogFragment : DialogFragment(), View.OnClickListener {

    private lateinit var sucessLayoutBinding: SuccessLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        sucessLayoutBinding = SuccessLayoutBinding.inflate(inflater, container, false)

        arguments?.let {
            val firstName = it.getString("FirstName")
            sucessLayoutBinding.nameTag.text = "Dear $firstName"
        }

        sucessLayoutBinding.continueBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireContext(), HomeActivity::class.java))
            requireActivity().finish()
        })
        return sucessLayoutBinding.root
    }

    override fun onClick(p0: View?) {


    }

}