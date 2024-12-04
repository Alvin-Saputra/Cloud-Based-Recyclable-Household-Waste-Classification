package com.example.cloud_based_recyclable_household_waste_classification.data.remote.response

data class PlacesResponse(
    val results: List<Place>
)

data class Place(
    val name: String,
    val vicinity: String,
    val geometry: Geometry,
    val place_id: String
)

data class Geometry(
    val location: Location
)

data class Location(
    val lat: Double,
    val lng: Double
)
