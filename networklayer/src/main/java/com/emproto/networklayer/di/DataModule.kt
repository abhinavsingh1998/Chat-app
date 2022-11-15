package com.emproto.networklayer.di

import android.app.Application
import com.emproto.networklayer.ApiService
import com.emproto.networklayer.BuildConfig
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.preferences.AppPreferenceImp
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.CipherSuite.Companion.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
import okhttp3.CipherSuite.Companion.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256
import okhttp3.CipherSuite.Companion.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
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
        val httpClient: OkHttpClient =
            UnsafeOkHttpClient.getUnsafeOkHttpClient(getAppPreference().getToken())
//        val spec: ConnectionSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
//            .tlsVersions(TlsVersion.TLS_1_2)
//            .cipherSuites(
//                TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
//                TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
//                TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
//            )
//            .build()
//
//        val defaultHttpClient: OkHttpClient =
//            OkHttpClient.Builder().connectionSpecs(Collections.singletonList(spec)).addInterceptor(
//                Interceptor { chain ->
//                    val request: Request = chain.request().newBuilder()
//                        .addHeader("jwt", getAppPreference().getToken()).addHeader("apptype", "app")
//                        .build()
//                    chain.proceed(request)
//                }).addInterceptor(loggingInterceptor)
//                .callTimeout(60, TimeUnit.SECONDS)
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .writeTimeout(60, TimeUnit.SECONDS)
//                .readTimeout(60, TimeUnit.SECONDS)
//                .build()
//

        return Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().serializeNulls().create()
                )
            )
            .client(httpClient)
            .baseUrl(BuildConfig.BASE_URL).build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

}