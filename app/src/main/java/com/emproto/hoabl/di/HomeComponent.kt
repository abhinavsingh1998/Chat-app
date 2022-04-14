package com.emproto.hoabl.di

import com.emproto.hoabl.HomeActivity
import com.emproto.hoabl.feature.login.LoginFragment
import com.emproto.hoabl.feature.home.views.fragments.HomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ HomeAppModule::class])
interface HomeComponent {
    fun inject(activity: HomeActivity)
    fun inject(homeFragment: HomeFragment)
    fun inject(loginFragment: LoginFragment)
}