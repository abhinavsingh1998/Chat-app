package com.emproto.networklayer.feature

class RegistrationDataSource {
   /* @Inject
    var apiService: ApiService? = null

    lateinit var dataComponent: DataComponent


    fun RegistrationDataSource(application: Application) {
        dataComponent = DaggerDataComponent.builder()
            .dataAppModule(DataAppModule(application))
            .dataModule(DataModule(application)).build()
        dataComponent.inject(this)
       *//* sharedPreferences =
            application.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)*//*
//        createApiServiceForProd()
    }*/

  /*  private fun createApiServiceForProd() {
        val loggingInterceptor2 = HttpLoggingInterceptor()
        loggingInterceptor2.level = HttpLoggingInterceptor.Level.BODY
        val httpClient2 = OkHttpClient.Builder()
      *//*  httpClient2.addInterceptor(RegistrationDataSource.BasicAuthInterceptor(
                "admin",
                "admin")
        )*//*
        httpClient2.connectTimeout(1000, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
        httpClient2.addInterceptor(loggingInterceptor2)
        val retrofit2 = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(httpClient2.build())
            .baseUrl(ApiConstants.BASE_URL).build()
        apiServiceTemp = retrofit2.create(ApiService::class.java)
    }

    class BasicAuthInterceptor(user: String?, password: String?) :
        Interceptor {
        private val credentials: String

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val authenticatedRequest = request.newBuilder()
                .header("Authorization", credentials).build()
            return chain.proceed(authenticatedRequest)
        }

        init {
            credentials = Credentials.basic(user, password)
        }
    }
*/

}