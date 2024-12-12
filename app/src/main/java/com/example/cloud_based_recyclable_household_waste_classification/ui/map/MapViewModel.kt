package com.example.cloud_based_recyclable_household_waste_classification.ui.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cloud_based_recyclable_household_waste_classification.BuildConfig
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.Place
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.PlaceDetails
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.PlaceDetailsResponse
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.retrofit.ApiConfig
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel : ViewModel() {

    private val _placeResult = MutableLiveData<List<Place>>()
    val placeResult = _placeResult

    private val _placeDetailsMap = MutableLiveData<Map<String, PlaceDetails>>()
    val placeDetailsMap = _placeDetailsMap


    fun fetchNearbyRestaurants(location: LatLng) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val locationString = "${location.latitude},${location.longitude}"
                val response = ApiConfig.getPlacesApiService().getNearbyRestaurants(
                    location = locationString,
                    radius = 5000,
                    keyword = "Bank Sampah",
                    apiKey = BuildConfig.API_KEY
                )

                val placeDetailsMap = mutableMapOf<String, PlaceDetails>()

                response.results.forEach { place ->
                    val placeDetails = fetchPlaceDetails(place.place_id)
                    placeDetails?.result?.let {
                        placeDetailsMap[place.place_id] = it
                    }
                }

                withContext(Dispatchers.Main) {
                    _placeResult.value = response.results
                    _placeDetailsMap.value = placeDetailsMap
                }


            } catch (e: Exception) {

            }
        }
    }

    suspend fun fetchPlaceDetails(placeId: String): PlaceDetailsResponse? {
        return try {
            val response = ApiConfig.getPlacesApiService().getPlaceDetails(
                placeId = placeId,
                apiKey = "AIzaSyB0st4VqyLg4ThcMafal0tz926SmB_HC8Y"
            )
            response
        } catch (e: Exception) {
            null
        }
    }

}