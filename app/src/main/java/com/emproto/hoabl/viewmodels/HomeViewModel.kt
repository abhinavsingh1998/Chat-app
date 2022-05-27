package com.emproto.hoabl.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emproto.core.Database.Dao.HomeSearchDao
import com.emproto.core.Database.TableModel.SearchModel
import com.emproto.networklayer.response.chats.ChatInitiateRequest
import com.emproto.hoabl.feature.home.data.LatesUpdatesPosition
import com.emproto.networklayer.response.chats.ChatResponse
import com.emproto.hoabl.repository.HomeRepository
import com.emproto.networklayer.request.refernow.ReferalRequest
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.chats.ChatDetailResponse
import com.emproto.networklayer.response.home.HomeResponse
import com.emproto.networklayer.response.home.PageManagementOrInsight
import com.emproto.networklayer.response.home.PageManagementOrLatestUpdate
import com.emproto.networklayer.response.promises.HomePagesOrPromise
import com.emproto.networklayer.response.promises.PromisesResponse
import com.emproto.networklayer.response.refer.ReferalResponse
import com.emproto.networklayer.response.terms.TermsConditionResponse
import javax.inject.Inject

class HomeViewModel(
    private var mapplication: Application,
    private var mhomeRepository: HomeRepository
) :
    ViewModel() {

    private var homeData = MutableLiveData<HomeResponse>()

    private var application: Application = mapplication
    private var homeRepository: HomeRepository = mhomeRepository
    private var promise = MutableLiveData<HomePagesOrPromise>()

    private var latestUpdates = MutableLiveData<PageManagementOrLatestUpdate>()
    private var insights = MutableLiveData<PageManagementOrInsight>()

    private var position = MutableLiveData<LatesUpdatesPosition>()

    @Inject
    lateinit var homeSearchDao: HomeSearchDao

    private var allSearchList: MutableLiveData<List<SearchModel>> = MutableLiveData()

    fun getRecordsObserver(): MutableLiveData<List<SearchModel>> {
        return allSearchList
    }

    fun getAllRecords() {
        val list = homeSearchDao.getAllSearchDetails()
        allSearchList.postValue(list)
    }

    fun insertRecord(searchModel: SearchModel) {
        homeSearchDao.insert(searchModel)
        getAllRecords()
    }

    fun getPromises(pageType: Int): LiveData<BaseResponse<PromisesResponse>> {
        return homeRepository.getPromises(pageType)
    }

    fun setSelectedPromise(promise: HomePagesOrPromise) {
        this.promise.postValue(promise)
    }

    fun getSelectedPromise(): LiveData<HomePagesOrPromise> {
        return promise
    }

    fun getDashBoardData(pageType: Int): LiveData<BaseResponse<HomeResponse>> {
        return homeRepository.getDashboardData(pageType)
    }

    fun setDashBoardData(data: HomeResponse) {
        homeData.postValue(data)
    }

    fun gethomeData(): LiveData<HomeResponse> {
        return homeData
    }

    fun getTermsCondition(pageType: Int): LiveData<BaseResponse<TermsConditionResponse>> {
        return mhomeRepository.getTermsCondition(pageType)
    }

    fun setSeLectedLatestUpdates(latestUpdates: PageManagementOrLatestUpdate) {
        this.latestUpdates.postValue(latestUpdates)
    }

    fun getSelectedLatestUpdates(): LiveData<PageManagementOrLatestUpdate> {
        return latestUpdates
    }

    fun setSelectedPosition(position: LatesUpdatesPosition) {
        this.position.postValue(position)
    }

    fun getSelectedPosition(): LiveData<LatesUpdatesPosition> {
        return position
    }

    fun setSeLectedInsights(insights: PageManagementOrInsight) {
        this.insights.postValue(insights)
    }

    fun getSelectedInsights(): LiveData<PageManagementOrInsight> {
        return insights
    }
    fun getChatsList(): LiveData<BaseResponse<ChatResponse>> {
        return homeRepository.getChatsList()
    }
    fun chatInitiate(chatInitiateRequest: ChatInitiateRequest): LiveData<BaseResponse<ChatDetailResponse>> {
        return homeRepository.chatInitiate(chatInitiateRequest)
    }
    fun getReferNow(referalRequest: ReferalRequest): LiveData<BaseResponse<ReferalResponse>> {
        return homeRepository.addReferral(referalRequest)
    }

}