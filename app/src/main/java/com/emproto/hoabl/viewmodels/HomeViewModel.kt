package com.emproto.hoabl.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emproto.core.Database.Dao.HomeSearchDao
import com.emproto.core.Database.TableModel.SearchModel
import com.emproto.hoabl.feature.home.data.LatesUpdatesPosition
import com.emproto.hoabl.repository.HomeRepository
import com.emproto.networklayer.request.refernow.ReferalRequest
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.home.HomeResponse
import com.emproto.networklayer.response.home.PageManagementOrInsight
import com.emproto.networklayer.response.marketingUpdates.Data
import com.emproto.networklayer.response.marketingUpdates.LatestUpdatesResponse
import com.emproto.networklayer.response.promises.HomePagesOrPromise
import com.emproto.networklayer.response.promises.PromisesResponse
import com.emproto.networklayer.response.refer.ReferalResponse
import com.emproto.networklayer.response.terms.TermsConditionResponse
import com.emproto.networklayer.response.testimonials.TestimonialsResponse
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

    private var testimonial = MutableLiveData<TestimonialsResponse>()
    private var selectedlatestUpdates = MutableLiveData<Data>()
    private var latestUpdates = MutableLiveData<LatestUpdatesResponse>()
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

    fun getDashBoardData(pageType: Int, refresh:Boolean): LiveData<BaseResponse<HomeResponse>> {
        return homeRepository.getDashboardData(pageType,refresh)
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

    fun getLatestUpdatesData(refresh: Boolean): LiveData<BaseResponse<LatestUpdatesResponse>>{
        return homeRepository.getlatestUpdatesData(refresh)
    }

    fun setLatestUpdatesData(data: LatestUpdatesResponse) {
        latestUpdates.postValue(data)
    }

    fun getLatestUpdates(): LiveData<LatestUpdatesResponse>{
        return latestUpdates
    }

    fun setSeLectedLatestUpdates(selectedlatestUpdates: Data) {
        this.selectedlatestUpdates.postValue(selectedlatestUpdates)
    }

    fun getSelectedLatestUpdates(): LiveData<Data> {
        return selectedlatestUpdates
    }

    fun setSelectedPosition(position: LatesUpdatesPosition) {
        this.position.postValue(position)
    }

    fun getSelectedPosition(): LiveData<LatesUpdatesPosition> {
        return position
    }

    fun getTestimonialsData(refresh: Boolean): LiveData<BaseResponse<TestimonialsResponse>>{
        return homeRepository.getTestimonialsData(refresh)
    }

    fun setTestimonials(data: TestimonialsResponse){
        testimonial.postValue(data)
    }

    fun setSeLectedInsights(insights: PageManagementOrInsight) {
        this.insights.postValue(insights)
    }

    fun getSelectedInsights(): LiveData<PageManagementOrInsight> {
        return insights
    }

    fun getReferNow(referalRequest: ReferalRequest): LiveData<BaseResponse<ReferalResponse>> {
        return homeRepository.addReferral(referalRequest)
    }
}