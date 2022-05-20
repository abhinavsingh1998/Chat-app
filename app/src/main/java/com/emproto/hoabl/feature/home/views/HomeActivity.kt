package com.emproto.hoabl.feature.home.views

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ActivityHomeBinding
import com.emproto.hoabl.databinding.FragmentNotificationBottomSheetBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.notification.HoabelNotifiaction
import com.emproto.hoabl.feature.home.notification.adapter.NotificationAdapter
import com.emproto.hoabl.feature.home.notification.data.NotificationDataModel
import com.emproto.hoabl.feature.chat.views.fragments.ChatsFragment
import com.emproto.hoabl.feature.home.views.fragments.HomeFragment
import com.emproto.hoabl.feature.investment.views.CategoryListFragment
import com.emproto.hoabl.feature.home.views.fragments.SearchResultFragment
import com.emproto.hoabl.feature.investment.views.InvestmentFragment
import com.emproto.hoabl.feature.portfolio.views.*
import com.emproto.hoabl.feature.promises.HoablPromises
import com.emproto.hoabl.feature.profile.ProfileFragment
import com.emproto.hoabl.feature.promises.PromisesDetailsFragment
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject

class HomeActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    val ScreenHome = 0
    val ScreenInvestment = 1
    val ScreenPortfolio = 2
    val ScreenPromises = 3
    val ScreenProfile = 4
    val ScreenNotification = 1
    lateinit var hoabelNotifiaction: HoabelNotifiaction
    lateinit var fragmentNotificationBottomSheetBinding: FragmentNotificationBottomSheetBinding
    lateinit var bottomSheetDialog: BottomSheetDialog
    var CurrentScreen = -1
    private var contentFrame = 0
    private var mContext: Context? = null
    private var closeApp = false
    private var toolbar: Toolbar? = null
    lateinit var activityHomeActivity: ActivityHomeBinding

    @Inject
    lateinit var factory: HomeFactory
    lateinit var homeViewModel: HomeViewModel
    var added = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHomeActivity = ActivityHomeBinding.inflate(layoutInflater)
        (application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
//        investmentViewModel =
//            ViewModelProvider(requireActivity(), investmentFactory).get(InvestmentViewModel::class.java)

        setContentView(activityHomeActivity.root)
        mContext = this
//        SharedPref.init(mContext)
        contentFrame = R.id.container
//        navigation = findViewById(R.id.bottomNavigation)
        activityHomeActivity.includeNavigation.bottomNavigation.setOnNavigationItemSelectedListener(
            this
        )

        if (savedInstanceState == null) {
            navigate(R.id.navigation_hoabl) // change to whichever id should be default
        }

        activityHomeActivity.searchLayout.imageBack.setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(p0: View?) {
                onBackPressed()
            }
        })

        activityHomeActivity.searchLayout.headset.setOnClickListener {
            val bundle = Bundle()
            val chatsFragment = ChatsFragment()
            chatsFragment.arguments = bundle
            replaceFragment(
                chatsFragment.javaClass,
                "",
                true,
                bundle,
                null,
                0,
                false
            )
            Toast.makeText(applicationContext, "Chat bot", Toast.LENGTH_SHORT).show()
        }

        initClickListener()
    }

    private fun initClickListener() {

        activityHomeActivity.searchLayout.search.onFocusChangeListener =
            View.OnFocusChangeListener { p0, p1 ->
                if (p1) {
                    Toast.makeText(this, "Focus", Toast.LENGTH_SHORT).show()
                    //show bottom dropdown
                } else {
                }
            }
        activityHomeActivity.searchLayout.search.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                //get the fragment

                if (p0.toString().isNotEmpty()) {
                    if (!added) {
                        added = true
                        addFragment(SearchResultFragment.newInstance(), false)
                    }
                } else {
                    added = false
                    onBackPressed()
                }
            }

        })

        activityHomeActivity.searchLayout.notification.setOnClickListener(View.OnClickListener {
            bottomSheetDialog = BottomSheetDialog(this)
            bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            fragmentNotificationBottomSheetBinding =
                FragmentNotificationBottomSheetBinding.inflate(layoutInflater)
            bottomSheetDialog.setContentView(fragmentNotificationBottomSheetBinding.root)

            view?.viewTreeObserver?.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val bottomSheetDialog =
                        (bottomSheetDialog as BottomSheetDialog).findViewById<View>(R.id.locUXView) as LinearLayout
                    BottomSheetBehavior.from<View>(bottomSheetDialog).apply {
                        peekHeight = 100
                    }

                    view?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                }
            })

            var data = ArrayList<NotificationDataModel>()
            for (i in 1..20) {
                data.add(
                    NotificationDataModel(
                        R.drawable.img,
                        "Notification Topic 1",
                        "It is a long established fact that a reader will be distracted ",
                        "1h"
                    )
                )
                Log.i("msg", "data")
            }
            val customAdapter = NotificationAdapter(this, data)

            bottomSheetDialog.findViewById<RecyclerView>(R.id.rv)?.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = customAdapter
            }


            launch_bottom_sheet()

        })

    }

    private fun launch_bottom_sheet() {
        bottomSheetDialog.show()
    }

    fun getCurrentFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(contentFrame)
    }

    fun navigate(id: Int) {
        activityHomeActivity.includeNavigation.bottomNavigation.selectedItemId = id
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
                val portfolioFragment = PortfolioFragment()
                portfolioFragment.setArguments(bundle)
                replaceFragment(portfolioFragment.javaClass, "", true, bundle, null, 0, false)
            }
            ScreenPromises -> {
                val cartFragment = HoablPromises()
                cartFragment.setArguments(bundle)
                replaceFragment(cartFragment.javaClass, "", true, bundle, null, 0, false)
            }
            ScreenProfile -> {
                val profileFragment = ProfileFragment()
                profileFragment.setArguments(bundle)
                replaceFragment(profileFragment.javaClass, "", true, bundle, null, 0, false)
            }
//            ScreenNotification -> {
//
//                val hoabelNotifiaction = HoabelNotifiaction()
//                hoabelNotifiaction.setArguments(bundle)
//                replaceFragment(hoabelNotifiaction.javaClass, "", true, bundle, null, 0, false)
//            }
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
                finalTag = fragmentClass.simpleName + extraTag
            else
                finalTag = fragmentClass.simpleName

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
                    fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit).commit()
                }
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }


    override fun onBackPressed() {
        added = false
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
            if (getCurrentFragment() is HomeFragment) {
                activityHomeActivity.includeNavigation.bottomNavigation.menu[0].isChecked = true
            } else if (getCurrentFragment() is PortfolioFragment) {
                activityHomeActivity.includeNavigation.bottomNavigation.menu[2].isChecked = true
            } else if (getCurrentFragment() is HoablPromises ||
                getCurrentFragment() is PromisesDetailsFragment
            ) {
                activityHomeActivity.includeNavigation.bottomNavigation.menu[3].isChecked = true
            }
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
                    .replace(R.id.container, fragment, fragment.javaClass.name)
                    .addToBackStack(fragment.javaClass.name).commit()
            }
        }
    }

    fun showErrorToast(message: String) {
        showErrorView(activityHomeActivity.root, message)
    }

    fun hideBackArrow() {
        activityHomeActivity.searchLayout.imageBack.hide()
    }

    fun showBackArrow() {
        activityHomeActivity.searchLayout.imageBack.show()
    }

    fun showBottomNavigation() {
        activityHomeActivity.includeNavigation.bottomNavigation.show()
    }

    fun hideBottomNavigation() {
        activityHomeActivity.includeNavigation.bottomNavigation.hide()
    }

}