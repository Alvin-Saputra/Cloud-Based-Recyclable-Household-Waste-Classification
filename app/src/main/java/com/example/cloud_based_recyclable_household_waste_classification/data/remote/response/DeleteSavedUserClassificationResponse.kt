package com.example.cloud_based_recyclable_household_waste_classification.data.remote.response

import com.google.gson.annotations.SerializedName

data class DeleteSavedUserClassificationResponse(

	@field:SerializedName("deleted_document_id")
	val deletedDocumentId: String,

	@field:SerializedName("message")
	val message: String
)
