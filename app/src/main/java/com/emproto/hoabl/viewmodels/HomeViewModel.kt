package com.emproto.hoabl.MVVM.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emproto.core.Database.Dao.HomeSearchDao
import com.emproto.core.Database.TableModel.SearchModel
import com.emproto.hoabl.MVVM.repositories.HomeRepository
import javax.inject.Inject

class HomeViewModel(var mapplication: Application, var mhomeRepository: HomeRepository) : ViewModel() {

   lateinit var application:Application
   lateinit var homeRepository :HomeRepository

   @Inject
   lateinit var homeSearchDao: HomeSearchDao

   var allSearchList:MutableLiveData<List<SearchModel>>

   init {
       this.mapplication = application
       this.mhomeRepository = homeRepository
       allSearchList=MutableLiveData()
   }

    fun getRecordsObserver():MutableLiveData<List<SearchModel>>{
        return allSearchList
    }

    fun getAllRecords(){
       val list= homeSearchDao.getAllSearchDetails()
        allSearchList.postValue(list)
    }

    fun insertRecord(searchModel: SearchModel){
        homeSearchDao.insert(searchModel)
        getAllRecords()
    }

}