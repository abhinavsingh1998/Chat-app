package com.emproto.hoabl.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.emproto.hoabl.repository.ProfileRepository
import com.emproto.networklayer.request.login.profile.EditUserNameRequest
import com.emproto.networklayer.request.login.profile.UploadProfilePictureRequest
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.profile.CitiesResponse
import com.emproto.networklayer.response.profile.*

class ProfileViewModel(
    private var mapplication: Application,
    private var mprofileRepository: ProfileRepository
) :
    ViewModel() {

    private var application: Application = mapplication
    private var profileRepository: ProfileRepository = mprofileRepository

    fun editUserNameProfile(editUserNameRequest: EditUserNameRequest): LiveData<BaseResponse<EditProfileResponse>> {
        return profileRepository.editUserNameProfile(editUserNameRequest)
    }

    fun uploadProfilePicture(uploadProfilePictureRequest: UploadProfilePictureRequest): LiveData<BaseResponse<ProfilePictureResponse>> {
        return profileRepository.uploadProfilePicture(uploadProfilePictureRequest)
    }

    fun deleteProfilePicture():LiveData<BaseResponse<EditProfileResponse>>{
        return profileRepository.deleteProfilePicture()
    }

    fun getCountries(pageType: Int): LiveData<BaseResponse<ProfileCountriesResponse>> {
        return profileRepository.getCountries(pageType)
    }

    fun getUserProfile(): LiveData<BaseResponse<ProfileResponse>> {
        return profileRepository.getUserProfile()
    }

    fun getStates(countryIsoCode:String):LiveData<BaseResponse<StatesResponse>>{
        return profileRepository.getStates(countryIsoCode)
    }

    fun getCities(stateIsoCode:String,countryIsoCode: String):LiveData<BaseResponse<CitiesResponse>>{
        return profileRepository.getCities(stateIsoCode,countryIsoCode)
    }

}


