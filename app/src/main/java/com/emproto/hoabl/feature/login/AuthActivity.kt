package com.emproto.hoabl.feature.login

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseActivity
import com.emproto.core.Constants
import com.emproto.core.customedittext.OnValueChangedListener
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ActivityAuthBinding
import com.emproto.hoabl.databinding.FragmentSigninIssueBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.viewmodels.AuthViewmodel
import com.emproto.hoabl.viewmodels.factory.AuthFactory
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.request.login.TroubleSigningRequest
import com.emproto.networklayer.response.enums.Status
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.regex.Pattern
import javax.inject.Inject

class AuthActivity : BaseActivity() {

    @Inject
    lateinit var authFactory: AuthFactory
    lateinit var authViewModel: AuthViewmodel

    @Inject
    lateinit var appPreference: AppPreference
    lateinit var activityAuthBinding: ActivityAuthBinding
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var signingInIssueBiding: FragmentSigninIssueBinding
    var issueDetail = ""
    var hMobileNo = ""
    var issuetype = ""
    var email = ""
    val phonepatterns = Pattern.compile("[1-9][0-9]{9}")
    val emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    var isHandlerStarted = false


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

        activityAuthBinding.textTrouble.setOnClickListener {
            launchBottomSheet()
        }
    }

    private fun initView() {
        replaceFragment(LoginFragment(), true)

        //for signinissue
        bottomSheetDialog = BottomSheetDialog(this)
        signingInIssueBiding = FragmentSigninIssueBinding.inflate(layoutInflater)
        signingInIssueBiding.rootView.setOnClickListener {
            hideSoftKeyboard()
        }
        bottomSheetDialog.setContentView(signingInIssueBiding.root)
        signingInIssueBiding.inputMobile.setValue(appPreference.getMobilenum())
        hMobileNo = appPreference.getMobilenum()
        signingInIssueBiding.emailInput.setText("")
        issuetype = issueChecked()
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

    private fun launchBottomSheet() {
        val list = ArrayList<String>()
        list.add("+91 | IND")
        signingInIssueBiding.inputMobile.addDropDownValues(list)
        signingInIssueBiding.inputMobile.setValue(appPreference.getMobilenum())
        if (!appPreference.getMobilenum().isNullOrEmpty()) {
            signingInIssueBiding.submitBtn.isEnabled = true
            signingInIssueBiding.submitBtn.isClickable = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                signingInIssueBiding.submitBtn.background =
                    resources.getDrawable(R.drawable.button_bg)
            }
        } else {
            signingInIssueBiding.submitBtn.isEnabled = false
            signingInIssueBiding.submitBtn.isClickable = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                signingInIssueBiding.submitBtn.background =
                    resources.getDrawable(R.drawable.unselect_button_bg)
            }
        }
        bottomSheetDialog.show()
        issuetype = issueChecked()
        initClickListner()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            if (!isHandlerStarted) {
                super.onBackPressed()
            } else {
                //Doing nothing
            }
        }
    }

    fun showErrorToast(message: String) {
        showErrorView(activityAuthBinding.root, message)
    }

    fun showSuccessToast(message: String) {
        showSuccessView(activityAuthBinding.root, message)
    }


    @SuppressLint("ResourceAsColor", "WrongConstant")
    private fun initClickListner() {
        hMobileNo = appPreference.getMobilenum()
        issuetype = issueChecked()
        signingInIssueBiding.inputMobile.onValueChangeListner(object : OnValueChangedListener {
            override fun onValueChanged(value: String?, countryCode: String) {
                signingInIssueBiding.emailLayout.isErrorEnabled = false
                signingInIssueBiding.textError.visibility = View.GONE
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun afterValueChanges(value1: String?) {
                hMobileNo = value1!!
                if (value1.isNullOrEmpty()) {
                    signingInIssueBiding.submitBtn.isEnabled = false
                    signingInIssueBiding.submitBtn.isClickable = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        signingInIssueBiding.submitBtn.background =
                            resources.getDrawable(R.drawable.unselect_button_bg)
                    }
                } else {
                    signingInIssueBiding.submitBtn.isEnabled = true
                    signingInIssueBiding.submitBtn.isClickable = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        signingInIssueBiding.submitBtn.background =
                            resources.getDrawable(R.drawable.button_bg)
                    }
                }
            }

        })

        signingInIssueBiding.editIssues.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                issueDetail = p0.toString().trim()

            }

            @SuppressLint("UseCompatLoadingForColorStateLists")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                issueDetail = p0.toString().trim()

                if (issueDetail.length == 250) {
                    signingInIssueBiding.editIssuesLayout.boxStrokeColor =
                        resources.getColor(R.color.text_red_color)
                    signingInIssueBiding.txtcount.isVisible = true
                    signingInIssueBiding.txtcount.text= "You have reached maximum limit"
                    signingInIssueBiding.maxDesc.isVisible = false
                    signingInIssueBiding.editIssues.setTextColor(resources.getColorStateList(R.color.text_red_color))
                } else {
                    signingInIssueBiding.editIssuesLayout.boxStrokeColor =
                        resources.getColor(R.color.app_color)
                    signingInIssueBiding.editIssuesLayout.isErrorEnabled = false
                    signingInIssueBiding.txtcount.isVisible = false
                    signingInIssueBiding.maxDesc.isVisible = true
                    signingInIssueBiding.maxDesc.text = "${issueDetail.length}/250" + " Characters"
                    signingInIssueBiding.editIssues.setTextColor(resources.getColorStateList(R.color.text_color))
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNullOrEmpty()) {
                    issueDetail = p0.toString().trim()
                }

            }

        })

        signingInIssueBiding.emailInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                signingInIssueBiding.emailLayout.isErrorEnabled = false
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                email = p0.toString()
                if (p0.isNullOrEmpty() || p0.isValidEmail()) {
                    signingInIssueBiding.emailLayout.isErrorEnabled = false
                    signingInIssueBiding.submitBtn.isEnabled = true
                    signingInIssueBiding.submitBtn.isClickable = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        signingInIssueBiding.submitBtn.background =
                            resources.getDrawable(R.drawable.button_bg)
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                email = p0.toString()
            }

        })
        signingInIssueBiding.editIssues.error = null
        signingInIssueBiding.submitBtn.setOnClickListener(View.OnClickListener {
            if (hMobileNo.isEmpty() || hMobileNo.length != 10 || !hMobileNo.ValidNO()) {
                signingInIssueBiding.textError.visibility = View.VISIBLE
                signingInIssueBiding.inputMobile.showError()
                return@OnClickListener
            }


            if (signingInIssueBiding.issueSeven.isChecked) {
                if (issueDetail.isNullOrEmpty() || issueDetail.trim().isEmpty()) {
                    signingInIssueBiding.editIssuesLayout.error = "Please Describe The Issue"
                    signingInIssueBiding.txtcount.text="Please Describe The Issue"
                    signingInIssueBiding.txtcount.isVisible=true
                    signingInIssueBiding.maxDesc.isVisible=false
                    Toast.makeText(this, "Please Describe The Issue", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }
            }


            if (!email.isEmpty()) {
                if (!email.isValidEmail()) {
                    signingInIssueBiding.emailLayout.error = Constants.PLEASE_ENTER_VALID_EMAIL
                    return@OnClickListener
                }
            }

            if (issueChecked().isNullOrEmpty()) {
                Toast.makeText(this, "Please select an issue", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            val troubleSigningRequest = TroubleSigningRequest(
                "1001",
                "91",
                issueDetail,
                email,
                issueChecked(),
                hMobileNo
            )

            authViewModel.submitTroubleCase(troubleSigningRequest).observe(
                this
            ) {
                when (it.status) {
                    Status.SUCCESS -> {

                        signingInIssueBiding.progressBar.visibility = View.GONE
                        signingInIssueBiding.submitBtn.visibility = View.VISIBLE
                        bottomSheetDialog.dismiss()
                        val dialog = IssueSubmittedConfirmationFragment()
                        dialog.isCancelable = true
                        dialog.show(supportFragmentManager, "Submit Card")
                        signingInIssueBiding.editIssues.text = null
                        signingInIssueBiding.issueList.clearCheck()
                        signingInIssueBiding.emailInput.text = null

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

            }

        })

    }

    fun CharSequence?.ValidNO() =
        phonepatterns.matcher(this).matches()

    fun CharSequence?.isValidEmail() =
        emailPattern.matcher(this).matches()

//        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()


    private fun issueChecked(): String {

        return when {
            signingInIssueBiding.issueOne.isChecked -> {
                "Unable to enter phhone number"
            }
            signingInIssueBiding.issueTwo.isChecked -> {
                "Unable to change country code"
            }
            signingInIssueBiding.issueThree.isChecked -> {
                "Did not get OTP"
            }

            signingInIssueBiding.issueFour.isChecked -> {
                "OTP Expired"
            }

            signingInIssueBiding.issueFive.isChecked -> {
                "Number Blocked"

            }
            signingInIssueBiding.issueSix.isChecked -> {
                "My issue is not listed"
            }
            signingInIssueBiding.issueSeven.isChecked -> {
                "Others"
            }

            else -> {
                ""
            }
        }
    }

    private fun editIssuechecked() {
        if (signingInIssueBiding.issueSeven.isChecked) {
            if (issueDetail.isNullOrEmpty() || issueDetail.trim().isEmpty()) {
                signingInIssueBiding.submitBtn.isEnabled = false
                signingInIssueBiding.submitBtn.isClickable = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    signingInIssueBiding.submitBtn.background =
                        AppCompatResources.getDrawable(
                            this@AuthActivity,
                            R.drawable.unselect_button_bg
                        )
                }
            }
        }

    }

    private fun close_sheet() {
        appPreference.setMobilenum("")
        signingInIssueBiding.sheetCloseBtn.setOnClickListener {
            bottomSheetDialog.dismiss()

        }
    }

}
