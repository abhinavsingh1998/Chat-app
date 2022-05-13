package com.emproto.networklayer.feature

import android.app.Application
import com.emproto.networklayer.response.chats.ChatResponse
import com.emproto.networklayer.di.DataAppModule
import com.emproto.networklayer.di.DataComponent
import com.emproto.networklayer.di.DataModule
import com.emproto.networklayer.ApiService
import com.emproto.networklayer.di.DaggerDataComponent
import com.emproto.networklayer.response.promises.PromisesResponse
import retrofit2.Response
import javax.inject.Inject

/**
 * Home Data Source.
 * All the api in home modules
 * @property application
 */
public class HomeDataSource(val application: Application) {
    @Inject
    lateinit var apiService: ApiService
    private var dataComponent: DataComponent =
        DaggerDataComponent.builder().dataAppModule(DataAppModule(application))
            .dataModule(DataModule(application)).build()

    init {
        dataComponent.inject(this)
    }

    //promises modules apis
    suspend fun getPromisesData(pageType: Int): Response<PromisesResponse> {
        return apiService.getPromises(pageType)
    }
    //chats list api
    suspend fun getChatsList(): Response<ChatResponse> {
        return apiService.getChatsList()
    }


}
