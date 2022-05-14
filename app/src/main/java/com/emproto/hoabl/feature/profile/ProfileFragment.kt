package com.emproto.hoabl.feature.profile

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentProfileMainBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.login.AuthActivity
import com.emproto.networklayer.preferences.AppPreference
import com.example.portfolioui.databinding.LogoutConfirmationBinding
import javax.inject.Inject

class ProfileFragment : BaseFragment() {

    lateinit var binding: FragmentProfileMainBinding

    val bundle = Bundle()

    @Inject
    lateinit var appPreference: AppPreference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        binding = FragmentProfileMainBinding.inflate(inflater, container, false)
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =
            true
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.isVisible =
            false
        initClickListener()
        return binding.root
    }

    private fun initClickListener() {
        binding.editProfile.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val editProfile = EditProfileFragment()
                (requireActivity() as HomeActivity).addFragment(editProfile, false)
            }
        })
        binding.viewAccount.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val accountDetailsFragment = AccountDetailsFragment()
                (requireActivity() as HomeActivity).addFragment(accountDetailsFragment, false)
            }

        })
        binding.settingsView.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    val settingsFragment = SecurityFragment()
                    (requireActivity() as HomeActivity).addFragment(settingsFragment, false)
                }

            }
        )
        binding.helpCenterTv.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    val helpCenterFragment = HelpCenterFragment()
                    (requireActivity() as HomeActivity).addFragment(helpCenterFragment, false)
                }

            }
        )
        binding.facilityManagement.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    val facilityManagerPopViewFragment = FacilityManagerPopViewFragment()
                    (requireActivity() as HomeActivity).addFragment(
                        facilityManagerPopViewFragment,
                        false
                    )
                }

            }
        )

        val logoutDialoglayout = LogoutConfirmationBinding.inflate(layoutInflater)
        val logoutDialog = Dialog(requireContext())
        logoutDialog.setCancelable(false)
        logoutDialog.setContentView(logoutDialoglayout.root)

        logoutDialoglayout.actionYes.setOnClickListener {
            appPreference.saveLogin(false)
            startActivity(Intent(context, AuthActivity::class.java))
            requireActivity().finish()
        }

        logoutDialoglayout.actionNo.setOnClickListener {
            logoutDialog.dismiss()
        }

        binding.Logoutbtn.setOnClickListener {
            logoutDialog.show()
        }

    }
}