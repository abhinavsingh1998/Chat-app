package com.emproto.networklayer

import com.emproto.networklayer.request.login.profile.EditUserNameRequest
import com.emproto.networklayer.request.login.profile.UploadProfilePictureRequest
import com.emproto.networklayer.request.login.AddNameRequest
import com.emproto.networklayer.request.login.OtpRequest
import com.emproto.networklayer.request.login.OtpVerifyRequest
import com.emproto.networklayer.request.login.TroubleSigningRequest
import com.emproto.networklayer.response.documents.DocumentsResponse
import com.emproto.networklayer.response.home.HomeResponse
import com.emproto.networklayer.response.investment.FaqDetailResponse
import com.emproto.networklayer.response.investment.InvestmentPromisesResponse
import com.emproto.networklayer.response.investment.InvestmentResponse
import com.emproto.networklayer.response.investment.ProjectDetailResponse
import com.emproto.networklayer.response.login.AddNameResponse
import com.emproto.networklayer.response.login.OtpResponse
import com.emproto.networklayer.response.login.TroubleSigningResponse
import com.emproto.networklayer.response.login.VerifyOtpResponse
import com.emproto.networklayer.response.portfolio.ivdetails.PortfolioData
import com.emproto.networklayer.response.portfolio.ivdetails.ProjectDetailsResponse
import com.emproto.networklayer.response.profile.EditProfileResponse
import com.emproto.networklayer.response.profile.ProfileCountriesResponse
import com.emproto.networklayer.response.profile.ProfilePictureResponse
import com.emproto.networklayer.response.promises.PromisesResponse
import com.emproto.networklayer.response.terms.TermsConditionResponse
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

    @GET(ApiConstants.PORTFOLIO_DASHBOARD)
    suspend fun getPortfolioDashboard(): Response<PortfolioData>

    @PUT(ApiConstants.EDITPROFILE)
    suspend fun addUserName(@Body editUserNameRequest: EditUserNameRequest): Response<EditProfileResponse>

    @PUT(ApiConstants.UPLOADPROFILEPICTURE)
    suspend fun uploadPicture(@Body uploadProfilePictureRequest: UploadProfilePictureRequest): Response<ProfilePictureResponse>

    @GET(ApiConstants.COUNTRY)
    suspend fun getCountryList(@Query("pageType") pageType: Int): Response<ProfileCountriesResponse>

    @GET(ApiConstants.INVESTMENT_DETAILS)
    suspend fun investmentDetails(@Path("inventoryId") id: Int): Response<ProjectDetailsResponse>

    @GET(ApiConstants.DOC_FILTER)
    suspend fun documentsList(@Query("projectId") projectId: Int): Response<DocumentsResponse>

    @GET(ApiConstants.TERMS_CONDITION)
    suspend fun getTermscondition(@Query("pageType") pageType: Int): Response<TermsConditionResponse>


    @GET(ApiConstants.INVESTMENT_PROMISES)
    suspend fun getInvestmentsPromises(): Response<InvestmentPromisesResponse>

    @GET(ApiConstants.INVESTMENT_PROJECT_FAQ)
    suspend fun getInvestmentsProjectFaq(@Path("projectContentId") projectContentId: Int): Response<FaqDetailResponse>
}