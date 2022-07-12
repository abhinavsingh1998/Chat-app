package com.emproto.hoabl.feature.login

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.text.color
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentVerifyOtpBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.viewmodels.AuthViewmodel
import com.emproto.hoabl.viewmodels.factory.AuthFactory
import com.emproto.networklayer.request.login.OtpRequest
import com.emproto.networklayer.request.login.OtpVerifyRequest
import com.emproto.networklayer.response.enums.Status
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject
import com.emproto.hoabl.smsverificatio.SmsBroadcastReceiver
import com.emproto.networklayer.preferences.AppPreference
import okhttp3.internal.wait


class OTPVerificationFragment : BaseFragment() {

    private lateinit var mBinding: FragmentVerifyOtpBinding
    lateinit var smsBroadcastReceiver: SmsBroadcastReceiver
    var counter = 30000L
    lateinit var countDownTimer: CountDownTimer

    /// lateinit var dialog: Dialog
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var isReadSMSGranted = false
    val permissionRequest: MutableList<String> = ArrayList()

    @Inject
    lateinit var authFactory: AuthFactory
    lateinit var authViewModel: AuthViewmodel

    lateinit var authActivity: AuthActivity

    @Inject
    lateinit var appPreference: AppPreference
    lateinit var bottomSheetDialog: BottomSheetDialog

    var attempts_num = 0
    val Invalid_otp =
        "You have incorrectly typed the OTP 5 times. Please retry again after an hour."
    val fisrt_attempt =
        "You have incorrectly typed the OTP 1 time. Click on Resend OTP to receive OTP again"
    val second_attempt =
        "You have incorrectly typed the OTP 2 times. Click on Resend OTP to receive OTP again"
    val third_attempt =
        "You have incorrectly typed the OTP 3 times. Click on Resend OTP to receive OTP again"
    val fourth_attempt =
        "You have incorrectly typed the OTP 4 times. Click on Resend OTP to receive OTP again"

    companion object {
        const val TAG = "SMS_USER_CONSENT"
        const val REQ_USER_CONSENT = 100
        var mobileno: String = ""
        var countryCode: String = ""
        var hint_txt: String = ""
        var iswhatsappenabled = false

        fun newInstance(
            mobileNumber: String,
            cCode: String,
            hintText: String,
            whatsappConsent: Boolean
        ): OTPVerificationFragment {
            val fragment = OTPVerificationFragment()
            val bundle = Bundle()
            mobileno = bundle.getString("mobilenumber", mobileNumber)
            countryCode = bundle.getString("countrycode", cCode)
            hint_txt = bundle.getString("hint_txt", hintText)
            iswhatsappenabled = bundle.getBoolean("whatsappConsent",whatsappConsent)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        authViewModel = ViewModelProvider(requireActivity(), authFactory)[AuthViewmodel::class.java]
        mBinding = FragmentVerifyOtpBinding.inflate(layoutInflater)
        authActivity = AuthActivity()
        initView()
        initClickListener()
        otpTimerCount()
        edit_number()
        resentOtp()

        return mBinding.root
    }

    override fun onPause() {
        super.onPause()
        dismissSnackBar()
    }
//    private fun startUserConsent() {
//        val client = SmsRetriever.getClient(requireContext())
//        client.startSmsUserConsent(null)
//    }

    private fun initView() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                isReadSMSGranted = permissions[Manifest.permission.READ_SMS] ?: isReadSMSGranted
            }

        requestPermission()
        mBinding.tvMobileNumber.text = "$countryCode-$mobileno"
        mBinding.tvMobileNumber.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        mBinding.loginEdittext.hint = hint_txt

