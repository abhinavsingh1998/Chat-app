package com.emproto.networklayer.feature

import android.app.Application
import com.emproto.networklayer.ApiService
import com.emproto.networklayer.di.DaggerDataComponent
import com.emproto.networklayer.di.DataAppModule
import com.emproto.networklayer.di.DataComponent
import com.emproto.networklayer.di.DataModule
import com.emproto.networklayer.response.documents.DocumentsResponse
import com.emproto.networklayer.response.investment.Data
import com.emproto.networklayer.response.portfolio.PortfolioData
import com.emproto.networklayer.response.portfolio.ivdetails.InvestmentDetails
import com.emproto.networklayer.response.promises.PromisesResponse
import retrofit2.Response
import javax.inject.Inject

/**
 * Portfolio Data Source.
 * All the api in portfolio modules
 * @property application
 */
class PortfolioDataSource(val application: Application) {

    @Inject
    lateinit var apiService: ApiService
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
    suspend fun getInvestmentDetails(crmId: Int): Response<InvestmentDetails> {
        return apiService.investmentDetails(crmId)
    }

    //get documents listing
    suspend fun getDocumentsListing(projectId: Int): Response<DocumentsResponse> {
        return apiService.documentsList(projectId)
    }


}