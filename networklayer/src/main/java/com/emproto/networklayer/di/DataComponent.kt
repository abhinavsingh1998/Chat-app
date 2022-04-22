package com.emproto.networklayer.di

import com.emproto.networklayer.di.DataAppModule
import com.emproto.networklayer.di.DataModule
import com.emproto.networklayer.feature.RegistrationDataSource
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataAppModule::class, DataModule::class])
interface DataComponent {
    fun inject(registrationDataSource: RegistrationDataSource)
}
