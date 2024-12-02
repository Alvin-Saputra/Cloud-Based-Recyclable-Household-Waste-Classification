package com.example.cloud_based_recyclable_household_waste_classification.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetUserClassificationResponse(

	@field:SerializedName("listStory")
	val listStory: List<ListStoryItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class ListStoryItem(

	@field:SerializedName("image_url")
	val imageUrl: String,

	@field:SerializedName("probability")
	val probability: Double,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("class_name")
	val className: String,

	@field:SerializedName("email")
	val email: String
)
