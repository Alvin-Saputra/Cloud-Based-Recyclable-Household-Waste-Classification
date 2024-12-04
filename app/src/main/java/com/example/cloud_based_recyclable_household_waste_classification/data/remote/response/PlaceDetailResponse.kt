package com.example.cloud_based_recyclable_household_waste_classification.data.remote.response

data class PlaceDetailsResponse(
    val result: PlaceDetails
)

data class PlaceDetails(
    val formatted_phone_number: String?
)