        startSmsUserConsent()

    }

    private fun initClickListener() {
        mBinding.etOtp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            @RequiresApi(Build.VERSION_CODES.M)
            @SuppressLint("ResourceType")
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 6) {
                    if (isNetworkAvailable()) {
                        internetState(true)
                        hideSoftKeyboard()
                        val otpVerifyRequest =
                            OtpVerifyRequest(
                                s.toString(),
                                mobileno,
                                iswhatsappenabled,
                                "+91",
                                appPreference.getNotificationToken()
                            )

                        authViewModel.verifyOtp(otpVerifyRequest).observe(viewLifecycleOwner,
                            Observer {
                                when (it.status) {
                                    Status.LOADING -> {
                                        mBinding.loader.visibility = View.VISIBLE
                                    }
                                    Status.ERROR -> {
                                        mBinding.loader.visibility = View.GONE
                                        if (it.message.toString().equals(fisrt_attempt)) {
                                            mBinding.loginEdittext.setHint("Enter OTP (4 attempts left)")
                                            mBinding.tryAgainTxt.isVisible = false
                                            Toast.makeText(
                                                requireContext(),
                                                it.message,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } else if (it.message.toString().equals(second_attempt)) {
                                            mBinding.loginEdittext.setHint("Enter OTP (3 attempts left)")
                                            Toast.makeText(
                                                requireContext(),
                                                it.message,
                                                Toast.LENGTH_LONG
                                            ).show()
                                            mBinding.tryAgainTxt.isVisible = false
                                        } else if (it.message.toString().equals(third_attempt)) {
                                            mBinding.loginEdittext.setHint("Enter OTP (2 attempts left)")
                                            mBinding.tryAgainTxt.isVisible = false
                                            Toast.makeText(
                                                requireContext(),
                                                it.message,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } else if (it.message.toString().equals(fourth_attempt)) {
                                            mBinding.loginEdittext.setHint("Enter OTP (1 attempts left)")
                                            mBinding.tryAgainTxt.isVisible = false
                                            Toast.makeText(
                                                requireContext(),
                                                it.message,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } else if (it.message.toString().equals(Invalid_otp)) {
                                            mBinding.loginEdittext.setHint("Enter OTP (0 attempts left)")
                                            block_for_one_hour(it.message.toString())
                                        }

                                    }
                                    Status.SUCCESS -> {
                                        //save token to preference
                                        mBinding.loader.visibility = View.INVISIBLE
                                        (requireActivity() as AuthActivity).showSuccessToast(
                                            "OTP Verified Successfully!"
                                        )

                                        Handler().postDelayed({
                                            it.data?.let {
                                                appPreference.setToken(it.token)
                                                if (it.user.contactType == "prelead" &&
                                                    it.user.firstName.isNullOrBlank()
                                                ) {
                                                    requireActivity().supportFragmentManager.popBackStack()
                                                    (requireActivity() as AuthActivity).replaceFragment(
                                                        NameInputFragment.newInstance(
                                                            if (it.user.firstName != null) it.user.firstName else "",
                                                            if (it.user.lastName != null) it.user.lastName else ""
                                                        ), true
                                                    )
                                                } else {
                                                    appPreference.saveLogin(true)
                                                    startActivity(
                                                        Intent(
                                                            requireContext(),
                                                            HomeActivity::class.java
                                                        )
                                                    )
                                                    requireActivity().finish()
                                                }
                                            }
                                        }, 1500)


                                    }
                                }
                            })
                    } else {
                        internetState(false)
                    }
                }
            }

        })

    }

    @SuppressLint("ResourceType")
    private fun internetState(state:Boolean){

        if (state==true){
            mBinding.layout1.setBackgroundColor(resources.getColor(R.color.app_color))
            mBinding.tvLogin.setTextColor(resources.getColor(R.color.app_color))
            mBinding.enter6Digit.setTextColor(resources.getColor(R.color.text_color))
            mBinding.tvMobileNumber.setTextColor(resources.getColor(R.color.app_color))
            mBinding.etEdit.setTextColor(resources.getColor(R.color.app_color))
            mBinding.etOtp.setTextColor(resources.getColor(R.color.app_color))
            mBinding.loginEdittext.setHintTextColor(resources.getColorStateList(R.color.app_color))
            mBinding.resentOtp.setTextColor(resources.getColor(R.color.app_color))
            mBinding.loginEdittext.boxStrokeColor= resources.getColor(R.color.app_color)
            mBinding.resentOtp.isClickable= true
            dismissSnackBar()

        } else{
            mBinding.layout1.setBackgroundColor(resources.getColor(R.color.background_grey))
            mBinding.tvLogin.setTextColor(resources.getColor(R.color.text_fade_color))
            mBinding.enter6Digit.setTextColor(resources.getColor(R.color.text_fade_color))
            mBinding.tvMobileNumber.setTextColor(resources.getColor(R.color.text_fade_color))
            mBinding.etEdit.setTextColor(resources.getColor(R.color.text_fade_color))
            mBinding.etOtp.setTextColor(resources.getColor(R.color.text_fade_color))
            mBinding.loginEdittext.setHintTextColor(resources.getColorStateList(R.color.text_fade_color))
            mBinding.resentOtp.setTextColor(resources.getColor(R.color.text_fade_color))
            mBinding.resentOtp.isClickable= false
            mBinding.loginEdittext.boxStrokeColor= resources.getColor(R.color.text_fade_color)
            mBinding.timerTxt.text = ""
            showSnackBar(mBinding.root)
            hideSoftKeyboard()
        }
    }

    private fun resentOtp() {

            mBinding.resentOtp.setOnClickListener(View.OnClickListener {
                if (isNetworkAvailable()){
                    internetState(true)
                    hideSoftKeyboard()
                    val otpRequest = OtpRequest(mobileno, "+91", "IN")
                    authViewModel.getOtp(otpRequest).observe(viewLifecycleOwner, Observer {
                        when (it.status) {
                            Status.SUCCESS -> {
                                mBinding.loader.visibility = View.INVISIBLE
                                // Toast.makeText(requireContext(), "resend OTP successfully", Toast.LENGTH_LONG).show()
                            }
                            Status.ERROR -> {
                                mBinding.loader.visibility = View.INVISIBLE
                                it.data
                                (requireActivity() as AuthActivity).showErrorToast(
                                    it.message!!
                                )

                            }
                            Status.LOADING -> {
                                mBinding.loader.visibility = View.VISIBLE
                            }
                        }
                    })

                    otpTimerCount()
                    startSmsUserConsent()
                } else{
                    internetState(false)
                }

            })
    }

    private fun startSMSRetrieverClient() {
        val client = SmsRetriever.getClient(requireActivity())
        val task: Task<Void> = client.startSmsRetriever()
        task.addOnSuccessListener { aVoid -> }
        task.addOnFailureListener { e -> }
    }


    private fun edit_number() {
        mBinding.etEdit.setOnClickListener(View.OnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        })
    }

    private fun requestPermission() {
        isReadSMSGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_SMS
        ) == PackageManager.PERMISSION_GRANTED


        if (!isReadSMSGranted) {
            permissionRequest.add(Manifest.permission.READ_SMS)
        }
        if (permissionRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }
    }

    override fun onStart() {
        super.onStart()
        registerToSmsBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(smsBroadcastReceiver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQ_USER_CONSENT -> {
                if ((resultCode == Activity.RESULT_OK) && (data != null)) {
                    //That gives all message to us. We need to get the code from inside with regex
                    val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                    val code = message?.let { fetchVerificationCode(it) }
                    mBinding.etOtp.setText(code!!.toString())
                    //etVerificationCode.setText(code)
                }
            }
        }
    }

    private fun startSmsUserConsent() {
        SmsRetriever.getClient(requireActivity()).also {
            //We can add user phone number or leave it blank
            it.startSmsUserConsent(null)
                .addOnSuccessListener {
                    Log.d("", "LISTENING_SUCCESS")
                }
                .addOnFailureListener {
                    Log.d("", "LISTENING_FAILURE")
                }
        }
    }

    private fun registerToSmsBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver().also {
            it.smsBroadcastReceiverListener =
                object : SmsBroadcastReceiver.SmsBroadcastReceiverListener {
                    override fun onSuccess(intent: Intent?) {
                        intent?.let { context -> startActivityForResult(context, REQ_USER_CONSENT) }
                    }

                    override fun onFailure() {
                    }
                }
        }

        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        requireActivity().registerReceiver(smsBroadcastReceiver, intentFilter)
    }

    private fun fetchVerificationCode(message: String): String {
        return Regex("(\\d{6})").find(message)?.value ?: ""
    }

    private fun otpTimerCount() {
        mBinding.resentOtp.isVisible = true
        countDownTimer = object : CountDownTimer((counter).toLong(), 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                when (millisUntilFinished / 1000) {
                    0L -> {
                        mBinding.resentOtp.isVisible = true
                        mBinding.timerTxt.isVisible = false
                        mBinding.tryAgainTxt.isVisible = false
                    }
                    else -> {

                        mBinding.resentOtp.isVisible = false
                        mBinding.timerTxt.visibility = View.VISIBLE
                        if ((millisUntilFinished / 1000) % 60 < 10) {
                            mBinding.resentOtp.isVisible = false
                            mBinding.timerTxt.text =
                                    SpannableStringBuilder()

                                        .color(Color.GRAY){
                                            append("RESEND OTP in ")
                                        }
                                        .append("0${(millisUntilFinished / 1000) / 60}:0${(millisUntilFinished / 1000) % 60} sec")
                        } else {
                            mBinding.timerTxt.text =

                                SpannableStringBuilder()
                                    .color(Color.GRAY){
                                        append("RESEND OTP in  ")
                                    }
//
                                    .append("0${(millisUntilFinished / 1000) / 60}:${(millisUntilFinished / 1000) % 60} sec")
                        }
                    }
                }
            }

            override fun onFinish() {
                mBinding.resentOtp.isVisible = true
                mBinding.timerTxt.isVisible = false
                mBinding.tryAgainTxt.isVisible = true
            }

        }.start()
        counter += 15000
    }

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun block_for_one_hour(msg: String) {
        mBinding.resentOtp.isEnabled = false
        mBinding.resentOtp.isClickable = false

        mBinding.resentOtp.setTextAppearance(R.font.jost_regular)
        mBinding.resentOtp.setTextColor(resources.getColor(R.color.completed_investment_ash_text_color))
        mBinding.etOtp.isFocusable = false
        mBinding.etOtp.setTextColor(resources.getColor(R.color.completed_investment_ash_text_color))
        mBinding.etOtp.isEnabled = false
        Toast.makeText(requireContext(), "Invalid otp", Toast.LENGTH_LONG).show()
        mBinding.timerTxt.isVisible = false
        mBinding.tryAgainTxt.isVisible = true
        mBinding.tryAgainTxt.text = msg
        countDownTimer.cancel()

    }
}