package com.emproto.hoabl.repository


import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emproto.core.BaseRepository
import com.emproto.networklayer.response.chats.ChatInitiateRequest
import com.emproto.networklayer.feature.HomeDataSource
import com.emproto.networklayer.feature.RegistrationDataSource
import com.emproto.networklayer.request.refernow.ReferalRequest
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.chats.ChatDetailResponse
import com.emproto.networklayer.response.chats.ChatResponse
import com.emproto.networklayer.response.home.HomeResponse
import com.emproto.networklayer.response.marketingUpdates.LatestUpdatesResponse
import com.emproto.networklayer.response.promises.PromisesResponse
import com.emproto.networklayer.response.refer.ReferalResponse
import com.emproto.networklayer.response.terms.TermsConditionResponse
import com.emproto.networklayer.response.testimonials.TestimonialsResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Exception

class HomeRepository @Inject constructor(application: Application) : BaseRepository(application) {

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + parentJob)
    val mPromisesResponse = MutableLiveData<BaseResponse<PromisesResponse>>()
    val mHomeResponse = MutableLiveData<BaseResponse<HomeResponse>>()
    val mLatestUpdates= MutableLiveData<BaseResponse<LatestUpdatesResponse>>()
    val mTestimonials= MutableLiveData<BaseResponse<TestimonialsResponse>>()



    /**
     * Get all promises
     *
     * @param pageType for promises it #5003
     * @return
     */
    fun getPromises(pageType: Int): LiveData<BaseResponse<PromisesResponse>> {
        if (mPromisesResponse.value == null) {
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
        }
        return mPromisesResponse
    }


    /**
     * Get all homeData
     *
     * @param pageType for promises it #5001
     * @return
     */
    fun getDashboardData(pageType: Int, refresh:Boolean=false): LiveData<BaseResponse<HomeResponse>> {

        if (mHomeResponse.value == null || refresh) {
            mHomeResponse.postValue(BaseResponse.loading())
            coroutineScope.launch {
                try {
                    val request = HomeDataSource(application).getDashboardData(pageType)
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

        }
        return mHomeResponse
    }

    fun getlatestUpdatesData(refresh:Boolean=false): LiveData<BaseResponse<LatestUpdatesResponse>> {

        if (mLatestUpdates.value == null || refresh) {
            mLatestUpdates.postValue(BaseResponse.loading())
            coroutineScope.launch {
                try {
                    val request = HomeDataSource(application).getLatestUpdatesData()
                    if (request.isSuccessful) {
                        if (request.body()!!.data != null)
                            mLatestUpdates.postValue(BaseResponse.success(request.body()!!))
                        else
                            mLatestUpdates.postValue(BaseResponse.Companion.error("No data found"))
                    } else {
                        mLatestUpdates.postValue(
                            BaseResponse.Companion.error(
                                getErrorMessage(
                                    request.errorBody()!!.string()
                                )
                            )
                        )
                    }
                } catch (e: Exception) {
                    mLatestUpdates.postValue(BaseResponse.Companion.error(e.localizedMessage))
                }
            }

        }
        return mLatestUpdates
    }

    fun getTestimonialsData(refresh:Boolean=false): LiveData<BaseResponse<TestimonialsResponse>> {

        if (mTestimonials.value == null || refresh) {
            mLatestUpdates.postValue(BaseResponse.loading())
            coroutineScope.launch {
                try {
                    val request = HomeDataSource(application).getTestimonialsData()
                    if (request.isSuccessful) {
                        if (request.body()!!.data != null)
                            mTestimonials.postValue(BaseResponse.success(request.body()!!))
                        else
                            mTestimonials.postValue(BaseResponse.Companion.error("No data found"))
                    } else {
                        mTestimonials.postValue(
                            BaseResponse.Companion.error(
                                getErrorMessage(
                                    request.errorBody()!!.string()
                                )
                            )
                        )
                    }
                } catch (e: Exception) {
                    mTestimonials.postValue(BaseResponse.Companion.error(e.localizedMessage))
                }
            }

        }
        return mTestimonials
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

    fun addReferral(referalRequest: ReferalRequest): LiveData<BaseResponse<ReferalResponse>> {
        val mAddUsernameResponse = MutableLiveData<BaseResponse<ReferalResponse>>()
        mAddUsernameResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = HomeDataSource(application).getRefer(referalRequest)
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

    fun getChatsList(): LiveData<BaseResponse<ChatResponse>> {
        val mChatResponse = MutableLiveData<BaseResponse<ChatResponse>>()
        mChatResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = HomeDataSource(application).getChatsList()
                if (request.isSuccessful) {
                    if (request.body() != null&& request.body() is ChatResponse) {
                        mChatResponse.postValue(BaseResponse.success(request.body()!!))

                    }
                    else
                        mChatResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mChatResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {

                mChatResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
            }
        }
        return mChatResponse
    }


    fun chatInitiate(chatInitiateRequest: ChatInitiateRequest): LiveData<BaseResponse<ChatDetailResponse>> {
        val mChatDetailResponse = MutableLiveData<BaseResponse<ChatDetailResponse>>()
        mChatDetailResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = HomeDataSource(application).chatInitiate(chatInitiateRequest)
                if (request.isSuccessful) {
                    if (request.body() != null&& request.body() is ChatDetailResponse) {
                        mChatDetailResponse.postValue(BaseResponse.success(request.body()!!))

                    }
                    else {
                        mChatDetailResponse.postValue(BaseResponse.Companion.error("No data found"))
                    }
                } else {
                    mChatDetailResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {

                mChatDetailResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
            }
        }
        return mChatDetailResponse
    }


}