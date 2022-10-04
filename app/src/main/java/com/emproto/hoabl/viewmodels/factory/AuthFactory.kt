package com.emproto.hoabl.viewmodels.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emproto.hoabl.repository.AuthRepository
import com.emproto.hoabl.viewmodels.AuthViewmodel
import javax.inject.Inject


class AuthFactory @Inject constructor(application: Application, authRepository: AuthRepository) :
    ViewModelProvider.Factory {

    var application: Application = application
    var authRepository: AuthRepository = authRepository

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewmodel::class.java)) {
            return AuthViewmodel(application, authRepository) as T
        }

        throw IllegalArgumentException("Viewmodel is not valid")
    }
}