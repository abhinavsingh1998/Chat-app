package com.emproto.hoabl.feature.login

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentVerifyOtpBinding


class OTPVerificationFragment : BaseFragment() {

    private lateinit var mBinding: FragmentVerifyOtpBinding
    var countOtp: Int = 3

    /// lateinit var dialog: Dialog
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var isReadSMSGranted = false
    val permissionRequest: MutableList<String> = ArrayList()

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
                    showSnackMessage("please enter valid OTP", mBinding.root)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 6) {
                    if (isNetworkAvailable(mBinding.root)) {

                        (requireActivity() as AuthActivity).replaceFragment(
                            NameInputFragment(), true
                        )
                        // startActivity(Intent(requireContext(), HomeActivity::class.java))
                        // dialog.dismiss()

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

//    private fun launchPermissionDialog() {
//        dialog = Dialog(requireContext(), android.R.style.Theme_Dialog)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setContentView(R.layout.dailog_permissions)
//        dialog.setCanceledOnTouchOutside(true)
//        val allow: TextView = dialog.findViewById(R.id.allow) as TextView
//        val dontAllow: TextView = dialog.findViewById(R.id.dont_allow) as TextView
//        allow.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(p0: View?) {
//                permissionRequest.add(Manifest.permission.READ_SMS)
//                dialog.dismiss()
//            }
//        })
//
//        dontAllow.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(p0: View?) {
//                dialog.dismiss()
//            }
//
//        })
//        dialog.setCanceledOnTouchOutside(false)
//        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
//        dialog.window?.setFlags(
//            WindowManager.LayoutParams.FLAG_DIM_BEHIND,
//            WindowManager.LayoutParams.FLAG_BLUR_BEHIND
//        )
//        dialog.getWindow()?.setLayout(
//            ConstraintLayout.LayoutParams.MATCH_PARENT,
//            ConstraintLayout.LayoutParams.WRAP_CONTENT
//        )
//        dialog.show()
//    }


}