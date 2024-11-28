package com.example.cloud_based_recyclable_household_waste_classification.data.remote

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @Multipart
    @POST("classify")
    fun classifyWaste(
        @Part file: MultipartBody.Part
    ): Call<ClassificationResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("latest?")
    fun getAllArticles(
        @Query("apikey") apikey: String? = null,
        @Query("q") q: String? = null,
    ): Call<ArticleResponse>

}