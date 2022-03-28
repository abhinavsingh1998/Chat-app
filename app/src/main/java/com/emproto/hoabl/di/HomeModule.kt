package com.emproto.hoabl.di

import android.app.Application
import dagger.Module
import dagger.Provides

@Module
class HomeModule(application: Application?) {

    private var application: Application? = null

    init {
        this.application = application
    }

   /* @Provides
    fun getApplication(): Application? {
        return application
    }
*/

}
