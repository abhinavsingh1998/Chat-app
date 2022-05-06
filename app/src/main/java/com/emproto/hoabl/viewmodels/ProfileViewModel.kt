package com.emproto.hoabl.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.emproto.hoabl.repository.ProfileRepository
import com.emproto.networklayer.request.login.profile.EditUserNameRequest
import com.emproto.networklayer.request.login.profile.UploadProfilePictureRequest
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.investment.InvestmentResponse
import com.emproto.networklayer.response.profile.EditProfileResponse
import com.emproto.networklayer.response.profile.ProfileCountriesResponse
import com.emproto.networklayer.response.profile.ProfilePictureResponse

class ProfileViewModel(private var mapplication: Application, private var mprofileRepository: ProfileRepository) :
    ViewModel() {

    private var application: Application = mapplication
    private var profileRepository: ProfileRepository = mprofileRepository

    fun editUserNameProfile(editUserNameRequest: EditUserNameRequest): LiveData<BaseResponse<EditProfileResponse>> {
        return profileRepository. editUserNameProfile(editUserNameRequest)
    }
    fun uploadProfilePicture(uploadProfilePictureRequest: UploadProfilePictureRequest):LiveData<BaseResponse<ProfilePictureResponse>>{
        return profileRepository. uploadProfilePicture(uploadProfilePictureRequest)
    }

    fun getCountries(pageType: Int): LiveData<BaseResponse<ProfileCountriesResponse>> {
        return profileRepository.getCountries(pageType)
    }

}


