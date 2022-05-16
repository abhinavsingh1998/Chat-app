package com.emproto.hoabl.feature.home.views.fragments

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ReferralDialogBinding


class ReferralDialog : DialogFragment(), View.OnClickListener {


    lateinit var mBinding: ReferralDialogBinding
    var charSequence1: Editable? = null
    var charSequence2: Editable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mBinding = ReferralDialogBinding.inflate(inflater, container, false)

        initClickListner()
        return mBinding.root
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            when (p0.id){
                R.id.close_btn ->{
                    dialog?.dismiss()
                }

            }
        }

    }

    private fun initClickListner() {
        mBinding.referralName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

                charSequence1 = p0
                charSequence2 = null
                if (p0.toString().isNullOrEmpty() || charSequence2.toString().isNullOrEmpty()) {
                    mBinding.referBtn.isEnabled = false
                    mBinding.referBtn.isClickable = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mBinding.referBtn.background =
                            AppCompatResources.getDrawable(requireContext(),
                                R.drawable.unselect_button_bg)
                    }
                } else {
                    mBinding.referBtn.isEnabled = true
                    mBinding.referBtn.isClickable = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mBinding.referBtn.background =
                            AppCompatResources.getDrawable(requireContext(), R.drawable.button_bg)

                    }
                }
            }
        })

        mBinding.referralNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

                charSequence2 = p0
                charSequence1 = null
                if (charSequence1.toString().isNullOrEmpty() || p0.toString().isNullOrEmpty()) {
                    mBinding.referBtn.isEnabled = false
                    mBinding.referBtn.isClickable = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mBinding.referBtn.background =
                            AppCompatResources.getDrawable(requireContext(),
                                R.drawable.unselect_button_bg)
                    }
                } else {
                    mBinding.referBtn.isEnabled = true
                    mBinding.referBtn.isClickable = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mBinding.referBtn.background =
                            AppCompatResources.getDrawable(requireContext(), R.drawable.button_bg)


                    }
                }
            }

        })
    }
}