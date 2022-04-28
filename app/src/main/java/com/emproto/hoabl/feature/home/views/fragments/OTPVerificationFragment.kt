package com.emproto.hoabl.feature.home.views.fragments

import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentVerifyOtpBinding
import com.emproto.hoabl.feature.login.AuthActivity
import com.emproto.hoabl.feature.login.NameInputFragment


class OTPVerificationFragment : BaseFragment() {

    private lateinit var mBinding: FragmentVerifyOtpBinding
    var countOtp: Int = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = FragmentVerifyOtpBinding.inflate(layoutInflater)
        initView()
        initClickListener()
        return mBinding.root
    }

    private fun initView() {
        mBinding.tvMobileNumber.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        /*getTimerCount()
        activityOtpVerifyBinding.textResend.setOnClickListener {
            countOtp--
            getTimerCount()
        }
        if (countOtp==0){
            showSnackMessage("OTP usage is over.Please try again later",activityOtpVerifyBinding.root)
        }*/
    }

    private fun initClickListener() {
        mBinding.etOtp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 5 && s?.length <= 6) {
                    showSnackMessage("please enter valid OTP", mBinding.root)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 6) {
                    if (isNetworkAvailable(mBinding.root)) {
                        mBinding.layout1.alpha = 1.0F
                        (requireActivity() as AuthActivity).replaceFragment(NameInputFragment(),
                            true)
                    } else {
                        mBinding.layout1.setBackgroundColor(resources.getColor(R.color.background_grey))
                        mBinding.layout1.alpha = 0.8F
                        showSnackBar(mBinding.root)
                    }
                }
            }

        })
    }
}




