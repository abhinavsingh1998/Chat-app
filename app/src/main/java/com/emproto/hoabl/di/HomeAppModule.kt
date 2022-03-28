package com.emproto.hoabl.di

import android.app.Application
import android.content.Context
import com.emproto.core.Database.Dao.HomeSearchDao
import com.emproto.core.Database.HoablAppDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HomeAppModule {

    private var app: Application? = null
    private var context: Context? = null

    constructor(app: Application, context: Context) {
        this.app = app
        this.context = context
    }

    @Provides
    @Singleton
    fun getSearchDao(appDataBase : HoablAppDataBase): HomeSearchDao {
        return appDataBase.homeSearchDao()
    }


    @Provides
    @Singleton
    fun getRoomDbInstance(): HoablAppDataBase {
        return HoablAppDataBase.getAppDataBaseInstance(provideApplication()!!)!!
    }

    @Provides
    fun provideApplication(): Application? {
        return app
    }

    @Provides
    fun provideContext(): Context? {
        return context
    }
}