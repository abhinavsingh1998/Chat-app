package com.emproto.hoabl.MVVM.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emproto.hoabl.MVVM.repositories.HomeRepository
import javax.inject.Inject


class HomeFactory @Inject constructor(application: Application,homeRepository: HomeRepository): ViewModelProvider.Factory {

    var mApplication: Application
    var mhomeRepository: HomeRepository

    init {
        mApplication=application
        mhomeRepository=homeRepository
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(mApplication, mhomeRepository) as T
        }

        throw IllegalArgumentException("Viewmodel is not valid")
    }
}