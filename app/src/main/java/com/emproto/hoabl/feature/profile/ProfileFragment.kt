package com.emproto.hoabl.feature.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.emproto.core.BaseFragment
import com.emproto.hoabl.HomeActivity
import com.emproto.hoabl.databinding.FragmentProfileMainBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.login.AuthActivity
import com.emproto.networklayer.preferences.AppPreference
import javax.inject.Inject

class ProfileFragment : BaseFragment() {

    lateinit var binding: FragmentProfileMainBinding
    lateinit var image_edit_profile: ImageButton
    lateinit var edit_profile: TextView
    lateinit var ivrightarrow: ImageView
    lateinit var imageviewsettings: ImageView
    lateinit var imageSecurityLock: ImageView
    lateinit var imageViewMessage: ImageView
    lateinit var AccountView: View
    lateinit var settingsView: View
    lateinit var help_center_tv: View
    lateinit var facility_management: View
    lateinit var messageView: View
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
                val profileSecondFragment = EditProfileFragment()
                (requireActivity() as HomeActivity).addFragment(profileSecondFragment, false)
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
                    (requireActivity() as HomeActivity).replaceFragment(
                        settingsFragment.javaClass,
                        "",
                        true,
                        bundle,
                        null,
                        0,
                        false
                    )
                }

            }
        )
        binding.helpCenterTv.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    val healthCenterFragment = HealthCenterFragment()
                    (requireActivity() as HomeActivity).replaceFragment(
                        healthCenterFragment.javaClass,
                        "",
                        true,
                        bundle,
                        null,
                        0,
                        false
                    )
                }

            }
        )
        binding.facilityManagement.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    val facilityManagerPopViewFragment = FacilityManagerPopViewFragment()
                    (requireActivity() as HomeActivity).replaceFragment(
                        facilityManagerPopViewFragment.javaClass,
                        "",
                        true,
                        bundle,
                        null,
                        0,
                        false
                    )
                }

            }
        )

        binding.Logoutbtn.setOnClickListener {
            appPreference.saveLogin(false)
            startActivity(Intent(context, AuthActivity::class.java))
            requireActivity().finish()
        }

    }
}