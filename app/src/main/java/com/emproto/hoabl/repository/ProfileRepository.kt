package com.emproto.hoabl.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emproto.core.BaseRepository
import com.emproto.networklayer.feature.InvestmentDataSource
import com.emproto.networklayer.feature.ProfileDataSource
import com.emproto.networklayer.request.login.profile.EditUserNameRequest
import com.emproto.networklayer.request.login.profile.UploadProfilePictureRequest
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.investment.InvestmentResponse
import com.emproto.networklayer.response.profile.EditProfileResponse
import com.emproto.networklayer.response.profile.ProfileCountriesResponse
import com.emproto.networklayer.response.profile.ProfilePictureResponse
import com.emproto.networklayer.response.profile.ProfileResponse

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


class ProfileRepository @Inject constructor(application: Application) :
    BaseRepository(application) {
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

                val request =
                    ProfileDataSource(application).uploadPictureProfile(uploadProfilePictureRequest)
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

    fun getCountries(pageType: Int): LiveData<BaseResponse<ProfileCountriesResponse>> {
        val mCountriesResponse = MutableLiveData<BaseResponse<ProfileCountriesResponse>>()
        mCountriesResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = ProfileDataSource(application).getCountry(pageType)
                if (request.isSuccessful) {
                    if (request.body()!!.data != null)
                        mCountriesResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mCountriesResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mCountriesResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mCountriesResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
            }
        }
        return mCountriesResponse
    }
}








