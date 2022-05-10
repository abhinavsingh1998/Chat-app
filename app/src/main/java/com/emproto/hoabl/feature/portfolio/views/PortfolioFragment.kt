package com.emproto.hoabl.feature.portfolio.views

import android.app.Activity.RESULT_OK
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentPortfolioBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.viewmodels.PortfolioViewModel
import com.emproto.hoabl.viewmodels.factory.PortfolioFactory
import com.emproto.networklayer.response.enums.Status
import java.util.concurrent.Executor
import javax.inject.Inject


class PortfolioFragment : BaseFragment(), View.OnClickListener {

    companion object {
        const val mRequestCode = 300
    }

    lateinit var binding: FragmentPortfolioBinding
    lateinit var keyguardManager: KeyguardManager
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    @Inject
    lateinit var portfolioFactory: PortfolioFactory
    lateinit var portfolioviewmodel: PortfolioViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        binding = FragmentPortfolioBinding.inflate(layoutInflater)
        portfolioviewmodel = ViewModelProvider(
            requireActivity(),
            portfolioFactory
        )[PortfolioViewModel::class.java]
        initObserver()
        return binding.root
    }

    private fun initObserver() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.isVisible =
            true
        setUpInitialUI()
        setUpClickListeners()
        setUpAuthentication()
    }

    private fun setUpClickListeners() {
        binding.btnExploreNewInvestmentProject.setOnClickListener(this)
    }

    private fun setUpInitialUI() {
        (activity as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility =
            View.VISIBLE
        setUpUI(false)
    }

    private fun setUpAuthentication() {
        executor = ContextCompat.getMainExecutor(this.requireContext())
        //Biometric dialog
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use Pattern")
            .build()

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        setUpKeyGuardManager()
                    } else if (errorCode == BiometricPrompt.ERROR_NO_BIOMETRICS) {
                        setUpUI(true)
                    } else {
                        setUpUI(true)

                    }
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
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

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            setUpKeyGuardManager()
        } else {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    fun setUpKeyGuardManager() {
        keyguardManager =
            (activity as HomeActivity).getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val intent = keyguardManager.createConfirmDeviceCredentialIntent(
                "Hi,User",
                "Verify your security PIN/Pattern"
            )
            startActivityForResult(intent, mRequestCode)
        } else {

        }
    }

    private fun setUpUI(authenticated: Boolean = false) {

        portfolioviewmodel.getUserProfile().observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBaar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBaar.hide()
                    //prelead
                    it.data?.let {
                        if (it.data.contactType != "prelead") {
                            binding.noUserView.show()
                            binding.portfolioTopImg.visibility = View.VISIBLE
                            binding.addYouProject.visibility = View.VISIBLE
                            binding.instriction.visibility = View.VISIBLE
                            binding.btnExploreNewInvestmentProject.visibility = View.VISIBLE
                        } else {
                            val financialSummaryFragment = PortfolioExistingUsersFragment()
                            (requireActivity() as HomeActivity).addFragment(
                                financialSummaryFragment,
                                false
                            )
                        }
                    }

                }
                Status.ERROR -> {

                }
            }
        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            mRequestCode -> {
                when (resultCode) {
                    RESULT_OK -> setUpUI(true)
                    else -> setUpUI(false)
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_explore_new_investment_project -> {
                (requireActivity() as HomeActivity).navigate(R.id.navigation_investment)
            }
        }
    }

}