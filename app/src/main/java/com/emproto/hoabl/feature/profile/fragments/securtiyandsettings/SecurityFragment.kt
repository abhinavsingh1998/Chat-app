package com.emproto.hoabl.feature.profile.fragments.securtiyandsettings

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentSecurityBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.dialogs.ApplicationSubmitDialog
import com.emproto.hoabl.feature.investment.dialogs.ConfirmationDialog
import com.emproto.hoabl.feature.login.AuthActivity
import com.emproto.hoabl.feature.profile.adapter.SecurityAdapter
import com.emproto.hoabl.feature.profile.adapter.SettingsAdapter
import com.emproto.hoabl.feature.profile.fragments.profile.ConfirmationLogOutDialog
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.viewmodels.ProfileViewModel
import com.emproto.hoabl.viewmodels.factory.ProfileFactory
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.request.profile.ReportSecurityRequest
import com.emproto.networklayer.request.profile.WhatsappConsentBody
import com.emproto.networklayer.response.enums.Status
import com.example.portfolioui.databinding.LogoutAllConfirmationBinding
import com.example.portfolioui.databinding.LogoutConfirmationBinding
import javax.inject.Inject


class SecurityFragment : BaseFragment(){
    @Inject
    lateinit var profileFactory: ProfileFactory
    private lateinit var profileViewModel: ProfileViewModel

    @Inject
    lateinit var appPreference: AppPreference

    lateinit var binding: FragmentSecurityBinding
    lateinit var adapter: SettingsAdapter
    lateinit var logoutDialog: Dialog

    val bundle = Bundle()

     private var isWhatsappEnabled = false
    private var showPushNotifications = false
    private var isSecurityTipsActive = false

