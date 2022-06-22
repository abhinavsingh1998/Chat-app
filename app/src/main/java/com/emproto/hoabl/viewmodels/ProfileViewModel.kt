package com.emproto.hoabl.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.emproto.hoabl.repository.ProfileRepository
import com.emproto.networklayer.request.login.profile.EditUserNameRequest
import com.emproto.networklayer.request.profile.FeedBackRequest
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.profile.CitiesResponse
import com.emproto.networklayer.response.profile.*
import com.emproto.networklayer.response.resourceManagment.ProflieResponse
import com.emproto.networklayer.response.terms.TermsConditionResponse
import java.io.File

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

    fun uploadProfilePicture(file:File,fileName:String): LiveData<BaseResponse<ProfilePictureResponse>> {
        return profileRepository.uploadProfilePicture(file,fileName)
    }
    fun presignedUrl(type: String, destinationFile: File): LiveData<BaseResponse<PresignedUrlResponse>> {
        return profileRepository.presignedUrl(type,destinationFile)
    }

    fun deleteProfilePicture():LiveData<BaseResponse<EditProfileResponse>>{
        return profileRepository.deleteProfilePicture()
    }
    fun deleteProfileImage(destinationFileName: String):LiveData<BaseResponse<EditProfileResponse>>{
        return profileRepository.deleteProfileImage(destinationFileName)
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

    fun getPrivacyAndPolicy(pageType: Int):LiveData<BaseResponse<TermsConditionResponse>>{
        return mprofileRepository.getPrivacyAndPolicy(pageType)
    }

    fun submitFeedback(feedBackRequest: FeedBackRequest): LiveData<BaseResponse<FeedBackResponse>> {
        return mprofileRepository.submitFeedback(feedBackRequest)
    }

    fun getAboutHoabl(pageType: Int): LiveData<BaseResponse<ProflieResponse>>{
        return mprofileRepository.getAboutHoaBl(pageType)
    }
    fun getFaqList(typeOfFAQ: String): LiveData<BaseResponse<ProfileFaqResponse>> {
        return profileRepository.getFaqList(typeOfFAQ)
    }

}


