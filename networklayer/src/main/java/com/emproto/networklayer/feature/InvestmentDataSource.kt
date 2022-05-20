package com.emproto.networklayer.feature

import android.app.Application
import com.emproto.networklayer.ApiService
import com.emproto.networklayer.di.DaggerDataComponent
import com.emproto.networklayer.di.DataAppModule
import com.emproto.networklayer.di.DataComponent
import com.emproto.networklayer.di.DataModule
import com.emproto.networklayer.response.investment.*
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

/**
 * Investment Data Source.
 * All the api in home modules
 * @property application
 */
class InvestmentDataSource(val application: Application) {

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

    //investment modules apis
    suspend fun getInvestmentsData(pageType: Int): Response<InvestmentResponse> {
        return apiService.getInvestments(pageType)
    }

    suspend fun getInvestmentsDetailData(id: Int): Response<ProjectDetailResponse> {
        return apiService.getInvestmentsProjectDetails(id)
    }

    suspend fun getInvestmentsPromises(): Response<InvestmentPromisesResponse> {
        return apiService.getInvestmentsPromises()
    }

    suspend fun getInvestmentsFaq(id: Int): Response<FaqDetailResponse> {
        return apiService.getInvestmentsProjectFaq(id)
    }
}