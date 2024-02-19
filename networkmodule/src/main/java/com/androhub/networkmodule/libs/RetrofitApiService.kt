package com.androhub.networkmodule.libs

import android.util.Log
import com.androhub.networkmodule.libs.ApiInterface
import com.androhub.networkmodule.MyApplication
import com.androhub.networkmodule.PrefConst
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitApiService {
    companion object {
        var time_out:Long=40
        private var loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        private var interpolator = Interceptor { chain ->
            val original: Request = chain.request()
            // Request customization: add request headers
            Log.e(":language",MyApplication.getAppManager().prefManager.language+"---")
            val requestBuilder: Request.Builder = original.newBuilder()
                    .header(
                            "x-auth",
                        MyApplication.getAppManager().prefManager.getString(PrefConst.PREF_USER_TOKEN_SECURITY)
                    ).header(
                            "lang",
                                   MyApplication.getAppManager().prefManager.language
                    )
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                .header("customerLoginWith","Android")
                .header("Connection", "close")
            val request: Request = requestBuilder.build()
            return@Interceptor chain.proceed(request)
        }
        private var okHttpClient =
            OkHttpClient.Builder().addInterceptor(interpolator)
            .readTimeout(time_out, TimeUnit.SECONDS)
            .connectTimeout(time_out, TimeUnit.SECONDS)
                .addNetworkInterceptor(loggingInterceptor).build()

//Staging
//        private var retrofit = Retrofit.Builder().client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create()).baseUrl(
//                "https://api.staging.balador.io/"
//            ).build()

        private var retrofit = Retrofit.Builder().client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()).baseUrl(
                        "https://api.staging.balador.io/"
                ).build()

        var apiInterface = retrofit.create(ApiInterface::class.java)



    }
}
