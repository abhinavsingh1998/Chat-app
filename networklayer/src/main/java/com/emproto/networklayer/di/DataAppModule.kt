package com.emproto.networklayer.di

import android.app.Application
import dagger.Module
import dagger.Provides

@Module
class DataAppModule(private val app: Application) {
    @Provides
    fun provideApplication(): Application {
        return app
    }
}