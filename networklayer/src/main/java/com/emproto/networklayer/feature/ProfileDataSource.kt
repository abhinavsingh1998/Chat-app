package com.emproto.networklayer.feature

import android.app.Application
import com.emproto.networklayer.ApiService
import com.emproto.networklayer.di.DaggerDataComponent
import com.emproto.networklayer.di.DataAppModule
import com.emproto.networklayer.di.DataComponent
import com.emproto.networklayer.di.DataModule
import com.emproto.networklayer.request.login.profile.EditUserNameRequest
import com.emproto.networklayer.request.login.profile.UploadProfilePictureRequest
import com.emproto.networklayer.request.profile.FeedBackRequest
import com.emproto.networklayer.response.chats.ChatDetailResponse
import com.emproto.networklayer.response.chats.ChatInitiateRequest
import com.emproto.networklayer.response.profile.CitiesResponse
import com.emproto.networklayer.response.profile.*
import com.emproto.networklayer.response.resourceManagment.ProflieResponse
import com.emproto.networklayer.response.terms.TermsConditionResponse
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

    suspend fun editProfile(editUserNameRequest: EditUserNameRequest): Response<EditProfileResponse> {
        return apiService.addUserName(editUserNameRequest)
    }

    //    fun uploadDocument(
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

    suspend fun presignedUrl(type: String, destinationFile: File): Response<PresignedUrlResponse> {
        return apiService.presignedUrl(type, destinationFile)
    }
    suspend fun getCountry(pageType: Int): Response<ProfileCountriesResponse> {
        return apiService.getCountryList(pageType)
    }

    suspend fun getStates(countryIsoCode: String): Response<StatesResponse> {
        return apiService.getStates(countryIsoCode)
    }

    suspend fun getCities(stateIsoCode: String, countryIsoCode: String): Response<CitiesResponse> {
        return apiService.getCities(stateIsoCode, countryIsoCode)
    }

    suspend fun shareFeedBack(feedBackRequest: FeedBackRequest): Response<FeedBackResponse>{
        return apiService.submitFeedback(feedBackRequest)
    }

    suspend fun getPrivacyAndPolicy(pageType: Int): Response<TermsConditionResponse> {
        return apiService.getTermscondition(pageType)
    }

    suspend fun getAboutHobal(pageType: Int): Response<ProflieResponse>{
        return apiService.getAboutHobal(pageType)
    }

    suspend fun getFaqList(typeOfFAQ: String): Response<ProfileFaqResponse> {
        return apiService.getFaqList(typeOfFAQ)
    }
}