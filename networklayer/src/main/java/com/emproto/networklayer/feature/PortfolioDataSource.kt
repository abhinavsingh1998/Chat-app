package com.emproto.networklayer.feature

import android.app.Application
import com.emproto.networklayer.ApiService
import com.emproto.networklayer.di.DaggerDataComponent
import com.emproto.networklayer.di.DataAppModule
import com.emproto.networklayer.di.DataComponent
import com.emproto.networklayer.di.DataModule
import com.emproto.networklayer.request.login.TroubleSigningRequest
import com.emproto.networklayer.response.bookingjourney.BookingJourneyResponse
import com.emproto.networklayer.response.documents.DocumentsResponse
import com.emproto.networklayer.response.login.TroubleSigningResponse
import com.emproto.networklayer.response.portfolio.dashboard.PortfolioData
import com.emproto.networklayer.response.portfolio.fm.FMResponse
import com.emproto.networklayer.response.portfolio.ivdetails.InvestmentDetailsResponse
import com.emproto.networklayer.response.portfolio.prtimeline.ProjectTimelineResponse
import com.emproto.networklayer.response.watchlist.WatchlistData
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

/**
 * Portfolio Data Source.
 * All the api in portfolio modules
 * @property application
 */
class PortfolioDataSource(val application: Application) : BaseDataSource(application) {

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
    suspend fun getInvestmentDetails(
        ivId: Int,
        projectId: Int
    ): Response<InvestmentDetailsResponse> {
        return apiService.investmentDetails(ivId, projectId)
    }

    //get documents listing
    suspend fun getDocumentsListing(projectId: String): Response<DocumentsResponse> {
        return apiService.documentsList(projectId)
    }

    //get watchlist
    suspend fun getMyWatchlist(): Response<WatchlistData> {
        return apiService.getMyWatchlist()
    }

    //get project timeline
    suspend fun getProjectTimeline(id: Int): Response<ProjectTimelineResponse> {
        return apiService.getProjectTimeline(id)
    }

    //get facility managment
    suspend fun getFacilityManagment(plotId: String?, crmId: String?): Response<FMResponse> {
        return apiService.getFacilityManagment(plotId, crmId)
    }

    suspend fun getBookingJourney(investedId: Int): Response<BookingJourneyResponse> {
        return apiService.getBookingJourney(investedId)
    }

    suspend fun submitTroubleCase(troubleSigningRequest: TroubleSigningRequest): Response<TroubleSigningResponse> {
        return apiService.submitTroubleCase(troubleSigningRequest)
    }
}