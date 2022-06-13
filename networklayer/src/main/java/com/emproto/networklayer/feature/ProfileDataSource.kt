package com.emproto.networklayer.feature

import android.app.Application
import com.emproto.networklayer.ApiService
import com.emproto.networklayer.di.DaggerDataComponent
import com.emproto.networklayer.di.DataAppModule
import com.emproto.networklayer.di.DataComponent
import com.emproto.networklayer.di.DataModule
import com.emproto.networklayer.request.login.profile.EditUserNameRequest
import com.emproto.networklayer.request.login.profile.UploadProfilePictureRequest
import com.emproto.networklayer.response.chats.ChatDetailResponse
import com.emproto.networklayer.response.chats.ChatInitiateRequest
import com.emproto.networklayer.response.profile.CitiesResponse
import com.emproto.networklayer.response.profile.*
import retrofit2.Response
import retrofit2.http.Body
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

    suspend fun uploadPictureProfile(uploadProfilePictureRequest: UploadProfilePictureRequest): Response<ProfilePictureResponse> {
        return apiService.uploadPicture(uploadProfilePictureRequest)
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
    suspend fun getFaqList(): Response<ProfileFaqResponse> {
        return apiService.getFaqList()
    }
}