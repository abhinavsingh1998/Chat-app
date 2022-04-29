package com.emproto.hoabl.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emproto.core.BaseRepository
import com.emproto.networklayer.feature.ProfileDataSource
import com.emproto.networklayer.profile.EditUserNameRequest
import com.emproto.networklayer.profile.UploadProfilePictureRequest
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.profile.EditProfileResponse
import com.emproto.networklayer.response.profile.ProfilePictureResponse
import com.emproto.networklayer.response.profile.ProfileResponse

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


class ProfileRepository @Inject constructor(application: Application) : BaseRepository(application) {
    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + parentJob)
    private val ProfileResponse = MutableLiveData<BaseResponse<ProfileResponse>>()

    fun editUserNameProfile(editUserNameRequest: EditUserNameRequest): LiveData<BaseResponse<EditProfileResponse>> {
        val mEditProfileResponse = MutableLiveData<BaseResponse<EditProfileResponse>>()
        mEditProfileResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {

                val request = ProfileDataSource(application).editProfile(editUserNameRequest)
                if (request.isSuccessful) {
                    mEditProfileResponse.postValue(BaseResponse.success(request.body()!!))
                } else {
                    mEditProfileResponse.postValue(BaseResponse.Companion.error(request.message()))
                }


            } catch (e: Exception) {
                mEditProfileResponse.postValue(BaseResponse.Companion.error(e.message!!))

            }
        }
        return mEditProfileResponse
    }
    fun uploadProfilePicture(uploadProfilePictureRequest: UploadProfilePictureRequest): LiveData<BaseResponse<ProfilePictureResponse>> {
        val mUploadProfilePicture = MutableLiveData<BaseResponse<ProfilePictureResponse>>()
        mUploadProfilePicture.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {

                val request = ProfileDataSource(application).uploadPictureProfile(uploadProfilePictureRequest)
                if (request.isSuccessful) {
                    mUploadProfilePicture.postValue(BaseResponse.success(request.body()!!))
                } else {
                    mUploadProfilePicture.postValue(BaseResponse.Companion.error(request.message()))
                }


            } catch (e: Exception) {
                mUploadProfilePicture.postValue(BaseResponse.Companion.error(e.message!!))

            }
        }
        return mUploadProfilePicture
    }
}








