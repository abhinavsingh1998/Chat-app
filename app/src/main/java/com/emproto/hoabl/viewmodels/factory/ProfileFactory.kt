package com.emproto.hoabl.viewmodels.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emproto.hoabl.repository.ProfileRepository
import com.emproto.hoabl.viewmodels.ProfileViewModel
import javax.inject.Inject


class ProfileFactory @Inject constructor(
    application: Application, profileRepository: ProfileRepository
) : ViewModelProvider.Factory {

    var application: Application = application
    var profileRepository: ProfileRepository = profileRepository


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(application, profileRepository) as T
        }

        throw IllegalArgumentException("Viewmodel is not valid")
    }
}
