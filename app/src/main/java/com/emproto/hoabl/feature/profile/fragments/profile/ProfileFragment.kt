package com.emproto.hoabl.feature.profile.fragments.profile

import android.app.Activity
import android.app.Dialog
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.emproto.core.BaseFragment
import com.emproto.core.Constants
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FacilitymanagerBinding
import com.emproto.hoabl.databinding.FragmentProfileMainBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.login.AuthActivity
import com.emproto.hoabl.feature.portfolio.views.CustomDialog
import com.emproto.hoabl.feature.portfolio.views.PortfolioFragment

import com.emproto.hoabl.feature.profile.fragments.edit_profile.EditProfileFragment
import com.emproto.hoabl.feature.profile.fragments.help_center.HelpCenterFragment
import com.emproto.hoabl.feature.profile.fragments.securtiyandsettings.SecurityFragment
import com.emproto.hoabl.feature.profile.adapter.ProfileOptionsAdapter
import com.emproto.hoabl.feature.profile.data.ProfileModel
import com.emproto.hoabl.feature.profile.data.ProfileOptionsData
import com.emproto.hoabl.feature.profile.fragments.accounts.AccountDetailsFragment
import com.emproto.hoabl.feature.profile.fragments.edit_profile.CircleTransform
import com.emproto.hoabl.viewmodels.ProfileViewModel
import com.emproto.hoabl.viewmodels.factory.ProfileFactory
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.portfolio.fm.FMResponse
import com.emproto.networklayer.response.profile.Data
import com.example.portfolioui.databinding.DailogSecurePinConfirmationBinding
import com.example.portfolioui.databinding.DialogSecurePinBinding

import com.example.portfolioui.databinding.LogoutConfirmationBinding
import java.util.concurrent.Executor
import javax.inject.Inject

class ProfileFragment : BaseFragment() {

    lateinit var binding: FragmentProfileMainBinding
    lateinit var keyguardManager: KeyguardManager
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var logoutDialog: Dialog
    lateinit var facilityManagerDialog: FacilitymanagerBinding
    lateinit var securePinDialog: CustomDialog
    lateinit var facilityDialog: CustomDialog


    private val mRequestCode = 300
    private val SETTING_REQUEST_CODE = 301
    lateinit var dialogSecurePinBinding: DialogSecurePinBinding
    lateinit var securePinConfirmationDialog: CustomDialog

    lateinit var dialogSecurePinConfirmationBinding: DailogSecurePinConfirmationBinding


    val bundle = Bundle()

    @Inject
    lateinit var profileFactory: ProfileFactory
    private lateinit var profileViewModel: ProfileViewModel
    lateinit var profileData: Data
    var fmData: FMResponse? = null
    var isTermsActive = false
    var isAboutUsActive = false
    var isSecurityTipsActive = false

    @Inject
    lateinit var appPreference: AppPreference

