package com.emproto.hoabl.feature.login

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.core.customedittext.OnValueChangedListener
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentLoginBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.viewmodels.AuthViewmodel
import com.emproto.hoabl.viewmodels.factory.AuthFactory
import com.emproto.networklayer.request.login.OtpRequest
import com.emproto.networklayer.response.enums.Status
import javax.inject.Inject
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat


class LoginFragment : BaseFragment() {
    private lateinit var mBinding: FragmentLoginBinding

    @Inject
    lateinit var authFactory: AuthFactory
    lateinit var authViewModel: AuthViewmodel
    var hMobileNo = ""
    var hCountryCode = ""

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
        val list = ArrayList<String>()
        list.add("+91")
        list.add("+1")
        list.add("+311")
        mBinding.inputMobile.addDropDownValues(list)
    }

    private fun initClickListeners() {

        mBinding.inputMobile.onValueChangeListner(object : OnValueChangedListener {
            override fun onValueChanged(value: String?, countryCode: String) {
                hCountryCode = countryCode
                mBinding.otpText.visibility = View.VISIBLE
                mBinding.textError.visibility = View.GONE
            }

            override fun afterValueChanges(value1: String?) {
                hMobileNo = value1!!
                if (value1.isNullOrEmpty()) {
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

        mBinding.switchWhatspp.setOnCheckedChangeListener { p0, p1 ->
            if (p1) {
                val face = ResourcesCompat.getFont(requireContext(), R.font.jost_medium)
                mBinding.tvSwitch.typeface = face
                mBinding.tvSwitch.setTextColor(resources.getColor(R.color.black))
            } else {
                val regular = ResourcesCompat.getFont(requireContext(), R.font.jost_regular)
                mBinding.tvSwitch.typeface = regular
                mBinding.tvSwitch.setTextColor(resources.getColor(R.color.text_lightgrey_color))

            }
        }


        mBinding.getOtpButton.setOnClickListener {
            //TODO uncomment for no api call
//            (requireActivity() as AuthActivity).replaceFragment(
//                OTPVerificationFragment.newInstance(
//                    mBinding.etMobile1.text.toString()
//                ), true
//            )
            //validate mobile no
            if (hMobileNo.isEmpty() || hMobileNo.length != 10) {
                mBinding.otpText.visibility = View.INVISIBLE
                mBinding.textError.visibility = View.VISIBLE
                mBinding.inputMobile.showError()
                return@setOnClickListener
            }

            val otpRequest = OtpRequest(hMobileNo, "+91", "IN")
            authViewModel.getOtp(otpRequest).observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        (requireActivity() as AuthActivity).addFragment(
                            OTPVerificationFragment.newInstance(
                                hMobileNo, "+91"
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

