package com.emproto.hoabl.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emproto.core.BaseRepository
import com.emproto.networklayer.feature.HomeDataSource
import com.emproto.networklayer.feature.InvestmentDataSource
import com.emproto.networklayer.feature.PortfolioDataSource
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.investment.Data
import com.emproto.networklayer.response.portfolio.PortfolioData
import com.emproto.networklayer.response.promises.PromisesResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class PortfolioRepository @Inject constructor(application: Application) : BaseRepository(application) {

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + parentJob)

    /**
     * Get all investments
     *
     * @param pageType for promises it #5002
     * @return
     */

    fun getPortfolioDashboard(): LiveData<BaseResponse<PortfolioData>> {
        val mPromisesResponse = MutableLiveData<BaseResponse<PortfolioData>>()
        mPromisesResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = PortfolioDataSource(application).getPortfolioDashboard()
                if (request.isSuccessful) {
                    if (request.body()!!.data != null)
                        mPromisesResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mPromisesResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mPromisesResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mPromisesResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
            }
        }
        return mPromisesResponse
    }
}