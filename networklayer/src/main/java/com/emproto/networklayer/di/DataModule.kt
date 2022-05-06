package com.emproto.networklayer.di

import android.app.Application
import com.emproto.networklayer.ApiConstants
import com.emproto.networklayer.ApiService
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.preferences.AppPreferenceImp
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class DataModule(private val application: Application) {

    @Provides
    @Singleton
    fun getAppPreference(): AppPreference {
        return AppPreferenceImp(application.applicationContext)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

//        val httpClient = OkHttpClient.Builder()
//        httpClient.connectTimeout(1000, TimeUnit.SECONDS)
//            .readTimeout(100, TimeUnit.SECONDS)
//            .writeTimeout(100, TimeUnit.SECONDS)
//        httpClient.addInterceptor(Interceptor {
//            val request = it.request().newBuilder().addHeader("jwt","").build()
//            return it.proceed(request)
//        })

        val defaultHttpClient: OkHttpClient =
            OkHttpClient.Builder().addInterceptor(ChuckInterceptor(application.applicationContext)).
            addInterceptor(
                Interceptor { chain ->
                    val request: Request = chain.request().newBuilder()
                        .addHeader("jwt", getAppPreference().getToken()).build()
                    chain.proceed(request)
                }).addInterceptor(loggingInterceptor).build()


        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(defaultHttpClient)
            .baseUrl(ApiConstants.BASE_URL_DEV).build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

}