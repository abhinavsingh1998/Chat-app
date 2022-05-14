package com.emproto.hoabl.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emproto.core.Database.Dao.HomeSearchDao
import com.emproto.core.Database.TableModel.SearchModel
import com.emproto.networklayer.response.chats.ChatResponse
import com.emproto.hoabl.repository.HomeRepository
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.promises.HoablPagesOrPromise
import com.emproto.networklayer.response.promises.PromisesResponse
import javax.inject.Inject

class HomeViewModel(
    private var mapplication: Application,
    private var mhomeRepository: HomeRepository
) :
    ViewModel() {

    private var application: Application = mapplication
    private var homeRepository: HomeRepository = mhomeRepository
    private var promise = MutableLiveData<HoablPagesOrPromise>()
    private var chatsList = MutableLiveData<ChatResponse>()


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
    fun getChatsList(): LiveData<BaseResponse<ChatResponse>> {
        return homeRepository.getChatsList()
    }

    fun setSelectedPromise(promise: HoablPagesOrPromise) {
        this.promise.postValue(promise)
    }

    fun getSelectedPromise(): LiveData<HoablPagesOrPromise> {
        return promise
    }


}