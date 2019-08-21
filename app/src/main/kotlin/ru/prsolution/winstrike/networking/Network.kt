package ru.prsolution.winstrike.networking

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import java.util.concurrent.TimeUnit

fun createNetworkClient(baseUrl: String, debug: Boolean = false) =
        retrofitClient(baseUrl, httpClient(debug))

private fun httpClient(debug: Boolean): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    val clientBuilder = OkHttpClient.Builder()
//    if (debug) {
//        clientBuilder.addInterceptor(httpLoggingInterceptor)
//        clientBuilder.addInterceptor(ChuckInterceptor(App.instance))
        clientBuilder
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor {
            val newRequest = it.request().newBuilder()
                .addHeader("Authorization", "Bearer ${PrefUtils.token}")
                .build()
            it.proceed(newRequest)
//        }
    }
    with(clientBuilder) {
        connectTimeout(120, TimeUnit.SECONDS)
        readTimeout(120, TimeUnit.SECONDS)
        writeTimeout(90, TimeUnit.SECONDS)
    }
    return clientBuilder.build()
}

private fun retrofitClient(baseUrl: String, httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
