package com.emproto.hoabl.di

import android.app.Application
import android.content.Context
import com.emproto.core.Database.Dao.HomeSearchDao
import com.emproto.core.Database.HoablAppDataBase
import com.emproto.hoabl.feature.home.views.Mixpanel
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.preferences.AppPreferenceImp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HomeAppModule(var application: Application, var context: Context) {

    @Provides
    @Singleton
    fun getSearchDao(appDataBase: HoablAppDataBase): HomeSearchDao {
        return appDataBase.homeSearchDao()
    }


    @Provides
    @Singleton
    fun getRoomDbInstance(): HoablAppDataBase {
        return HoablAppDataBase.getAppDataBaseInstance(provideApplication())!!
    }

    @Provides
    fun provideApplication(): Application {
        return application
    }

    @Provides
    fun provideContext(): Context {
        return context
    }

    @Provides
    @Singleton
    fun getAppPreference(): AppPreference {
        return AppPreferenceImp(context)
    }

    @Provides
    @Singleton
    fun getMixPanel():Mixpanel{
        return Mixpanel(context)
    }

}