package com.emproto.networklayer.feature

import android.app.Application
import com.emproto.networklayer.di.DataAppModule
import com.emproto.networklayer.di.DataComponent
import com.emproto.networklayer.di.DataModule
import com.emproto.networklayer.ApiService
import com.emproto.networklayer.di.DaggerDataComponent
import com.emproto.networklayer.request.login.AddNameRequest
import com.emproto.networklayer.request.login.OtpRequest
import com.emproto.networklayer.request.login.OtpVerifyRequest
import com.emproto.networklayer.request.login.TroubleSigningRequest
import com.emproto.networklayer.response.home.HomeResponse
import com.emproto.networklayer.response.login.AddNameResponse
import com.emproto.networklayer.response.login.OtpResponse
import com.emproto.networklayer.response.login.TroubleSigningResponse
import com.emproto.networklayer.response.login.VerifyOtpResponse
import com.emproto.networklayer.response.promises.PromisesResponse
import retrofit2.Response
import javax.inject.Inject

/**
 * Home Data Source.
 * All the api in home modules
 * @property application
 */
public class HomeDataSource(val application: Application) {
    @Inject
    lateinit var apiService: ApiService
    private var dataComponent: DataComponent =
        DaggerDataComponent.builder().dataAppModule(DataAppModule(application))
            .dataModule(DataModule(application)).build()

    init {
        dataComponent.inject(this)
    }

    // home modules apis
    suspend fun getHomeData(pageType: Int): Response<HomeResponse>{
        return apiService.getHome(pageType)
    }

    //promises modules apis
    suspend fun getPromisesData(pageType: Int): Response<PromisesResponse> {
        return apiService.getPromises(pageType)
    }




}
