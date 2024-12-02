package com.example.cloud_based_recyclable_household_waste_classification.ui.detail

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.cloud_based_recyclable_household_waste_classification.data.pref.UserModel
import com.example.cloud_based_recyclable_household_waste_classification.data.pref.UserRepository
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.retrofit.ApiConfig
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.ArticleResponse
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.DeleteSavedUserClassificationResponse
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.ResultsItem
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.SaveUserClassificationResponse
import com.example.cloud_based_recyclable_household_waste_classification.ui.utils.uriToFile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel (private val repository: UserRepository): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _articles = MutableLiveData<List<ResultsItem>>()
    val articles: LiveData<List<ResultsItem>> = _articles


    private val _savingClassfication = MutableLiveData<SaveUserClassificationResponse>()
    val savingClassfication: LiveData<SaveUserClassificationResponse> = _savingClassfication

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message


    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isSuccessDelete = MutableLiveData<Boolean>()
    val isSuccessDelete: LiveData<Boolean> = _isSuccessDelete


    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }


    fun getArticles(waste_class: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val client = ApiConfig.getArticleApiService().getAllArticles("pub_598649f14ff9210b611c1c5a8a9ad8ff2ba28", "recycle ${waste_class}")
            client.enqueue(object : Callback<ArticleResponse> {
                override fun onResponse(
                    call: Call<ArticleResponse>,
                    response: Response<ArticleResponse>
                ) {
                    if (response.isSuccessful) {
                        _isLoading.value = false
                        val responseBody = response.body()
                        if (responseBody != null) {
                            Log.d("articleRequest", "Fetch Article Success")
                            _articles.value = responseBody.results
                        } else {
                            Log.d("articleRequest", "Failed Fetch Data")
                        }
                    } else {
                        Log.d("articleRequest", "articleRequest: ${response.message()}")
                    }
                }


                override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                    Log.e("articleRequest", "Error: ${t.message}")
                }
            })
        }
    }


    fun SaveClassificationResult(context: Context, token: String, email:String, className:String, probability: Double, imageUri: Uri?) {
        imageUri?.let { uri ->
            val imageFile = uriToFile(uri, context)
            Log.d("Image File", "showImage: ${imageFile.path}")
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestImageFile
            )

            val emailRequestBody = email.toRequestBody("text/plain".toMediaType())
            val classNameRequestBody = className.toRequestBody("text/plain".toMediaType())
            val probabilityRequestBody = probability.toString().toRequestBody("text/plain".toMediaType())

            viewModelScope.launch {
                _isLoading.value = true
                _isSuccess.value = false
                val client = ApiConfig.getApiService().saveClassification(multipartBody, classNameRequestBody, probabilityRequestBody, emailRequestBody, token)
                client.enqueue(object : Callback<SaveUserClassificationResponse> {
                    override fun onResponse(
                        call: Call<SaveUserClassificationResponse>,
                        response: Response<SaveUserClassificationResponse>
                    ) {
                        if (response.isSuccessful) {
                            _isLoading.value = false
                            val responseBody= response.body()

                            if (responseBody != null) {
                                _isSuccess.value = true
                                _savingClassfication.value = responseBody!!
                            }
                            else{
                                Log.e("SavingClassification", "Response body is null")
                                _isSuccess.value = false
                                _message.value = "Response body is null"
                            }
                        }
                        else {
                            Log.e("SavingClassification", "Response body is null")
                            _isSuccess.value = false
                            _message.value = "Response body is null"
                        }
                    }

                    override fun onFailure(call: Call<SaveUserClassificationResponse>, t: Throwable) {
                        Log.e("SavingClassification", "onFailure: ${t.message}")
                        _isLoading.value = false
                        _isSuccess.value = false
                        _message.value = "onFailure: ${t.message}"
                    }
                })
            }

        } ?: run{_message.value = "No Image Added"}
    }


    fun deleteClassificationResult(email: String, itemId: String, token: String) {
        _isLoading.value = true
        _isSuccessDelete.value = false
        viewModelScope.launch {
            val client = ApiConfig.getApiService().deleteSavedClassification(email, itemId, token)
            client.enqueue(object : Callback<DeleteSavedUserClassificationResponse> {
                override fun onResponse(
                    call: Call<DeleteSavedUserClassificationResponse>,
                    response: Response<DeleteSavedUserClassificationResponse>
                ) {
                    if (response.isSuccessful) {
                        _isLoading.value = false
                        _isSuccessDelete.value = true
                        val responseBody = response.body()
                        if (responseBody != null) {
                            Log.d("articleRequest", "Fetch Article Success")
                            _message.value = responseBody.message
                        } else {
                            Log.d("articleRequest", "Failed Fetch Data")
                            _message.value = "Error Occurred, Try Again"
                        }
                    } else {
                        Log.d("articleRequest", "articleRequest: ${response.message()}")
                        _message.value = "Error Occurred, Try Again"
                    }
                }


                override fun onFailure(call: Call<DeleteSavedUserClassificationResponse>, t: Throwable) {
                    Log.e("articleRequest", "Error: ${t.message}")
                    _message.value = "Error Occurred, Try Again"
                }
            })
        }
    }

}