package com.example.cloud_based_recyclable_household_waste_classification.data.remote.retrofit

import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.ArticleResponse
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.ClassificationResponse
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.DeleteSavedUserClassificationResponse
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.GetUserClassificationResponse
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.LoginResponse
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.RegisterResponse
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.SaveUserClassificationResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @Multipart
    @POST("classify")
    fun classifyWaste(
        @Part file: MultipartBody.Part,
        @Header("Authorization") token: String
    ): Call<ClassificationResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("signup")
    fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @GET("get-data")
    fun getSavedClassification(
        @Query("email") email: String? = null,
        @Header("Authorization") token: String
    ): Call<GetUserClassificationResponse>

    @Multipart
    @POST("save-classify")
    fun saveClassification(
        @Part image: MultipartBody.Part,
        @Part("class_name") className: RequestBody,
        @Part("probability") probability: RequestBody,
        @Part("email") email: RequestBody,
        @Header("Authorization") token: String
    ): Call<SaveUserClassificationResponse>

    @FormUrlEncoded
    @POST("delete-history")
    fun deleteSavedClassification(
        @Field("email") email: String,
        @Field("document_id") document_id: String,
        @Header("Authorization") token: String
    ): Call<DeleteSavedUserClassificationResponse>

    @GET("latest?")
    fun getAllArticles(
        @Query("apikey") apikey: String? = null,
        @Query("q") q: String? = null,
    ): Call<ArticleResponse>

}