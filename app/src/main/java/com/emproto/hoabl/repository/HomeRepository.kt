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
import com.emproto.networklayer.response.HomeActionItemResponse
import com.emproto.networklayer.response.chats.ChatDetailResponse
import com.emproto.networklayer.response.chats.ChatResponse
import com.emproto.networklayer.response.ddocument.DDocumentResponse
import com.emproto.networklayer.response.documents.DocumentsResponse
import com.emproto.networklayer.response.home.HomeResponse
import com.emproto.networklayer.response.insights.InsightsResponse
import com.emproto.networklayer.response.investment.AllProjectsResponse
import com.emproto.networklayer.response.marketingUpdates.LatestUpdatesResponse
import com.emproto.networklayer.response.notification.dataResponse.NotificationResponse
import com.emproto.networklayer.response.notification.readStatus.ReadNotificationReponse
import com.emproto.networklayer.response.portfolio.fm.FMResponse
import com.emproto.networklayer.response.profile.AccountsResponse
import com.emproto.networklayer.response.promises.PromisesResponse
import com.emproto.networklayer.response.refer.ReferalResponse
import com.emproto.networklayer.response.search.SearchResponse
import com.emproto.networklayer.response.terms.TermsConditionResponse
import com.emproto.networklayer.response.testimonials.TestimonialsResponse
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.Exception

