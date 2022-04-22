package com.emproto.hoabl.feature.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.databinding.FragmentVerifyOtpBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.viewmodels.AuthViewmodel
import com.emproto.hoabl.viewmodels.factory.AuthFactory
import com.emproto.networklayer.request.login.OtpVerifyRequest
import com.emproto.networklayer.response.enums.Status
import javax.inject.Inject


class OTPVerificationFragment : BaseFragment() {

    private lateinit var mBinding: FragmentVerifyOtpBinding
    var countOtp: Int = 3

    /// lateinit var dialog: Dialog
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var isReadSMSGranted = false
    val permissionRequest: MutableList<String> = ArrayList()

    @Inject
    lateinit var authFactory: AuthFactory
    lateinit var authViewModel: AuthViewmodel

    @Inject
    lateinit var appPreference: AppPreference


    companion object {
        var mobileno: String = ""

        fun newInstance(mobileNumber: String): OTPVerificationFragment {
            val fragment = OTPVerificationFragment()
            val bundle = Bundle()
            mobileno = bundle.getString("mobilenumber", mobileNumber)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        authViewModel = ViewModelProvider(requireActivity(), authFactory)[AuthViewmodel::class.java]
        mBinding = FragmentVerifyOtpBinding.inflate(layoutInflater)
        initView()
        initClickListener()
        return mBinding.root
    }

    private fun initView() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                isReadSMSGranted = permissions[Manifest.permission.READ_SMS] ?: isReadSMSGranted
            }
        requestPermission()
        mBinding.tvMobileNumber.text = mobileno
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
                    showSnackMessage("Please Enter Valid Otp", mBinding.root)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 6) {
                    if (isNetworkAvailable(mBinding.root)) {
                        val otpVerifyRequest = OtpVerifyRequest(s.toString(), mobileno, false)

                        authViewModel.verifyOtp(otpVerifyRequest).observe(viewLifecycleOwner,
                            Observer {
                                when (it.status) {
                                    Status.LOADING -> {
                                        mBinding.loader.visibility = View.VISIBLE
                                    }
                                    Status.ERROR -> {
                                        mBinding.loader.visibility = View.INVISIBLE
                                    }
                                    Status.SUCCESS -> {
                                        //save token to preference
                                        mBinding.loader.visibility = View.INVISIBLE
                                        it.data?.let {
                                            appPreference.setToken(it.token)
                                            if (it.user.contactType == "prelead") {
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

                                    }
                                }
                            })
                    } else {
                        mBinding.layout1.setBackgroundColor(resources.getColor(R.color.background_grey))
                        showSnackBar(mBinding.root)
                    }
                }
            }

        })

    }

/*    private fun getTimerCount() {
        activityOtpVerifyBinding.timerLayout.isVisible = true
        activityOtpVerifyBinding.resendLayout.isVisible = false
        activityOtpVerifyBinding.otpLeft.isVisible = false
        object : CountDownTimer(30000,1000){
            override fun onTick(millisUntilFinished: Long) {
                val time:String= (millisUntilFinished/1000).toString()
                activityOtpVerifyBinding.timerText.text=time+" sec"
            }

            override fun onFinish() {
                activityOtpVerifyBinding.timerLayout.isVisible=false
                activityOtpVerifyBinding.resendLayout.isVisible=true
                activityOtpVerifyBinding.otpLeft.isVisible = true
                activityOtpVerifyBinding.otpLeft.text = "[" + count_otp.toString() + " more attempts left]"
            }

        }.start()
    }*/

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


}