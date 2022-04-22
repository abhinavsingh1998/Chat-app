package com.emproto.hoabl.feature.portfolio.views

import android.app.Activity.RESULT_OK
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
<<<<<<< HEAD
=======
import android.hardware.biometrics.BiometricPrompt
>>>>>>> 18f31f70846a8f1a1f13937359f0310e561d1e04
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
<<<<<<< HEAD
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.emproto.core.BaseFragment
import com.emproto.hoabl.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentPortfolioBinding
=======
import androidx.core.content.ContextCompat
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentPortfolioBinding
import com.emproto.hoabl.feature.home.views.HomeActivity
>>>>>>> 18f31f70846a8f1a1f13937359f0310e561d1e04
import java.util.concurrent.Executor


class PortfolioFragment : BaseFragment(),View.OnClickListener {

    companion object{
        const val mRequestCode = 300
    }

    lateinit var binding: FragmentPortfolioBinding
    lateinit var keyguardManager: KeyguardManager
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
<<<<<<< HEAD
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
=======
//    private lateinit var promptInfo: BiometricPrompt.PromptInfo
>>>>>>> 18f31f70846a8f1a1f13937359f0310e561d1e04

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPortfolioBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpInitialUI()
        setUpClickListeners()
<<<<<<< HEAD
        setUpAuthentication()
=======
        //setUpAuthentication()
>>>>>>> 18f31f70846a8f1a1f13937359f0310e561d1e04

    }

    private fun setUpClickListeners() {
        binding.btnExploreNewInvestmentProject.setOnClickListener(this)
    }

    private fun setUpInitialUI() {
        (activity as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility = View.VISIBLE
        setUpUI(false)
    }

<<<<<<< HEAD
    private fun setUpAuthentication() {
        executor = ContextCompat.getMainExecutor(this.requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        keyguardManager =  (activity as HomeActivity).getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            keyguardManager.createConfirmDeviceCredentialIntent("Hi,User","Verify your security PIN/Pattern")
                        } else {
                            TODO("VERSION.SDK_INT < LOLLIPOP")
                        }
                        startActivityForResult(intent, mRequestCode)
                    } else {

                    }
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(context, "Authentication succeeded!", Toast.LENGTH_SHORT).show()
                    setUpUI(true)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
                    setUpUI(false)
                }
            })

        //Biometric dialog
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use Pattern")
            .build()
        biometricPrompt.authenticate(promptInfo)

    }
=======
//    private fun setUpAuthentication() {
//        executor = ContextCompat.getMainExecutor(this.requireContext())
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            biometricPrompt = BiometricPrompt(this, executor,
//                object : BiometricPrompt.AuthenticationCallback() {
//                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
//                        super.onAuthenticationError(errorCode, errString)
//                        if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
//                            keyguardManager =
//                                (activity as HomeActivity).getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
//                            val intent =
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                    keyguardManager.createConfirmDeviceCredentialIntent(
//                                        "Hi,User",
//                                        "Verify your security PIN/Pattern"
//                                    )
//                                } else {
//                                    TODO("VERSION.SDK_INT < LOLLIPOP")
//                                }
//                            startActivityForResult(intent, mRequestCode)
//                        } else {
//
//                        }
//                    }
//
//                    override fun onAuthenticationSucceeded(
//                        result: BiometricPrompt.AuthenticationResult
//                    ) {
//                        super.onAuthenticationSucceeded(result)
//                        Toast.makeText(context, "Authentication succeeded!", Toast.LENGTH_SHORT)
//                            .show()
//                        setUpUI(true)
//                    }
//
//                    override fun onAuthenticationFailed() {
//                        super.onAuthenticationFailed()
//                        Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
//                        setUpUI(false)
//                    }
//                })
//        }
//
//        //Biometric dialog
//        promptInfo = BiometricPrompt.PromptInfo.Builder()
//            .setTitle("Biometric login for my app")
//            .setSubtitle("Log in using your biometric credential")
//            .setNegativeButtonText("Use Pattern")
//            .build()
//        biometricPrompt.authenticate(promptInfo)
//
//    }
>>>>>>> 18f31f70846a8f1a1f13937359f0310e561d1e04

    private fun setUpUI(authenticated:Boolean = false){
        val conditionalView = when(authenticated){
            true -> View.VISIBLE
            else -> View.GONE
        }
        binding.portfolioTopImg.visibility = conditionalView
        binding.addYouProject.visibility = conditionalView
        binding.instriction.visibility = conditionalView
        binding.btnExploreNewInvestmentProject.visibility = conditionalView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            mRequestCode -> {
                when(resultCode){
                    RESULT_OK -> setUpUI(true)
                    else -> setUpUI(false)
                }
            }
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_explore_new_investment_project -> {
                val financialSummaryFragment = PortfolioExistingUsersFragment()
                (requireActivity() as HomeActivity).replaceFragment(financialSummaryFragment.javaClass, "", true, null, null, 0, false)
            }
        }
    }

}


