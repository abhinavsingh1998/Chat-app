package com.emproto.hoabl.viewmodels.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emproto.hoabl.repository.HomeRepository
import com.emproto.hoabl.viewmodels.HomeViewModel
import javax.inject.Inject


class HomeFactory @Inject constructor(application: Application, homeRepository: HomeRepository) :
    ViewModelProvider.Factory {

    var application: Application = application
    var homeRepository: HomeRepository = homeRepository

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(application, homeRepository) as T
        }

        throw IllegalArgumentException("Viewmodel is not valid")
    }
}