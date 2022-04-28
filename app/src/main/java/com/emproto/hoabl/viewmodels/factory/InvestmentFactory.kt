package com.emproto.hoabl.viewmodels.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emproto.hoabl.repository.InvestmentRepository
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import javax.inject.Inject


class InvestmentFactory @Inject constructor(private var application: Application, private var investmentRepository: InvestmentRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InvestmentViewModel::class.java)) {
            return InvestmentViewModel(application, investmentRepository) as T
        }

        throw IllegalArgumentException("Viewmodel is not valid")
    }
}