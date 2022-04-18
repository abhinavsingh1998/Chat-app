package com.emproto.hoabl.repository


import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emproto.core.BaseRepository
import com.emproto.networklayer.feature.RegistrationDataSource
import com.emproto.networklayer.request.login.OtpRequest
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.login.OtpResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Exception

class HomeRepository @Inject constructor(application: Application) : BaseRepository(application) {

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + parentJob)
    private val loginResponse = MutableLiveData<BaseResponse<OtpResponse>>()

    fun getLoginResponse(otpRequest: OtpRequest): LiveData<BaseResponse<OtpResponse>> {
        loginResponse.postValue(BaseResponse.loading());
        coroutineScope.launch {
            try {
                val request = RegistrationDataSource(application).getOtp(otpRequest)
                if (request.isSuccessful) {
                    loginResponse.postValue(BaseResponse.success(request.body()!!))
                } else {
                    loginResponse.postValue(BaseResponse.error("Something Went Wrong"))
                }
            } catch (e: Exception) {
                loginResponse.postValue(BaseResponse.error(e.localizedMessage))
            }
        }
        return loginResponse;
    }

}