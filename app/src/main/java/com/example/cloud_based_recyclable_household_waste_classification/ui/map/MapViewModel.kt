package com.example.cloud_based_recyclable_household_waste_classification.ui.map

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cloud_based_recyclable_household_waste_classification.BuildConfig
import com.example.cloud_based_recyclable_household_waste_classification.R
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.ClassificationResponse
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.Place
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.PlaceDetails
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.PlaceDetailsResponse
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.PlacesResponse
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.retrofit.ApiConfig
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel: ViewModel() {

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

//                withContext(Dispatchers.Main) {
//                    response.results.forEach { place ->
//                        val restaurantLocation = LatLng(
//                            place.geometry.location.lat,
//                            place.geometry.location.lng
//                        )
//
////                         Mendapatkan detail tempat termasuk nomor telepon
//                        val placeDetails = fetchPlaceDetails(place.place_id)
//
//                        // Menambahkan marker untuk restoran
//                        val markerOptions = MarkerOptions()
//                            .position(restaurantLocation)
//                            .title(place.name)
//                            .snippet(place.vicinity)
//                            .icon(vectorToBitmap(R.drawable.baseline_recycling_24, Color.parseColor("#3DDC84")))
//
////                         Menampilkan nomor telepon jika tersedia
//                        placeDetails?.result?.formatted_phone_number?.let {
//                            markerOptions.snippet("${place.vicinity} | Phone: $it")
//                        }
//
//                        mMap.addMarker(markerOptions)
//                    }
//                }
            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(
//                        requireContext(),
//                        "Error fetching restaurants: ${e.message}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
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