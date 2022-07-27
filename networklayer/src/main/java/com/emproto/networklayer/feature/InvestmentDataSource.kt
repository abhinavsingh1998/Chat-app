package com.emproto.networklayer.feature

import android.app.Application
import com.emproto.networklayer.ApiService
import com.emproto.networklayer.di.DaggerDataComponent
import com.emproto.networklayer.di.DataAppModule
import com.emproto.networklayer.di.DataComponent
import com.emproto.networklayer.di.DataModule
import com.emproto.networklayer.request.investment.AddInventoryBody
import com.emproto.networklayer.request.investment.VideoCallBody
import com.emproto.networklayer.request.investment.WatchListBody
import com.emproto.networklayer.response.investment.*
import com.emproto.networklayer.response.testimonials.TestimonialsResponse
import com.emproto.networklayer.response.watchlist.WatchlistData
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

/**
 * Investment Data Source.
 * All the api in home modules
 * @property application
 */
class InvestmentDataSource(val application: Application) : BaseDataSource(application) {

    @Inject
    lateinit var apiService: ApiService


    private var dataComponent: DataComponent =
        DaggerDataComponent.builder().dataAppModule(DataAppModule(application))
            .dataModule(DataModule(application)).build()

    init {
        dataComponent.inject(this)
    }

    //get investment dashboard
    suspend fun getInvestmentsData(pageType: Int): Response<InvestmentResponse> {
        return apiService.getInvestments(pageType)
    }

    //get investment media galleries
    suspend fun getMediaGallery(projectId: Int): Response<ProjectMediaGalleryResponse> {
        return apiService.getProjectMediaGalleries(projectId)
    }

    //get investment detail
    suspend fun getInvestmentsDetailData(id: Int): Response<ProjectDetailResponse> {
        return apiService.getInvestmentsProjectDetails(id,true)
    }

    //get all investments
    suspend fun getAllInvestments(): Response<AllProjectsResponse> {
        return apiService.getAllInvestmentProjects()
    }

    //get promises
    suspend fun getInvestmentsPromises(): Response<InvestmentPromisesResponse> {
        return apiService.getInvestmentsPromises()
    }

    //get faqs
    suspend fun getInvestmentsFaq(id: Int): Response<FaqDetailResponse> {
        return apiService.getInvestmentsProjectFaq(id)
    }

    //get watchlist
    suspend fun getMyWatchlist(): Response<WatchlistData> {
        return apiService.getMyWatchlist()
    }

    //delete watchlist
    suspend fun deleteWatchlist(id: Int): Response<WatchListResponse> {
        return apiService.deleteWatchlist(id)
    }

    //add watchlist
    suspend fun addWatchList(watchListBody: WatchListBody): Response<WatchListResponse> {
        return apiService.addWatchList(watchListBody)
    }

    //get Inventories
    suspend fun getInventories(id: Int): Response<GetInventoriesResponse> {
        return apiService.getInventories(id)
    }

    //add Inventory
    suspend fun addInventory(addInventoryBody: AddInventoryBody): Response<WatchListResponse> {
        return apiService.addInventory(addInventoryBody)
    }

    //schedule video call
    suspend fun scheduleVideoCall(videoCallBody: VideoCallBody): Response<VideoCallResponse> {
        return apiService.scheduleVideoCall(videoCallBody)
    }

    //get testimonials
    suspend fun getTestimonialsData(): Response<TestimonialsResponse> {
        return apiService.getTestimonials()
    }
}