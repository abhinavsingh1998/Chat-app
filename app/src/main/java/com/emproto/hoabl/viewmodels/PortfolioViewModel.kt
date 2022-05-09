package com.emproto.hoabl.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.emproto.hoabl.repository.PortfolioRepository
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.documents.DocumentsResponse
import com.emproto.networklayer.response.portfolio.ivdetails.PortfolioData
import com.emproto.networklayer.response.portfolio.ivdetails.ProjectDetailsResponse

class PortfolioViewModel(
    private var mapplication: Application,
    private var portfolioRepository: PortfolioRepository
) : ViewModel() {

    fun getPortfolioDashboard(): LiveData<BaseResponse<PortfolioData>> {
        return portfolioRepository.getPortfolioDashboard()
    }

    fun getInvestmentDetails(crmId: Int): LiveData<BaseResponse<ProjectDetailsResponse>> {
        return portfolioRepository.getInvestmentDetails(crmId)
    }

    fun getDocumentList(projectId: Int): LiveData<BaseResponse<DocumentsResponse>> {
        return portfolioRepository.getDocumentsListing(projectId)
    }
}