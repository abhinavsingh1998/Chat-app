package com.emproto.hoabl.repository


import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emproto.core.BaseRepository
import com.emproto.networklayer.response.chats.ChatResponse
import com.emproto.networklayer.feature.HomeDataSource
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.promises.PromisesResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Exception

class HomeRepository @Inject constructor(application: Application) : BaseRepository(application) {

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + parentJob)


    /**
     * Get all promises
     *
     * @param pageType for promises it #5003
     * @return
     */
    fun getPromises(pageType: Int): LiveData<BaseResponse<PromisesResponse>> {
        val mPromisesResponse = MutableLiveData<BaseResponse<PromisesResponse>>()
        mPromisesResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = HomeDataSource(application).getPromisesData(pageType)
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

    fun getChatsList(): LiveData<BaseResponse<ChatResponse>> {
        val mChatResponse = MutableLiveData<BaseResponse<ChatResponse>>()
        mChatResponse.postValue(BaseResponse.loading())
        coroutineScope.launch {
            try {
                val request = HomeDataSource(application).getChatsList()
                if (request.isSuccessful) {
                    if (request.body()!!.chatList != null)
                        mChatResponse.postValue(BaseResponse.success(request.body()!!))
                    else
                        mChatResponse.postValue(BaseResponse.Companion.error("No data found"))
                } else {
                    mChatResponse.postValue(
                        BaseResponse.Companion.error(
                            getErrorMessage(
                                request.errorBody()!!.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                mChatResponse.postValue(BaseResponse.Companion.error(e.localizedMessage))
            }
        }
        return mChatResponse
    }


}