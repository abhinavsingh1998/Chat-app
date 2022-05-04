package com.emproto.networklayer

import com.emproto.networklayer.request.login.AddNameRequest
import com.emproto.networklayer.request.login.OtpRequest
import com.emproto.networklayer.request.login.OtpVerifyRequest
import com.emproto.networklayer.request.login.TroubleSigningRequest
import com.emproto.networklayer.response.home.HomeResponse
import com.emproto.networklayer.response.investment.InvestmentResponse
import com.emproto.networklayer.response.investment.ProjectDetailResponse
import com.emproto.networklayer.response.investment.ProjectIdResponse
import com.emproto.networklayer.response.login.AddNameResponse
import com.emproto.networklayer.response.login.OtpResponse
import com.emproto.networklayer.response.login.TroubleSigningResponse
import com.emproto.networklayer.response.login.VerifyOtpResponse
import com.emproto.networklayer.response.promises.PromisesResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * @author Hoabl.
 * ApiService.
 * Mention all end point of all different modules.
 */
public interface ApiService {

    //auth-apis(all login module apis)
    @POST(ApiConstants.GENERATE_OTP)
    suspend fun getOtp(@Body otpRequest: OtpRequest): Response<OtpResponse>

    @PUT(ApiConstants.VALIDATE_OTP)
    suspend fun verifyOtp(@Body verifyOtpRequest: OtpVerifyRequest): Response<VerifyOtpResponse>

    @PUT(ApiConstants.SET_NAME)
    suspend fun addName(@Body addNameRequest: AddNameRequest): Response<AddNameResponse>

    @POST(ApiConstants.TROUBLE_SIGNING)
    suspend fun submitTroubleCase(@Body troubleSigningRequest: TroubleSigningRequest): Response<TroubleSigningResponse>

    @GET(ApiConstants.PROMISES)
    suspend fun getPromises(@Query("pageType") pageType: Int): Response<PromisesResponse>

    @GET(ApiConstants.INVESTMENT)
    suspend fun getInvestments(@Query("pageType") pageType: Int): Response<InvestmentResponse>

    @GET(ApiConstants.HOME)
    suspend fun getHome(@Query("pageType") pageType: Int): Response<HomeResponse>

    @GET(ApiConstants.INVESTMENT_PROJECT_DETAIL)
    suspend fun getInvestmentsProjectDetails(@Path("id") id: Int): Response<ProjectDetailResponse>

}