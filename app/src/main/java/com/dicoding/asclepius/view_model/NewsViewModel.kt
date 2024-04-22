package com.dicoding.asclepius.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.network.api.NetworkConfig
import com.dicoding.asclepius.data.network.response.ArticlesItem
import com.dicoding.asclepius.data.network.response.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel : ViewModel() {

    private var _listOfNews = MutableLiveData<List<ArticlesItem>?>()
    val listOfNews: LiveData<List<ArticlesItem>?> = _listOfNews

    private var _isLoadingNewsData = MutableLiveData<Boolean>()
    val isLoadingNewsData: LiveData<Boolean> = _isLoadingNewsData

    private var _errorResult = MutableLiveData<String>()
    val errorResult: LiveData<String> = _errorResult

    init {
        getNewsResponse()
    }

    private fun getNewsResponse() {
        _isLoadingNewsData.value = true
        NetworkConfig.getNetworkService().getNewsResponse().enqueue(
            object : Callback<NewsResponse> {
                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {
                    _isLoadingNewsData.value = false
                    if (response.isSuccessful) {
                        _listOfNews.value = response.body()?.articles
                    } else {
                        _errorResult.value = "Something went wrong while fetching data"
                    }

                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    _isLoadingNewsData.value = false
                    Log.e("NewsViewModel", t.message.toString())
                    _errorResult.value = "Something went wrong while fetching data"
                }

            }
        )
    }
}