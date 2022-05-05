package com.emproto.hoabl.viewmodels

import com.emproto.networklayer.response.investment.InvestmentResponse
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.emproto.hoabl.repository.InvestmentRepository
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.investment.ProjectDetailResponse
import com.emproto.networklayer.response.investment.ProjectIdResponse

class InvestmentViewModel(private var mapplication: Application, private var investmentRepository: InvestmentRepository) :ViewModel(){

    fun getInvestments(pageType: Int): LiveData<BaseResponse<InvestmentResponse>> {
        return investmentRepository.getInvestments(pageType)
    }

    fun getInvestmentsDetail(id: Int): LiveData<BaseResponse<ProjectDetailResponse>> {
        return investmentRepository.getInvestmentsDetail(id)
    }
}