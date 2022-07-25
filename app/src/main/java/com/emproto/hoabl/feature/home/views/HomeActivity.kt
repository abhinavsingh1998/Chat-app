package com.emproto.hoabl.feature.home.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ActivityHomeBinding
import com.emproto.hoabl.databinding.FragmentNotificationBottomSheetBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.chat.views.fragments.ChatsFragment
import com.emproto.hoabl.feature.home.adapters.LatestUpdateAdapter
import com.emproto.hoabl.feature.notification.HoabelNotifiaction
import com.emproto.hoabl.feature.home.views.fragments.HomeFragment
import com.emproto.hoabl.feature.home.views.fragments.InsightsFragment
import com.emproto.hoabl.feature.home.views.fragments.LatestUpdatesFragment
import com.emproto.hoabl.feature.home.views.fragments.SearchResultFragment
import com.emproto.hoabl.feature.investment.views.InvestmentFragment
import com.emproto.hoabl.feature.notification.adapter.NotificationAdapter
import com.emproto.hoabl.feature.portfolio.views.*
import com.emproto.hoabl.feature.profile.fragments.about_us.AboutUsFragment
import com.emproto.hoabl.feature.promises.HoablPromises
import com.emproto.hoabl.feature.profile.fragments.profile.ProfileFragment
import com.emproto.hoabl.feature.promises.PromisesDetailsFragment
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.request.notification.UnReadNotifications
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.notification.dataResponse.NotificationResponse
import com.emproto.networklayer.response.notification.readStatus.ReadNotificationReponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mixpanel.android.mpmetrics.MixpanelAPI
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
    lateinit var unReadNotifications:UnReadNotifications


    @Inject
    lateinit var factory: HomeFactory
    lateinit var homeViewModel: HomeViewModel
    var added = false
    val appURL = "https://hoabl.in/"
    private var oneTimeValidation = false

    var topText = ""

    @Inject
    lateinit var appPreference: AppPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHomeActivity = ActivityHomeBinding.inflate(layoutInflater)
        (application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        activityHomeActivity.searchLayout.rotateText.text = " "
//        investmentViewModel =
//            ViewModelProvider(requireActivity(), investmentFactory).get(InvestmentViewModel::class.java)

        activityHomeActivity.searchLayout.rotateText.isSelected = true
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

        activityHomeActivity.searchLayout.imageBack.setOnClickListener { onBackPressed() }
        initData()
        initClickListener()
        trackEvent()
    }

    private fun trackEvent() {
        val mixpanelAPI = MixpanelAPI.getInstance(this, getString(R.string.MIXPANEL_KEY))
        mixpanelAPI.identify(appPreference.getMobilenum())
    }

    private fun initClickListener() {

        activityHomeActivity.searchLayout.headsetView.setOnClickListener {

//            Toast.makeText(this, "Chat is Under Development", Toast.LENGTH_LONG).show()
            val bundle = Bundle()
            val chatsFragment = ChatsFragment()
            chatsFragment.arguments = bundle
            replaceFragment(
                chatsFragment.javaClass, "", true, bundle, null, 0, false
            )
        }

        activityHomeActivity.searchLayout.search.setOnClickListener {
            val fragment = SearchResultFragment()
            val bundle = Bundle()
            bundle.putString("TopText", topText)
            fragment.arguments = bundle
            addFragment(fragment, false)
        }

        activityHomeActivity.searchLayout.notificationView.setOnClickListener(View.OnClickListener {
            bottomSheetDialog = BottomSheetDialog(this)
            fragmentNotificationBottomSheetBinding =
                FragmentNotificationBottomSheetBinding.inflate(layoutInflater)
            bottomSheetDialog.setContentView(fragmentNotificationBottomSheetBinding.root)

            callNotificationApi()
            launch_bottom_sheet()
        })

        activityHomeActivity.searchLayout.layout.setOnClickListener(View.OnClickListener {
            val fragment = AboutUsFragment()
            addFragment(fragment, false)
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
                homeFragment.arguments = bundle
                replaceFragment(homeFragment.javaClass, "", true, bundle, null, 0, false)
            }
            ScreenInvestment -> {
                val favouriteFragment = InvestmentFragment()
//                val favouriteFragment = Testimonials()
                favouriteFragment.arguments = bundle
                replaceFragment(favouriteFragment.javaClass, "", true, bundle, null, 0, false)
            }
            ScreenPortfolio -> {
                val portfolioFragment = PortfolioFragment()
                portfolioFragment.arguments = bundle
                replaceFragment(portfolioFragment.javaClass, "", true, bundle, null, 0, false)
            }
            ScreenPromises -> {
                val cartFragment = HoablPromises()
                cartFragment.arguments = bundle
                replaceFragment(cartFragment.javaClass, "", true, bundle, null, 0, false)
            }
            ScreenProfile -> {
                val profileFragment = ProfileFragment()
                profileFragment.arguments = bundle
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
                    fragmentTransaction.setCustomAnimations(
                        R.anim.enter,
                        R.anim.exit,
                        R.anim.enter_right,
                        R.anim.exit_left
                    ).commit()


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
            } else if (getCurrentFragment() is InvestmentFragment) {
                activityHomeActivity.includeNavigation.bottomNavigation.menu[1].isChecked = true
            } else if (getCurrentFragment() is PortfolioFragment) {
                activityHomeActivity.includeNavigation.bottomNavigation.menu[2].isChecked = true
            } else if (getCurrentFragment() is HoablPromises ||
                getCurrentFragment() is PromisesDetailsFragment
            ) {
                activityHomeActivity.includeNavigation.bottomNavigation.menu[3].isChecked = true
            }else if (getCurrentFragment() is ProfileFragment) {
                activityHomeActivity.includeNavigation.bottomNavigation.menu[4].isChecked = true
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
                fragmentTransaction.setCustomAnimations(
                    R.anim.enter,
                    R.anim.exit,
                    R.anim.enter_right,
                    R.anim.exit_left
                )
                    .replace(R.id.container, fragment, fragment.javaClass.name)
                    .addToBackStack(fragment.javaClass.name).commit()
            }
        }
    }

    fun showErrorToast(message: String) {
        showErrorView(activityHomeActivity.root, message)
    }

    fun showHeader() {
        activityHomeActivity.searchLayout.toolbarLayout.show()
    }

    fun hideHeader() {
        activityHomeActivity.searchLayout.toolbarLayout.hide()
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

    fun fingerprintValidation(status: Boolean) {
        oneTimeValidation = status
    }

    fun isFingerprintValidate(): Boolean {
        return oneTimeValidation
    }

    @SuppressLint("SetTextI18n")
    fun initData() {

        homeViewModel.gethomeData().observe(this, Observer {

            it.let {
                val totalLandsold: String? = String.format(
                    getString(R.string.header),
                    it.data?.page?.mastheadSection?.totalSqftOfLandTransacted?.displayName,
                    it.data?.page?.mastheadSection?.totalSqftOfLandTransacted?.value
                )
                //it.data?.page?.mastheadSection?.totalSqftOfLandTransacted?.displayName + " " + it.data?.page?.mastheadSection?.totalSqftOfLandTransacted?.value

                val totalAmtLandSold: String? = String.format(
                    getString(R.string.header),
                    it.data?.page?.mastheadSection?.totalAmoutOfLandTransacted?.displayName,
                    it.data?.page?.mastheadSection?.totalAmoutOfLandTransacted?.value
                )

                //it.data?.page?.mastheadSection?.totalAmoutOfLandTransacted?.displayName + " " + it.data?.page?.mastheadSection?.totalAmoutOfLandTransacted?.value
                val grossWeight: String? = String.format(
                    getString(R.string.header),
                    it.data?.page?.mastheadSection?.grossWeightedAvgAppreciation?.displayName,
                    it.data?.page?.mastheadSection?.grossWeightedAvgAppreciation?.value
                )
                //it.data?.page?.mastheadSection?.grossWeightedAvgAppreciation?.displayName + " " + it.data?.page?.mastheadSection?.grossWeightedAvgAppreciation?.value
                val num_User: String? = String.format(
                    getString(R.string.header),
                    it.data?.page?.mastheadSection?.totalNumberOfUsersWhoBoughtTheLand?.displayName,
                    it.data?.page?.mastheadSection?.totalNumberOfUsersWhoBoughtTheLand?.value
                )
                //it.data?.page?.mastheadSection?.totalNumberOfUsersWhoBoughtTheLand?.displayName + " " + it.data?.page?.mastheadSection?.totalNumberOfUsersWhoBoughtTheLand?.value
                activityHomeActivity.searchLayout.rotateText.text = showHTMLText(
                    "$totalAmtLandSold           $totalLandsold    $grossWeight    $num_User"
                )
                topText = showHTMLText(
                    "$totalAmtLandSold    $totalLandsold    $grossWeight    $num_User"
                ).toString()
            }

        })
    }

    fun callNotificationApi() {
        homeViewModel.getNotification(20, 1)
            .observe(this, object : Observer<BaseResponse<NotificationResponse>> {
                override fun onChanged(it: BaseResponse<NotificationResponse>?) {
                    when (it!!.status) {

                        Status.LOADING->{
                            fragmentNotificationBottomSheetBinding.loader.isVisible= true
                            fragmentNotificationBottomSheetBinding.markAllRead.isVisible= false

                        }
                        Status.ERROR -> {
                            fragmentNotificationBottomSheetBinding.loader.isVisible= false

                        }
                        Status.SUCCESS -> {
                            fragmentNotificationBottomSheetBinding.loader.isVisible= false
                            fragmentNotificationBottomSheetBinding.markAllRead.isVisible= true

                            var itemList= ArrayList<Int>()
                            for (i in 0..it?.data?.data!!.size-1){
                                if (it!!.data!!.data[i].readStatus==false){
                                    itemList.add(it?.data?.data!![i].id)
                                }else{
                                    fragmentNotificationBottomSheetBinding.markAllRead.setTextColor(resources.getColor(R.color.text_fade_color))
                                }
                            }
                            unReadNotifications= UnReadNotifications(itemList)
                            fragmentNotificationBottomSheetBinding.markAllRead.setOnClickListener(View.OnClickListener {
                                setReadStatus(unReadNotifications)
                                        bottomSheetDialog.dismiss()
                            })

                            it?.data.let {
                                it?.data
                                bottomSheetDialog.findViewById<RecyclerView>(R.id.rv)?.apply {
                                    val customAdapter = NotificationAdapter(
                                        this@HomeActivity,
                                        it!!.data,
                                        object : NotificationAdapter.ItemsClickInterface {
                                            override fun onClickItem(id: Int, posittion: Int) {
                                                var list = ArrayList<Int>()
                                                list.add(id)

                                                unReadNotifications= UnReadNotifications(list)
                                                setReadStatus(unReadNotifications)
                                                if (it!!.data[posittion]!!.notification.targetPage == 1) {

                                                    bottomSheetDialog.dismiss()
                                                }
                                                else if  (it!!.data[posittion]!!.notification.targetPage == 2) {
                                                    val bundle = Bundle()
                                                    val latestUpdatesFragment =
                                                        LatestUpdatesFragment()
                                                    latestUpdatesFragment.arguments = bundle
                                                    replaceFragment(
                                                        latestUpdatesFragment.javaClass,
                                                        "",
                                                        true,
                                                        bundle,
                                                        null,
                                                        0,
                                                        false
                                                    )
                                                    bottomSheetDialog.dismiss()

                                                }
                                                else if  (it!!.data[posittion]!!.notification.targetPage == 3) {
                                                    val bundle = Bundle()
                                                    val insightsFragment = InsightsFragment()
                                                    insightsFragment.arguments = bundle
                                                    replaceFragment(
                                                        insightsFragment.javaClass,
                                                        "",
                                                        true,
                                                        bundle,
                                                        null,
                                                        0,
                                                        false
                                                    )
                                                    bottomSheetDialog.dismiss()

                                                }
                                                else if  (it!!.data[posittion]!!.notification.targetPage == 4) {

                                                    (this@HomeActivity).navigate(R.id.navigation_investment)
                                                    bottomSheetDialog.dismiss()

                                                }
                                                else if (it!!.data[posittion]!!.notification.targetPage == 5) {
                                                    (this@HomeActivity).navigate(R.id.navigation_portfolio)
                                                    bottomSheetDialog.dismiss()

                                                }
                                                else if  (it!!.data[posittion]!!.notification.targetPage == 6) {
                                                    (this@HomeActivity).navigate(R.id.navigation_promises)
                                                    bottomSheetDialog.dismiss()

                                                }
                                                else if  (it!!.data[posittion]!!.notification.targetPage == 7) {
                                                    (this@HomeActivity).navigate(R.id.navigation_profile)
                                                    bottomSheetDialog.dismiss()
                                                }
                                                else{
                                                    bottomSheetDialog.dismiss()
                                                }
                                            }

                                        })
                                    layoutManager =
                                        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                                    adapter = customAdapter
                                }


                            }
                        }
                    }
                }

            })
    }

    fun setReadStatus(ids: UnReadNotifications){
        homeViewModel.setReadStatus(ids).observe(
            this@HomeActivity,
            object :
                Observer<BaseResponse<ReadNotificationReponse>> {
                override fun onChanged(it: BaseResponse<ReadNotificationReponse>?) {
                    when (it!!.status) {
                        Status.LOADING ->{
                        }
                        Status.SUCCESS -> {
                            callNotificationApi()                        }
                        Status.ERROR -> {
                            Log.i("error", it.message.toString())

                        }

                    }
                }

            })
    }

    fun share_app() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "The House Of Abhinandan Lodha $appURL")
        startActivity(shareIntent)
    }

    override fun onResume() {
        super.onResume()
        hideSoftKeyboard()
    }

}