    companion object{
        const val SPEECH_REQUEST_CODE = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible=false
        profileViewModel =
            ViewModelProvider(requireActivity(), profileFactory)[ProfileViewModel::class.java]
        binding = FragmentSecurityBinding.inflate(layoutInflater)
        arguments.let {
            isWhatsappEnabled = it?.getBoolean("whatsappConsentEnabled") as Boolean
            showPushNotifications = it.getBoolean("showPushNotifications") as Boolean
            isSecurityTipsActive = it.getBoolean("isSecurityTipsActive") as Boolean
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataList: ArrayList<RecyclerViewItem> = ArrayList<RecyclerViewItem>()
        dataList.add(RecyclerViewItem(SecurityAdapter.VIEW_REPORT))
//        dataList.add(RecyclerViewItem(SecurityAdapter.VIEW_SECURITY_AUTHENTICATE))
        dataList.add(RecyclerViewItem(SecurityAdapter.VIEW_SECURITY_WHATSAPP_COMMUNICATION))
        when(isSecurityTipsActive){
            true -> {
                dataList.add(RecyclerViewItem(SecurityAdapter.VIEW_SECURITY_TIPS))
            }
        }
        dataList.add(RecyclerViewItem(SecurityAdapter.VIEW_SIGN_OUT_ALL))
        dataList.add(RecyclerViewItem(SecurityAdapter.VIEW_SETTINGS_ALL_OPTIONS))

        val logoutDialoglayout = LogoutAllConfirmationBinding.inflate(layoutInflater)
        logoutDialog = Dialog(requireContext())
        logoutDialog.setCancelable(false)
        logoutDialog.setContentView(logoutDialoglayout.root)

        val adapter = SecurityAdapter(this.requireContext(), dataList, itemClickListener, isWhatsappEnabled, showPushNotifications)
        binding.rvHelpCenter.adapter = adapter

        binding.arrowimage.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        logoutDialoglayout.actionYes.setOnClickListener {
            logOutFromAllDevices()
        }

        logoutDialoglayout.actionNo.setOnClickListener {
            logoutDialog.dismiss()
        }

    }

    val itemClickListener = object : ItemClickListener {
        override fun onItemClicked(view: View, position: Int, item: String) {
            when(view.id){
                R.id.switch1 -> {
                    when(item){
                        "true" -> {
                            isWhatsappEnabled = true
                            callWhatsAppConsentApi(isWhatsappEnabled,showPushNotifications)
                        }
                        "false" -> {
                            isWhatsappEnabled = false
                            callWhatsAppConsentApi(isWhatsappEnabled,showPushNotifications)
                        }
                    }
                }
                R.id.cl_security_tips -> {
                    when(item){
                        "Security Tips" -> {
                            val securityTipsFragment = SecurityTipsFragment()
                            (requireActivity() as HomeActivity).addFragment(securityTipsFragment,false)
                        }
                        "Sign out from all devices" -> {
                            logoutDialog.show()
                        }
                    }

                }
                R.id.button_view -> {
//                    val u = Uri.parse("tel:" + "8939122576")
//                    val intent = Intent(Intent.ACTION_DIAL,u)
//                    try {
//                        startActivity(intent)
//                    } catch (s: SecurityException) {
//                        Toast.makeText(context, "An error occurred", Toast.LENGTH_LONG).show()
//                    }
                    profileViewModel.submitTroubleCase(ReportSecurityRequest(
                        caseType = "1005",
                        description = "I want to raise a security emergency")).observe(viewLifecycleOwner, Observer {
                            when(it.status){
                                Status.LOADING -> {
                                    binding.progressBar.show()
                                }
                                Status.SUCCESS -> {
                                    binding.progressBar.hide()
                                    if (it.data != null) {
                                        it.data?.let {
                                            val applicationSubmitDialog = ApplicationSubmitDialog(
                                                "Request Sent",
                                                "A relationship manager will get back to you to discuss more about it.",
                                                false
                                            )
                                            applicationSubmitDialog.show(parentFragmentManager, "ApplicationSubmitDialog")
                                        }
                                    }
                                }
                                Status.ERROR -> {
                                    binding.progressBar.hide()
                                    it.data
                                    (requireActivity() as HomeActivity).showErrorToast(
                                        it.message!!
                                    )
                                }
                            }
                    })
                }
                R.id.setting_switch -> {
                    when(item){
                        "true" -> {
                            showPushNotifications = true
                            callWhatsAppConsentApi(isWhatsappEnabled,showPushNotifications)
                        }
                        "false" -> {
                            showPushNotifications = false
                            callWhatsAppConsentApi(isWhatsappEnabled,showPushNotifications)
                        }
                        "Voice Command" -> {
//                            displaySpeechRecognizer()
                        }
                    }
                }
            }
        }
    }


    private fun logOutFromAllDevices() {
        profileViewModel.logOutFromAll().observe(viewLifecycleOwner,Observer{
            when(it.status){
                Status.LOADING -> {
                    binding.progressBar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBar.hide()
                    if (it.data != null) {
                        it.data?.let {
                            logoutDialog.dismiss()
                            appPreference.saveLogin(false)
                            startActivity(Intent(context, AuthActivity::class.java))
                            requireActivity().finish()
                        }
                    }
                }
                Status.ERROR -> {
                    logoutDialog.dismiss()
                    binding.progressBar.hide()
                    (requireActivity() as HomeActivity).showErrorToast(
                        it.message!!
                    )
                }
            }
        })
    }

    private fun displaySpeechRecognizer() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        }
        // This starts the activity and populates the intent with the speech text.
        startActivityForResult(intent, SPEECH_REQUEST_CODE)
    }

    private fun callWhatsAppConsentApi(status: Boolean,showPushNotifications:Boolean) {
        profileViewModel.putWhatsappconsent(WhatsappConsentBody(whatsappConsent = status,showPushNotifications=showPushNotifications)).observe(viewLifecycleOwner,Observer{
            when(it.status){
                Status.LOADING -> {
                    binding.progressBar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBar.hide()
                    if (it.data != null) {
                        it.data?.let {

                        }
                    }
                }
                Status.ERROR -> {
                    binding.progressBar.hide()
                    it.data
                    (requireActivity() as HomeActivity).showErrorToast(
                        it.message!!
                    )
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val spokenText: String? =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).let { results ->
                    results!![0]
                }
            // Do something with spokenText.
            Toast.makeText(this.requireContext(), spokenText.toString(), Toast.LENGTH_SHORT).show()
            when{
                spokenText.toString().contains("Investment", ignoreCase = true) -> {
                    (requireActivity() as HomeActivity).navigate(R.id.navigation_investment)
                }
                spokenText.toString().contains("Promise", ignoreCase = true) -> {
                    (requireActivity() as HomeActivity).navigate(R.id.navigation_promises)
                }
                spokenText.toString().contains("Portfolio", ignoreCase = true) -> {
                    (requireActivity() as HomeActivity).navigate(R.id.navigation_portfolio)
                }
                spokenText.toString().contains("Home", ignoreCase = true) -> {
                    (requireActivity() as HomeActivity).navigate(R.id.navigation_hoabl)
                }
                spokenText.toString().contains("Profile", ignoreCase = true) -> {
                    (requireActivity() as HomeActivity).navigate(R.id.navigation_profile)
                }
            }
        }
    }

}

