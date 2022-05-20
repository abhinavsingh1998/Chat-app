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

    private var skusLiveData = MutableLiveData<InventoryBucketContent>()
    private var smartDeals = MutableLiveData<List<PageManagementsOrCollectionOneModel>>()
    private var trendingProjects = MutableLiveData<List<PageManagementsOrCollectionTwoModel>>()
    private var projectid = MutableLiveData<Int>()
    private var mapLocationData = MutableLiveData<LocationInfrastructure>()
    private var oppDocData = MutableLiveData<List<OpprotunityDoc>>()
    private var landSkusData = MutableLiveData<List<InventoryBucketContent>>()
    private var mediaData  = MutableLiveData<ProjectCoverImages>()
    private var mediaViewItem = MutableLiveData<MediaViewItem>()
    private var newInvestments = MutableLiveData<List<PageManagementsOrNewInvestment>>()
    private var allInvestments = MutableLiveData<List<ApData>>()
    private var sDLiveData = MutableLiveData<Boolean>()
    private var tPLiveData = MutableLiveData<Boolean>()
    private var nLLiveData = MutableLiveData<Boolean>()
    private var aPLiveData = MutableLiveData<Boolean>()

    fun getInvestments(pageType: Int): LiveData<BaseResponse<InvestmentResponse>> {
        return investmentRepository.getInvestments(pageType)
    }

    fun getInvestmentsDetail(id: Int): LiveData<BaseResponse<ProjectDetailResponse>> {
        return investmentRepository.getInvestmentsDetail(id)
    }

    fun getInvestmentsPromises(): LiveData<BaseResponse<InvestmentPromisesResponse>> {
        return investmentRepository.getInvestmentsPromises()
    }

    fun getAllInvestmentsProjects(): LiveData<BaseResponse<AllProjectsResponse>> {
        return investmentRepository.getAllInvestmentsProjects()
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

    fun setNewInvestments(newInvestments: List<PageManagementsOrNewInvestment>) {
        this.newInvestments.postValue(newInvestments)
    }

    fun getNewInvestments(): LiveData<List<PageManagementsOrNewInvestment>> {
        return newInvestments
    }

    fun setAllInvestments(allInvestments: List<ApData>) {
        this.allInvestments.postValue(allInvestments)
    }

    fun getAllInvestments(): LiveData<List<ApData>> {
        return allInvestments
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

    fun setSku(skusData: InventoryBucketContent) {
        this.skusLiveData.postValue(skusData)
    }

    fun getSku(): LiveData<InventoryBucketContent> {
        return skusLiveData
    }

    fun setSd(data:Boolean){
        this.sDLiveData.postValue(data)
    }

    fun getSd(): LiveData<Boolean> {
        return sDLiveData
    }

    fun setTp(data:Boolean){
        this.tPLiveData.postValue(data)
    }

    fun getTp(): LiveData<Boolean> {
        return tPLiveData
    }

    fun setNl(data:Boolean){
        this.nLLiveData.postValue(data)
    }

    fun getNl(): LiveData<Boolean> {
        return nLLiveData
    }

    fun setAp(data:Boolean){
        this.aPLiveData.postValue(data)
    }

    fun getAp(): LiveData<Boolean> {
        return aPLiveData
    }

}