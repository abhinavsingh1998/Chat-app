package com.emproto.hoabl.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emproto.hoabl.model.MapLocationModel
import com.emproto.hoabl.model.MediaViewItem
import com.emproto.hoabl.repository.InvestmentRepository
import com.emproto.networklayer.request.investment.AddInventoryBody
import com.emproto.networklayer.request.investment.VideoCallBody
import com.emproto.networklayer.request.investment.WatchListBody
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.investment.*
import com.emproto.networklayer.response.promises.HomePagesOrPromise

class InvestmentViewModel(
    private var mapplication: Application,
    private var investmentRepository: InvestmentRepository
) : ViewModel() {

    private var skusLiveData = MutableLiveData<Inventory>()
    private var smartDeals = MutableLiveData<List<PageManagementsOrCollectionOneModel>>()
    private var trendingProjects = MutableLiveData<List<PageManagementsOrCollectionTwoModel>>()
    private var projectid = MutableLiveData<Int>()
    private var mapLocationData = MutableLiveData<LocationInfrastructure>()
    private var oppDocData = MutableLiveData<OpprotunityDoc>()
    private var landSkusData = MutableLiveData<List<InventoryBucketContent>>()
    private var mediaData = MutableLiveData<ProjectCoverImages>()
    private var mediaViewItem = MutableLiveData<MediaViewItem>()
    private var mediaViewListItem = MutableLiveData<List<MediaViewItem>>()
    private var newInvestments = MutableLiveData<List<PageManagementsOrNewInvestment>>()
    private var allInvestments = MutableLiveData<List<ApData>>()
    private var sDLiveData = MutableLiveData<Boolean>()
    private var tPLiveData = MutableLiveData<Boolean>()
    private var nLLiveData = MutableLiveData<Boolean>()
    private var aPLiveData = MutableLiveData<Boolean>()
    private var locationData = MutableLiveData<MapLocationModel>()
    private var mediaContent = ArrayList<MediaViewItem>()
    private var mediaPos = MutableLiveData<Int>()
    private var watchListId = MutableLiveData<Int>()
    private var faqPosition = MutableLiveData<Int>()
    private var isImageActive = MutableLiveData<Boolean>()
    private var isVideoActive = MutableLiveData<Boolean>()
    private var isDroneActive = MutableLiveData<Boolean>()
    private var isThreeSixtyActive = MutableLiveData<Boolean>()

    fun getInvestments(pageType: Int): LiveData<BaseResponse<InvestmentResponse>> {
        return investmentRepository.getInvestments(pageType)
    }

    fun getInvestmentsMediaGallery(projectId: Int): LiveData<BaseResponse<ProjectMediaGalleryResponse>> {
        return investmentRepository.getProjectMediaGalleries(projectId)
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

    fun addWatchList(watchListBody: WatchListBody): LiveData<BaseResponse<WatchListResponse>> {
        return investmentRepository.addWatchList(watchListBody)
    }

    fun deleteWatchList(id: Int): LiveData<BaseResponse<WatchListResponse>> {
        return investmentRepository.deleteWatchList(id)
    }

    fun getAllInventories(id: Int): LiveData<BaseResponse<GetInventoriesResponse>> {
        return investmentRepository.getAllInventories(id)
    }

    fun addInventory(addInventoryBody: AddInventoryBody): LiveData<BaseResponse<WatchListResponse>> {
        return investmentRepository.addInventory(addInventoryBody)
    }

    fun scheduleVideoCall(videoCallBody: VideoCallBody): LiveData<BaseResponse<VideoCallResponse>> {
        return investmentRepository.scheduleVideoCall(videoCallBody)
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

    fun setOpportunityDoc(oppDocData: OpprotunityDoc) {
        this.oppDocData.postValue(oppDocData)
    }

    fun getOpportunityDoc(): LiveData<OpprotunityDoc> {
        return oppDocData
    }

    fun setSkus(skus: List<InventoryBucketContent>) {
        this.landSkusData.postValue(skus)
    }

    fun getSkus(): LiveData<List<InventoryBucketContent>> {
        return landSkusData
    }

    fun setWatchListId(id: Int) {
        this.watchListId.postValue(id)
    }

    fun getWatchListId(): LiveData<Int> {
        return watchListId
    }

    fun setMedia(media: ProjectCoverImages) {
        this.mediaData.postValue(media)
    }

    fun setMediaContent(content: ArrayList<MediaViewItem>) {
        this.mediaContent = content
    }

    fun getMediaContent(): ArrayList<MediaViewItem> {
        return this.mediaContent
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

    fun setMediaListItem(media: List<MediaViewItem>) {
        this.mediaViewListItem.postValue(media)
    }

    fun getMediaListItem(): LiveData<List<MediaViewItem>> {
        return mediaViewListItem
    }

    fun setMediaListPosition(pos: Int) {
        this.mediaPos.postValue(pos)
    }

    fun getMediaListPosition(): LiveData<Int> {
        return mediaPos
    }

    fun setSku(skusData: Inventory) {
        this.skusLiveData.postValue(skusData)
    }

    fun getSku(): LiveData<Inventory> {
        return skusLiveData
    }

    fun setSd(data: Boolean) {
        this.sDLiveData.postValue(data)
    }

    fun getSd(): LiveData<Boolean> {
        return sDLiveData
    }

    fun setTp(data: Boolean) {
        this.tPLiveData.postValue(data)
    }

    fun getTp(): LiveData<Boolean> {
        return tPLiveData
    }

    fun setNl(data: Boolean) {
        this.nLLiveData.postValue(data)
    }

    fun getNl(): LiveData<Boolean> {
        return nLLiveData
    }

    fun setAp(data: Boolean) {
        this.aPLiveData.postValue(data)
    }

    fun getAp(): LiveData<Boolean> {
        return aPLiveData
    }

    fun setMapLocation(data: MapLocationModel) {
        this.locationData.postValue(data)
    }

    fun getMapLocation(): LiveData<MapLocationModel> {
        return locationData
    }

    fun setFaqPosition(pos:Int) {
        this.faqPosition.postValue(pos)
    }

    fun getFaqPosition(): LiveData<Int> {
        return faqPosition
    }

    fun setImageActive(isImageActive:Boolean) {
        this.isImageActive.postValue(isImageActive)
    }

    fun getImageActive(): LiveData<Boolean> {
        return isImageActive
    }

    fun setVideoActive(isVideoActive:Boolean) {
        this.isVideoActive.postValue(isVideoActive)
    }

    fun getVideoActive(): LiveData<Boolean> {
        return isVideoActive
    }

    fun setDroneActive(isDroneActive:Boolean) {
        this.isDroneActive.postValue(isDroneActive)
    }

    fun getDroneActive(): LiveData<Boolean> {
        return isDroneActive
    }

    fun setThreeSixtyActive(isThreeSixtyActive:Boolean) {
        this.isThreeSixtyActive.postValue(isThreeSixtyActive)
    }

    fun getThreeSixtyActive(): LiveData<Boolean> {
        return isThreeSixtyActive
    }

}