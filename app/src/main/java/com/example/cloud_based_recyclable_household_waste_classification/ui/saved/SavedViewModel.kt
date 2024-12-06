package com.example.cloud_based_recyclable_household_waste_classification.ui.saved

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
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.ArticleResponse
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.ClassificationResponse
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.GetUserClassificationResponse
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.retrofit.ApiConfig

import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.ListStoryItem
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

class SavedViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _userSavedClassfication = MutableLiveData<List<ListStoryItem>>()
    val userSavedClassfication: LiveData<List<ListStoryItem>> = _userSavedClassfication

    private val _savingClassfication = MutableLiveData<SaveUserClassificationResponse>()
    val savingClassfication: LiveData<SaveUserClassificationResponse> = _savingClassfication

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getSavedClassification(email: String, token:String) {
        _isLoading.value = true
        _isSuccess.value = false
        viewModelScope.launch {
            val client = ApiConfig.getApiService().getSavedClassification(email, token)
            client.enqueue(object : Callback<GetUserClassificationResponse> {
                override fun onResponse(
                    call: Call<GetUserClassificationResponse>,
                    response: Response<GetUserClassificationResponse>
                ) {
                    val responseBody = response.body()
                    if (response.isSuccessful) {
                        _isLoading.value = false
                        _isSuccess.value = true

                        if (responseBody != null) {
                            Log.d("getUserSavedClassificationRequest", "Fetch UserSavedClassificationRequest Success")
                            _userSavedClassfication.value = responseBody.listStory
                            _message.value = "Successfully Fetch Data"
                        } else {
                            _isLoading.value = false
                            Log.d("getUserSavedClassificationRequest", "Failed Fetch Data")
                            _message.value = "Error Occur, Try Again Later"
                        }
                    } else {
                        _isLoading.value = false
                        Log.d("getUserSavedClassificationRequest", "articleRequest: ${response.message()}")
                        _message.value = "No Data Have Been Saved"
                    }
                }


                override fun onFailure(call: Call<GetUserClassificationResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e("getUserSavedClassificationRequest", "Error: ${t.message}")
                    _message.value = "Error Occur, Try Again Later"
                }
            })
        }
    }


}