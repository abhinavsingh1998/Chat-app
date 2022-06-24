package com.emproto.hoabl.feature.profile

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.emproto.core.BaseFragment
import com.emproto.hoabl.BuildConfig
import com.emproto.hoabl.R

import com.emproto.hoabl.databinding.FragmentProfileMainBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.login.AuthActivity
import com.emproto.hoabl.feature.portfolio.views.FmFragment
import com.emproto.hoabl.feature.profile.adapter.ProfileOptionsAdapter
import com.emproto.hoabl.feature.profile.data.ProfileModel
import com.emproto.hoabl.feature.profile.data.ProfileOptionsData
import com.emproto.hoabl.viewmodels.ProfileViewModel
import com.emproto.hoabl.viewmodels.factory.ProfileFactory
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.portfolio.fm.FMResponse
import com.emproto.networklayer.response.profile.Data
import com.example.portfolioui.databinding.LogoutConfirmationBinding

import javax.inject.Inject

class ProfileFragment : BaseFragment(), ProfileOptionsAdapter.HelpItemInterface {

    lateinit var binding: FragmentProfileMainBinding

    val bundle = Bundle()

    @Inject
    lateinit var profileFactory: ProfileFactory
    private lateinit var profileViewModel: ProfileViewModel
    lateinit var profileData: Data
    var fmData: FMResponse? = null

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
                    Log.i("Data Check", it.data.toString())

                    binding.progressBaar.hide()
                    if (it.data != null) {
                        Log.i("Data", it.data.toString())
                        it.data?.let {
                            profileData = it.data
                            isWhatsappConsent = it.data.whatsappConsent
                            isPushNotificationSend = it.data.showPushNotifications
                        }
                        setUiData(profileData)
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
    }

    private fun setUiData(profileData: Data) {
        binding.tvName.text = profileData.firstName + " " + profileData.lastName

        /*for user pic not available show username as pic label*/
        if (profileData.profilePictureUrl != null && profileData.profilePictureUrl!!.isNotEmpty()) {
            binding.profileImage.visibility = View.VISIBLE
            binding.profileUserLetters.visibility = View.GONE
            Glide.with(requireContext())
                .load(profileData.profilePictureUrl)
                .into(binding.profileImage)
        } else {
            binding.profileImage.visibility = View.GONE
            binding.profileUserLetters.visibility = View.VISIBLE
            setUserNamePIC(profileData)
        }
    }

    private fun setUserNamePIC(profileData: Data) {
        val firstLetter: String = profileData.firstName.substring(0, 1)
        val lastLetter = when{
            profileData.lastName.isNotEmpty() -> {
                 profileData.lastName.substring(0, 1)
            }
            else -> { "" }
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
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =
            true
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.isVisible =
            false

//        binding.version.text = "App Version: v" + BuildConfig.VERSION_NAME


        val item1 = ProfileOptionsData(
            requireContext().resources.getString(R.string.my_accounts),
            requireContext().resources.getString(R.string.payment_history_kyc),
            R.drawable.ic_profile,
            R.drawable.rightarrow
        )

        val item2 =
            ProfileOptionsData(
                requireContext().resources.getString(R.string.securityandsetting),
                requireContext().resources.getString(R.string.report_emergency_control_authentication_etc),
                R.drawable.shield,
                R.drawable.rightarrow
            )

        val item3 = ProfileOptionsData(
            requireContext().resources.getString(R.string.help_center),
            requireContext().resources.getString(R.string.contact_about_us_privacy_policy_chat),
            R.drawable.helpmesg,
            R.drawable.rightarrow
        )

        val item4 = ProfileOptionsData(
            requireContext().resources.getString(R.string.facility_management),
            requireContext().resources.getString(R.string.manage_your_land_book),
            R.drawable.management_image,
            R.drawable.rightarrow
        )

        val listHolder = ArrayList<ProfileModel>()
        listHolder.add(ProfileModel(item1))
        listHolder.add(ProfileModel(item2))
        listHolder.add(ProfileModel(item3))
        listHolder.add(ProfileModel(item4))

        binding.profileOptionsRecyclerview.layoutManager = LinearLayoutManager(requireActivity())
        binding.profileOptionsRecyclerview.adapter =
            ProfileOptionsAdapter(requireContext(), listHolder, this)
    }


    private fun initClickListener() {
        logOut()

        binding.editProfile.setOnClickListener {
            val editProfile = EditProfileFragment()
            bundle.putSerializable("profileData", profileData)
            editProfile.arguments = bundle
            (requireActivity() as HomeActivity).addFragment(editProfile, false)
        }
        binding.version.text = "App version:" + BuildConfig.VERSION_NAME


    }

    override fun onClickItem(position: Int) {
        when (position) {
            0 -> {
                val myAcccount = AccountDetailsFragment()
                (requireActivity() as HomeActivity).addFragment(myAcccount, false)
            }
            1 -> {
                val bundle = Bundle()
                bundle.putBoolean("whatsappConsentEnabled",isWhatsappConsent)
                bundle.putBoolean("showPushNotifications",isPushNotificationSend)
                val securityFragment = SecurityFragment()
                securityFragment.arguments = bundle
                (requireActivity() as HomeActivity).addFragment(securityFragment, false)
            }
            2 -> {
                val helpCenterFragment = HelpCenterFragment()
                (requireActivity() as HomeActivity).addFragment(helpCenterFragment, false)
            }
            3 -> {
                val facilityManagerPopViewFragment = FacilityManagerPopViewFragment()

                if(appPreference.isFacilityCard()==true){
                    if (fmData != null) {
                        (requireActivity() as HomeActivity).addFragment(
                            FmFragment.newInstance(
                                fmData!!.data.web_url,
                                ""
                            ), false
                        )

                    } else {
                        (requireActivity() as HomeActivity).showErrorToast(
                            "Something Went Wrong"
                        )
                    }
                }else{
                    (requireActivity() as HomeActivity).addFragment(
                        facilityManagerPopViewFragment,
                        false
                    )

                }

            }


        }


    }

    private fun logOut() {
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