package com.example.cloud_based_recyclable_household_waste_classification.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.ApiConfig
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.ArticleResponse
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.ResultsItem
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _articles = MutableLiveData<List<ResultsItem>>()
    val articles: LiveData<List<ResultsItem>> = _articles

    fun getArticles() {
        _isLoading.value = true
        viewModelScope.launch {
            val client = ApiConfig.getArticleApiService().getAllArticles("pub_598649f14ff9210b611c1c5a8a9ad8ff2ba28", "Sampah Plastik")
            client.enqueue(object : Callback<ArticleResponse> {
                override fun onResponse(
                    call: Call<ArticleResponse>,
                    response: Response<ArticleResponse>
                ) {
                    if (response.isSuccessful) {
                        _isLoading.value = false
                        val responseBody = response.body()
                        if (responseBody != null) {
                            Log.d("storiesRequest", "Fetch Stories Success")
                            _articles.value = responseBody.results
                        } else {
                            Log.d("storiesRequest", "Failed Fetch Data")
                        }
                    } else {
                        Log.d("storiesRequest", "storiesRequest: ${response.message()}")
                    }
                }


                override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                    Log.e("storiesRequest", "Error: ${t.message}")
                }
            })
        }
    }
}