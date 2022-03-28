package com.emproto.hoabl.di

import com.emproto.hoabl.HomeActivity
import com.emproto.hoabl.feature.home.views.IntroSliderActivity
import com.emproto.hoabl.feature.home.views.fragments.HomeFragment
import com.emproto.hoabl.feature.home.views.fragments.SearchResultFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [HomeModule::class, HomeAppModule::class])
interface HomeComponent {
    //fun inject(activity: IntroSliderActivity)
    fun inject(activity: HomeActivity)
}