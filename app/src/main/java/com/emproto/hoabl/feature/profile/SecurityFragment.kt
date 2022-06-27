package com.emproto.hoabl.feature.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentSecurityBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.dialogs.ApplicationSubmitDialog
import com.emproto.hoabl.feature.investment.dialogs.ConfirmationDialog
import com.emproto.hoabl.feature.profile.adapter.SecurityAdapter
import com.emproto.hoabl.feature.profile.adapter.SettingsAdapter
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.viewmodels.ProfileViewModel
import com.emproto.hoabl.viewmodels.factory.ProfileFactory
import com.emproto.networklayer.request.login.TroubleSigningRequest
import com.emproto.networklayer.request.profile.ReportSecurityRequest
import com.emproto.networklayer.request.profile.WhatsappConsentBody
import com.emproto.networklayer.response.enums.Status
import javax.inject.Inject


class SecurityFragment : Fragment(){
    @Inject
    lateinit var profileFactory: ProfileFactory
    private lateinit var profileViewModel: ProfileViewModel

    lateinit var binding: FragmentSecurityBinding
    lateinit var adapter: SettingsAdapter

    val bundle = Bundle()

    private var isWhatsappEnabled = false
    private var showPushNotifications = false

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
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataList: ArrayList<RecyclerViewItem> = ArrayList<RecyclerViewItem>()
        dataList.add(RecyclerViewItem(SecurityAdapter.VIEW_REPORT))
//        dataList.add(RecyclerViewItem(SecurityAdapter.VIEW_SECURITY_AUTHENTICATE))
        dataList.add(RecyclerViewItem(SecurityAdapter.VIEW_SECURITY_WHATSAPP_COMMUNICATION))
        dataList.add(RecyclerViewItem(SecurityAdapter.VIEW_SECURITY_LOCATION))
        dataList.add(RecyclerViewItem(SecurityAdapter.VIEW_SETTINGS_ALL_OPTIONS))

        val adapter = SecurityAdapter(this.requireContext(), dataList, itemClickListener, isWhatsappEnabled, showPushNotifications)
        binding.rvHelpCenter.adapter = adapter

        binding.arrowimage.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
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
                    val securityTipsFragment = SecurityTipsFragment()
                    (requireActivity() as HomeActivity).addFragment(securityTipsFragment,false)
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
                                    (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
                                }
                                Status.SUCCESS -> {
                                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
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
                                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
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
                    }
                }
            }
        }
    }

    private fun callWhatsAppConsentApi(status: Boolean,showPushNotifications:Boolean) {
        profileViewModel.putWhatsappconsent(WhatsappConsentBody(whatsappConsent = status,showPushNotifications=showPushNotifications)).observe(viewLifecycleOwner,Observer{
            when(it.status){
                Status.LOADING -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
                }
                Status.SUCCESS -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                    if (it.data != null) {
                        it.data?.let {

                        }
                    }
                }
                Status.ERROR -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                    it.data
                    (requireActivity() as HomeActivity).showErrorToast(
                        it.message!!
                    )
                }
            }
        })
    }

}

