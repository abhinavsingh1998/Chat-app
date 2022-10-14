package com.emproto.hoabl.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emproto.core.BaseRepository
import com.emproto.core.Constants
import com.emproto.networklayer.feature.InvestmentDataSource
import com.emproto.networklayer.request.investment.AddInventoryBody
import com.emproto.networklayer.request.investment.VideoCallBody
import com.emproto.networklayer.request.investment.WatchListBody
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.investment.*
import kotlinx.coroutines.*
import javax.inject.Inject

class InvestmentRepository @Inject constructor(application: Application) :
    BaseRepository(application) {

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + parentJob)

    val mInvestmentResponse = MutableLiveData<BaseResponse<InvestmentResponse>>()

    /**
     * Get all investments
     *
     * @param pageType for investments it #5002
     * @return
     */

    fun getInvestments(
        pageType: Int,
        refresh: Boolean
    ): LiveData<BaseResponse<InvestmentResponse>> {
        if ((mInvestmentResponse.value == null || mInvestmentResponse.value!!.status == Status.ERROR) || refresh) {
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
                    mInvestmentResponse.postValue(BaseResponse.error(getErrorMessage(e)))
                }
            }
        }
        return mInvestmentResponse
    }

    fun getProjectMediaGalleries(projectId: Int): LiveData<BaseResponse<ProjectMediaGalleryResponse>> {
        val mInvestmentResponse = MutableLiveData<BaseResponse<ProjectMediaGalleryResponse>>()
        mInvestmentResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request =
                    InvestmentDataSource(application).getMediaGallery(projectId = projectId)
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
                mInvestmentResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return mInvestmentResponse
    }

    fun getInvestmentsDetail(id: Int): LiveData<BaseResponse<ProjectDetailResponse>> {
        val mInvestmentDetailResponse = MutableLiveData<BaseResponse<ProjectDetailResponse>>()
        mInvestmentDetailResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val watchList = async { InvestmentDataSource(application).getMyWatchlist() }
                val inventories = async { InvestmentDataSource(application).getInventories(id) }
                val testimonials = async { InvestmentDataSource(application).getTestimonialsData() }
                val investmentDetail =
                    async { InvestmentDataSource(application).getInvestmentsDetailData(id) }
                val watchlistResponse = watchList.await()
                val inventoriesResponse = inventories.await()
                val testimonialsResponse = testimonials.await()
                val investmentDetailResponse = investmentDetail.await()
                if (investmentDetailResponse.isSuccessful) {
                    if (investmentDetailResponse.body()!!.data != null) {
                        if (watchlistResponse.isSuccessful) {
                            investmentDetailResponse.body()!!.data.projectContent.watchlist =
                                watchlistResponse.body()!!.data
                        }
                        if (inventoriesResponse.isSuccessful) {
                            investmentDetailResponse.body()!!.data.projectContent.inventoriesList =
                                inventoriesResponse.body()!!.data
                        }
                        if (testimonialsResponse.isSuccessful) {
                            investmentDetailResponse.body()!!.data.projectContent.testimonials =
                                testimonialsResponse.body()!!.data
                        }
                        mInvestmentDetailResponse.postValue(
                            BaseResponse.success(
                                investmentDetailResponse.body()!!
                            )
                        )
                    } else
                        mInvestmentDetailResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mInvestmentDetailResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                investmentDetailResponse.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mInvestmentDetailResponse.postValue(BaseResponse.error(getErrorMessage(e)))
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
                mAllInvestmentsResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return mAllInvestmentsResponse
    }

    fun getInvestmentsPromises(): LiveData<BaseResponse<InvestmentPromisesResponse>> {
        val mInvestmentPromisesResponse =
            MutableLiveData<BaseResponse<InvestmentPromisesResponse>>()
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
                mInvestmentPromisesResponse.postValue(BaseResponse.error(getErrorMessage(e)))
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
                mInvestmentFaqResponse.postValue(BaseResponse.error(getErrorMessage(e)))
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
                mWatchListResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return mWatchListResponse
    }

    fun deleteWatchList(id: Int): LiveData<BaseResponse<WatchListResponse>> {
        val mDeleteWatchListResponse = MutableLiveData<BaseResponse<WatchListResponse>>()
        mDeleteWatchListResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = InvestmentDataSource(application).deleteWatchlist(id)
                if (request.isSuccessful) {
                    if (request.body() != null)
                        mDeleteWatchListResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mDeleteWatchListResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mDeleteWatchListResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mDeleteWatchListResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return mDeleteWatchListResponse
    }

    fun getAllInventories(id: Int): LiveData<BaseResponse<GetInventoriesResponse>> {
        val mgetAllInventoriesResponse = MutableLiveData<BaseResponse<GetInventoriesResponse>>()
        mgetAllInventoriesResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = InvestmentDataSource(application).getInventories(id)
                if (request.isSuccessful) {
                    if (request.body()!!.data != null)
                        mgetAllInventoriesResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mgetAllInventoriesResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mgetAllInventoriesResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mgetAllInventoriesResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return mgetAllInventoriesResponse
    }

    fun addInventory(addInventoryBody: AddInventoryBody): LiveData<BaseResponse<WatchListResponse>> {
        val mAddInventoryResponse = MutableLiveData<BaseResponse<WatchListResponse>>()
        mAddInventoryResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = InvestmentDataSource(application).addInventory(addInventoryBody)
                if (request.isSuccessful) {
                    mAddInventoryResponse.postValue(BaseResponse.success(request.body()!!))
                } else {
                    mAddInventoryResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mAddInventoryResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return mAddInventoryResponse
    }

    fun scheduleVideoCall(videoCallBody: VideoCallBody): LiveData<BaseResponse<VideoCallResponse>> {
        val mVideoCallResponse = MutableLiveData<BaseResponse<VideoCallResponse>>()
        mVideoCallResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = InvestmentDataSource(application).scheduleVideoCall(videoCallBody)
                if (request.isSuccessful) {
                    mVideoCallResponse.postValue(BaseResponse.success(request.body()!!))
                } else {
                    mVideoCallResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mVideoCallResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return mVideoCallResponse
    }
}