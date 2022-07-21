package com.emproto.networklayer.feature

import android.app.Application
import com.emproto.networklayer.di.DataAppModule
import com.emproto.networklayer.di.DataComponent
import com.emproto.networklayer.di.DataModule
import com.emproto.networklayer.di.DaggerDataComponent
import com.emproto.networklayer.ApiService
import com.emproto.networklayer.request.chat.SendMessageBody
import com.emproto.networklayer.response.HomeActionItemResponse
import com.emproto.networklayer.response.actionItem.HomeActionItem
import com.emproto.networklayer.response.chats.*
import com.emproto.networklayer.response.documents.DocumentsResponse
import com.emproto.networklayer.response.home.HomeResponse
import com.emproto.networklayer.response.insights.InsightsResponse
import com.emproto.networklayer.response.investment.AllProjectsResponse
import com.emproto.networklayer.response.marketingUpdates.LatestUpdatesResponse
import com.emproto.networklayer.response.notification.dataResponse.NotificationResponse
import com.emproto.networklayer.response.notification.readStatus.ReadNotificationReponse
import com.emproto.networklayer.response.portfolio.fm.FMResponse
import com.emproto.networklayer.response.profile.AccountsResponse
import com.emproto.networklayer.response.promises.PromisesResponse
import com.emproto.networklayer.response.search.SearchResponse
import com.emproto.networklayer.response.testimonials.TestimonialsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Named

/**
 * Home Data Source.
 * All the api in home modules
 * @property application
 */
public class HomeDataSource(val application: Application) : BaseDataSource(application) {
    @Inject
    lateinit var apiService: ApiService

    @Named("dummy")

    private var dataComponent: DataComponent =
        DaggerDataComponent.builder().dataAppModule(DataAppModule(application))
            .dataModule(DataModule(application)).build()

    init {
        dataComponent.inject(this)
    }

    // home modules apis
    suspend fun getDashboardData(pageType: Int): Response<HomeResponse> {
        return apiService.getDashboardData(pageType)
    }

    // all Latest udates modules apis
    suspend fun getLatestUpdatesData(byPrority: Boolean): Response<LatestUpdatesResponse> {
        return apiService.getLatestUpdates(byPrority)
    }

    suspend fun getInsightsData(byPrority: Boolean): Response<InsightsResponse> {
        return apiService.getInsightsData(byPrority)
    }

    suspend fun getTestimonialsData(): Response<TestimonialsResponse> {
        return apiService.getTestimonials()
    }

    //promises modules apis
    suspend fun getPromisesData(pageType: Int): Response<PromisesResponse> {
        return apiService.getPromises(pageType)
    }

    //get all investments
    suspend fun getAllInvestments(): Response<AllProjectsResponse> {
        return apiService.getAllInvestmentProjects()
    }

    //get facility managment
    suspend fun getFacilityManagment(): Response<FMResponse> {
        return apiService.getFacilityManagment("", "", true)
    }

    //chats list api
    suspend fun getChatsList(): Response<ChatResponse> {
        return apiService.getChatsList()
    }

    //chats initiate api
    suspend fun chatInitiate(@Body chatInitiateRequest: ChatInitiateRequest): Response<ChatDetailResponse> {
        return apiService.chatInitiate(chatInitiateRequest)
    }

    //chats history api
    suspend fun getChatHistory(projectId: String, isInvested:Boolean): Response<ChatHistoryResponse> {
        return apiService.getChatHistory(projectId, isInvested)
    }

    //send message api
    suspend fun sendMessage(sendMessageBody: SendMessageBody): Response<SendMessageResponse> {
        return apiService.sendMessage(sendMessageBody)
    }

    //search api
    suspend fun getSearchResults(searchWord: String): Response<SearchResponse> {
        return apiService.getSearchResults(searchWord)
    }

    //search doc api
    suspend fun getSearchDocResults(): Response<DocumentsResponse> {
        return apiService.getSearchDocResults()
    }

    suspend fun getSearchDocResultsQuery(searchWord: String): Response<DocumentsResponse> {
        return apiService.getSearchDocResultsQuery(searchWord)
    }


    suspend fun getAccountsList(): Response<AccountsResponse> {
        return apiService.getAccountsList()
    }

    suspend fun getActionItem():Response<HomeActionItem>{
        return apiService.getActionItem()
    }

    suspend fun getNotificationList(size:Int, index:Int):Response<NotificationResponse>{
        return apiService.getNotificationList(size, index)
    }

    suspend fun setReadStatus(ids:List<String>):Response<ReadNotificationReponse>{
        return apiService.setReadStatus(ids)
    }
}
