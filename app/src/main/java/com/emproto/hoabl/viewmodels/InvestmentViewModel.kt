package com.emproto.hoabl.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emproto.hoabl.model.MediaViewItem
import com.emproto.hoabl.repository.InvestmentRepository
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.investment.*
import com.emproto.networklayer.response.promises.HomePagesOrPromise

class InvestmentViewModel(private var mapplication: Application, private var investmentRepository: InvestmentRepository) :ViewModel(){

    var skusLiveData = MutableLiveData<InventoryBucketContent>()
    private var smartDeals = MutableLiveData<List<PageManagementsOrCollectionOneModel>>()
    private var trendingProjects = MutableLiveData<List<PageManagementsOrCollectionTwoModel>>()
    private var projectid = MutableLiveData<Int>()
    private var mapLocationData = MutableLiveData<LocationInfrastructure>()
    private var oppDocData = MutableLiveData<List<OpprotunityDoc>>()
    private var landSkusData = MutableLiveData<List<InventoryBucketContent>>()
    private var mediaData  = MutableLiveData<ProjectCoverImages>()
    private var mediaViewItem = MutableLiveData<MediaViewItem>()

    fun getInvestments(pageType: Int): LiveData<BaseResponse<InvestmentResponse>> {
        return investmentRepository.getInvestments(pageType)
    }

    fun getInvestmentsDetail(id: Int): LiveData<BaseResponse<ProjectDetailResponse>> {
        return investmentRepository.getInvestmentsDetail(id)
    }

    fun getInvestmentsPromises(): LiveData<BaseResponse<InvestmentPromisesResponse>> {
        return investmentRepository.getInvestmentsPromises()
    }

    fun getInvestmentsFaq(id: Int): LiveData<BaseResponse<FaqDetailResponse>> {
        return investmentRepository.getInvestmentsFaq(id)
    }

    fun setSmartDealsList(smartDeals: List<PageManagementsOrCollectionOneModel>) {
        this.smartDeals.postValue(smartDeals)
    }

    fun getSmartDealsList(): LiveData<List<PageManagementsOrCollectionOneModel>> {
        return smartDeals
    }

    fun setTrendingList(trending: List<PageManagementsOrCollectionTwoModel>) {
        this.trendingProjects.postValue(trending)
    }

    fun getTrendingList(): LiveData<List<PageManagementsOrCollectionTwoModel>> {
        return trendingProjects
    }

    fun setProjectId(projectId: Int) {
        this.projectid.postValue(projectId)
    }

    fun getProjectId(): LiveData<Int> {
        return projectid
    }

    fun setMapLocationInfrastructure(mapLocationData: LocationInfrastructure) {
        this.mapLocationData.postValue(mapLocationData)
    }

    fun getMapLocationInfrastructure(): LiveData<LocationInfrastructure> {
        return mapLocationData
    }

    fun setOpportunityDoc(oppDocData: List<OpprotunityDoc>) {
        this.oppDocData.postValue(oppDocData)
    }

    fun getOpportunityDoc(): LiveData<List<OpprotunityDoc>> {
        return oppDocData
    }

    fun setSkus(skus: List<InventoryBucketContent>) {
        this.landSkusData.postValue(skus)
    }

    fun getSkus(): LiveData<List<InventoryBucketContent>> {
        return landSkusData
    }

    fun setMedia(media: ProjectCoverImages) {
        this.mediaData.postValue(media)
    }

    fun getMedia(): LiveData<ProjectCoverImages> {
        return mediaData
    }

    fun setMediaItem(media: MediaViewItem) {
        this.mediaViewItem.postValue(media)
    }

    fun getMediaItem(): LiveData<MediaViewItem> {
        return mediaViewItem
    }
}