package com.emproto.networklayer.feature

import android.app.Application
import com.emproto.networklayer.ApiService
import com.emproto.networklayer.di.DaggerDataComponent
import com.emproto.networklayer.di.DataAppModule
import com.emproto.networklayer.di.DataComponent
import com.emproto.networklayer.di.DataModule
import com.emproto.networklayer.request.login.TroubleSigningRequest
import com.emproto.networklayer.request.login.profile.EditUserNameRequest
import com.emproto.networklayer.request.profile.FeedBackRequest
import com.emproto.networklayer.request.profile.ReportSecurityRequest
import com.emproto.networklayer.response.investment.FaqDetailResponse
import com.emproto.networklayer.response.login.TroubleSigningResponse
import com.emproto.networklayer.response.portfolio.fm.FMResponse
import com.emproto.networklayer.response.profile.CitiesResponse
import com.emproto.networklayer.response.profile.*
import com.emproto.networklayer.response.resourceManagment.ProflieResponse
import com.emproto.networklayer.response.terms.TermsConditionResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class ProfileDataSource(val application: Application) : BaseDataSource(application) {

    @Inject
    lateinit var apiService: ApiService
    private var dataComponent: DataComponent =
        DaggerDataComponent.builder().dataAppModule(DataAppModule(application))
            .dataModule(DataModule(application)).build()

    init {
        dataComponent.inject(this)
    }

//    profile modules apis


    suspend fun getUserProfile(): Response<ProfileResponse> {
        return apiService.getUserProfile()
    }

    suspend fun deleteProfilePic(): Response<EditProfileResponse> {
        return apiService.deleteProfilePic()
    }

    suspend fun deleteProfileImage(destinationFileName: String): Response<EditProfileResponse> {
        return apiService.deleteProfileImage(destinationFileName)
    }

    suspend fun editProfile(editUserNameRequest: EditUserNameRequest): Response<EditProfileResponse> {
        return apiService.addUserName(editUserNameRequest)
    }

    suspend fun putWhatsappConsent(whatsappConsentBody: com.emproto.networklayer.request.profile.WhatsappConsentBody): Response<WhatsappConsentResponse> {
        return apiService.putWhatsappConsent(whatsappConsentBody)
    }

    //        fun uploadDocument(
//        token: String?,
//        document: File,
//        documentType: String?
//    ): org.graalvm.compiler.nodes.memory.MemoryCheckpoint.Single<DocumentResponse?>? {
//        return apiService.uploadDocument(
//            token,
//            MultipartBody.Part.createFormData(
//                "image", document.name,
//                MultipartBody.create(MediaType.parse("image/*"), document)
//            ),
//            MultipartBody.Part.createFormData("documentType", documentType!!)
//        )
//    }
    suspend fun uploadPictureProfile(
        file: File,
        fileName: String

    ): Response<ProfilePictureResponse> {

        return apiService.uploadPicture(

            MultipartBody.Part.createFormData(
                "file", file.name,
                RequestBody.create("image/*".toMediaTypeOrNull(), file)
            ), MultipartBody.Part.createFormData("fileName", fileName!!)
        )
    }

    suspend fun uploadKycDocument(
        extension: String,
        file: File,
        selectedDoc: Int

    ): Response<UploadKycResponse> {
        return apiService.uploadKycDocument(
            MultipartBody.Part.createFormData("extension", extension!!), MultipartBody.Part.createFormData("file", file.name, RequestBody.create("image/*".toMediaTypeOrNull(), file)), MultipartBody.Part.createFormData("documentType", selectedDoc.toString())
        )
    }

    suspend fun presignedUrl(type: String, destinationFile: File): Response<PresignedUrlResponse> {
        return apiService.presignedUrl(type, destinationFile)
    }

    suspend fun getCountry(pageType: Int): Response<ProfileCountriesResponse> {
        return apiService.getCountryList(pageType)
    }
    suspend fun getCountries(): Response<CountryResponse> {
        return apiService.getCountries()
    }
    suspend fun getStates(countryIsoCode: String): Response<StatesResponse> {
        return apiService.getStates(countryIsoCode)
    }

    suspend fun getCities(stateIsoCode: String, countryIsoCode: String): Response<CitiesResponse> {
        return apiService.getCities(stateIsoCode, countryIsoCode)
    }

    suspend fun shareFeedBack(feedBackRequest: FeedBackRequest): Response<FeedBackResponse> {
        return apiService.submitFeedback(feedBackRequest)
    }

    suspend fun getPrivacyAndPolicy(pageType: Int): Response<TermsConditionResponse> {
        return apiService.getTermscondition(pageType)
    }

    suspend fun getAboutHobal(pageType: Int): Response<ProflieResponse> {
        return apiService.getAboutHobal(pageType)
    }

    suspend fun getFaqList(typeOfFAQ: String): Response<GeneralFaqResponse> {
        return apiService.getFaqList(typeOfFAQ)
    }

    suspend fun getAllProjects(): Response<AllProjectsResponse> {
        return apiService.getAllProjects()
    }

    suspend fun getSecurityTips(pageType: Int): Response<SecurityTipsResponse> {
        return apiService.getSecurityTips(pageType)
    }

    suspend fun getGeneralFaqs(categoryType: Int): Response<FaqDetailResponse> {
        return apiService.getGeneralFaqs(categoryType)
    }

    suspend fun submitTroubleCase(troubleSigningRequest: ReportSecurityRequest): Response<TroubleSigningResponse> {
        return apiService.reportSecurityEmergency(troubleSigningRequest)
    }

    //get facility managment
    suspend fun getFacilityManagment(): Response<FMResponse> {
        return apiService.getFacilityManagment("", "", true)
    }

}