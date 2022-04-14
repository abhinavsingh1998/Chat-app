package com.emproto.hoabl.feature.login

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ActivityLoginBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.viewmodels.AuthViewmodel
import com.emproto.hoabl.viewmodels.factory.AuthFactory
import com.emproto.networklayer.request.OtpRequest
import com.emproto.networklayer.response.enums.Status
import javax.inject.Inject

class LoginFragment : BaseFragment() {
    private lateinit var activityLoginActivity: ActivityLoginBinding

    @Inject
    lateinit var authFactory: AuthFactory
    lateinit var authViewModel: AuthViewmodel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        authViewModel = ViewModelProvider(requireActivity(), authFactory)[AuthViewmodel::class.java]
        activityLoginActivity = ActivityLoginBinding.inflate(inflater, container, false)
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
                        activityLoginActivity.getOtpButton.background =
                            resources.getDrawable(R.drawable.unselect_button_bg)
                    }
                } else {
                    activityLoginActivity.getOtpButton.isEnabled = true
                    activityLoginActivity.getOtpButton.isClickable = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        activityLoginActivity.getOtpButton.background =
                            resources.getDrawable(R.drawable.button_bg)
                    }
                }
            }

        })
        //TODO to star next screen without calling api
        activityLoginActivity.getOtpButton.setOnClickListener {
            //TODO uncomment for no api call
//            (requireActivity() as AuthActivity).addFragment(
//                OTPVerificationFragment.newInstance(
//                    activityLoginActivity.etMobile1.text.toString()
//                ), true
//            )

            val otpRequest = OtpRequest(activityLoginActivity.etMobile1.text.toString())
            authViewModel.getOtp(otpRequest).observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        (requireActivity() as AuthActivity).addFragment(
                            OTPVerificationFragment.newInstance(
                                activityLoginActivity.etMobile1.text.toString()
                            ), true
                        )
                    }
                    Status.ERROR -> {
                        activityLoginActivity.getOtpButton.visibility = View.VISIBLE
                        activityLoginActivity.progressBar.visibility = View.GONE
                    }
                    Status.LOADING -> {
                        activityLoginActivity.getOtpButton.visibility = View.GONE
                        activityLoginActivity.progressBar.visibility = View.VISIBLE
                    }
                }
            })
        }
    }

}

