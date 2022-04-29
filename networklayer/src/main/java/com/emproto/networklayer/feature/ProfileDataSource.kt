package com.emproto.networklayer.feature

import android.app.Application
import com.emproto.networklayer.ApiService
import com.emproto.networklayer.di.DaggerDataComponent

import com.emproto.networklayer.di.DataAppModule
import com.emproto.networklayer.di.DataComponent
import com.emproto.networklayer.di.DataModule
import com.emproto.networklayer.profile.EditUserNameRequest
import com.emproto.networklayer.profile.UploadProfilePictureRequest
import com.emproto.networklayer.response.profile.EditProfileResponse
import com.emproto.networklayer.response.profile.ProfilePictureResponse
import retrofit2.Response
import javax.inject.Inject

class ProfileDataSource(val application: Application) {

    @Inject
    lateinit var apiService:ApiService
    private var dataComponent: DataComponent =
        DaggerDataComponent.builder().dataAppModule(DataAppModule(application))
            .dataModule(DataModule(application)).build()
    init {
        dataComponent.inject(this)
    }

//    profile modules apis

    suspend fun editProfile(editUserNameRequest: EditUserNameRequest): Response<EditProfileResponse> {
        return apiService.addUserName(editUserNameRequest)
    }
    suspend fun uploadPictureProfile(uploadProfilePictureRequest: UploadProfilePictureRequest):Response<ProfilePictureResponse>{
        return apiService.uploadPicture(uploadProfilePictureRequest)
    }

}