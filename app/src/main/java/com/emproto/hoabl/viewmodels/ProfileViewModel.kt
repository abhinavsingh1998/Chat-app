package com.emproto.hoabl.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.emproto.hoabl.repository.ProfileRepository
import com.emproto.networklayer.request.profile.EditUserNameRequest
import com.emproto.networklayer.request.profile.FeedBackRequest
import com.emproto.networklayer.request.profile.ReportSecurityRequest
import com.emproto.networklayer.request.profile.WhatsappConsentBody
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.ddocument.DDocumentResponse
import com.emproto.networklayer.response.investment.FaqDetailResponse
import com.emproto.networklayer.response.login.TroubleSigningResponse
import com.emproto.networklayer.response.portfolio.fm.FMResponse
import com.emproto.networklayer.response.profile.CitiesResponse
import com.emproto.networklayer.response.profile.*
import com.emproto.networklayer.response.resourceManagment.ProflieResponse
import com.emproto.networklayer.response.terms.TermsConditionResponse
import java.io.File

class ProfileViewModel(
    private var mapplication: Application,
    private var mprofileRepository: ProfileRepository
) :
    ViewModel() {

    private var application: Application = mapplication
    private var profileRepository: ProfileRepository = mprofileRepository
    private var paymentHistory = ArrayList<AccountsResponse.Data.PaymentHistory>()

    fun editUserNameProfile(editUserNameRequest: EditUserNameRequest): LiveData<BaseResponse<EditProfileResponse>> {
        return profileRepository.editUserNameProfile(editUserNameRequest)
    }

    fun uploadProfilePicture(
        file: File,
        fileName: String
    ): LiveData<BaseResponse<ProfilePictureResponse>> {
        return profileRepository.uploadProfilePicture(file, fileName)
    }

    fun uploadKycDocument(
        extension: String,
        file: File,
        selectedDoc: Int
    ): LiveData<BaseResponse<UploadKycResponse>> {
        return profileRepository.uploadKycDocument(extension, file, selectedDoc)
    }


    fun presignedUrl(
        type: String,
        destinationFile: File
    ): LiveData<BaseResponse<PresignedUrlResponse>> {
        return profileRepository.presignedUrl(type, destinationFile)
    }

    fun deleteProfilePicture(): LiveData<BaseResponse<EditProfileResponse>> {
        return profileRepository.deleteProfilePicture()
    }

    fun deleteProfileImage(destinationFileName: String): LiveData<BaseResponse<EditProfileResponse>> {
        return profileRepository.deleteProfileImage(destinationFileName)
    }

    fun getCountries(pageType: Int): LiveData<BaseResponse<ProfileCountriesResponse>> {
        return profileRepository.getCountries(pageType)
    }

    fun getUserProfile(): LiveData<BaseResponse<ProfileResponse>> {
        return profileRepository.getUserProfile()
    }

    fun getStates(countryIsoCode: String, refresh: Boolean): LiveData<BaseResponse<StatesResponse>> {
        return profileRepository.getStates(countryIsoCode, refresh)
    }
    fun getCountries(refresh: Boolean = false): LiveData<BaseResponse<CountryResponse>> {
        return profileRepository.getCountries(refresh)
    }

    fun getCities(
        stateIsoCode: String,
        countryIsoCode: String,
        refresh: Boolean=false
    ): LiveData<BaseResponse<CitiesResponse>> {
        return profileRepository.getCities(stateIsoCode, countryIsoCode,refresh)
    }

    fun getPrivacyAndPolicy(pageType: Int): LiveData<BaseResponse<TermsConditionResponse>> {
        return mprofileRepository.getPrivacyAndPolicy(pageType)
    }

    fun submitFeedback(feedBackRequest: FeedBackRequest): LiveData<BaseResponse<FeedBackResponse>> {
        return mprofileRepository.submitFeedback(feedBackRequest)
    }

    fun getAboutHoabl(pageType: Int): LiveData<BaseResponse<ProflieResponse>> {
        return mprofileRepository.getAboutHoaBl(pageType)
    }

    fun getFaqList(typeOfFAQ: String): LiveData<BaseResponse<GeneralFaqResponse>> {
        return profileRepository.getFaqList(typeOfFAQ)
    }

    fun putWhatsappconsent(whatsappConsentBody: WhatsappConsentBody): LiveData<BaseResponse<WhatsappConsentResponse>> {
        return profileRepository.putWhatsappConsent(whatsappConsentBody)
    }

    fun getSecurityTips(pageType: Int): LiveData<BaseResponse<SecurityTipsResponse>> {
        return profileRepository.getSecurityTips(pageType)
    }

    fun getGeneralFaqs(categoryType: Int): LiveData<BaseResponse<FaqDetailResponse>> {
        return profileRepository.getGeneralFaqs(categoryType)
    }

    fun getAllProjects(refresh: Boolean): LiveData<BaseResponse<AllProjectsResponse>> {
        return profileRepository.getAllProjects(refresh)
    }

    fun submitTroubleCase(signingRequest: ReportSecurityRequest): LiveData<BaseResponse<TroubleSigningResponse>> {
        return profileRepository.submitTroubleCase(signingRequest)
    }

    fun getFacilityManagment(): LiveData<BaseResponse<FMResponse>> {
        return profileRepository.getFacilitymanagment()
    }

    fun logOutFromCurrent(): LiveData<BaseResponse<LogOutFromCurrentResponse>> {
        return profileRepository.logOutFromCurrent()
    }

    fun logOutFromAll(): LiveData<BaseResponse<LogOutFromCurrentResponse>> {
        return profileRepository.logOutFromAllDevices()
    }

    fun savePaymentHistory(payment: List<AccountsResponse.Data.PaymentHistory>) {
        paymentHistory.addAll(payment)
    }

    fun getAllPayment(): ArrayList<AccountsResponse.Data.PaymentHistory> {
        return paymentHistory
    }

    fun getAccountsList(): LiveData<BaseResponse<AccountsResponse>> {
        return profileRepository.getAccountsList()
    }

    fun downloadDocument(path: String): LiveData<BaseResponse<DDocumentResponse>> {
        return profileRepository.downloadDocument(path)
    }
}


