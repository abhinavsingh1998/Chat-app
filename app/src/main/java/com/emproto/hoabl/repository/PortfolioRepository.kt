package com.emproto.hoabl.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emproto.core.BaseRepository
import com.emproto.networklayer.feature.PortfolioDataSource
import com.emproto.networklayer.feature.RegistrationDataSource
import com.emproto.networklayer.request.login.TroubleSigningRequest
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.bookingjourney.BookingJourneyResponse
import com.emproto.networklayer.response.ddocument.DDocumentResponse
import com.emproto.networklayer.response.documents.DocumentsResponse
import com.emproto.networklayer.response.login.TroubleSigningResponse
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
    val mDocumentsResponse = MutableLiveData<BaseResponse<ProjectTimelineResponse>>()
    val investmentResponseList = ArrayList<InvestmentDetailsResponse>()
    val projectTimeLineMediaResponse = MutableLiveData<ProjectTimelineResponse>()


    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        mPromisesResponse.postValue(BaseResponse.Companion.error(exception.localizedMessage))
    }

    /**
     * Get all investments
     *
     * @param pageType for promises it #5002
     * @return
     */

    fun getPortfolioDashboard(refresh: Boolean = false): LiveData<BaseResponse<PortfolioData>> {
        if (mPromisesResponse.value == null || refresh) {
            mPromisesResponse.postValue(BaseResponse.loading())
            coroutineScope.launch {
                try {
                    coroutineScope {
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
        val serchResult = investmentResponseList.filter { it.data.investmentInformation.id == ivID }
        if (serchResult.isNotEmpty()) {
            mPromisesResponse.postValue(BaseResponse.success(serchResult[0]))
        } else {
            coroutineScope.launch {
                try {
                    val topResponse =
                        PortfolioDataSource(application).getInvestmentDetails(ivID, projectId)
                    if (topResponse.isSuccessful) {
                        if (topResponse.body()!!.data != null) {
                            val crmId =
                                topResponse.body()!!.data.investmentInformation.crmLaunchPhaseId
                            val documentResponse =
                                PortfolioDataSource(application).getDocumentsListing(crmId)
                            if (documentResponse.isSuccessful && documentResponse.body()!!.data.isNotEmpty()) {
                                topResponse.body()!!.data.documentList =
                                    documentResponse.body()!!.data
                                mPromisesResponse.postValue(BaseResponse.success(topResponse.body()!!))
                                //local caching
                                investmentResponseList.add(topResponse.body()!!)
                            } else {
                                mPromisesResponse.postValue(BaseResponse.success(topResponse.body()!!))
                                investmentResponseList.add(topResponse.body()!!)
                            }
                        } else
                            mPromisesResponse.postValue(BaseResponse.Companion.error("No data found"))
                    } else {
                        mPromisesResponse.postValue(
                            BaseResponse.Companion.error(
                                getErrorMessage(
                                    topResponse.errorBody()!!.string()
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

    fun getDocumentsListing(projectId: String): LiveData<BaseResponse<DocumentsResponse>> {
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

        if (mDocumentsResponse.value == null || mDocumentsResponse.value!!.data!!.data.projectContent.id != id) {
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
        }
        return mDocumentsResponse
    }

    fun getProjectTimelineMedia(category: String,projectContentId:String): LiveData<BaseResponse<ProjectTimelineResponse>> {
        val mTimelineMediaResponse = MutableLiveData<BaseResponse<ProjectTimelineResponse>>()
        mTimelineMediaResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = PortfolioDataSource(application).getProjectTimelineMedia(category,projectContentId)
                if (request.isSuccessful) {
                    if (request.body()!!.data != null)
                        mTimelineMediaResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mTimelineMediaResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mTimelineMediaResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mTimelineMediaResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
            }
        }
        return mTimelineMediaResponse
    }

    fun getFacilitymanagment(plotId: String?, crmId: String?): LiveData<BaseResponse<FMResponse>> {
        val mDocumentsResponse = MutableLiveData<BaseResponse<FMResponse>>()
        mDocumentsResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = PortfolioDataSource(application).getFacilityManagment(plotId, crmId)
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

    fun downloadDocument(path: String): MutableLiveData<BaseResponse<DDocumentResponse>> {
        val mDocumentsResponse = MutableLiveData<BaseResponse<DDocumentResponse>>()
        mDocumentsResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = PortfolioDataSource(application).downloadDocument(path)
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

    fun getBookingJourney(id: Int): LiveData<BaseResponse<BookingJourneyResponse>> {
        val mDocumentsResponse = MutableLiveData<BaseResponse<BookingJourneyResponse>>()
        mDocumentsResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = PortfolioDataSource(application).getBookingJourney(id)
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

    fun submitTroubleCase(signingRequest: TroubleSigningRequest): LiveData<BaseResponse<TroubleSigningResponse>> {
        val mCaseResponse = MutableLiveData<BaseResponse<TroubleSigningResponse>>()

        mCaseResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = RegistrationDataSource(application).submitTroubleCase(signingRequest)
                if (request.isSuccessful) {
                    mCaseResponse.postValue(BaseResponse.success(request.body()!!))
                } else {
                    mCaseResponse.postValue(BaseResponse.Companion.error(request.message()))
                }

            } catch (e: Exception) {
                mCaseResponse.postValue(BaseResponse.Companion.error(e.message!!))
            }
        }
        return mCaseResponse
    }


}