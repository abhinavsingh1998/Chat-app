package com.emproto.hoabl.feature.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.emproto.core.BaseActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ActivityAuthBinding

class AuthActivity : BaseActivity() {

    lateinit var activityAuthBinding:ActivityAuthBinding

    val signinIssueFragment: SigninIssueFragment = SigninIssueFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAuthBinding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(activityAuthBinding.root)


        initView()
        initClickListener()
    }

    private fun initClickListener() {

        activityAuthBinding.textTrouble.setOnClickListener(View.OnClickListener {
            launch_bottom_sheet()
        })
    }

    private fun initView() {
        addFragment(LoginFragment(),true)
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

    private fun launch_bottom_sheet(){

        signinIssueFragment.show(supportFragmentManager,
            "add_photo_dialog_fragment")
    }

}