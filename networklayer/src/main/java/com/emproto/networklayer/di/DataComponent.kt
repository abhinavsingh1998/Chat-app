package com.emproto.datalayer.di

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataAppModule::class, DataModule::class])
interface DataComponent {
    //fun inject(registrationDataSource: RegistrationDataSource)
}
