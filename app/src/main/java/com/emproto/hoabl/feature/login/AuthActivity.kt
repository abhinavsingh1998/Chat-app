package com.emproto.hoabl.feature.login

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ActivityAuthBinding
import com.emproto.hoabl.databinding.FragmentSigninIssueBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.viewmodels.AuthViewmodel
import com.emproto.hoabl.viewmodels.factory.AuthFactory
import com.emproto.networklayer.request.login.TroubleSigningRequest
import com.emproto.networklayer.response.enums.Status
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject

class AuthActivity : BaseActivity() {

    @Inject
    lateinit var authFactory: AuthFactory
    lateinit var authViewModel: AuthViewmodel

    lateinit var activityAuthBinding: ActivityAuthBinding
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var signingInIssueBiding: FragmentSigninIssueBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as HomeComponentProvider).homeComponent().inject(this)
        authViewModel = ViewModelProvider(this, authFactory)[AuthViewmodel::class.java]
        activityAuthBinding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(activityAuthBinding.root)

        initView()
        close_sheet()
        initClickListener()
    }

    private fun initClickListener() {

        activityAuthBinding.textTrouble.setOnClickListener(View.OnClickListener {
            launch_bottom_sheet()
        })
    }

    private fun initView() {
        replaceFragment(LoginFragment(), true)

        //for signinissue
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        signingInIssueBiding = FragmentSigninIssueBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(signingInIssueBiding.root)
    }


    fun addFragment(fragment: Fragment, showAnimation: Boolean) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (!showAnimation) {
            fragmentTransaction.add(R.id.fragmentContainerAuth, fragment, fragment.javaClass.name)
                .addToBackStack(fragment.javaClass.name).commit()
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit)
                .add(R.id.fragmentContainerAuth, fragment, fragment.javaClass.name)
                .addToBackStack(fragment.javaClass.name).commit()
        }
    }

    fun replaceFragment(fragment: Fragment, showAnimation: Boolean) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (!showAnimation) {
            fragmentTransaction.replace(
                R.id.fragmentContainerAuth,
                fragment,
                fragment.javaClass.name
            ).addToBackStack(fragment.javaClass.name).commit()
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit)
                .replace(R.id.fragmentContainerAuth, fragment, fragment.javaClass.name)
                .addToBackStack(fragment.javaClass.name).commit()
        }
    }

    private fun launch_bottom_sheet() {

        bottomSheetDialog.show()
        initClickListner()

    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    fun showErrorToast(message: String) {
        showErrorView(activityAuthBinding.root, message)
    }

    @SuppressLint("ResourceAsColor")
    private fun initClickListner() {
        signingInIssueBiding.mobileNumInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                signingInIssueBiding.numLayout.error = ""

                if (p0.toString().isNullOrEmpty()) {
                    signingInIssueBiding.submitBtn.isEnabled = false
                    signingInIssueBiding.submitBtn.isClickable = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        signingInIssueBiding.submitBtn.background =
                            AppCompatResources.getDrawable(
                                this@AuthActivity,
                                R.drawable.unselect_button_bg
                            )
                    }
                } else {
                    signingInIssueBiding.submitBtn.isEnabled = true
                    signingInIssueBiding.submitBtn.isClickable = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        signingInIssueBiding.submitBtn.background =
                            AppCompatResources.getDrawable(this@AuthActivity, R.drawable.button_bg)
                    }
                }
            }
        })

        signingInIssueBiding.emailInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                signingInIssueBiding.emailLayout.error = ""

                if (p0.toString().isNullOrEmpty()) {
                    signingInIssueBiding.submitBtn.isEnabled = false
                    signingInIssueBiding.submitBtn.isClickable = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        signingInIssueBiding.submitBtn.background =
                            AppCompatResources.getDrawable(
                                this@AuthActivity,
                                R.drawable.unselect_button_bg
                            )
                    }
                } else {
                    signingInIssueBiding.submitBtn.isEnabled = true
                    signingInIssueBiding.submitBtn.isClickable = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        signingInIssueBiding.submitBtn.background =
                            AppCompatResources.getDrawable(this@AuthActivity, R.drawable.button_bg)


                    }
                }
            }

        })

        signingInIssueBiding.submitBtn.setOnClickListener(View.OnClickListener {

            if (signingInIssueBiding.mobileNumInput.text.length != 10) {
                signingInIssueBiding.numLayout.error = "Please Enter Valid Phone No."
                return@OnClickListener
            }
            if (!signingInIssueBiding.emailInput.text.isValidEmail()) {
                signingInIssueBiding.emailLayout.error = "Please Enter Valid Email"
                return@OnClickListener
            }

            val troubleSigningRequest = TroubleSigningRequest(
                "1001",
                "91",
                signingInIssueBiding.editIssues.text.toString(),
                signingInIssueBiding.emailInput.text.toString(),
                issueChecked(),
                signingInIssueBiding.mobileNumInput.text.toString()
            )

            authViewModel.submitTroubleCase(troubleSigningRequest).observe(this,
                Observer {
                    when (it.status) {
                        Status.SUCCESS -> {

                            signingInIssueBiding.progressBar.visibility = View.GONE
                            signingInIssueBiding.submitBtn.visibility = View.VISIBLE
                            bottomSheetDialog.dismiss()
                            val dialog = IssueSubmittedConfirmationFragment()
                            dialog.isCancelable = true
                            dialog.show(supportFragmentManager, "Submit Card")
                        }
                        Status.ERROR -> {
                            bottomSheetDialog.dismiss()
                            signingInIssueBiding.submitBtn.visibility = View.VISIBLE
                            signingInIssueBiding.progressBar.visibility = View.INVISIBLE
                            showErrorToast(
                                it.message!!
                            )
                        }
                        Status.LOADING -> {
                            signingInIssueBiding.submitBtn.visibility = View.INVISIBLE
                            signingInIssueBiding.progressBar.visibility = View.VISIBLE
                        }
                    }

                })

        })

    }

    fun CharSequence?.isValidEmail() =
        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    private fun issueChecked(): String {

        return when {
            signingInIssueBiding.issueOne.isChecked -> {
                "2001"
            }
            signingInIssueBiding.issueTwo.isChecked -> {
                "2002"
            }
            signingInIssueBiding.issueThree.isChecked -> {
                "2003"
            }

            signingInIssueBiding.issueFour.isChecked -> {
                "2004"
            }

            signingInIssueBiding.issueFive.isChecked -> {
                "2005"
            }
            signingInIssueBiding.issueSix.isChecked -> {
                "2006"
            }
            signingInIssueBiding.issueSeven.isChecked -> {
                "2007"
            }

            else -> {
                " "
            }
        }
    }

    private fun close_sheet(){
        signingInIssueBiding.sheetCloseBtn.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })
    }
}