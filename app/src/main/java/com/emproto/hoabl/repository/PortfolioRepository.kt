package com.emproto.hoabl.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emproto.core.BaseRepository
import com.emproto.networklayer.feature.PortfolioDataSource
import com.emproto.networklayer.feature.RegistrationDataSource
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.documents.DocumentsResponse
import com.emproto.networklayer.response.portfolio.dashboard.PortfolioData
import com.emproto.networklayer.response.portfolio.fm.FMResponse
import com.emproto.networklayer.response.portfolio.ivdetails.InvestmentDetailsResponse
import com.emproto.networklayer.response.portfolio.prtimeline.ProjectTimelineResponse
import com.emproto.networklayer.response.profile.ProfileResponse
import com.emproto.networklayer.response.watchlist.WatchlistData
import kotlinx.coroutines.*
import javax.inject.Inject

class PortfolioRepository @Inject constructor(application: Application) :
    BaseRepository(application) {

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + parentJob)
    val mPromisesResponse = MutableLiveData<BaseResponse<PortfolioData>>()


    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        mPromisesResponse.postValue(BaseResponse.Companion.error(exception.localizedMessage))
    }
    /**
     * Get all investments
     *
     * @param pageType for promises it #5002
     * @return
     */

    fun getPortfolioDashboard(): LiveData<BaseResponse<PortfolioData>> {
        if (mPromisesResponse.value == null) {
            mPromisesResponse.postValue(BaseResponse.loading())
            coroutineScope.launch(exceptionHandler) {
                try {
                    val watchlist = async { PortfolioDataSource(application).getMyWatchlist() }
                    val dashboard =
                        async { PortfolioDataSource(application).getPortfolioDashboard() }
                    //val request = PortfolioDataSource(application).getPortfolioDashboard()
                    val watchlistResponse = watchlist.await()
                    val dashboardResponse = dashboard.await()
                    if (dashboardResponse.isSuccessful) {
                        if (dashboardResponse.body()!!.data != null) {
                            if (watchlistResponse.isSuccessful) {
                                dashboardResponse.body()!!.data.watchlist =
                                    watchlistResponse.body()!!.data
                            }
                            mPromisesResponse.postValue(BaseResponse.success(dashboardResponse.body()!!))
                        } else
                            mPromisesResponse.postValue(BaseResponse.Companion.error("No data found"))
                    } else {
                        mPromisesResponse.postValue(
                            BaseResponse.Companion.error(
                                getErrorMessage(
                                    dashboardResponse.errorBody()!!.string()
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

    fun getInvestmentDetails(
        ivID: Int,
        projectId: Int
    ): LiveData<BaseResponse<InvestmentDetailsResponse>> {
        val mPromisesResponse = MutableLiveData<BaseResponse<InvestmentDetailsResponse>>()
        mPromisesResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = PortfolioDataSource(application).getInvestmentDetails(ivID, projectId)
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

    fun getDocumentsListing(projectId: Int): LiveData<BaseResponse<DocumentsResponse>> {
        val mDocumentsResponse = MutableLiveData<BaseResponse<DocumentsResponse>>()
        mDocumentsResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = PortfolioDataSource(application).getDocumentsListing(projectId)
                if (request.isSuccessful) {
                    if (request.body()!!.data != null)
                        mDocumentsResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mDocumentsResponse.postValue(BaseResponse.Companion.error("No data found"))
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

    fun getMyWatchlist(): LiveData<BaseResponse<WatchlistData>> {
        val mDocumentsResponse = MutableLiveData<BaseResponse<WatchlistData>>()
        mDocumentsResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = PortfolioDataSource(application).getMyWatchlist()
                if (request.isSuccessful) {
                    if (request.body()!!.data != null)
                        mDocumentsResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mDocumentsResponse.postValue(BaseResponse.Companion.error("No data found"))
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

    fun getUserProfile(): LiveData<BaseResponse<ProfileResponse>> {
        val mDocumentsResponse = MutableLiveData<BaseResponse<ProfileResponse>>()
        mDocumentsResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = RegistrationDataSource(application).getUserProfile()
                if (request.isSuccessful) {
                    if (request.body()!!.data != null)
                        mDocumentsResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mDocumentsResponse.postValue(BaseResponse.Companion.error("No data found"))
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

    fun getProjectTimeline(id: Int): LiveData<BaseResponse<ProjectTimelineResponse>> {
        val mDocumentsResponse = MutableLiveData<BaseResponse<ProjectTimelineResponse>>()
        mDocumentsResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = PortfolioDataSource(application).getProjectTimeline(id)
                if (request.isSuccessful) {
                    if (request.body()!!.data != null)
                        mDocumentsResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mDocumentsResponse.postValue(BaseResponse.Companion.error("No data found"))
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

    fun getFacilitymanagment(): LiveData<BaseResponse<FMResponse>> {
        val mDocumentsResponse = MutableLiveData<BaseResponse<FMResponse>>()
        mDocumentsResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = PortfolioDataSource(application).getFacilityManagment()
                if (request.isSuccessful) {
                    if (request.body()!!.data != null)
                        mDocumentsResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mDocumentsResponse.postValue(BaseResponse.Companion.error("No data found"))
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


}