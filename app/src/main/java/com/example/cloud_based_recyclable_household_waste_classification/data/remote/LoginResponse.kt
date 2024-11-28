package com.example.cloud_based_recyclable_household_waste_classification.data.remote

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("error")
	val error: String,

	@field:SerializedName("exp")
	val exp: Int,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("user")
	val user: String,

	@field:SerializedName("token")
	val token: String
)
