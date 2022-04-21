package com.emproto.hoabl

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.emproto.core.BaseActivity
import com.emproto.hoabl.databinding.ActivityHoablSplashBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.login.IntroSliderActivity
import com.emproto.networklayer.preferences.AppPreference
import javax.inject.Inject

class HoablSplashActivity : BaseActivity() {

    lateinit var mBinding: ActivityHoablSplashBinding

    @Inject
    lateinit var appPreference: AppPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as HomeComponentProvider).homeComponent().inject(this)

        mBinding = ActivityHoablSplashBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        Handler().postDelayed(Runnable { // Show white background behind floating label
            if (appPreference.isUserLoggedIn()) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, IntroSliderActivity::class.java))
                finish()
            }
        }, 1000)

    }

}