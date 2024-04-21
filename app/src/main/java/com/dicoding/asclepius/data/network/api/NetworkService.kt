package com.dicoding.asclepius.data.network.api

import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.network.response.NewsResponse
import com.dicoding.asclepius.utils.NEWS_CATEGORY
import com.dicoding.asclepius.utils.NEWS_LANGUAGE
import com.dicoding.asclepius.utils.NEWS_QUERY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    @GET("top-headlines")
    fun getNewsResponse(
        @Query("q") query: String = NEWS_QUERY,
        @Query("category") category: String = NEWS_CATEGORY,
        @Query("language") language: String = NEWS_LANGUAGE,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): Call<NewsResponse>
}