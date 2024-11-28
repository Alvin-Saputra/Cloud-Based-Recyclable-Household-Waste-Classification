package com.example.cloud_based_recyclable_household_waste_classification.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.cloud_based_recyclable_household_waste_classification.data.pref.UserViewModelFactory
import com.example.cloud_based_recyclable_household_waste_classification.ui.detail.DetailActivity
import com.example.cloud_based_recyclable_household_waste_classification.databinding.FragmentHomeBinding
import com.example.cloud_based_recyclable_household_waste_classification.ui.login.LoginActivity
import com.example.cloud_based_recyclable_household_waste_classification.ui.utils.getImageUri

class HomeFragment : Fragment() {
//    private lateinit var viewModel: HomeViewModel
    companion object {
        private const val REQUIRED_PERMISSION_CAMERA = Manifest.permission.CAMERA
        private const val REQUIRED_PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
        private const val REQUIRED_PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
        private const val REQUIRED_PERMISSION_READ_MEDIA_IMAGES = Manifest.permission.READ_MEDIA_IMAGES
    }


    private val viewModel by viewModels<HomeViewModel> {
        UserViewModelFactory.getInstance(requireContext())
    }

    private var _binding: FragmentHomeBinding? = null

    private var token = ""

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

//        val homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            } else {
                token = "Bearer ${user.token}"
            }
        }

        binding.progressBar.visibility = View.GONE

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(
                arrayOf(REQUIRED_PERMISSION_CAMERA, REQUIRED_PERMISSION_WRITE_EXTERNAL_STORAGE, REQUIRED_PERMISSION_READ_EXTERNAL_STORAGE, REQUIRED_PERMISSION_READ_MEDIA_IMAGES)
            )
        }

//        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding.buttonGallery.setOnClickListener{
            startGallery()
        }

        binding.buttonCamera.setOnClickListener{
            startCamera()
        }

        binding.buttonClassify.setOnClickListener{
            viewModel.uploadImage(requireContext())
        }



        viewModel.isLoading.observe(viewLifecycleOwner) { loadingState ->
            if (loadingState == true) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.isSuccess.observe(viewLifecycleOwner){ isSuccess ->

            if(isSuccess == true){
                viewModel.classificationResult.observe(viewLifecycleOwner){ result ->

                    val classificationres = result
                    Log.d("HomeFragment", "Hasil Klasifikasi: $classificationres")

                    result?.let{
                        val intent = Intent(requireContext(), DetailActivity::class.java)
                        intent.putExtra(DetailActivity.KEY_PROB, result.probability)
                        intent.putExtra(DetailActivity.KEY_CLASSNAME, result.className)
                        intent.putExtra(DetailActivity.KEY_URI, viewModel.currentImageUri)
                        startActivity(intent)
                    }

                }
            }

        }

        return root
    }

    private fun finish() {
        TODO("Not yet implemented")
    }


    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.currentImageUri = uri
            showImage()

        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }


    private fun startCamera() {
        viewModel.currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        viewModel.currentImageUri?.let { uri->
            Log.d("Image URI", "showImage: $uri")
            binding.imageView.setImageURI(uri)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}