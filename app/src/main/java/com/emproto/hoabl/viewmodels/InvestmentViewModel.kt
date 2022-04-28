package com.emproto.hoabl.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.emproto.hoabl.repository.InvestmentRepository
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.investment.Data
import com.emproto.networklayer.response.promises.PromisesResponse

class InvestmentViewModel(private var mapplication: Application, private var investmentRepository: InvestmentRepository) :ViewModel(){

    fun getInvestments(pageType: Int): LiveData<BaseResponse<Data>> {
        return investmentRepository.getInvestments(pageType)
    }
}