package com.emproto.hoabl.feature.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.emproto.core.BaseFragment
import com.emproto.hoabl.BuildConfig
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentProfileMainBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.login.AuthActivity
import com.emproto.hoabl.feature.profile.adapter.ProfileOptionsAdapter
import com.emproto.hoabl.feature.profile.data.ProfileModel
import com.emproto.hoabl.feature.profile.data.ProfileOptionsData
import com.emproto.hoabl.viewmodels.ProfileViewModel
import com.emproto.hoabl.viewmodels.factory.ProfileFactory
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.profile.Data
import javax.inject.Inject

class ProfileFragment : BaseFragment(), ProfileOptionsAdapter.HelpItemInterface {

    lateinit var binding: FragmentProfileMainBinding

    val bundle = Bundle()

    @Inject
    lateinit var profileFactory: ProfileFactory
    lateinit var profileViewModel: ProfileViewModel
    lateinit var profileData: Data

    @Inject
    lateinit var appPreference: AppPreference

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
                            /* val contactType=it.data.contactType
                             val countryCode=it.data.countryCode
                             val crmId=it.data.crmId
                             val Id=it.data.id
                             val profilePictureUrl=it.data.profilePictureUrl
                             val state=it.data.state
                             val firstName=it.data.firstName
                             val lastName=it.data.lastName
                             val gender=it.data.gender
                             val city=it.data.city
                             val address=it.data.streetAddress
                             val dateOfBirth=it.data.dateOfBirth
                             val phoneNumber=it.data.phoneNumber
                             val pincode=it.data.pincode
                             val emailId=it.data.email
                             val otpVerified=it.data.otpVerified
                             val status=it.data.status
                             val whtsappConsent=it.data.whatsappConsent
                             val country=it.data.country
                             val houseNUmber=it.data.houseNumber
                             val locality=it.data.locality
                             profileData=Data(contactType,countryCode,crmId,dateOfBirth,emailId,firstName,gender,id,lastName,
                                     otpVerified,phoneNumber,profilePictureUrl,status,whtsappConsent,country,city,state,
                                     pincode,locality,houseNUmber,address)*/
                            profileData = it.data
                        }
                    }
                }
                Status.ERROR -> {
                    binding.progressBaar.hide()
                }

            }
        })
    }

    private fun initView() {
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =
            true
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.isVisible =
            false

        binding.version.text = "App Version: v" + BuildConfig.VERSION_NAME

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
        binding.Logoutbtn.setOnClickListener {
            appPreference.saveLogin(false)
            startActivity(Intent(context, AuthActivity::class.java))
            requireActivity().finish()
        }

        binding.editProfile.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val editProfile = EditProfileFragment()
                bundle.putSerializable("profileData", profileData)
                editProfile.arguments = bundle
                (requireActivity() as HomeActivity).addFragment(editProfile, false)
            }
        })

    }

    override fun onClickItem(position: Int) {
        when (position) {
            0 -> {
                val myAcccount = AccountDetailsFragment()
                (requireActivity() as HomeActivity).addFragment(myAcccount, false)
            }
            1 -> {
                val settingsFragment = SecurityFragment()
                (requireActivity() as HomeActivity).addFragment(settingsFragment, false)
            }
            2 -> {
                val helpCenterFragment = HelpCenterFragment()
                (requireActivity() as HomeActivity).addFragment(helpCenterFragment, false)
            }
            3 -> {
                val facilityManagerPopViewFragment = FacilityManagerPopViewFragment()
                (requireActivity() as HomeActivity).addFragment(
                    facilityManagerPopViewFragment,
                    false
                )
            }
        }
    }
}