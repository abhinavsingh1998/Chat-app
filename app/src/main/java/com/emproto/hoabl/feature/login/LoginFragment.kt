package com.emproto.hoabl.feature.login

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ActivityLoginBinding

class LoginFragment : BaseFragment() {
    private lateinit var activityLoginActivity: ActivityLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activityLoginActivity= ActivityLoginBinding.inflate(inflater,container,false)
        initView()
        initClickListeners()
        return activityLoginActivity.root
    }

    private fun initView() {

    }

    private fun initClickListeners() {

        activityLoginActivity.etMobile1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNullOrEmpty() || p0.toString().length < 10) {
                    activityLoginActivity.getOtpButton.isEnabled = false
                    activityLoginActivity.getOtpButton.isClickable = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        activityLoginActivity.getOtpButton.background = resources.getDrawable(R.drawable.unselect_button_bg)
                    }
                } else {
                    activityLoginActivity.getOtpButton.isEnabled = true
                    activityLoginActivity.getOtpButton.isClickable = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        activityLoginActivity.getOtpButton.background = resources.getDrawable(R.drawable.button_bg)
                    }
                }
            }

        })
        activityLoginActivity.getOtpButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                (requireActivity() as AuthActivity).replaceFragment(
                    OTPVerificationFragment.newInstance(
                        activityLoginActivity.etMobile1.text.toString()
                    ),true)
            }
        })

    }

}