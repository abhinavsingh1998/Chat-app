package com.emproto.hoabl.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emproto.hoabl.repository.PortfolioRepository
import com.emproto.networklayer.request.login.TroubleSigningRequest
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.bookingjourney.BJHeader
import com.emproto.networklayer.response.bookingjourney.BookingJourneyResponse
import com.emproto.networklayer.response.bookingjourney.PaymentHistory
import com.emproto.networklayer.response.bookingjourney.PaymentReceipt
import com.emproto.networklayer.response.ddocument.DDocumentResponse
import com.emproto.networklayer.response.documents.DocumentsResponse
import com.emproto.networklayer.response.login.TroubleSigningResponse
import com.emproto.networklayer.response.portfolio.dashboard.InvestmentHeadingDetails
import com.emproto.networklayer.response.portfolio.dashboard.PortfolioData
import com.emproto.networklayer.response.portfolio.fm.FMResponse
import com.emproto.networklayer.response.portfolio.ivdetails.InvestmentDetailsResponse
import com.emproto.networklayer.response.portfolio.ivdetails.InvestmentInformation
import com.emproto.networklayer.response.portfolio.ivdetails.ProjectExtraDetails
import com.emproto.networklayer.response.portfolio.prtimeline.MediaResponse
import com.emproto.networklayer.response.portfolio.prtimeline.ProjectTimelineResponse
import com.emproto.networklayer.response.profile.AccountsResponse
import com.emproto.networklayer.response.profile.ProfileResponse
import com.emproto.networklayer.response.watchlist.WatchlistData

class PortfolioViewModel(
    private var mapplication: Application,
    private var portfolioRepository: PortfolioRepository,
    private var paymentReceipt: ArrayList<PaymentReceipt> = ArrayList<PaymentReceipt>()

) : ViewModel() {

    private var portfolioData = MutableLiveData<PortfolioData>()
    private lateinit var projectDetails: ProjectExtraDetails
    private var investmentInfo: InvestmentInformation? = null
    private lateinit var paymentHistory: List<PaymentHistory>
    private lateinit var investmentHeadingDetails: InvestmentHeadingDetails
    private var bjHeader: BJHeader? = null

    fun getPortfolioDashboard(refresh: Boolean): LiveData<BaseResponse<PortfolioData>> {
        return portfolioRepository.getPortfolioDashboard(refresh)
    }

    fun setPortfolioData(data: PortfolioData) {
        portfolioData.postValue(data)
    }

    fun getPortfolioData(): LiveData<PortfolioData> {
        return portfolioData
    }

    fun setprojectAddress(address: ProjectExtraDetails) {
        this.projectDetails = address
    }

    fun saveHeadingDetails(headingDetails: InvestmentHeadingDetails) {
        this.investmentHeadingDetails = headingDetails
    }

    fun getHeadingDetails(): InvestmentHeadingDetails {
        return this.investmentHeadingDetails
    }

    fun getprojectAddress(): ProjectExtraDetails {
        return this.projectDetails
    }

    fun setInvestmentInfo(investementInfo: InvestmentInformation) {
        this.investmentInfo = investementInfo
    }

    fun getInvestmentInfo(): InvestmentInformation? {
        return this.investmentInfo
    }

    fun setPaymentHistory(history: List<PaymentHistory>) {
        this.paymentHistory = history
    }

    fun getPaymentHistory(): List<PaymentHistory> {
        return this.paymentHistory
    }

    fun getInvestmentDetails(
        ivId: Int,
        projectId: Int
    ): LiveData<BaseResponse<InvestmentDetailsResponse>> {
        return portfolioRepository.getInvestmentDetails(ivId, projectId)
    }

    fun getDocumentList(projectId: String): LiveData<BaseResponse<DocumentsResponse>> {
        return portfolioRepository.getDocumentsListing(projectId)
    }

    fun getWatchlist(): LiveData<BaseResponse<WatchlistData>> {
        return portfolioRepository.getMyWatchlist()
    }

    fun getUserProfile(): LiveData<BaseResponse<ProfileResponse>> {
        return portfolioRepository.getUserProfile()
    }

    fun getProjectTimeline(id: Int): LiveData<BaseResponse<ProjectTimelineResponse>> {
        return portfolioRepository.getProjectTimeline(id)
    }

    fun getProjectTimelineMedia(
        category: String,
        projectContentId: String
    ): LiveData<BaseResponse<MediaResponse>> {
        return portfolioRepository.getProjectTimelineMedia(category, projectContentId)
    }
    fun getAllPaymentReceipt(): ArrayList<PaymentReceipt>{
        return paymentReceipt
    }
    fun savePaymentReceipt(payment: ArrayList<PaymentReceipt>) {
        paymentReceipt.addAll(payment)
    }

    fun getFacilityManagment(
        plotId: String? = null,
        crmId: String? = null
    ): LiveData<BaseResponse<FMResponse>> {
        return portfolioRepository.getFacilitymanagment(plotId, crmId)
    }

    fun downloadDocument(path: String): LiveData<BaseResponse<DDocumentResponse>> {
        return portfolioRepository.downloadDocument(path)
    }

    fun getBookingJourney(id: Int): LiveData<BaseResponse<BookingJourneyResponse>> {
        return portfolioRepository.getBookingJourney(id)
    }

    fun submitTroubleCase(signingRequest: TroubleSigningRequest): LiveData<BaseResponse<TroubleSigningResponse>> {
        return portfolioRepository.submitTroubleCase(signingRequest)
    }

    fun saveBookingHeader(headerData: BJHeader) {
        this.bjHeader = headerData
    }

    fun getBookingHeader(): BJHeader? {
        return bjHeader
    }

}