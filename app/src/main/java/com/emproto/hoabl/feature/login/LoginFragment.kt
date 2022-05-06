package com.emproto.hoabl.feature.login

import android.graphics.Color
import android.os.Build
import android.os.Bundle
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
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.emproto.core.databinding.TermsConditionDialogBinding
import com.emproto.hoabl.databinding.FragmentSigninIssueBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


class LoginFragment : BaseFragment() {
    private lateinit var mBinding: FragmentLoginBinding

    @Inject
    lateinit var authFactory: AuthFactory
    lateinit var authViewModel: AuthViewmodel
    var hMobileNo = ""
    var hCountryCode = ""
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var termsConditionDialogBinding: TermsConditionDialogBinding

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

        bottomSheetDialog = BottomSheetDialog(requireContext())
        termsConditionDialogBinding = TermsConditionDialogBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(termsConditionDialogBinding.root)

        mBinding.textTerms.makeLinks(
            Pair("Terms of services", View.OnClickListener {
                bottomSheetDialog.show()
            }),
            Pair("Privacy policy", View.OnClickListener {
                bottomSheetDialog.show()
            })
        )

        termsConditionDialogBinding.acitonClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
    }

    private fun initClickListeners() {

        mBinding.inputMobile.onValueChangeListner(object : OnValueChangedListener {
            override fun onValueChanged(value: String?, countryCode: String) {
                hCountryCode = countryCode
                mBinding.otpText.visibility = View.VISIBLE
                mBinding.textError.visibility = View.GONE
                mBinding.switchWhatspp.isChecked = true
            }

            override fun afterValueChanges(value1: String?) {
                hMobileNo = value1!!
                if (value1.isNullOrEmpty()) {
                    mBinding.getOtpButton.isEnabled = false
                    mBinding.getOtpButton.isClickable = false
                    mBinding.switchWhatspp.isChecked = false
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            mBinding.getOtpButton.background =
                                resources.getDrawable(R.drawable.unselect_button_bg)
                        }
                } else {
                    mBinding.getOtpButton.isEnabled = true
                    mBinding.getOtpButton.isClickable = true
                    appPreference.setMobilenum(hMobileNo)
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
//
            if (hMobileNo.isEmpty() || hMobileNo.length != 10) {
                mBinding.otpText.visibility = View.INVISIBLE
                mBinding.textError.visibility = View.VISIBLE
                mBinding.inputMobile.showError()
                return@setOnClickListener
            }

            if (isNetworkAvailable(mBinding.root)) {
                hideSoftKeyboard()
                mBinding.layout1.setBackgroundColor(resources.getColor(R.color.app_color))
            val otpRequest = OtpRequest(hMobileNo, "+91", "IN")
            authViewModel.getOtp(otpRequest).observe(viewLifecycleOwner, Observer {
                    when (it.status) {
                        Status.SUCCESS -> {
                            (requireActivity() as AuthActivity).replaceFragment(
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
            else{
                internetOffState()
            }
        }

    }

    private fun internetOffState(){
        mBinding.layout1.setBackgroundColor(resources.getColor(R.color.background_grey))
        mBinding.getOtpButton.isEnabled=false
        mBinding.getOtpButton.isClickable=false
        mBinding.getOtpButton.background =
            resources.getDrawable(R.drawable.unselect_button_bg)
        mBinding.switchWhatspp.isChecked= false
        showSnackBar(mBinding.root)
    }

    private fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
        val spannableString = SpannableString(this.text)
        var startIndexOfLink = -1
        for (link in links) {
            val clickableSpan = object : ClickableSpan() {
                override fun updateDrawState(textPaint: TextPaint) {
                    // use this to change the link color
                    textPaint.color = Color.BLACK
                    // toggle below value to enable/disable
                    // the underline shown below the clickable text
                    textPaint.isUnderlineText = true
                }

                override fun onClick(view: View) {
                    Selection.setSelection((view as TextView).text as Spannable, 0)
                    view.invalidate()
                    link.second.onClick(view)
                }
            }
            startIndexOfLink = this.text.toString().indexOf(link.first, startIndexOfLink + 1)
//      if(startIndexOfLink == -1) continue // todo if you want to verify your texts contains links text
            spannableString.setSpan(
                clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        this.movementMethod =
            LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
        this.setText(spannableString, TextView.BufferType.SPANNABLE)
    }

}

