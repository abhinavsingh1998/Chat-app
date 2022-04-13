package com.emproto.networklayer

import com.emproto.networklayer.request.OtpRequest
import com.emproto.networklayer.response.LoginResponse
import com.emproto.networklayer.response.OtpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * @author Hoabl.
 * ApiService.
 * Mention all end point of all different modules.
 */
public interface ApiService {

    //auth-apis(all login module apis)
    @POST(ApiConstants.GENERATE_OTP)
    suspend fun getOtp(@Body otpRequest: OtpRequest): Response<OtpResponse>

}