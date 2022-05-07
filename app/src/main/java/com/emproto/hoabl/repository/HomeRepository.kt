package com.emproto.hoabl.repository


import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emproto.core.BaseRepository
import com.emproto.networklayer.feature.HomeDataSource
import com.emproto.networklayer.feature.RegistrationDataSource
import com.emproto.networklayer.request.login.OtpRequest
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.home.HomeResponse
import com.emproto.networklayer.response.login.OtpResponse
import com.emproto.networklayer.response.promises.PromisesResponse
import com.emproto.networklayer.response.terms.TermsConditionResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Exception

class HomeRepository @Inject constructor(application: Application) : BaseRepository(application) {

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + parentJob)


    /**
     * Get all promises
     *
     * @param pageType for promises it #5003
     * @return
     */
    fun getPromises(pageType: Int): LiveData<BaseResponse<PromisesResponse>> {
        val mPromisesResponse = MutableLiveData<BaseResponse<PromisesResponse>>()
        mPromisesResponse.postValue(BaseResponse.loading())

        coroutineScope.launch {
            try {
                val request = HomeDataSource(application).getPromisesData(pageType)
                if (request.isSuccessful) {
                    if (request.body()!!.data != null)
                        mPromisesResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mPromisesResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mPromisesResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mPromisesResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
            }
        }
        return mPromisesResponse
    }


    /**
     * Get all homeData
     *
     * @param pageType for promises it #5001
     * @return
     */
    fun getHome(pageType: Int): LiveData<BaseResponse<HomeResponse>>{

        val mHomeResponse = MutableLiveData<BaseResponse<HomeResponse>>()
        mHomeResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = HomeDataSource(application).getHomeData(pageType)
                if (request.isSuccessful) {
                    if (request.body()!!.data != null)
                        mHomeResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mHomeResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mHomeResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mHomeResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
            }
        }
        return mHomeResponse

    }

    fun getTermsCondition(pageType: Int): LiveData<BaseResponse<TermsConditionResponse>> {
        val mAddUsernameResponse = MutableLiveData<BaseResponse<TermsConditionResponse>>()
        mAddUsernameResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = RegistrationDataSource(application).getTermsCondition(pageType)
                if (request.isSuccessful) {
                    mAddUsernameResponse.postValue(BaseResponse.success(request.body()!!))
                } else {
                    mAddUsernameResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }

            } catch (e: Exception) {
                mAddUsernameResponse.postValue(BaseResponse.Companion.error(e.message!!))
            }
        }
        return mAddUsernameResponse
    }

}