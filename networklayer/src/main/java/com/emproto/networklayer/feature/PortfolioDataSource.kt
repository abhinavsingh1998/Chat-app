package com.emproto.networklayer.feature

import android.app.Application
import com.emproto.networklayer.ApiService
import com.emproto.networklayer.di.DaggerDataComponent
import com.emproto.networklayer.di.DataAppModule
import com.emproto.networklayer.di.DataComponent
import com.emproto.networklayer.di.DataModule
import com.emproto.networklayer.response.documents.DocumentsResponse
import com.emproto.networklayer.response.portfolio.dashboard.PortfolioData
import com.emproto.networklayer.response.portfolio.ivdetails.InvestmentDetailsResponse
import com.emproto.networklayer.response.watchlist.WatchlistData
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

/**
 * Portfolio Data Source.
 * All the api in portfolio modules
 * @property application
 */
class PortfolioDataSource(val application: Application) {

    @Inject
    lateinit var apiService: ApiService

    @Named("dummy")
    @Inject
    lateinit var apiService2: ApiService
    private var dataComponent: DataComponent =
        DaggerDataComponent.builder().dataAppModule(DataAppModule(application))
            .dataModule(DataModule(application)).build()

    init {
        dataComponent.inject(this)
    }

    // get portfolio dashboard data
    suspend fun getPortfolioDashboard(): Response<PortfolioData> {
        return apiService.getPortfolioDashboard()
    }

    //get investment details
    suspend fun getInvestmentDetails(ivId: Int, projectId: Int): Response<InvestmentDetailsResponse> {
        return apiService.investmentDetails(ivId, projectId)
    }

    //get documents listing
    suspend fun getDocumentsListing(projectId: Int): Response<DocumentsResponse> {
        return apiService.documentsList(projectId)
    }

    //get watchlist
    suspend fun getMyWatchlist(): Response<WatchlistData> {
        return apiService.getMyWatchlist()
    }


}