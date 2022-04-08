package com.emproto.hoabl.di

import com.emproto.hoabl.HomeActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [HomeModule::class, HomeAppModule::class])
interface HomeComponent {
    fun inject(activity: HomeActivity)
}