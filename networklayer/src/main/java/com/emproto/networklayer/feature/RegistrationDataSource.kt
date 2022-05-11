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
import com.emproto.networklayer.response.login.AddNameResponse
import com.emproto.networklayer.response.login.OtpResponse
import com.emproto.networklayer.response.login.TroubleSigningResponse
import com.emproto.networklayer.response.login.VerifyOtpResponse
import com.emproto.networklayer.response.profile.ProfileResponse
import com.emproto.networklayer.response.terms.TermsConditionResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

/**
 * RegistrationDataSource
 * All End point needed for login module to be specified here too.
 */
public class RegistrationDataSource(val application: Application) {
    @Inject
    lateinit var apiService: ApiService
    var dataComponent: DataComponent =
        DaggerDataComponent.builder().dataAppModule(DataAppModule(application))
            .dataModule(DataModule(application)).build()

    @Named("dummy")
    @Inject
    lateinit var apiService2: ApiService

    init {
        dataComponent.inject(this)
    }


    suspend fun getOtp(otpRequest: OtpRequest): Response<OtpResponse> {
        return apiService.getOtp(otpRequest);
    }

    suspend fun verifyOtp(otpVerifyRequest: OtpVerifyRequest): Response<VerifyOtpResponse> {
        return apiService.verifyOtp(otpVerifyRequest)
    }

    suspend fun addUserDetails(addNameRequest: AddNameRequest): Response<AddNameResponse> {
        return apiService.addName(addNameRequest)
    }

    suspend fun submitTroubleCase(troubleSigningRequest: TroubleSigningRequest): Response<TroubleSigningResponse> {
        return apiService.submitTroubleCase(troubleSigningRequest)
    }

    suspend fun getTermsCondition(pageType: Int): Response<TermsConditionResponse> {
        return apiService2.getTermscondition(pageType)
    }

    suspend fun getUserProfile(): Response<ProfileResponse> {
        return apiService.getUserProfile()
    }


}
