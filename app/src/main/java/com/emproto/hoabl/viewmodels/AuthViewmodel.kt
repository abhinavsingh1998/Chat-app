package com.emproto.hoabl.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.emproto.hoabl.repository.AuthRepository
import com.emproto.networklayer.request.OtpRequest
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.OtpResponse

class AuthViewmodel(var mapplication: Application, var mauthRepository: AuthRepository) :
    ViewModel() {

    var application: Application = mapplication
    var authRepository: AuthRepository = mauthRepository


    fun getOtp(otpRequest: OtpRequest): LiveData<BaseResponse<OtpResponse>> {
        return authRepository.getOtpOnMobile(otpRequest);
    }

}