package com.emproto.hoabl.feature.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.emproto.hoabl.databinding.SuccessLayoutBinding


class SucessDialogFragment : DialogFragment(),View.OnClickListener {

    private lateinit var sucessLayoutBinding: SuccessLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        sucessLayoutBinding= SuccessLayoutBinding.inflate(inflater,container,false)

        return sucessLayoutBinding.root
    }

    override fun onClick(p0: View?) {


    }

}