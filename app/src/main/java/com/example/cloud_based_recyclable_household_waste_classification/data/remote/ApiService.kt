package com.example.cloud_based_recyclable_household_waste_classification.data.remote

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST(".") // "." digunakan karena API endpoint hanya base URL
    fun classifyWaste(
        @Part file: MultipartBody.Part
    ): Call<ClassificationResponse>
}