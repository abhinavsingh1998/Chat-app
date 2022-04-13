package com.emproto.hoabl

import android.app.Application
import com.emproto.hoabl.di.*

class HoablApplication : Application(), HomeComponentProvider {

    override fun homeComponent(): HomeComponent {
        return DaggerHomeComponent.builder()
            .homeAppModule(HomeAppModule(this, applicationContext))
            .build()
    }


}