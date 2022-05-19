package com.emproto.hoabl.di

import android.window.SplashScreen
import com.emproto.hoabl.HoablSplashActivity
import com.emproto.hoabl.feature.chat.views.fragments.ChatsFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.home.views.fragments.*
import com.emproto.hoabl.feature.investment.views.InvestmentFragment
import com.emproto.hoabl.feature.login.*
import com.emproto.hoabl.feature.investment.views.FaqDetailFragment
import com.emproto.hoabl.feature.investment.views.LandSkusFragment
import com.emproto.hoabl.feature.investment.views.ProjectDetailFragment
import com.emproto.hoabl.feature.portfolio.views.PortfolioExistingUsersFragment
import com.emproto.hoabl.feature.portfolio.views.PortfolioFragment
import com.emproto.hoabl.feature.portfolio.views.PortfolioSpecificProjectView
import com.emproto.hoabl.feature.portfolio.views.ProjectTimelineFragment
import com.emproto.hoabl.feature.profile.EditProfileFragment
import com.emproto.hoabl.feature.profile.ProfileFragment
import com.emproto.hoabl.feature.promises.HoablPromises
import com.emproto.hoabl.feature.promises.PromisesDetailsFragment
import com.emproto.hoabl.notification.MyFirebasemessagingService
import com.emproto.networklayer.response.home.LatestUpdates
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
    fun inject(fragment: HoablPromises)
    fun inject(fragment: PromisesDetailsFragment)
    fun inject(fragment: InvestmentFragment)
    fun inject(fragment: ProjectDetailFragment)
    fun inject(fragment: PortfolioFragment)
    fun inject(fragment: PortfolioExistingUsersFragment)
    fun inject(service: MyFirebasemessagingService)
    fun inject(fragment: PortfolioSpecificProjectView)
    fun inject(fragment: EditProfileFragment)
    fun inject(fragment: ProjectTimelineFragment)
    fun inject(fragment: FaqDetailFragment)
    fun inject(fragment: LandSkusFragment)
    fun inject(fragment: Testimonials)
    fun inject(fragment: InsightsDetailsFragment)
    fun inject(fragment: LatestUpdatesDetailsFragment)
    fun inject(fragment: LatestUpdatesFragment)
    fun inject(fragment: InsightsFragment)
    fun inject(fragment: SplashScreen)
    fun inject(fragment: ChatsFragment)

}