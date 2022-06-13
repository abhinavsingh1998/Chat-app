package com.emproto.hoabl.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emproto.core.BaseRepository
import com.emproto.networklayer.feature.HomeDataSource
import com.emproto.networklayer.feature.ProfileDataSource
import com.emproto.networklayer.request.login.profile.EditUserNameRequest
import com.emproto.networklayer.request.login.profile.UploadProfilePictureRequest
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.chats.ChatDetailResponse
import com.emproto.networklayer.response.chats.ChatInitiateRequest
import com.emproto.networklayer.response.profile.CitiesResponse
import com.emproto.networklayer.response.profile.*

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


class ProfileRepository @Inject constructor(application: Application) :
    BaseRepository(application) {
    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + parentJob)
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
    fun presignedUrl(type: String, destinationFile: File): LiveData<BaseResponse<PresignedUrlResponse>> {
        val presignedUrlResponse = MutableLiveData<BaseResponse<PresignedUrlResponse>>()
        presignedUrlResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {

                val request =
                    ProfileDataSource(application).presignedUrl(type,destinationFile)
                if (request.isSuccessful) {
                    presignedUrlResponse.postValue(BaseResponse.success(request.body()!!))
                } else {
                    presignedUrlResponse.postValue(BaseResponse.Companion.error(request.message()))
                }


            } catch (e: Exception) {
                presignedUrlResponse.postValue(BaseResponse.Companion.error(e.message!!))

            }
        }
        return presignedUrlResponse
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

    fun getStates(countryIsoCode: String): LiveData<BaseResponse<StatesResponse>> {
        val mStatesResponse = MutableLiveData<BaseResponse<StatesResponse>>()
        mStatesResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = ProfileDataSource(application).getStates(countryIsoCode)
                if (request.isSuccessful) {
                    if (request.body()!!.data != null)
                        mStatesResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mStatesResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mStatesResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mStatesResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
            }
        }
        return mStatesResponse
    }

    fun getCities(stateIsoCode:String,countryIsoCode: String):LiveData<BaseResponse<CitiesResponse>>{
        val mCitiesResponse = MutableLiveData<BaseResponse<CitiesResponse>>()
        mCitiesResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = ProfileDataSource(application).getCities(stateIsoCode,countryIsoCode)
                if (request.isSuccessful) {
                    if (request.body()!!.data != null)
                        mCitiesResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mCitiesResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mCitiesResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mCitiesResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
            }
        }
        return mCitiesResponse
    }

    fun getUserProfile(): LiveData<BaseResponse<ProfileResponse>> {
        val mDocumentsResponse = MutableLiveData<BaseResponse<ProfileResponse>>()
        mDocumentsResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = ProfileDataSource(application).getUserProfile()
                if (request.isSuccessful) {
                    Log.i("Request",request.message())
                    mDocumentsResponse.postValue(BaseResponse.success(request.body()!!))
                } else {
                    mDocumentsResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mDocumentsResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
            }
        }
        return mDocumentsResponse
    }

    fun deleteProfilePicture(): LiveData<BaseResponse<EditProfileResponse>> {
        val deleteResponse = MutableLiveData<BaseResponse<EditProfileResponse>>()
        deleteResponse.postValue(BaseResponse.loading())

        coroutineScope.launch {
            try {
                val request = ProfileDataSource(application).deleteProfilePic()
                if (request.isSuccessful) {
                    if (request.body()!!.data != null) {
                        deleteResponse.postValue(BaseResponse.success(request.body()!!))
                    } else {
                        deleteResponse.postValue(BaseResponse.Companion.error("No Data Found"))
                    }
                } else {
                    deleteResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                deleteResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
            }
        }
        return deleteResponse
    }


    fun getFaqList(): LiveData<BaseResponse<ProfileFaqResponse>> {
        val faqResponse = MutableLiveData<BaseResponse<ProfileFaqResponse>>()
        faqResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = ProfileDataSource(application).getFaqList()
                if (request.isSuccessful) {
                    if (request.body() != null && request.body() is ProfileFaqResponse) {
                        faqResponse.postValue(BaseResponse.success(request.body()!!))

                    } else {
                        faqResponse.postValue(BaseResponse.Companion.error("No data found"))
                    }
                } else {
                    faqResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {

                faqResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
            }
        }
        return faqResponse
    }


}








