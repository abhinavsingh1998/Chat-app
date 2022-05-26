package com.emproto.hoabl.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emproto.core.BaseRepository
import com.emproto.networklayer.feature.InvestmentDataSource
import com.emproto.networklayer.request.investment.WatchListBody
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.investment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class InvestmentRepository @Inject constructor(application: Application) : BaseRepository(application) {

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + parentJob)

    /**
     * Get all investments
     *
     * @param pageType for investments it #5002
     * @return
     */

    fun getInvestments(pageType: Int): LiveData<BaseResponse<InvestmentResponse>> {
        val mInvestmentResponse = MutableLiveData<BaseResponse<InvestmentResponse>>()
        mInvestmentResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = InvestmentDataSource(application).getInvestmentsData(pageType)
                if (request.isSuccessful) {
                    if (request.body()!!.data != null)
                        mInvestmentResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mInvestmentResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mInvestmentResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mInvestmentResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
            }
        }
        return mInvestmentResponse
    }

    fun getInvestmentsDetail(id: Int): LiveData<BaseResponse<ProjectDetailResponse>> {
        val mInvestmentDetailResponse = MutableLiveData<BaseResponse<ProjectDetailResponse>>()
        mInvestmentDetailResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = InvestmentDataSource(application).getInvestmentsDetailData(id)
                if (request.isSuccessful) {
                    if (request.body()!!.data != null)
                        mInvestmentDetailResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mInvestmentDetailResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mInvestmentDetailResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mInvestmentDetailResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
            }
        }
        return mInvestmentDetailResponse
    }

    fun getAllInvestmentsProjects(): LiveData<BaseResponse<AllProjectsResponse>> {
        val mAllInvestmentsResponse = MutableLiveData<BaseResponse<AllProjectsResponse>>()
        mAllInvestmentsResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = InvestmentDataSource(application).getAllInvestments()
                if (request.isSuccessful) {
                    if (request.body()!!.data != null)
                        mAllInvestmentsResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mAllInvestmentsResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mAllInvestmentsResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mAllInvestmentsResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
            }
        }
        return mAllInvestmentsResponse
    }

    fun getInvestmentsPromises(): LiveData<BaseResponse<InvestmentPromisesResponse>> {
        val mInvestmentPromisesResponse = MutableLiveData<BaseResponse<InvestmentPromisesResponse>>()
        mInvestmentPromisesResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = InvestmentDataSource(application).getInvestmentsPromises()
                if (request.isSuccessful) {
                    if (request.body()!!.data != null)
                        mInvestmentPromisesResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mInvestmentPromisesResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mInvestmentPromisesResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mInvestmentPromisesResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
            }
        }
        return mInvestmentPromisesResponse
    }

    fun getInvestmentsFaq(id: Int): LiveData<BaseResponse<FaqDetailResponse>> {
        val mInvestmentFaqResponse = MutableLiveData<BaseResponse<FaqDetailResponse>>()
        mInvestmentFaqResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = InvestmentDataSource(application).getInvestmentsFaq(id)
                if (request.isSuccessful) {
                    if (request.body()!!.data != null)
                        mInvestmentFaqResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mInvestmentFaqResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mInvestmentFaqResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mInvestmentFaqResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
            }
        }
        return mInvestmentFaqResponse
    }

    fun addWatchList(watchListBody: WatchListBody): LiveData<BaseResponse<WatchListResponse>> {
        val mWatchListResponse = MutableLiveData<BaseResponse<WatchListResponse>>()
        mWatchListResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = InvestmentDataSource(application).addWatchList(watchListBody)
                if (request.isSuccessful) {
                    mWatchListResponse.postValue(BaseResponse.success(request.body()!!))
                } else {
                    mWatchListResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mWatchListResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
            }
        }
        return mWatchListResponse
    }
}