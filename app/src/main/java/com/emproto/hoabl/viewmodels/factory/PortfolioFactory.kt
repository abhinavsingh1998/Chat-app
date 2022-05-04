package com.emproto.hoabl.viewmodels.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emproto.hoabl.repository.PortfolioRepository
import com.emproto.hoabl.viewmodels.PortfolioViewModel
import javax.inject.Inject


class PortfolioFactory @Inject constructor(
    private var application: Application, private var portfolioRepository: PortfolioRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PortfolioViewModel::class.java)) {
            return PortfolioViewModel(application, portfolioRepository) as T
        }

        throw IllegalArgumentException("Viewmodel is not valid")
    }
}