package com.emproto.networklayer.feature

import android.app.Application
import com.emproto.networklayer.ApiService
import com.emproto.networklayer.di.DaggerDataComponent
import com.emproto.networklayer.di.DataAppModule
import com.emproto.networklayer.di.DataComponent
import com.emproto.networklayer.di.DataModule
import com.emproto.networklayer.response.investment.Data
import com.emproto.networklayer.response.promises.PromisesResponse
import retrofit2.Response
import javax.inject.Inject

/**
 * Investment Data Source.
 * All the api in home modules
 * @property application
 */
class InvestmentDataSource(val application: Application) {

    @Inject
    lateinit var apiService: ApiService
    private var dataComponent: DataComponent =
        DaggerDataComponent.builder().dataAppModule(DataAppModule(application))
            .dataModule(DataModule(application)).build()

    init {
        dataComponent.inject(this)
    }

    //investment modules apis
    suspend fun getInvestmentsData(pageType: Int): Response<Data> {
        return apiService.getInvestments(pageType)
    }
}