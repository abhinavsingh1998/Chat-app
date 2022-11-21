package com.emproto.hoabl.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emproto.core.BaseRepository
import com.emproto.core.Constants
import com.emproto.networklayer.feature.HomeDataSource
import com.emproto.networklayer.feature.PortfolioDataSource
import com.emproto.networklayer.feature.ProfileDataSource
import com.emproto.networklayer.request.profile.EditUserNameRequest
import com.emproto.networklayer.request.profile.FeedBackRequest
import com.emproto.networklayer.request.profile.ReportSecurityRequest
import com.emproto.networklayer.request.profile.WhatsappConsentBody
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.ddocument.DDocumentResponse
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.fm.FmUploadResponse
import com.emproto.networklayer.response.investment.FaqDetailResponse
import com.emproto.networklayer.response.login.TroubleSigningResponse
import com.emproto.networklayer.response.portfolio.fm.FMResponse
import com.emproto.networklayer.response.profile.*
import com.emproto.networklayer.response.resourceManagment.ProflieResponse
import com.emproto.networklayer.response.terms.TermsConditionResponse
import kotlinx.coroutines.*
import java.io.File
import javax.inject.Inject

class ProfileRepository @Inject constructor(application: Application) :
    BaseRepository(application) {
    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + parentJob)
    val termsConditionResponse = MutableLiveData<BaseResponse<TermsConditionResponse>>()
    val allprojects = MutableLiveData<BaseResponse<AllProjectsResponse>>()
    val aboutusResponse = MutableLiveData<BaseResponse<ProflieResponse>>()
    val mDocumentsResponse = MutableLiveData<BaseResponse<ProfileResponse>>()

    fun editUserNameProfile(editUserNameRequest: EditUserNameRequest): LiveData<BaseResponse<EditProfileResponse>> {
        val mEditProfileResponse = MutableLiveData<BaseResponse<EditProfileResponse>>()
        mEditProfileResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = ProfileDataSource(application).editProfile(editUserNameRequest)
                if (request.isSuccessful) {
                    mEditProfileResponse.postValue(BaseResponse.success(request.body()!!))
                } else {
                    mEditProfileResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mEditProfileResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return mEditProfileResponse
    }

    fun uploadProfilePicture(
        file: File,
        fileName: String
    ): LiveData<BaseResponse<ProfilePictureResponse>> {
        val mUploadProfilePicture = MutableLiveData<BaseResponse<ProfilePictureResponse>>()
        mUploadProfilePicture.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request =
                    ProfileDataSource(application).uploadPictureProfile(file, fileName)
                if (request.isSuccessful) {
                    mUploadProfilePicture.postValue(BaseResponse.success(request.body()!!))
                } else {
                    mUploadProfilePicture.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mUploadProfilePicture.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return mUploadProfilePicture
    }

    fun uploadKycDocument(
        extension: String,
        file: File,
        selectedDoc: Int
    ): LiveData<BaseResponse<UploadKycResponse>> {
        val mUploadKycDocument = MutableLiveData<BaseResponse<UploadKycResponse>>()
        mUploadKycDocument.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request =
                    ProfileDataSource(application).uploadKycDocument(extension, file, selectedDoc)
                if (request.isSuccessful) {
                    mUploadKycDocument.postValue(BaseResponse.success(request.body()!!))
                } else {
                    mUploadKycDocument.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }


            } catch (e: Exception) {
                mUploadKycDocument.postValue(BaseResponse.error(getErrorMessage(e)))

            }
        }
        return mUploadKycDocument
    }

    fun presignedUrl(
        type: String,
        destinationFile: File
    ): LiveData<BaseResponse<PresignedUrlResponse>> {
        val presignedUrlResponse = MutableLiveData<BaseResponse<PresignedUrlResponse>>()
        presignedUrlResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {

                val request =
                    ProfileDataSource(application).presignedUrl(type, destinationFile)
                if (request.isSuccessful) {
                    presignedUrlResponse.postValue(BaseResponse.success(request.body()!!))
                } else {
                    presignedUrlResponse.postValue(BaseResponse.Companion.error(request.message()))
                }


            } catch (e: Exception) {
                presignedUrlResponse.postValue(BaseResponse.error(getErrorMessage(e)))

            }
        }
        return presignedUrlResponse
    }

    fun getCountries(pageType: Int): LiveData<BaseResponse<ProfileCountriesResponse>> {
        val mCountriesResponse = MutableLiveData<BaseResponse<ProfileCountriesResponse>>()
        mCountriesResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = ProfileDataSource(application).getCountry(pageType)
                if (request.isSuccessful) {
                    if (request.body()!!.data != null)
                        mCountriesResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mCountriesResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mCountriesResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mCountriesResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return mCountriesResponse
    }

    fun getCountries(refresh: Boolean = false): LiveData<BaseResponse<CountryResponse>> {
        val mCountryResponse = MutableLiveData<BaseResponse<CountryResponse>>()
        if ((mCountryResponse.value == null || mCountryResponse.value!!.status == Status.ERROR) || refresh) {
            mCountryResponse.postValue(BaseResponse.loading())
            coroutineScope.launch {
                try {
                    val request = async { ProfileDataSource(application).getCountries() }
                    val countryResponse = request.await()
                    if (countryResponse.isSuccessful) {
                        if (countryResponse.body()!!.data != null)
                            mCountryResponse.postValue(BaseResponse.success(countryResponse.body()!!))
                        else
                            mCountryResponse.postValue(BaseResponse.Companion.error("No data found"))
                    } else {
                        mCountryResponse.postValue(
                            BaseResponse.Companion.error(
                                getErrorMessage(
                                    countryResponse.errorBody()!!.string()
                                )
                            )
                        )
                    }
                } catch (e: Exception) {
                    mCountryResponse.postValue(BaseResponse.error(getErrorMessage(e)))
                }
            }
        }
        return mCountryResponse
    }

    fun getStates(
        countryIsoCode: String,
        refresh: Boolean
    ): LiveData<BaseResponse<StatesResponse>> {
        val mStatesResponse = MutableLiveData<BaseResponse<StatesResponse>>()
        if ((mStatesResponse.value == null || mStatesResponse.value!!.status == Status.ERROR) || refresh) {
            mStatesResponse.postValue(BaseResponse.loading())
            coroutineScope.launch {
                try {
                    val request = async { ProfileDataSource(application).getStates(countryIsoCode) }
                    val stateResponse = request.await()
                    if (stateResponse.isSuccessful) {
                        if (stateResponse.body()!!.data != null)
                            mStatesResponse.postValue(BaseResponse.success(stateResponse.body()!!))
                        else
                            mStatesResponse.postValue(BaseResponse.Companion.error("No data found"))
                    } else {
                        mStatesResponse.postValue(
                            BaseResponse.Companion.error(
                                getErrorMessage(
                                    stateResponse.errorBody()!!.string()
                                )
                            )
                        )
                    }
                } catch (e: Exception) {
                    mStatesResponse.postValue(BaseResponse.error(getErrorMessage(e)))
                }
            }
        }

        return mStatesResponse
    }

    fun getCities(
        stateIsoCode: String,
        countryIsoCode: String, refresh: Boolean
    ): LiveData<BaseResponse<CitiesResponse>> {
        val mCitiesResponse = MutableLiveData<BaseResponse<CitiesResponse>>()
        if ((mCitiesResponse.value == null || mCitiesResponse.value!!.status == Status.ERROR) || refresh) {
            mCitiesResponse.postValue(BaseResponse.loading())
            coroutineScope.launch {
                try {
                    val request = async {
                        ProfileDataSource(application).getCities(
                            stateIsoCode,
                            countryIsoCode
                        )
                    }
                    val cityResponse = request.await()
                    if (cityResponse.isSuccessful) {
                        if (cityResponse.body()!!.data != null)
                            mCitiesResponse.postValue(BaseResponse.success(cityResponse.body()!!))
                        else
                            mCitiesResponse.postValue(BaseResponse.Companion.error("No data found"))
                    } else {
                        mCitiesResponse.postValue(
                            BaseResponse.Companion.error(
                                getErrorMessage(
                                    cityResponse.errorBody()!!.string()
                                )
                            )
                        )
                    }
                } catch (e: Exception) {
                    mCitiesResponse.postValue(BaseResponse.error(getErrorMessage(e)))
                }
            }
        }

        return mCitiesResponse
    }

    fun getUserProfile(refresh: Boolean = false): LiveData<BaseResponse<ProfileResponse>> {
        if ((mDocumentsResponse.value == null || mDocumentsResponse.value!!.status == Status.ERROR) || refresh) {
            mDocumentsResponse.postValue(BaseResponse.loading())
            coroutineScope.launch {
                try {
                    val request = ProfileDataSource(application).getUserProfile()
                    if (request.isSuccessful) {
                        mDocumentsResponse.postValue(BaseResponse.success(request.body()!!))
                    } else {
                        mDocumentsResponse.postValue(
                            BaseResponse.Companion.error(
                                getErrorMessage(
                                    request.errorBody()!!.string()
                                )
                            )
                        )
                    }
                } catch (e: Exception) {
                    mDocumentsResponse.postValue(BaseResponse.error(getErrorMessage(e)))
                }
            }
        }
        return mDocumentsResponse
    }

    fun deleteProfilePicture(): LiveData<BaseResponse<EditProfileResponse>> {
        val deleteResponse = MutableLiveData<BaseResponse<EditProfileResponse>>()
        deleteResponse.postValue(BaseResponse.loading())

        coroutineScope.launch {
            try {
                val request = ProfileDataSource(application).deleteProfilePic()
                if (request.isSuccessful) {
                    if (request.body()!!.data != null) {
                        deleteResponse.postValue(BaseResponse.success(request.body()!!))
                    } else {
                        deleteResponse.postValue(BaseResponse.Companion.error("No Data Found"))
                    }
                } else {
                    deleteResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                deleteResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return deleteResponse
    }

    fun deleteProfileImage(destinationFileName: String): LiveData<BaseResponse<EditProfileResponse>> {
        val deleteResponse = MutableLiveData<BaseResponse<EditProfileResponse>>()
        deleteResponse.postValue(BaseResponse.loading())

        coroutineScope.launch {
            try {
                val request = ProfileDataSource(application).deleteProfileImage(destinationFileName)
                if (request.isSuccessful) {
                    if (request.body()!!.data != null) {
                        deleteResponse.postValue(BaseResponse.success(request.body()!!))
                    } else {
                        deleteResponse.postValue(BaseResponse.Companion.error("No Data Found"))
                    }
                } else {
                    deleteResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                deleteResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return deleteResponse
    }


    fun submitFeedback(feedBackRequest: FeedBackRequest): LiveData<BaseResponse<FeedBackResponse>> {
        val mCaseResponse = MutableLiveData<BaseResponse<FeedBackResponse>>()

        mCaseResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = ProfileDataSource(application).shareFeedBack(feedBackRequest)
                if (request.isSuccessful) {
                    mCaseResponse.postValue(BaseResponse.success(request.body()!!))
                } else {
                    mCaseResponse.postValue(BaseResponse.Companion.error(request.message()))
                }

            } catch (e: Exception) {
                mCaseResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return mCaseResponse
    }

    fun getPrivacyAndPolicy(pageType: Int): LiveData<BaseResponse<TermsConditionResponse>> {
        termsConditionResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = ProfileDataSource(application).getPrivacyAndPolicy(pageType)
                if (request.isSuccessful) {
                    termsConditionResponse.postValue(BaseResponse.success(request.body()!!))
                } else {
                    termsConditionResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }

            } catch (e: Exception) {
                termsConditionResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return termsConditionResponse
    }

    fun getAboutHoaBl(pageType: Int): LiveData<BaseResponse<ProflieResponse>> {
        aboutusResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = ProfileDataSource(application).getAboutHobal(pageType)
                if (request.isSuccessful) {
                    aboutusResponse.postValue(BaseResponse.success(request.body()!!))

                } else {
                    aboutusResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }

            } catch (e: Exception) {
                aboutusResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return aboutusResponse
    }

    fun getFaqList(typeOfFAQ: String): LiveData<BaseResponse<GeneralFaqResponse>> {
        val faqResponse = MutableLiveData<BaseResponse<GeneralFaqResponse>>()
        faqResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = ProfileDataSource(application).getFaqList(typeOfFAQ)
                if (request.isSuccessful) {
                    if (request.body() != null && request.body() is GeneralFaqResponse) {
                        faqResponse.postValue(BaseResponse.success(request.body()!!))

                    } else {
                        faqResponse.postValue(BaseResponse.Companion.error("No data found"))
                    }
                } else {
                    faqResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {

                faqResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return faqResponse
    }

    fun putWhatsappConsent(whatsappConsentBody: WhatsappConsentBody): LiveData<BaseResponse<WhatsappConsentResponse>> {
        val wcResponse = MutableLiveData<BaseResponse<WhatsappConsentResponse>>()
        wcResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = ProfileDataSource(application).putWhatsappConsent(whatsappConsentBody)
                if (request.isSuccessful) {
                    wcResponse.postValue(BaseResponse.success(request.body()!!))
                } else {
                    wcResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }

            } catch (e: Exception) {
                wcResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return wcResponse
    }

    // get All Projects
    fun getAllProjects(refresh: Boolean,status:Boolean): LiveData<BaseResponse<AllProjectsResponse>> {
        if ((allprojects.value == null || allprojects.value!!.status== Status.ERROR) || refresh) {
            allprojects.postValue(BaseResponse.loading())
            coroutineScope.launch {
                try {
                    val request = ProfileDataSource(application).getAllProjects(status)
                    if (request.isSuccessful) {
                        if (request.body()!!.data != null)
                            allprojects.postValue(BaseResponse.success(request.body()!!))
                        else
                            allprojects.postValue(BaseResponse.Companion.error("No data found"))
                    } else {
                        allprojects.postValue(
                            BaseResponse.Companion.error(
                                getErrorMessage(
                                    request.errorBody()!!.string()
                                )
                            )
                        )
                    }
                } catch (e: Exception) {
                    allprojects.postValue(BaseResponse.error(getErrorMessage(e)))
                }
            }
        }
        return allprojects
    }

    fun getSecurityTips(pageType: Int): LiveData<BaseResponse<SecurityTipsResponse>> {
        val stResponse = MutableLiveData<BaseResponse<SecurityTipsResponse>>()
        stResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = ProfileDataSource(application).getSecurityTips(pageType)
                if (request.isSuccessful) {
                    stResponse.postValue(BaseResponse.success(request.body()!!))
                } else {
                    stResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }

            } catch (e: Exception) {
                stResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return stResponse
    }

    fun getGeneralFaqs(categoryType: Int): LiveData<BaseResponse<FaqDetailResponse>> {
        val stResponse = MutableLiveData<BaseResponse<FaqDetailResponse>>()
        stResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = ProfileDataSource(application).getGeneralFaqs(categoryType)
                if (request.isSuccessful) {
                    stResponse.postValue(BaseResponse.success(request.body()!!))
                } else {
                    stResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }

            } catch (e: Exception) {
                stResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return stResponse
    }

    fun submitTroubleCase(signingRequest: ReportSecurityRequest): LiveData<BaseResponse<TroubleSigningResponse>> {
        val mCaseResponse = MutableLiveData<BaseResponse<TroubleSigningResponse>>()

        mCaseResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = ProfileDataSource(application).submitTroubleCase(signingRequest)
                if (request.isSuccessful) {
                    mCaseResponse.postValue(BaseResponse.success(request.body()!!))
                } else {
                    mCaseResponse.postValue(BaseResponse.Companion.error(request.message()))
                }

            } catch (e: Exception) {
                mCaseResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return mCaseResponse
    }

    fun getFacilitymanagment(): LiveData<BaseResponse<FMResponse>> {
        val mDocumentsResponse = MutableLiveData<BaseResponse<FMResponse>>()
        mDocumentsResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = ProfileDataSource(application).getFacilityManagment()
                if (request.isSuccessful) {
                    if (request.body()!!.data != null)
                        mDocumentsResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mDocumentsResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mDocumentsResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mDocumentsResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return mDocumentsResponse
    }

    fun logOutFromCurrent(): LiveData<BaseResponse<LogOutFromCurrentResponse>> {
        val mLogoutFromCurrentResponse = MutableLiveData<BaseResponse<LogOutFromCurrentResponse>>()
        mLogoutFromCurrentResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = ProfileDataSource(application).logOutFromCurrent()
                if (request.isSuccessful) {
                    if (request.body() != null)
                        mLogoutFromCurrentResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mLogoutFromCurrentResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mLogoutFromCurrentResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mLogoutFromCurrentResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return mLogoutFromCurrentResponse
    }

    fun logOutFromAllDevices(): LiveData<BaseResponse<LogOutFromCurrentResponse>> {
        val mLogOutFromAllResponse = MutableLiveData<BaseResponse<LogOutFromCurrentResponse>>()
        mLogOutFromAllResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = ProfileDataSource(application).logOutFromAllDevices()
                if (request.isSuccessful) {
                    if (request.body() != null)
                        mLogOutFromAllResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mLogOutFromAllResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mLogOutFromAllResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mLogOutFromAllResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return mLogOutFromAllResponse
    }

    fun getAccountsList(): LiveData<BaseResponse<AccountsResponse>> {
        val mAccountsResponse = MutableLiveData<BaseResponse<AccountsResponse>>()
        mAccountsResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = HomeDataSource(application).getAccountsList()
                if (request.isSuccessful) {
                    if (request.body() != null && request.body() is AccountsResponse) {
                        mAccountsResponse.postValue(BaseResponse.success(request.body()!!))

                    } else
                        mAccountsResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mAccountsResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {

                mAccountsResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return mAccountsResponse
    }

    fun downloadDocument(path: String): MutableLiveData<BaseResponse<DDocumentResponse>> {
        val mDocumentsResponse = MutableLiveData<BaseResponse<DDocumentResponse>>()
        mDocumentsResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = PortfolioDataSource(application).downloadDocument(path)
                if (request.isSuccessful) {
                    if (request.body()!!.data != null)
                        mDocumentsResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mDocumentsResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mDocumentsResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mDocumentsResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return mDocumentsResponse
    }

    fun uploadFm(
        type: String,
        pageName: String,
        image: File
    ): LiveData<BaseResponse<FmUploadResponse>> {
        val mUploadFmResponse = MutableLiveData<BaseResponse<FmUploadResponse>>()
        mUploadFmResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = ProfileDataSource(application).uploadFm(type, pageName, image)
                if (request.isSuccessful) {
                    if (request.body() != null)
                        mUploadFmResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mUploadFmResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mUploadFmResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mUploadFmResponse.postValue(BaseResponse.error(getErrorMessage(e)))
            }
        }
        return mUploadFmResponse
    }

}








