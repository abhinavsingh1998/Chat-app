package com.emproto.networklayer

import com.emproto.networklayer.request.investment.AddInventoryBody
import com.emproto.networklayer.request.investment.VideoCallBody
import com.emproto.networklayer.request.investment.WatchListBody
import com.emproto.networklayer.request.login.profile.EditUserNameRequest
import com.emproto.networklayer.request.login.profile.UploadProfilePictureRequest
import com.emproto.networklayer.request.login.AddNameRequest
import com.emproto.networklayer.request.login.OtpRequest
import com.emproto.networklayer.request.login.OtpVerifyRequest
import com.emproto.networklayer.request.login.TroubleSigningRequest
import com.emproto.networklayer.request.profile.FeedBackRequest
import com.emproto.networklayer.request.profile.ReportSecurityRequest
import com.emproto.networklayer.request.profile.WhatsappConsentBody
import com.emproto.networklayer.request.refernow.ReferalRequest
import com.emproto.networklayer.response.HomeActionItemResponse
import com.emproto.networklayer.response.actionItem.HomeActionItem
import com.emproto.networklayer.response.bookingjourney.BookingJourneyResponse
import com.emproto.networklayer.response.bookingjourney.BookingJourneyX
import com.emproto.networklayer.response.chats.ChatDetailResponse
import com.emproto.networklayer.response.chats.ChatInitiateRequest
import com.emproto.networklayer.response.chats.ChatResponse
import com.emproto.networklayer.response.ddocument.DDocumentResponse
import com.emproto.networklayer.response.documents.DocumentsResponse
import com.emproto.networklayer.response.home.HomeResponse
import com.emproto.networklayer.response.insights.InsightsResponse
import com.emproto.networklayer.response.investment.*
import com.emproto.networklayer.response.login.AddNameResponse
import com.emproto.networklayer.response.login.OtpResponse
import com.emproto.networklayer.response.login.TroubleSigningResponse
import com.emproto.networklayer.response.login.VerifyOtpResponse
import com.emproto.networklayer.response.marketingUpdates.LatestUpdatesResponse
import com.emproto.networklayer.response.portfolio.dashboard.PortfolioData
import com.emproto.networklayer.response.portfolio.fm.FMResponse
import com.emproto.networklayer.response.portfolio.ivdetails.InvestmentDetailsResponse
import com.emproto.networklayer.response.portfolio.prtimeline.ProjectTimelineResponse
import com.emproto.networklayer.response.profile.*
import com.emproto.networklayer.response.profile.AllProjectsResponse
import com.emproto.networklayer.response.promises.PromisesResponse
import com.emproto.networklayer.response.refer.ReferalResponse
import com.emproto.networklayer.response.resourceManagment.ProflieResponse
import com.emproto.networklayer.response.search.SearchResponse
import com.emproto.networklayer.response.terms.TermsConditionResponse
import com.emproto.networklayer.response.testimonials.TestimonialsResponse
import com.emproto.networklayer.response.watchlist.WatchlistData
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import java.io.File

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

    @POST(ApiConstants.TROUBLE_SIGNING)
    suspend fun reportSecurityEmergency(@Body reportSecurityRequest: ReportSecurityRequest): Response<TroubleSigningResponse>

    @GET(ApiConstants.PROMISES)
    suspend fun getPromises(@Query("pageType") pageType: Int): Response<PromisesResponse>

    @GET(ApiConstants.INVESTMENT)
    suspend fun getInvestments(@Query("pageType") pageType: Int): Response<InvestmentResponse>

    @GET(ApiConstants.PROJECT_MEDIA_GALLERIES)
    suspend fun getProjectMediaGalleries(@Path("projectContentId") projectContentId: Int): Response<ProjectMediaGalleryResponse>

    @GET(ApiConstants.HOME)
    suspend fun getDashboardData(@Query("pageType") pageType: Int): Response<HomeResponse>

    @GET(ApiConstants.LatestUpdates)
    suspend fun getLatestUpdates(@Query("byPrority") priority: Boolean = true): Response<LatestUpdatesResponse>

    @GET(ApiConstants.INSIGHTS)
    suspend fun getInsightsData(@Query("byPrority") byPrority: Boolean): Response<InsightsResponse>

    @GET(ApiConstants.TESTIMONIALS)
    suspend fun getTestimonials(): Response<TestimonialsResponse>

    @GET(ApiConstants.INVESTMENT_PROJECT_DETAIL)
    suspend fun getInvestmentsProjectDetails(@Path("id") id: Int): Response<ProjectDetailResponse>

    @GET(ApiConstants.INVESTMENT_ALL_PROJECT)
    suspend fun getAllInvestmentProjects(): Response<com.emproto.networklayer.response.investment.AllProjectsResponse>

    @GET(ApiConstants.PORTFOLIO_DASHBOARD)
    suspend fun getPortfolioDashboard(): Response<PortfolioData>

    @PUT(ApiConstants.EDITPROFILE)
    suspend fun addUserName(@Body editUserNameRequest: EditUserNameRequest): Response<EditProfileResponse>


    @Multipart
    @PUT(ApiConstants.UPLOADPROFILEPICTURE)
    suspend fun uploadPicture(
        @Part file: MultipartBody.Part,
        @Part fileName: MultipartBody.Part
    ): Response<ProfilePictureResponse>

    @Multipart
    @POST(ApiConstants.UPLOAD_DOC)
    suspend fun uploadKycDocument(
        @Part extension: MultipartBody.Part,
        @Part file: MultipartBody.Part,
        @Part selectedDoc: MultipartBody.Part
    ): Response<UploadKycResponse>

    @PUT(ApiConstants.PRESIGNEDURL)
    suspend fun presignedUrl(
        @Query("type") type: String, @Query("key") destinationFile: File
    ): Response<PresignedUrlResponse>

    @GET(ApiConstants.COUNTRY)
    suspend fun getCountryList(@Query("pageType") pageType: Int): Response<ProfileCountriesResponse>

    @GET(ApiConstants.INVESTMENT_DETAILS)
    suspend fun investmentDetails(
        @Query("investmentId") investmentId: Int,
        @Query("projectId") projectId: Int
    ): Response<InvestmentDetailsResponse>

    @GET(ApiConstants.DOC_FILTER)
    suspend fun documentsList(
        @Query("crmLaunchPhaseId") projectId: String,
        @Query("documentCategory") category: Int = 100104
    ): Response<DocumentsResponse>

    @GET(ApiConstants.TERMS_CONDITION)
    suspend fun getTermscondition(@Query("pageType") pageType: Int): Response<TermsConditionResponse>

    @GET(ApiConstants.WATCHLIST)
    suspend fun getMyWatchlist(): Response<WatchlistData>

    @DELETE(ApiConstants.DELETE_WATCHLIST)
    suspend fun deleteWatchlist(@Path("id") id: Int): Response<WatchListResponse>

    @GET(ApiConstants.GET_PROFILE)
    suspend fun getUserProfile(): Response<ProfileResponse>

    @DELETE(ApiConstants.UPLOADPROFILEPICTURE)
    suspend fun deleteProfilePic(): Response<EditProfileResponse>

    @DELETE(ApiConstants.DELETE_PROFILE)
    suspend fun deleteProfileImage(@Path("key") destinationFileName: String): Response<EditProfileResponse>

    @GET(ApiConstants.PROJECT_TIMELINE)
    suspend fun getProjectTimeline(@Path("id") id: Int): Response<ProjectTimelineResponse>

    @GET(ApiConstants.FACILITY_MANAGMENT)
    suspend fun getFacilityManagment(
        @Query("plotNumber") plotNumber: String,
        @Query("crmProjectId") projectId: String,
        @Query("isTest") isTest: Boolean = true
    ): Response<FMResponse>

    @GET(ApiConstants.INVESTMENT_PROMISES)
    suspend fun getInvestmentsPromises(): Response<InvestmentPromisesResponse>

    @GET(ApiConstants.INVESTMENT_PROJECT_FAQ)
    suspend fun getInvestmentsProjectFaq(@Path("projectContentId") projectContentId: Int): Response<FaqDetailResponse>

    @POST(ApiConstants.REFER_NOW)
    suspend fun referNow(@Body referBody: ReferalRequest): Response<ReferalResponse>

    @GET(ApiConstants.COUNTRIES)
    suspend fun getCountries(): Response<CountryResponse>

    @GET(ApiConstants.STATES)
    suspend fun getStates(@Path("countryIsoCode") countryIsoCode: String): Response<StatesResponse>

    @GET(ApiConstants.CITIES)
    suspend fun getCities(
        @Query("stateIsoCode") stateIsoCode: String,
        @Query("countryIsoCode") countryIsoCode: String
    ): Response<CitiesResponse>

    @POST(ApiConstants.WATCHLIST)
    suspend fun addWatchList(@Body watchListBody: WatchListBody): Response<WatchListResponse>

    @GET(ApiConstants.PROJECT_INVENTORIES)
    suspend fun getInventories(@Path("id") id: Int): Response<GetInventoriesResponse>

    @POST(ApiConstants.ADD_INVENTORY)
    suspend fun addInventory(@Body addInventoryBody: AddInventoryBody): Response<WatchListResponse>

    @POST(ApiConstants.VIDEO_CALL)
    suspend fun scheduleVideoCall(@Body videoCallBody: VideoCallBody): Response<VideoCallResponse>

    @GET(ApiConstants.DOCUMENT_DOWNLOAD)
    suspend fun downloadDocument(@Query("path") path: String): Response<DDocumentResponse>

    @GET(ApiConstants.CHATS_LIST)
    suspend fun getChatsList(): Response<ChatResponse>

    @PUT(ApiConstants.CHATS_INITIATE)
    suspend fun chatInitiate(@Body chatInitiateRequest: ChatInitiateRequest): Response<ChatDetailResponse>

    @GET(ApiConstants.PROFILE_FAQ)
    suspend fun getFaqList(@Query("typeOfFAQ") typeOfFAQ: String): Response<GeneralFaqResponse>

    @GET(ApiConstants.BOOKING_JOURNEY)
    suspend fun getBookingJourney(@Query("investmentId") investmentId: Int): Response<BookingJourneyResponse>

    @GET(ApiConstants.ACCOUNTS_LIST)
    suspend fun getAccountsList(): Response<AccountsResponse>

    @GET(ApiConstants.SEARCH)
    suspend fun getSearchResults(@Query("searchKey") searchWord: String): Response<SearchResponse>

    @GET(ApiConstants.SEARCH_DOCS)
    suspend fun getSearchDocResults(): Response<DocumentsResponse>

    @GET(ApiConstants.SEARCH_DOCS)
    suspend fun getSearchDocResultsQuery(@Query("searchKey") searchWord: String): Response<DocumentsResponse>

    @POST(ApiConstants.FEEDBACK)
    suspend fun submitFeedback(@Body feedBackResponse: FeedBackRequest): Response<FeedBackResponse>

    @GET(ApiConstants.PROFILE_RESOURCE)
    suspend fun getAboutHobal(@Query("pageType") pageType: Int): Response<ProflieResponse>

    @GET(ApiConstants.GET_ALL_PROJECTS)
    suspend fun getAllProjects(): Response<AllProjectsResponse>

    @PUT(ApiConstants.WHATSAPP_CONSENT)
    suspend fun putWhatsappConsent(@Body whatsappConsentBody: WhatsappConsentBody): Response<WhatsappConsentResponse>

    @GET(ApiConstants.TERMS_CONDITION)
    suspend fun getSecurityTips(@Query("pageType") pageType: Int): Response<SecurityTipsResponse>

    @GET(ApiConstants.GENERAL_FAQ)
    suspend fun getGeneralFaqs(@Query("categoryType") categoryType: Int):Response<FaqDetailResponse>

    @GET(ApiConstants.ACTION_ITEM)
    suspend fun getActionItem():Response<HomeActionItem>
}