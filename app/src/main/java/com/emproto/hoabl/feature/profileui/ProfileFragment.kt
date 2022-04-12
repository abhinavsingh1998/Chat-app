package com.emproto.hoabl.feature.profileui
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

class ProfileFragment : BaseFragment() {

    lateinit var binding: FragmentProfileMainBinding
    lateinit var image_edit_profile: ImageButton
    lateinit var edit_profile: TextView
    lateinit var ivrightarrow: ImageView
    lateinit var imageviewsettings: ImageView
    lateinit var imageSecurityLock: ImageView
    lateinit var imageViewMessage: ImageView
    lateinit var AccountView:View
    lateinit var settingsView:View
    lateinit var securityView:View
    lateinit var messageView:View
    val bundle = Bundle()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileMainBinding.inflate(inflater, container, false)


        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible=true
        (requireActivity()as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.isVisible= false
        initClickListener()
        return binding.root
    }

    private fun initClickListener() {
        binding.editProfile.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val profileSecondFragment = ProfileSecondFragment()
                (requireActivity()as HomeActivity).replaceFragment(profileSecondFragment.javaClass, "", true, bundle, null, 0, false)}
        })
        binding.AccountView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val accountDetailsFragment = Account_Details_Fragment()
                (requireActivity()as HomeActivity).replaceFragment(accountDetailsFragment.javaClass, "", true, bundle, null, 0, false)}

        })
        binding.settingsView.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    val settingsFragment = SettingsFragment()
                    (requireActivity()as HomeActivity).replaceFragment(settingsFragment.javaClass, "", true, bundle, null, 0, false)}

            }
        )
        binding.securityView.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    val securityFragment = SecurityFragment()
                    (requireActivity()as HomeActivity).replaceFragment(securityFragment.javaClass, "", true, bundle, null, 0, false)}

            }
        )
        binding.messageView.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    val healthCenterFragment = HealthCenterFragment()
                    (requireActivity()as HomeActivity).replaceFragment(healthCenterFragment.javaClass, "", true, bundle, null, 0, false)}

            }
        )

    }
}