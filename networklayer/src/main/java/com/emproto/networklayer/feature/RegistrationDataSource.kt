package com.emproto.networklayer.feature

import android.app.Application
import com.emproto.datalayer.di.DaggerDataComponent
import com.emproto.networklayer.di.DataAppModule
import com.emproto.datalayer.di.DataComponent
import com.emproto.datalayer.di.DataModule
import com.emproto.networklayer.ApiService
import com.emproto.networklayer.request.OtpRequest
import com.emproto.networklayer.response.OtpResponse
import retrofit2.Response
import javax.inject.Inject

public class RegistrationDataSource(val application: Application) {
    @Inject
    lateinit var apiService: ApiService
    var dataComponent: DataComponent =
        DaggerDataComponent.builder().dataAppModule(DataAppModule(application))
            .dataModule(DataModule(application)).build()

    init {
        dataComponent.inject(this)
    }


    suspend fun getOtp(otpRequest: OtpRequest): Response<OtpResponse> {
        return apiService.getOtp(otpRequest);
    }


}
