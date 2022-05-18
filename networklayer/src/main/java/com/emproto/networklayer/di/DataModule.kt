package com.emproto.networklayer.di

import android.app.Application
import com.emproto.networklayer.ApiConstants
import com.emproto.networklayer.ApiService
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.preferences.AppPreferenceImp
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
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
            OkHttpClient.Builder().addInterceptor(
                Interceptor { chain ->
                    val request: Request = chain.request().newBuilder()
                        .addHeader("apptype","app")
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

    @Provides
    @Singleton
    @Named("dummytoken")
    fun provideDummy(): Retrofit {
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
            OkHttpClient.Builder().addInterceptor(
                Interceptor { chain ->
                    val request: Request = chain.request().newBuilder()
                        .addHeader("jwt", getAppPreference().getToken())
                        .addHeader("apptype","app").build()
                   // .addHeader("jwt", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXlsb2FkIjp7ImlkIjoxLCJlbWFpbCI6Im9ta2FyQGVtcHJvdG8uY29tIiwibGF0ZXN0QWRtaW5Mb2dJZCI6MzAsIm1vZHVsZXMiOltudWxsXSwiaXNzdWVkQXQiOjE2NTE4OTk5NzY5NzUsImV4cGlyZWRBdCI6MTY1MjUwNDc3Njk3NX0sImlhdCI6MTY1MTg5OTk3NiwiZXhwIjoxNjU0NDkxOTc2fQ.Onv30rpwuOk8gxphwljYQd47JZ2M7giMp4qUGXurf0w").build()
                    chain.proceed(request)
                }).addInterceptor(loggingInterceptor).build()


        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(defaultHttpClient)
            .baseUrl(ApiConstants.BASE_URL_DEV).build()
    }

    @Provides
    @Singleton
    @Named("dummy")
    fun dummyApiService(@Named("dummytoken")retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

}