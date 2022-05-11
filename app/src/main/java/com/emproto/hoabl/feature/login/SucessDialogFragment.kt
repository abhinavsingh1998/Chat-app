package com.emproto.hoabl.feature.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.emproto.hoabl.databinding.SuccessLayoutBinding
import com.emproto.hoabl.feature.home.views.HomeActivity
import java.util.*


class SucessDialogFragment : DialogFragment(), View.OnClickListener {

    private lateinit var sucessLayoutBinding: SuccessLayoutBinding
    lateinit var  timer: Timer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        sucessLayoutBinding = SuccessLayoutBinding.inflate(inflater, container, false)

        arguments?.let {
            val firstName = it.getString("FirstName")
            sucessLayoutBinding.nameTag.text = "Dear $firstName"
        }

        clickListner()
        showDialog()
        return sucessLayoutBinding.root
    }

    override fun onClick(p0: View?) {

    }

    fun clickListner(){
        sucessLayoutBinding.continueBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireContext(), HomeActivity::class.java))
            requireActivity().finish()
        })
    }

     fun showDialog (){
         Handler().postDelayed({
             startActivity(Intent(requireContext(), HomeActivity::class.java))
             requireActivity().finish()
         }, 5000)
     }

}