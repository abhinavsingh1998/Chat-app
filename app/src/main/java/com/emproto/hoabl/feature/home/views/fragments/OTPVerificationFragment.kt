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
import com.emproto.hoabl.databinding.ActivityOtpVerifyBinding
import com.emproto.hoabl.feature.home.login.AuthActivity
import com.emproto.hoabl.feature.home.login.NameInputFragment


class OTPVerificationFragment : BaseFragment() {

    private lateinit var activityOtpVerifyBinding: ActivityOtpVerifyBinding
    var countOtp: Int = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        activityOtpVerifyBinding = ActivityOtpVerifyBinding.inflate(layoutInflater)
        initView()
        initClickListener()
        return activityOtpVerifyBinding.root
    }

    private fun initView() {
        activityOtpVerifyBinding.tvMobileNumber.paintFlags = Paint.UNDERLINE_TEXT_FLAG
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
        activityOtpVerifyBinding.etOtp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 5 && s?.length <= 6) {
                    showSnackMessage("please enter valid OTP", activityOtpVerifyBinding.root)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 6) {
                    if (isNetworkAvailable(activityOtpVerifyBinding.root)) {
                        activityOtpVerifyBinding.layout1.alpha = 1.0F
                        (requireActivity() as AuthActivity).replaceFragment(NameInputFragment(), true)
                    } else {
                        activityOtpVerifyBinding.layout1.setBackgroundColor(resources.getColor(R.color.background_grey))
                        activityOtpVerifyBinding.layout1.alpha = 0.8F
                        showSnackBar(activityOtpVerifyBinding.root)
                    }
                }
            }

        })
    }}




