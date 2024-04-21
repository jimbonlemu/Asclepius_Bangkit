package com.dicoding.asclepius.data.network.api

import com.dicoding.asclepius.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class NetworkConfig {
    companion object {
        fun getNetworkService(): NetworkService {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }
            val clientBuilder = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).addConverterFactory(
                GsonConverterFactory.create())
                .client(clientBuilder)
                .build().create<NetworkService>()

        }
    }
}