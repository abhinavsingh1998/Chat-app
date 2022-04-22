package com.emproto.hoabl.feature.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
<<<<<<< HEAD
import com.emproto.hoabl.HomeActivity
=======
>>>>>>> 18f31f70846a8f1a1f13937359f0310e561d1e04
import com.emproto.hoabl.databinding.SuccessLayoutBinding
import com.emproto.hoabl.feature.home.views.HomeActivity


class SucessDialogFragment : DialogFragment(), View.OnClickListener {

    private lateinit var sucessLayoutBinding: SuccessLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        sucessLayoutBinding = SuccessLayoutBinding.inflate(inflater, container, false)

        sucessLayoutBinding.continueBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireContext(), HomeActivity::class.java))
            requireActivity().finish()
        })
        return sucessLayoutBinding.root
    }

    override fun onClick(p0: View?) {


    }

}