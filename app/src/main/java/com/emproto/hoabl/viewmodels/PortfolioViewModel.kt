package com.emproto.hoabl.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emproto.hoabl.repository.PortfolioRepository
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.documents.DocumentsResponse
import com.emproto.networklayer.response.portfolio.dashboard.PortfolioData
import com.emproto.networklayer.response.portfolio.ivdetails.InvestmentDetailsResponse
import com.emproto.networklayer.response.portfolio.prtimeline.ProjectTimelineResponse
import com.emproto.networklayer.response.profile.ProfileResponse
import com.emproto.networklayer.response.watchlist.WatchlistData

class PortfolioViewModel(
    private var mapplication: Application,
    private var portfolioRepository: PortfolioRepository
) : ViewModel() {

    private var portfolioData = MutableLiveData<PortfolioData>()

    fun getPortfolioDashboard(): LiveData<BaseResponse<PortfolioData>> {
        return portfolioRepository.getPortfolioDashboard()
    }

    fun setPortfolioData(data: PortfolioData) {
        portfolioData.postValue(data)
    }

    fun getPortfolioData(): LiveData<PortfolioData> {
        return portfolioData
    }

    fun getInvestmentDetails(
        ivId: Int,
        projectId: Int
    ): LiveData<BaseResponse<InvestmentDetailsResponse>> {
        return portfolioRepository.getInvestmentDetails(ivId, projectId)
    }

    fun getDocumentList(projectId: Int): LiveData<BaseResponse<DocumentsResponse>> {
        return portfolioRepository.getDocumentsListing(projectId)
    }

    fun getWatchlist(): LiveData<BaseResponse<WatchlistData>> {
        return portfolioRepository.getMyWatchlist()
    }

    fun getUserProfile(): LiveData<BaseResponse<ProfileResponse>> {
        return portfolioRepository.getUserProfile()
    }

    fun getProjectTimeline(id: Int): LiveData<BaseResponse<ProjectTimelineResponse>> {
        return portfolioRepository.getProjectTimeline(id)
    }
}