    private var isWhatsappConsent = false
    private var isPushNotificationSend = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        binding = FragmentProfileMainBinding.inflate(inflater, container, false)
        profileViewModel =
            ViewModelProvider(requireActivity(), profileFactory)[ProfileViewModel::class.java]
        initView()
        initObserver()
        initClickListener()
        return binding.root
    }

    private fun initObserver() {
        profileViewModel.getUserProfile().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBaar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBaar.hide()
                    if (it.data != null) {
                        it.data?.let {
                            profileData = it.data
                            isWhatsappConsent = it.data.whatsappConsent
                            isPushNotificationSend = it.data.showPushNotifications
                            isTermsActive = it.data.pageManagement.data.page.isTermsActive
                            isAboutUsActive = it.data.pageManagement.data.page.isAboutUsActive
                            isSecurityTipsActive =
                                it.data.pageManagement.data.page.isSecurityTipsActive
                        }
                        if (::profileData.isInitialized) {
                            setUiData(profileData)
                        }
                    }
                }
                Status.ERROR -> {
                    binding.progressBaar.hide()
                    it.data
                    (requireActivity() as HomeActivity).showErrorToast(
                        it.message!!
                    )
                }

            }
        })

        profileViewModel.getFacilityManagment()
            .observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data.let {
                            fmData = it!!
                        }
                    }
                }
            })

    }

    private fun setUiData(profileData: Data) {
        binding.tvName.text = profileData.firstName + " " + profileData.lastName

        /*for user pic not available show username as pic label*/
        if (profileData.profilePictureUrl != null && profileData.profilePictureUrl!!.isNotEmpty()) {
            binding.cvProfileImage.visibility = View.VISIBLE
            binding.profileUserLetters.visibility = View.GONE
            Glide.with(requireContext())
                .load(profileData.profilePictureUrl)
                .transform(CircleTransform(requireContext()))
                .into(binding.ivProfile)
        } else {
            binding.cvProfileImage.visibility = View.GONE
            binding.profileUserLetters.visibility = View.VISIBLE
            setUserNamePIC(profileData)
        }
    }

    private fun setUserNamePIC(profileData: Data) {
        val firstLetter: String = profileData.firstName.substring(0, 1)
        val lastLetter = when {
            profileData.lastName.isNotEmpty() -> {
                profileData.lastName.substring(0, 1)
            }
            else -> {
                ""
            }
        }
        binding.tvUserName.text = firstLetter + "" + lastLetter

        if (profileData.lastName.isNullOrEmpty()) {
            val firstLetter: String = profileData.firstName.substring(0, 2)

            binding.tvUserName.text = firstLetter
        } else {
            val firstLetter: String = profileData.firstName.substring(0, 1)

            val lastLetter: String = profileData.lastName.substring(0, 1)
            binding.tvUserName.text = firstLetter + "" + lastLetter

        }
    }


    private fun initView() {
        (requireActivity() as HomeActivity).showBottomNavigation()
        (requireActivity() as HomeActivity).hideHeader()
        facilityManagerDialog = FacilitymanagerBinding.inflate(layoutInflater)
        facilityDialog = CustomDialog(requireContext())

        facilityDialog.setContentView(facilityManagerDialog.root)
        facilityDialog.setCancelable(false)

        dialogSecurePinBinding = DialogSecurePinBinding.inflate(layoutInflater)
        dialogSecurePinBinding.tvTitle.text="Secure Your Account"
        dialogSecurePinConfirmationBinding =
            DailogSecurePinConfirmationBinding.inflate(layoutInflater)
        dialogSecurePinConfirmationBinding.tvTitle.text="Are you sure you don't want to \n secure your account?"
        securePinDialog = CustomDialog(requireContext())
        securePinConfirmationDialog = CustomDialog(requireContext())

        securePinDialog.setContentView(dialogSecurePinBinding.root)
        securePinDialog.setCancelable(false)
        securePinConfirmationDialog.setContentView(dialogSecurePinConfirmationBinding.root)
        securePinConfirmationDialog.setCancelable(false)


        val item1 = ProfileOptionsData(
            Constants.MY_ACCOUNT_TITLE,
            Constants.MY_ACCOUNT_DESCRIPTION,
            R.drawable.ic_profile,
            R.drawable.rightarrow
        )
        val item2 =
            ProfileOptionsData(
                Constants.SECURITY_SETTINGS_TITLE,
                Constants.SECURITY_SETTINGS_DESCRIPTION,
                R.drawable.shield,
                R.drawable.rightarrow
            )
        val item3 = ProfileOptionsData(
            Constants.HELP_CENTER_TITLE,
            Constants.HELP_CENTER_DESCRIPTION,
            R.drawable.helpmesg,
            R.drawable.rightarrow
        )
        val item4 = ProfileOptionsData(
            Constants.FACILITY_MANAGEMENT_TITLE,
            Constants.FACILITY_MANAGEMENT_DESCRIPTION,
            R.drawable.management_image,
            R.drawable.rightarrow
        )

        val listHolder = ArrayList<ProfileModel>()
        listHolder.add(ProfileModel(ProfileOptionsAdapter.VIEW_ITEM, item1))
        listHolder.add(ProfileModel(ProfileOptionsAdapter.VIEW_ITEM, item2))
        listHolder.add(ProfileModel(ProfileOptionsAdapter.VIEW_ITEM, item3))
        listHolder.add(ProfileModel(ProfileOptionsAdapter.VIEW_ITEM, item4))
        listHolder.add(ProfileModel(ProfileOptionsAdapter.VIEW_FOOTER, item1))

        binding.profileOptionsRecyclerview.layoutManager = LinearLayoutManager(requireActivity())
        binding.profileOptionsRecyclerview.adapter =
            ProfileOptionsAdapter(requireContext(),
                listHolder,
                object : ProfileOptionsAdapter.ProfileItemInterface {
                    override fun onClickItem(position: Int) {
                        when (position) {
                            0 -> {
                                if (!(requireActivity() as HomeActivity).isFingerprintValidate()) {
                                    setUpAuthentication()
                                } else {
                                    openMyAccount()
                                }
                            }
                            1 -> {
                                val bundle = Bundle()
                                bundle.putBoolean(
                                    Constants.WHATSAPP_CONSENT_ENABLED,
                                    isWhatsappConsent
                                )
                                bundle.putBoolean(
                                    Constants.SHOW_PUSH_NOTIFICATION,
                                    isPushNotificationSend
                                )
                                bundle.putBoolean(
                                    Constants.IS_SECURITY_TIPS_ACTIVE,
                                    isSecurityTipsActive
                                )
                                val securityFragment = SecurityFragment()
                                securityFragment.arguments = bundle
                                (requireActivity() as HomeActivity).addFragment(
                                    securityFragment,
                                    true
                                )
                            }
                            2 -> {
                                val bundle = Bundle()
                                bundle.putBoolean(Constants.IS_TERM_ACTIVE, isTermsActive)
                                bundle.putBoolean(Constants.IS_ABOUT_US_ACTIVE, isAboutUsActive)
                                val helpCenterFragment = HelpCenterFragment()
                                helpCenterFragment.arguments = bundle
                                (requireActivity() as HomeActivity).addFragment(
                                    helpCenterFragment,
                                    true
                                )
                            }
                            3 -> {

                                if (appPreference.isFacilityCard()) {
                                    (requireActivity() as HomeActivity).navigate(R.id.navigation_promises)
                                } else {
                                    facilityManagerDialogBox()
                                }
                            }
                        }
                    }
                },
                object : ProfileOptionsAdapter.ProfileFooterInterface {
                    override fun onLogoutClickItem(position: Int) {
                        logoutDialog.show()
                    }
                }
            )
    }

    private fun facilityManagerDialogBox() {
        facilityDialog.show()
        facilityManagerDialog.actionOk.setOnClickListener {
            facilityDialog.dismiss()
        }
    }

    private fun initClickListener() {
        logOut()
        //secure pin dialog actions
        dialogSecurePinBinding.acitionSecure.setOnClickListener {
            startActivityForResult(
                Intent(android.provider.Settings.ACTION_SETTINGS),
                PortfolioFragment.SETTING_REQUEST_CODE
            );
            securePinDialog.dismiss()
        }

        dialogSecurePinBinding.actionDontsecure.setOnClickListener {
            securePinDialog.dismiss()
            securePinConfirmationDialog.show()
        }

        dialogSecurePinConfirmationBinding.actionNo.setOnClickListener {
            securePinConfirmationDialog.dismiss()
            securePinDialog.show()
        }

        dialogSecurePinConfirmationBinding.actionYes.setOnClickListener {
            securePinConfirmationDialog.dismiss()
            openMyAccount()
            (requireActivity() as HomeActivity).fingerprintValidation(true)
        }
        binding.editProfile.setOnClickListener {
            if (::profileData.isInitialized) {
                val editProfile = EditProfileFragment()
                bundle.putSerializable(Constants.PROFILE_DATA, profileData)
                editProfile.arguments = bundle
                (requireActivity() as HomeActivity).addFragment(editProfile, true)
            }
        }
    }


    private fun setUpAuthentication() {
        executor = ContextCompat.getMainExecutor(this.requireContext())
        //Biometric dialog
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(Constants.HOABL)
            .setSubtitle(Constants.LOG_IN_USING_BIOMETRIC_CREDENTIAL)
            .setNegativeButtonText(Constants.USE_PATTERN)
            .build()

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        setUpKeyGuardManager()
                    } else if (errorCode == BiometricPrompt.ERROR_NO_BIOMETRICS) {
                        securePinDialog.show()
                    } else if (errorCode == BiometricPrompt.ERROR_USER_CANCELED) {
                        (requireActivity() as HomeActivity).onBackPressed()
                    } else if (errorCode == BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL) {
                        //no enrollment
                    } else if (errorCode == BiometricPrompt.ERROR_HW_NOT_PRESENT) {
                        //setUpUI(true)
                        setUpKeyGuardManager()
                    } else {
                        openMyAccount()
                        (requireActivity() as HomeActivity).fingerprintValidation(true)
                    }
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    openMyAccount()
                    (requireActivity() as HomeActivity).fingerprintValidation(true)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
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
                Constants.HI_USER,
                Constants.VERIFY_YOUR_SERCURITY_PIN_PATTERN
            )
            if (intent != null)
                startActivityForResult(intent, mRequestCode)
            else {
                openMyAccount()
                (requireActivity() as HomeActivity).fingerprintValidation(true)
            }
        } else {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            mRequestCode -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        (requireActivity() as HomeActivity).fingerprintValidation(true)
                        openMyAccount()
                    }
                }
            }
            SETTING_REQUEST_CODE -> {
                setUpAuthentication()
            }
        }
    }

    private fun logOut() {
        val logoutDialoglayout = LogoutConfirmationBinding.inflate(layoutInflater)
        logoutDialog = Dialog(requireContext())
        logoutDialog.setCancelable(false)
        logoutDialog.setContentView(logoutDialoglayout.root)

        logoutDialoglayout.actionYes.setOnClickListener {
            logOutFromCurrentDevice()
        }

        logoutDialoglayout.actionNo.setOnClickListener {
            logoutDialog.dismiss()
        }

    }

    private fun logOutFromCurrentDevice() {
        profileViewModel.logOutFromCurrent().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBaar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBaar.hide()
                    if (it.data != null) {
                        appPreference.saveLogin(false)
                        startActivity(Intent(context, AuthActivity::class.java))
                        requireActivity().finish()
                    }
                }
                Status.ERROR -> {
                    binding.progressBaar.hide()
                    logoutDialog.dismiss()
                    Log.d("Code", "message= ${it.message.toString()}")
                    when (it.message) {
                        Constants.ACCESS_DENIED -> {
                            appPreference.saveLogin(false)
                            startActivity(Intent(context, AuthActivity::class.java))
                            requireActivity().finish()
                        }
                        else -> {
                            (requireActivity() as HomeActivity).showErrorToast(
                                it.message!!
                            )
                        }
                    }
                }
            }
        })
    }

    private fun openMyAccount() {
        val myAccount = AccountDetailsFragment()
        (requireActivity() as HomeActivity).addFragment(myAccount, true)
    }

    override fun onPause() {
        super.onPause()
        logoutDialog.dismiss()
    }
}