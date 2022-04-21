package com.emproto.hoabl.feature.login

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentLoginBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.viewmodels.AuthViewmodel
import com.emproto.hoabl.viewmodels.factory.AuthFactory
import com.emproto.networklayer.request.login.OtpRequest
import com.emproto.networklayer.response.enums.Status
import javax.inject.Inject

class LoginFragment : BaseFragment() {
    private lateinit var mBinding: FragmentLoginBinding

    @Inject
    lateinit var authFactory: AuthFactory
    lateinit var authViewModel: AuthViewmodel

    @Inject
    lateinit var appPreference: AppPreference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        authViewModel = ViewModelProvider(requireActivity(), authFactory)[AuthViewmodel::class.java]
        mBinding = FragmentLoginBinding.inflate(inflater, container, false)
        initView()
        initClickListeners()
        return mBinding.root
    }

    private fun initView() {

    }

    private fun initClickListeners() {

        mBinding.etMobile1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNullOrEmpty() || p0.toString().length < 10) {
                    mBinding.getOtpButton.isEnabled = false
                    mBinding.getOtpButton.isClickable = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mBinding.getOtpButton.background =
                            resources.getDrawable(R.drawable.unselect_button_bg)
                    }
                } else {
                    mBinding.getOtpButton.isEnabled = true
                    mBinding.getOtpButton.isClickable = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mBinding.getOtpButton.background =
                            resources.getDrawable(R.drawable.button_bg)
                    }
                }
            }

        })

        mBinding.getOtpButton.setOnClickListener {
            //TODO uncomment for no api call
//            (requireActivity() as AuthActivity).replaceFragment(
//                OTPVerificationFragment.newInstance(
//                    mBinding.etMobile1.text.toString()
//                ), true
//            )

            val otpRequest = OtpRequest(mBinding.etMobile1.text.toString(), "", "IN")
            authViewModel.getOtp(otpRequest).observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        (requireActivity() as AuthActivity).addFragment(
                            OTPVerificationFragment.newInstance(
                                mBinding.etMobile1.text.toString()
                            ), true
                        )
                    }
                    Status.ERROR -> {
                        mBinding.getOtpButton.visibility = View.VISIBLE
                        mBinding.progressBar.visibility = View.INVISIBLE
                        it.data
                        (requireActivity() as AuthActivity).showErrorToast(
                            it.message!!
                        )
                    }
                    Status.LOADING -> {
                        mBinding.getOtpButton.visibility = View.INVISIBLE
                        mBinding.progressBar.visibility = View.VISIBLE
                    }
                }
            })
        }
    }

}

