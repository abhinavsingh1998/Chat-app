package com.emproto.hoabl.feature.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseActivity
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
    var email= ""
    val phonepatterns  = Pattern.compile("[1-9][0-9]{9}")

//    val emailPattern= Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as HomeComponentProvider).homeComponent().inject(this)
        authViewModel = ViewModelProvider(this, authFactory)[AuthViewmodel::class.java]
        activityAuthBinding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(activityAuthBinding.root)

//        otherIssueCheck()
        initView()
        close_sheet()
        initClickListener()
        editIssuechecked()
/*        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            var window:Window = this.getWindow()
            window.setStatusBarColor(this.resources.getColor(R.color.black))
        }*/
    }

    private fun initClickListener() {

        activityAuthBinding.textTrouble.setOnClickListener({
            launch_bottom_sheet()
        })
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
        hMobileNo= appPreference.getMobilenum()
        signingInIssueBiding.emailInput.setText("")
        issuetype= issueChecked()
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

    fun launch_bottom_sheet() {
        signingInIssueBiding.inputMobile.setValue(appPreference.getMobilenum())
        if(!appPreference.getMobilenum().isNullOrEmpty()){
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
        issuetype= issueChecked()
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

    fun showSuccessToast(message: String){
        showSuccessView(activityAuthBinding.root, message)
    }


    @SuppressLint("ResourceAsColor")
    private fun initClickListner() {
        hMobileNo= appPreference.getMobilenum()
        issuetype= issueChecked()
        signingInIssueBiding.inputMobile.onValueChangeListner(object : OnValueChangedListener {
            override fun onValueChanged(value: String?, countryCode: String) {
                signingInIssueBiding.emailLayout.isErrorEnabled=false
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

        signingInIssueBiding.editIssues.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                issueDetail=p0.toString()

            }

            @SuppressLint("UseCompatLoadingForColorStateLists")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                issueDetail=p0.toString()
                if( p0.toString().length==250){
                signingInIssueBiding.editIssuesLayout.setBoxStrokeColor(resources.getColor(R.color.text_red_color))
                signingInIssueBiding.txtcount.isVisible= true
                signingInIssueBiding.editIssues.setTextColor(resources.getColorStateList(R.color.text_red_color))

                }
                else{
                    signingInIssueBiding.editIssuesLayout.setBoxStrokeColor(resources.getColor(R.color.app_color))
                    signingInIssueBiding.txtcount.isVisible= false
                    signingInIssueBiding.editIssues.setTextColor(resources.getColorStateList(R.color.text_color))
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNullOrEmpty()) {
                    issueDetail=p0.toString()

                }

            }

        })

/*
        signingInIssueBiding.emailInput.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                signingInIssueBiding.emailLayout.isErrorEnabled= false
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                email= p0.toString()
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
                email= p0.toString()
            }

        })
*/

        signingInIssueBiding.submitBtn.setOnClickListener(View.OnClickListener {

            if (hMobileNo.isEmpty() || hMobileNo.length != 10 || !hMobileNo.ValidNO()) {
                signingInIssueBiding.textError.visibility = View.VISIBLE
                signingInIssueBiding.inputMobile.showError()
                return@OnClickListener
            }


            if (signingInIssueBiding.issueSeven.isChecked){
                if (issueDetail.isEmpty()){
                    signingInIssueBiding.editIssues.error = "Please Describe The Issue"
                    Toast.makeText(this, "Please Describe The Issue", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }
            }
            if (signingInIssueBiding.issueSeven.isChecked){
/*                if (issueDetail.length< 30){
                    signingInIssueBiding.editIssues.error = "Please Describe in more words"
                    Toast.makeText(this, "Please Describe in more words", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }*/
            }

/*            if(!email.isValidEmail()){
                signingInIssueBiding.emailLayout.error = "Please enter valid email"
                return@OnClickListener
            }*/


            if (issueChecked().isNullOrEmpty()){
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

            authViewModel.submitTroubleCase(troubleSigningRequest).observe(this
            ) {
                when (it.status) {
                    Status.SUCCESS -> {

                        signingInIssueBiding.progressBar.visibility = View.GONE
                        signingInIssueBiding.submitBtn.visibility = View.VISIBLE
                        bottomSheetDialog.dismiss()
                        val dialog = IssueSubmittedConfirmationFragment()
                        dialog.isCancelable = true
                        dialog.show(supportFragmentManager, "Submit Card")
                        signingInIssueBiding.editIssues.text=null

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

/*    fun CharSequence?.isValidEmail() =
        emailPattern.matcher(this).matches()

        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()*/


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
                ""
            }
        }
    }

    private fun editIssuechecked(){
        if (signingInIssueBiding.issueSeven.isChecked){
            if (issueDetail.isNullOrEmpty()){
                signingInIssueBiding.submitBtn.isEnabled = false
                signingInIssueBiding.submitBtn.isClickable = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    signingInIssueBiding.submitBtn.background =
                        AppCompatResources.getDrawable(
                            this@AuthActivity,
                            R.drawable.unselect_button_bg)
                }
            }}

    }

    private fun close_sheet() {
        appPreference.setMobilenum("")
        signingInIssueBiding.sheetCloseBtn.setOnClickListener {

            bottomSheetDialog.dismiss()

        }
    }

}
