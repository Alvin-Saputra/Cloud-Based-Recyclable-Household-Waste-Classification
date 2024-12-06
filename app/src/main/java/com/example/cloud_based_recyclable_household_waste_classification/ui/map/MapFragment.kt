package com.example.cloud_based_recyclable_household_waste_classification.ui.map

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.ViewModelProvider
import com.example.cloud_based_recyclable_household_waste_classification.R
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.PlaceDetailsResponse
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.retrofit.ApiConfig
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.retrofit.ApiService
import com.example.cloud_based_recyclable_household_waste_classification.databinding.FragmentMapBinding
import com.example.cloud_based_recyclable_household_waste_classification.databinding.SavedFragmentBinding
import com.example.cloud_based_recyclable_household_waste_classification.ui.register.RegisterViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MapFragment : Fragment(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var viewModel: MapViewModel
    var phoneNumber = ""
    var placeName = ""
    var address = ""
    private var activeMarker: Marker? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)

        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.placeInfoCard.visibility = View.GONE

        setupLocationClient()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        enableMyLocation()

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mMap.setOnMarkerClickListener { marker ->

            activeMarker?.remove()

            var location = LatLng(
                marker.position.latitude,
                marker.position.longitude
            )

            activeMarker = addMarker(location, marker.title ?: "")

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13f))

            binding.placeInfoCard.visibility = View.VISIBLE
            marker.snippet?.let {
                // Split snippet by delimiter to find phone and vicinity
                val infoParts = it.split("|")
                phoneNumber = infoParts.find { part -> part.contains("Phone:") }
                    ?.substringAfter("Phone:")?.trim() ?: "No phone number available"
                address = infoParts.find { part -> part.contains("Address:") }
                    ?.substringAfter("Address:")?.trim() ?: "No address available"
            }

            placeName = marker.title ?: ""
            binding.tvAddress.text = address
            binding.tvPlaceName.text = placeName
            binding.tvPhoneNumber.text = phoneNumber

            true
        }

        binding.btnWhatsApp.setOnClickListener {
            sendWhatsAppMessage(phoneNumber, "Hello,....")
        }

        binding.btnCall.setOnClickListener {
            dialPhoneNumber(phoneNumber)
        }
        binding.btnMessage.setOnClickListener {
            sendSMS(phoneNumber, "Hello,....")
        }


    }

    private fun setupLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val userLocation = LatLng(it.latitude, it.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12f))
                addMarker(userLocation, "Your Location")
                viewModel.fetchNearbyRestaurants(userLocation)

                mMap.addCircle(
                    CircleOptions()
                        .center(userLocation)
                        .radius(5000.0)
                        .fillColor(0x2208ff08)
                        .strokeColor(Color.BLUE)
                        .strokeWidth(5f)
                )
                viewLifecycleOwner.let { lifecycleOwner ->
                    viewModel.placeResult.observe(viewLifecycleOwner) { places ->
                        viewModel.placeDetailsMap.observe(viewLifecycleOwner) { placeDetailsMap ->
                            places.forEach { place ->
                                val restaurantLocation = LatLng(
                                    place.geometry.location.lat,
                                    place.geometry.location.lng
                                )

                                val placeDetail = placeDetailsMap[place.place_id]

                                val markerOptions = MarkerOptions()
                                    .position(restaurantLocation)
                                    .title(place.name)
                                    .snippet("Address: ${place.vicinity}" +
                                            (placeDetail?.formatted_phone_number?.let { " | Phone: $it" }
                                                ?: ""))
                                    .icon(
                                        vectorToBitmap(
                                            R.drawable.baseline_recycling_24,
                                            Color.parseColor("#3DDC84")
                                        )
                                    )

                                mMap.addMarker(markerOptions)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun addMarker(location: LatLng, title: String, snippet: String? = null): Marker {
        val markerOptions = MarkerOptions().position(location).title(title)
        snippet?.let { markerOptions.snippet(it) }
        return mMap.addMarker(markerOptions)!!
    }


    fun sendWhatsAppMessage(phoneNumber: String, message: String) {
        val uri =
            Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}")
        val intent = Intent(Intent.ACTION_VIEW, uri)

        // Pastikan WhatsApp terinstal
        intent.setPackage("com.whatsapp")

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "WhatsApp tidak terinstal", Toast.LENGTH_SHORT).show()
        }
    }

    fun dialPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)
    }

    fun sendSMS(phoneNumber: String, message: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$phoneNumber")
            putExtra("sms_body", message)
        }
        startActivity(intent)
    }


    private fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
        if (vectorDrawable == null) {
            Log.e("BitmapHelper", "Resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        }
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    //
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

}