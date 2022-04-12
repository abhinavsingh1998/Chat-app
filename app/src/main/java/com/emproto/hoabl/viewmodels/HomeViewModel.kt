package com.emproto.hoabl.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emproto.core.Database.Dao.HomeSearchDao
import com.emproto.core.Database.TableModel.SearchModel
import com.emproto.hoabl.repository.HomeRepository
import com.emproto.networklayer.request.OtpRequest
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.OtpResponse
import javax.inject.Inject

class HomeViewModel(var mapplication: Application, var mhomeRepository: HomeRepository) :
    ViewModel() {

    var application: Application = mapplication
    var homeRepository: HomeRepository = mhomeRepository

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

    fun getOtp(otpRequest: OtpRequest): LiveData<BaseResponse<OtpResponse>> {
        return homeRepository.getLoginResponse(otpRequest);
    }

}