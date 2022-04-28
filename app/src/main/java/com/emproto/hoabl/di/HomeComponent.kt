package com.emproto.hoabl.di

import com.emproto.hoabl.HoablSplashActivity
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.home.views.fragments.HomeFragment
import com.emproto.hoabl.feature.investment.views.InvestmentFragment
import com.emproto.hoabl.feature.login.*
import com.emproto.hoabl.feature.profile.ProfileFragment
import com.emproto.hoabl.feature.promises.HoabelPromises
import com.emproto.hoabl.feature.promises.PromisesDetailsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [HomeAppModule::class])
interface HomeComponent {
    fun inject(activity: AuthActivity)
    fun inject(activity: HomeActivity)
    fun inject(homeFragment: HomeFragment)
    fun inject(loginFragment: LoginFragment)
    fun inject(fragment: OTPVerificationFragment)
    fun inject(fragment: NameInputFragment)
    fun inject(activity: HoablSplashActivity)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: HoabelPromises)
    fun inject(fragment: PromisesDetailsFragment)
    fun inject(fragment: InvestmentFragment)
}