package com.example.cloud_based_recyclable_household_waste_classification.data.remote

import com.google.gson.annotations.SerializedName

data class ClassificationResponse(

	@field:SerializedName("probability")
	val probability: Double,

	@field:SerializedName("class_name")
	val className: String
)
