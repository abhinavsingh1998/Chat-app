package com.emproto.hoabl

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseActivity
import com.emproto.hoabl.databinding.ActivityHomeBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.fragments.FinancialAndProjectFragment
import com.emproto.hoabl.feature.home.views.fragments.HomeFragment
import com.emproto.hoabl.feature.investment.views.InvestmentFragment
import com.emproto.hoabl.feature.home.promisesUi.HoabelPromises
import com.emproto.hoabl.feature.profileui.ProfileFragment
import com.emproto.hoabl.fragments.PromisesFragment
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import javax.inject.Inject

class HomeActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    val ScreenHome = 0
    val ScreenInvestment = 1
    val ScreenPortfolio = 2
    val ScreenPromises = 3
    val ScreenProfile = 4
    var CurrentScreen = -1
    private var contentFrame = 0
    private var mContext: Context? = null
    private var closeApp = false
    private var toolbar: Toolbar? = null
    lateinit var activityHomeActivity: ActivityHomeBinding
    @Inject
    lateinit var factory:HomeFactory
    lateinit var homeViewModel:HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHomeActivity = ActivityHomeBinding.inflate(layoutInflater)
        (application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        setContentView(activityHomeActivity.root)
        mContext = this
//        SharedPref.init(mContext)
        contentFrame = R.id.container
//        navigation = findViewById(R.id.bottomNavigation)
        activityHomeActivity.includeNavigation.bottomNavigation.setOnNavigationItemSelectedListener(
            this
        )

        if (savedInstanceState == null) {
            activityHomeActivity.includeNavigation.bottomNavigation.selectedItemId=
                R.id.navigation_hoabl  // change to whichever id should be default
        }

        activityHomeActivity.searchLayout.imageBack.setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(p0: View?) {
                onBackPressed()
            }
        })

    }

    fun getCurrentFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(contentFrame)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_hoabl -> {
                openScreen(ScreenHome, "", false)
                return true
            }
            R.id.navigation_investment -> {
                openScreen(ScreenInvestment, "", false)
                return true
            }
            R.id.navigation_portfolio -> {
                openScreen(ScreenPortfolio, "", false)
                return true
            }
            R.id.navigation_promises -> {
                openScreen(ScreenPromises, "", false)
                return true
            }
            R.id.navigation_profile -> {
                openScreen(ScreenProfile, "", false)
                return true
            }
        }
        return false
    }

    private fun openScreen(screen: Int, metaData: String, isInit: Boolean) {
        val bundle = Bundle()
        bundle.putString("Type", metaData)
        CurrentScreen = screen
        when (screen) {
            ScreenHome -> {
                val homeFragment = HomeFragment()
                homeFragment.setArguments(bundle)
                replaceFragment(homeFragment.javaClass, "", true, bundle, null, 0, false)
            }
            ScreenInvestment -> {
                val favouriteFragment = InvestmentFragment()
//                val favouriteFragment = Testimonials()
                favouriteFragment.setArguments(bundle)
                replaceFragment(favouriteFragment.javaClass, "", true, bundle, null, 0, false)
            }
            ScreenPortfolio -> {
                val membershipFragment = FinancialAndProjectFragment()
                membershipFragment.setArguments(bundle)
                replaceFragment(membershipFragment.javaClass, "", true, bundle, null, 0, false)
            }
            ScreenPromises -> {
                val cartFragment = HoabelPromises()
                cartFragment.setArguments(bundle)
                replaceFragment(cartFragment.javaClass, "", true, bundle, null, 0, false)
            }
            ScreenProfile -> {
                val profileFragment = ProfileFragment()
                profileFragment.setArguments(bundle)
                replaceFragment(profileFragment.javaClass, "", true, bundle, null, 0, false)
            }
        }
    }

    fun replaceFragment(
        fragmentClass: Class<*>,
        extraTag: String?,
        addToBackStack: Boolean,
        bundle: Bundle?,
        fragmentForResult: Fragment?,
        targetRequestCode: Int,
        replaceWithAnimation: Boolean
    ) {
        try {
            val finalTag: String
            if (extraTag != null && extraTag.equals(""))
                finalTag=fragmentClass.simpleName + extraTag
            else
                finalTag=fragmentClass.simpleName

            val isPopBackStack = supportFragmentManager.popBackStackImmediate(finalTag, 0)
            if (!isPopBackStack) {
                var fragment = supportFragmentManager.findFragmentByTag(finalTag)
                if (fragment == null) try {
                    fragment = fragmentClass.newInstance() as Fragment
                } catch (e: InstantiationException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
                if (fragment != null) {
                    if (bundle != null) {
                        fragment.arguments = bundle
                    }
                    if (fragmentForResult != null) {
                        fragment.setTargetFragment(fragmentForResult, targetRequestCode)
                    }
                    val fragmentTransaction: FragmentTransaction =
                        supportFragmentManager.beginTransaction()
//                    fragmentTransaction.setCustomAnimations()
                    fragmentTransaction.replace(contentFrame, fragment, finalTag)
                    if (addToBackStack) fragmentTransaction.addToBackStack(finalTag)
                    supportFragmentManager.executePendingTransactions()
                    fragmentTransaction.commit()
                }
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }


    override fun onBackPressed() {
        if (getCurrentFragment() is HomeFragment) {
            if (closeApp) {
                finishAffinity()
            } else {
                closeApp = true
                Handler().postDelayed({ closeApp = false }, 2000)
                Toast.makeText(mContext, "Please press again to exit", Toast.LENGTH_LONG).show()
            }
        } else {
            super.onBackPressed()
        }
    }

    fun addFragment(fragment: Fragment, showAnimation: Boolean) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        when (showAnimation) {
            false -> {
                fragmentTransaction.replace(R.id.container, fragment, fragment.javaClass.name)
                    .addToBackStack(fragment.javaClass.name).commit()
            }
            true -> {
                fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit)
                    .replace(R.id.container,fragment,fragment.javaClass.name)
                    .addToBackStack(fragment.javaClass.name).commit()
            }
        }
    }


}