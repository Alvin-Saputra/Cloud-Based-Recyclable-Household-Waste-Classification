package com.example.cloud_based_recyclable_household_waste_classification.data.remote.response

import com.google.gson.annotations.SerializedName

data class SaveUserClassificationResponse(

	@field:SerializedName("image_url")
	val imageUrl: String,

	@field:SerializedName("document_id")
	val documentId: String,

	@field:SerializedName("message")
	val message: String
)
