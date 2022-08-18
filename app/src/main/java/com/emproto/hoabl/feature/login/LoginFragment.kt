package com.emproto.hoabl.feature.login

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.method.ScrollingMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.core.customedittext.OnValueChangedListener
import com.emproto.core.databinding.TermsConditionDialogBinding
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentLoginBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.viewmodels.AuthViewmodel
import com.emproto.hoabl.viewmodels.factory.AuthFactory
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.request.login.OtpRequest
import com.emproto.networklayer.response.enums.Status
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.regex.Pattern
import javax.inject.Inject


class LoginFragment : BaseFragment() {
    private lateinit var mBinding: FragmentLoginBinding

    @Inject
    lateinit var authFactory: AuthFactory
    lateinit var authViewModel: AuthViewmodel
    var hMobileNo = ""
    var hCountryCode = ""
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var termsConditionDialogBinding: TermsConditionDialogBinding
    val patterns  = Pattern.compile("^(0|[1-9][0-9]*)\$")
    val first_attempt= "Please enter the OTP sent to your mobile number. You have 4 attempts remaining to verify your OTP."
    val second_attempts= "Please enter the OTP sent to your mobile number. You have 3 attempts remaining to verify your OTP."
    val third_attempts= "Please enter the OTP sent to your mobile number. You have 2 attempts remaining to verify your OTP."
    val four_attempts= "Please enter the OTP sent to your mobile number. You have 1 attempt remaining to verify your OTP."
    val five_attempts= R.string.five_attempts_remaining
    val try_one_hour= R.string.try_after_one_hour
    var hint_text= ""
    var isCheck = false


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
        initObserver()
        return mBinding.root
    }


    private fun initObserver() {
        authViewModel.getTermsCondition(5005).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let {
                        termsConditionDialogBinding.tvTitle.text =
                            showHTMLText(it.data.page.termsAndConditions.description)
                        termsConditionDialogBinding.tvTitle.setMovementMethod(
                            ScrollingMovementMethod()
                        )
                    }
                }
            }
        })
    }

    private fun initView() {
        val list = ArrayList<String>()
        list.add("+91")
        mBinding.inputMobile.addDropDownValues(list)

        hMobileNo= appPreference.getMobilenum()
        bottomSheetDialog = BottomSheetDialog(requireContext())
        termsConditionDialogBinding = TermsConditionDialogBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(termsConditionDialogBinding.root)

        mBinding.textTerms.makeLinks(
            Pair("Terms and Conditions & Privacy Policy", View.OnClickListener {
                termsConditionDialogBinding.tvHeading.text = getString(R.string.termscondition)
                bottomSheetDialog.show()
            }),
//            Pair("Privacy Policy", View.OnClickListener {
//                termsConditionDialogBinding.tvHeading.text = getString(R.string.privacypolicy)
//                bottomSheetDialog.show()
//            })
        )

        termsConditionDialogBinding.acitonClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
    }



    private fun initClickListeners() {

        mBinding.inputMobile.onValueChangeListner(object : OnValueChangedListener {
            override fun onValueChanged(value: String?, countryCode: String) {
                appPreference.setMobilenum(hMobileNo)
                hCountryCode = countryCode
                mBinding.otpText.visibility = View.VISIBLE
                mBinding.textError.visibility = View.GONE
                mBinding.switchWhatspp.isChecked= true
            }

            override fun afterValueChanges(value1: String?) {
                mBinding.switchWhatspp.isChecked= true
                hMobileNo = value1!!
                appPreference.setMobilenum(hMobileNo)
                if (value1.isNullOrEmpty()) {
                    mBinding.getOtpButton.isEnabled = false
                    mBinding.getOtpButton.isClickable = false
                    mBinding.switchWhatspp.isChecked= false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mBinding.getOtpButton.background =
                            resources.getDrawable(R.drawable.unselect_button_bg)
                    }
                } else {
                    mBinding.getOtpButton.isEnabled = true
                    mBinding.getOtpButton.isClickable = true
                    mBinding.switchWhatspp.isChecked= true
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
                isCheck = true
            } else {
                val regular = ResourcesCompat.getFont(requireContext(), R.font.jost_regular)
                mBinding.tvSwitch.typeface = regular
                mBinding.tvSwitch.setTextColor(resources.getColor(R.color.text_lightgrey_color))
                isCheck = false
            }
        }

        mBinding.getOtpButton.setOnClickListener {

            if (hMobileNo.isNullOrEmpty() || hMobileNo.length!= 10 || !hMobileNo.isNumber()) {
                mBinding.otpText.visibility = View.INVISIBLE
                mBinding.textError.visibility = View.VISIBLE
                mBinding.inputMobile.showError()
                return@setOnClickListener
            }


            val otpRequest = OtpRequest(hMobileNo, "+91", "IN")
            authViewModel.getOtp(otpRequest).observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        if (it.data?.message.toString().equals("Please enter the OTP sent to your mobile number")){
                            hint_text="Enter OTP"
                        } else if (it.data?.message.toString().equals(four_attempts)){
                            hint_text="Enter OTP (1 attempts left)"
                        } else if (it.data?.message.toString().equals(third_attempts)){
                            hint_text= "Enter OTP (2 attempts left)"
                        }else if (it.data?.message.toString().equals(second_attempts)){
                            hint_text= "Enter OTP (3 attempts left)"
                        }else if (it.data?.message.toString().equals(first_attempt)){
                            hint_text= "Enter OTP (4 attempts left)"
                        } else{
                            hint_text= "Enter OTP"
                        }

                        mBinding.getOtpButton.visibility = View.VISIBLE
                        mBinding.progressBar.visibility = View.INVISIBLE

                        (requireActivity() as AuthActivity).replaceFragment(
                            OTPVerificationFragment.newInstance(
                                hMobileNo, "+91",
                            hint_text,isCheck), true
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

    fun CharSequence?.isNumber() =
        patterns.matcher(this).matches()

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