class HomeRepository @Inject constructor(application: Application) : BaseRepository(application) {

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + parentJob)
    val mPromisesResponse = MutableLiveData<BaseResponse<PromisesResponse>>()
    val mHomeResponse = MutableLiveData<BaseResponse<HomeResponse>>()
    val mLatestUpdates = MutableLiveData<BaseResponse<LatestUpdatesResponse>>()

    val mInsights = MutableLiveData<BaseResponse<InsightsResponse>>()
    val mTestimonials = MutableLiveData<BaseResponse<TestimonialsResponse>>()
    val mActionItem = MutableLiveData<BaseResponse<HomeActionItemResponse>>()


    /**
     * Get all promises
     *
     * @param pageType for promises it #5003
     * @return
     */
    fun getPromises(pageType: Int, refresh: Boolean= false): LiveData<BaseResponse<PromisesResponse>> {
        if (mPromisesResponse.value == null || refresh) {
            mPromisesResponse.postValue(BaseResponse.loading())
            coroutineScope.launch {
                try {
                    coroutineScope{
                        val request = async { HomeDataSource(application).getPromisesData(pageType)}
                        val promisesResponse= request.await()
                        if (promisesResponse.isSuccessful) {
                            if (promisesResponse.body()!!.data != null)
                                mPromisesResponse.postValue(BaseResponse.success(promisesResponse.body()!!))
                            else
                                mPromisesResponse.postValue(BaseResponse.Companion.error("No data found"))
                        } else {
                            mPromisesResponse.postValue(
                                BaseResponse.Companion.error(
                                    getErrorMessage(
                                        promisesResponse.errorBody()!!.string()
                                    )
                                )
                            )
                        }

                    }

                }
                catch (e: Exception) {
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
    fun getDashboardData(
        pageType: Int,
        refresh: Boolean = false
    ): LiveData<BaseResponse<HomeResponse>> {

        if (mHomeResponse.value == null || refresh) {
            mHomeResponse.postValue(BaseResponse.loading())
            coroutineScope.launch {
                try {

                    coroutineScope{

                        val dasboard = async { HomeDataSource(application).getDashboardData(pageType)}
                        val actionItem= async { HomeDataSource(application).getActionItem() }

                        val dashBoardResponse= dasboard.await()
                        val actionItemResponse= actionItem.await()
                        if (dashBoardResponse.isSuccessful) {
                            if (dashBoardResponse.body()!!.data != null){
                                if(actionItemResponse.isSuccessful){
                                    dashBoardResponse.body()!!.data.actionItem = actionItemResponse.body()!!.data
                                }
                                mHomeResponse.postValue(BaseResponse.success(dashBoardResponse.body()!!))
                            } else
                                mHomeResponse.postValue(BaseResponse.Companion.error("No data found"))
                        } else {
                            mHomeResponse.postValue(
                                BaseResponse.Companion.error(
                                    getErrorMessage(
                                        dashBoardResponse.errorBody()!!.string()
                                    )
                                )
                            )
                        }
                    }

                } catch (e: Exception) {
                    mHomeResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
                }
            }

        }
        return mHomeResponse
    }

    fun getlatestUpdatesData(refresh: Boolean = false,
        byPrority: Boolean
    ): LiveData<BaseResponse<LatestUpdatesResponse>> {

        if (mLatestUpdates.value == null || refresh) {
            mLatestUpdates.postValue(BaseResponse.loading())
            coroutineScope.launch {
                try {
                    val request = HomeDataSource(application).getLatestUpdatesData(byPrority)
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

    fun getInsightsData(
        refresh: Boolean = false,
        byPrority: Boolean
    ): LiveData<BaseResponse<InsightsResponse>> {

        if (mInsights.value == null || refresh) {
            mInsights.postValue(BaseResponse.loading())
            coroutineScope.launch {
                try {
                    val request = HomeDataSource(application).getInsightsData(byPrority)
                    if (request.isSuccessful) {
                        if (request.body()!!.data != null)
                            mInsights.postValue(BaseResponse.success(request.body()!!))
                        else
                            mInsights.postValue(BaseResponse.Companion.error("No data found"))
                    } else {
                        mInsights.postValue(
                            BaseResponse.Companion.error(
                                getErrorMessage(
                                    request.errorBody()!!.string()
                                )
                            )
                        )
                    }
                } catch (e: Exception) {
                    mInsights.postValue(BaseResponse.Companion.error(e.localizedMessage))
                }
            }

        }
        return mInsights
    }

    fun getTestimonialsData(refresh: Boolean = false): LiveData<BaseResponse<TestimonialsResponse>> {

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

    fun getAllInvestmentsProjects(): LiveData<BaseResponse<AllProjectsResponse>> {
        val mAllInvestmentsResponse = MutableLiveData<BaseResponse<AllProjectsResponse>>()
        mAllInvestmentsResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = HomeDataSource(application).getAllInvestments()
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

    fun getFacilitymanagment(): LiveData<BaseResponse<FMResponse>> {
        val mDocumentsResponse = MutableLiveData<BaseResponse<FMResponse>>()
        mDocumentsResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = HomeDataSource(application).getFacilityManagment()
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

    fun getChatsList(): LiveData<BaseResponse<ChatResponse>> {
        val mChatResponse = MutableLiveData<BaseResponse<ChatResponse>>()
        mChatResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = HomeDataSource(application).getChatsList()
                if (request.isSuccessful) {
                    if (request.body() != null && request.body() is ChatResponse) {
                        mChatResponse.postValue(BaseResponse.success(request.body()!!))

                    } else
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
                    if (request.body() != null && request.body() is ChatDetailResponse) {
                        mChatDetailResponse.postValue(BaseResponse.success(request.body()!!))

                    } else {
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

    fun getAccountsList(): LiveData<BaseResponse<AccountsResponse>> {
        val mAccountsResponse = MutableLiveData<BaseResponse<AccountsResponse>>()
        mAccountsResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = HomeDataSource(application).getAccountsList()
                if (request.isSuccessful) {
                    if (request.body() != null && request.body() is AccountsResponse) {
                        mAccountsResponse.postValue(BaseResponse.success(request.body()!!))

                    } else
                        mAccountsResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mAccountsResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {

                mAccountsResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
            }
        }
        return mAccountsResponse
    }
    fun getSearchResult(searchWord: String): LiveData<BaseResponse<SearchResponse>> {
        val mSearchResponse = MutableLiveData<BaseResponse<SearchResponse>>()
        mSearchResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val search =   HomeDataSource(application).getSearchResults(searchWord)
                if (search.isSuccessful) {
                    if (search.body()!!.data != null){
                        mSearchResponse.postValue(BaseResponse.success(search.body()!!))
                    }
                    else
                        mSearchResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mSearchResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                search.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mSearchResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
            }
        }
        return mSearchResponse
    }

    fun getSearchDocResult(searchWord: String): LiveData<BaseResponse<DocumentsResponse>> {
        val mSearchDocResponse = MutableLiveData<BaseResponse<DocumentsResponse>>()
        mSearchDocResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val docs = when(searchWord){
                    "" -> HomeDataSource(application).getSearchDocResults()
                    else -> HomeDataSource(application).getSearchDocResultsQuery(searchWord)
                }
                if (docs.isSuccessful) {
                    if (docs.body()!!.data != null){
                        mSearchDocResponse.postValue(BaseResponse.success(docs.body()!!))
                    }
                    else
                        mSearchDocResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mSearchDocResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                docs.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mSearchDocResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
            }
        }
        return mSearchDocResponse
    }

    fun downloadDocument(path: String): MutableLiveData<BaseResponse<DDocumentResponse>> {
        val mDocumentsResponse = MutableLiveData<BaseResponse<DDocumentResponse>>()
        mDocumentsResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = HomeDataSource(application).downloadDocument(path)
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
//    fun getAccountsList(): LiveData<BaseResponse<AccountsResponse>> {
//        val mAccountsResponse = MutableLiveData<BaseResponse<AccountsResponse>>()
//        mAccountsResponse.postValue(BaseResponse.loading())
//        coroutineScope.launch {
//            try {
//                val request = HomeDataSource(application).getAccountsList()
//                if (request.isSuccessful) {
//                    if (request.body() != null && request.body() is AccountsResponse) {
//                        mAccountsResponse.postValue(BaseResponse.success(request.body()!!))
//
//                    } else
//                        mAccountsResponse.postValue(BaseResponse.Companion.error("No data found"))
//                } else {
//                    mAccountsResponse.postValue(
//                        BaseResponse.Companion.error(
//                            getErrorMessage(
//                                request.errorBody()!!.string()
//                            )
//                        )
//                    )
//                }
//            } catch (e: Exception) {
//
//                mAccountsResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
//            }
//        }
//        return mAccountsResponse
//    }


//    fun getActionItem(refresh: Boolean = false): LiveData<BaseResponse<HomeActionItemResponse>> {
//
//        if ( mActionItem .value == null || refresh) {
//            mActionItem.postValue(BaseResponse.loading())
//            coroutineScope.launch {
//                try {
//                    val request = HomeDataSource(application).getActionItem()
//                    if (request.isSuccessful) {
//                        if (request.body()!!.data != null)
//                            mActionItem.postValue(BaseResponse.success(request.body()!!))
//                        else
//                            mTestimonials.postValue(BaseResponse.Companion.error("No data found"))
//                    } else {
//                        mTestimonials.postValue(
//                            BaseResponse.Companion.error(
//                                getErrorMessage(
//                                    request.errorBody()!!.string()
//                                )
//                            )
//                        )
//                    }
//                } catch (e: Exception) {
//                    mActionItem .postValue(BaseResponse.Companion.error(e.localizedMessage))
//                }
//            }
//
//        }
//        return  mActionItem
//    }



    fun getNotificationList(size:Int, index:Int): LiveData<BaseResponse<NotificationResponse>> {
        val mNotificationResponse = MutableLiveData<BaseResponse<NotificationResponse>>()
        mNotificationResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = HomeDataSource(application).getNotificationList(size, index)
                if (request.isSuccessful) {
                    if (request.body() != null && request.body() is NotificationResponse) {
                        mNotificationResponse.postValue(BaseResponse.success(request.body()!!))

                    } else
                        mNotificationResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mNotificationResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {

                mNotificationResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
            }
        }
        return mNotificationResponse
    }

    fun setReadStatus(ids:List<String>): LiveData<BaseResponse<ReadNotificationReponse>> {
        val mNotificationResponse = MutableLiveData<BaseResponse<ReadNotificationReponse>>()
        mNotificationResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = HomeDataSource(application).setReadStatus(ids)
                if (request.isSuccessful) {
                    if (request.body() != null && request.body() is ReadNotificationReponse) {
                        mNotificationResponse.postValue(BaseResponse.success(request.body()!!))

                    } else
                        mNotificationResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mNotificationResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {

                mNotificationResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
            }
        }
        return mNotificationResponse
    }



}