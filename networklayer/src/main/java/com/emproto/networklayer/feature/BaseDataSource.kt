package com.emproto.networklayer.feature

import android.app.Application
import com.emproto.networklayer.di.DataAppModule
import com.emproto.networklayer.di.DataComponent
import com.emproto.networklayer.di.DataModule
import com.emproto.networklayer.ApiService
import com.emproto.networklayer.di.DaggerDataComponent
import com.emproto.networklayer.request.refernow.ReferalRequest
import com.emproto.networklayer.response.home.HomeResponse
import com.emproto.networklayer.response.promises.PromisesResponse
import com.emproto.networklayer.response.refer.ReferalResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

/**
 * Base Data Source.
 * All the common end point used accross the sub modules.
 * @property application
 */
public abstract class BaseDataSource(val baseApplication: Application) {
    @Inject
    lateinit var baseService: ApiService

    private var dataComponent: DataComponent =
        DaggerDataComponent.builder().dataAppModule(DataAppModule(baseApplication))
            .dataModule(DataModule(baseApplication)).build()

    init {
        dataComponent.inject(this)
    }

    suspend fun getRefer(referalRequest: ReferalRequest): Response<ReferalResponse> {
        return baseService.referNow(referalRequest)
    }

}
