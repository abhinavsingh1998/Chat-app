package com.emproto.hoabl.di

import com.emproto.hoabl.HoablSplashActivity
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.login.LoginFragment
import com.emproto.hoabl.feature.home.views.fragments.HomeFragment
import com.emproto.hoabl.feature.login.NameInputFragment
import com.emproto.hoabl.feature.login.OTPVerificationFragment
import com.emproto.hoabl.feature.login.SigninIssueFragment
import com.emproto.hoabl.feature.profile.ProfileFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [HomeAppModule::class])
interface HomeComponent {
    fun inject(activity: HomeActivity)
    fun inject(homeFragment: HomeFragment)
    fun inject(loginFragment: LoginFragment)
    fun inject(fragment: OTPVerificationFragment)
    fun inject(fragment: NameInputFragment)
    fun inject(activity: HoablSplashActivity)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: SigninIssueFragment)
}