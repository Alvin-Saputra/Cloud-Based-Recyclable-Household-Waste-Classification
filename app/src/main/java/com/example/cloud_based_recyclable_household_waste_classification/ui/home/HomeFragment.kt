package com.example.cloud_based_recyclable_household_waste_classification.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cloud_based_recyclable_household_waste_classification.databinding.FragmentHomeBinding
import com.example.cloud_based_recyclable_household_waste_classification.ui.utils.getImageUri

class HomeFragment : Fragment() {

    companion object {
        private const val REQUIRED_PERMISSION_CAMERA = Manifest.permission.CAMERA
        private const val REQUIRED_PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
        private const val REQUIRED_PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
        private const val REQUIRED_PERMISSION_READ_MEDIA_IMAGES = Manifest.permission.READ_MEDIA_IMAGES
    }

    private var _binding: FragmentHomeBinding? = null

    private var currentImageUri: Uri? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[REQUIRED_PERMISSION_CAMERA] == true) {
                Toast.makeText(requireContext(), "Camera permission granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_LONG).show()
            }

            if (permissions[REQUIRED_PERMISSION_WRITE_EXTERNAL_STORAGE] == true || permissions[REQUIRED_PERMISSION_READ_EXTERNAL_STORAGE] == true) {
                Toast.makeText(requireContext(), "Storage permission granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Storage permission denied", Toast.LENGTH_LONG).show()
            }

            if (permissions[REQUIRED_PERMISSION_READ_MEDIA_IMAGES] == true) {
                Toast.makeText(requireContext(), "Read Media Images permission granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Read Media Images denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(requireContext(), REQUIRED_PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED
        val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            true
        } else {
            ContextCompat.checkSelfPermission(requireContext(), REQUIRED_PERMISSION_WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(requireContext(), REQUIRED_PERMISSION_READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
        val readMediaImages = ContextCompat.checkSelfPermission(requireContext(), REQUIRED_PERMISSION_READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
        return cameraPermission && storagePermission && readMediaImages
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(
                arrayOf(REQUIRED_PERMISSION_CAMERA, REQUIRED_PERMISSION_WRITE_EXTERNAL_STORAGE, REQUIRED_PERMISSION_READ_EXTERNAL_STORAGE, REQUIRED_PERMISSION_READ_MEDIA_IMAGES)
            )
        }

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        binding.buttonGallery.setOnClickListener{
            startGallery()
        }

        binding.buttonCamera.setOnClickListener{
            startCamera()
        }

        return root
    }


    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
//            currentImageUri = uri
//            showImage()

        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }


    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
//            showImage()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}