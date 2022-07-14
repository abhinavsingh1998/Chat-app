package com.emproto.hoabl

import android.app.Application
import com.emproto.hoabl.di.*
import com.mixpanel.android.mpmetrics.MixpanelAPI

class HoablApplication : Application(), HomeComponentProvider {

    override fun onCreate() {
        super.onCreate()
        val mixPanel = MixpanelAPI.getInstance(this, getString(R.string.MIXPANEL_KEY))
        mixPanel.track("Launch")
    }

    override fun homeComponent(): HomeComponent {
        return DaggerHomeComponent.builder()
            .homeAppModule(HomeAppModule(this, applicationContext))
            .build()
    }


}