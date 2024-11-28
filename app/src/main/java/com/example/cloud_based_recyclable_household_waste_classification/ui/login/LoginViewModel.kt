package com.example.cloud_based_recyclable_household_waste_classification.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cloud_based_recyclable_household_waste_classification.data.pref.UserModel
import com.example.cloud_based_recyclable_household_waste_classification.data.pref.UserRepository
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.ApiConfig
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginValue = MutableLiveData<LoginResponse>()
    val loginValue: LiveData<LoginResponse> = _loginValue

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun loginRequest(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val client = ApiConfig.getApiService().login(email, password)
            client.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {

                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        _isLoading.value = false
                        if (loginResponse != null) {
                            Log.d("LoginRequest", "Login Success: ${loginResponse.message}")
                            _loginValue.value = loginResponse!!
                            _message.value = loginResponse?.message
                        } else {
                            Log.d("LoginRequest", "Login Failed: ${loginResponse?.message}")
                            _message.value = loginResponse?.message
                        }
                    } else {
                        _isLoading.value = false
//                        Log.d("LoginRequest", "Login Failed: ${response.message()}")
                        _message.value = "Email Has Been Taken or Invalid Password"
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e("LoginRequest", "Error: ${t.message}")
                    _message.value = "Error: No Intenet"
                }
            })
        }
    }

}