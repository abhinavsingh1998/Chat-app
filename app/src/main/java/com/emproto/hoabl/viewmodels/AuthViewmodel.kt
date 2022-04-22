package com.emproto.hoabl.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.emproto.hoabl.repository.AuthRepository
import com.emproto.networklayer.request.login.AddNameRequest
import com.emproto.networklayer.request.login.OtpRequest
import com.emproto.networklayer.request.login.OtpVerifyRequest
import com.emproto.networklayer.request.login.TroubleSigningRequest
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.login.AddNameResponse
import com.emproto.networklayer.response.login.OtpResponse
import com.emproto.networklayer.response.login.TroubleSigningResponse
import com.emproto.networklayer.response.login.VerifyOtpResponse

class AuthViewmodel(private var mapplication: Application, private var mauthRepository: AuthRepository) :
    ViewModel() {

    private var application: Application = mapplication
    private var authRepository: AuthRepository = mauthRepository


    fun getOtp(otpRequest: OtpRequest): LiveData<BaseResponse<OtpResponse>> {
        return authRepository.getOtpOnMobile(otpRequest);
    }

    fun verifyOtp(otpVerifyRequest: OtpVerifyRequest): LiveData<BaseResponse<VerifyOtpResponse>> {
        return authRepository.verifyOtp(otpVerifyRequest)
    }

    fun addUsernameDetails(addNameRequest: AddNameRequest): LiveData<BaseResponse<AddNameResponse>> {
        return authRepository.addUsernameDetails(addNameRequest)
    }

    fun submitTroubleCase(signingRequest: TroubleSigningRequest): LiveData<BaseResponse<TroubleSigningResponse>> {
        return authRepository.submitTroubleCase(signingRequest)
    }

}