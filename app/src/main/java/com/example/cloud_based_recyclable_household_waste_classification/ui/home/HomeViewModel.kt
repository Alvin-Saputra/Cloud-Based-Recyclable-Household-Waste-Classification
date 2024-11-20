package com.example.cloud_based_recyclable_household_waste_classification.ui.home

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.ApiConfig
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.ClassificationResponse
import com.example.cloud_based_recyclable_household_waste_classification.ui.utils.uriToFile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    var currentImageUri: Uri? = null

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _classificationResult = MutableLiveData<ClassificationResponse?>()
    val classificationResult = _classificationResult

    fun uploadImage(context: Context) {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, context)
            Log.d("Image File", "showImage: ${imageFile.path}")
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "file",
                imageFile.name,
                requestImageFile
            )

            viewModelScope.launch {
                _isLoading.value = true
                _isSuccess.value = false
                val client = ApiConfig.getApiService().classifyWaste(multipartBody)
                client.enqueue(object : Callback<ClassificationResponse> {
                    override fun onResponse(
                        call: Call<ClassificationResponse>,
                        response: Response<ClassificationResponse>
                    ) {
                        if (response.isSuccessful) {
                            _isLoading.value = false
                            val responseBody= response.body()

                            if (responseBody != null) {
                                _isSuccess.value = true
                                _classificationResult.value = responseBody
                            }
                            else{
                                    Log.e(TAG, "Response body is null")
                                    _isSuccess.value = false
                                    _message.value = "Response body is null"
                                }
                            }
                        else {
                            Log.e(TAG, "Response body is null")
                            _isSuccess.value = false
                            _message.value = "Response body is null"
                        }
                    }

                    override fun onFailure(call: Call<ClassificationResponse>, t: Throwable) {
                        Log.e(TAG, "onFailure: ${t.message}")
                        _isLoading.value = false
                        _isSuccess.value = false
                        _message.value = "onFailure: ${t.message}"
                    }
                })
            }

        } ?: run{_message.value = "No Image Added"}
    }
}