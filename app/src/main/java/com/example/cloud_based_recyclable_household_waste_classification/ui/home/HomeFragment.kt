package com.example.cloud_based_recyclable_household_waste_classification.ui.home

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.cloud_based_recyclable_household_waste_classification.R
import com.example.cloud_based_recyclable_household_waste_classification.data.pref.UserViewModelFactory
import com.example.cloud_based_recyclable_household_waste_classification.ui.detail.DetailActivity
import com.example.cloud_based_recyclable_household_waste_classification.databinding.FragmentHomeBinding
import com.example.cloud_based_recyclable_household_waste_classification.ui.login.LoginActivity
import com.example.cloud_based_recyclable_household_waste_classification.ui.utils.getImageUri
import com.example.cloud_based_recyclable_household_waste_classification.ui.utils.saveRotatedBitmap
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yalantis.ucrop.UCrop
import java.io.File

class HomeFragment : Fragment() {
//    private lateinit var viewModel: HomeViewModel
    companion object {
        private const val REQUIRED_PERMISSION_CAMERA = Manifest.permission.CAMERA
        private const val REQUIRED_PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
        private const val REQUIRED_PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
        private const val REQUIRED_PERMISSION_READ_MEDIA_IMAGES = Manifest.permission.READ_MEDIA_IMAGES
        private const val REQUIRED_PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val REQUIRED_PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
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
                Toast.makeText(requireContext(), "Camera permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show()
            }

            if (permissions[REQUIRED_PERMISSION_WRITE_EXTERNAL_STORAGE] == true || permissions[REQUIRED_PERMISSION_READ_EXTERNAL_STORAGE] == true) {
                Toast.makeText(requireContext(), "Storage permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Storage permission denied", Toast.LENGTH_SHORT).show()
            }

            if (permissions[REQUIRED_PERMISSION_READ_MEDIA_IMAGES] == true) {
                Toast.makeText(requireContext(), "Read Media Images permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Read Media Images denied", Toast.LENGTH_SHORT).show()
            }

            if (permissions[REQUIRED_PERMISSION_ACCESS_FINE_LOCATION] == true) {
                Toast.makeText(requireContext(), "Access fine location permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Access fine location denied", Toast.LENGTH_SHORT).show()
            }

            if (permissions[REQUIRED_PERMISSION_ACCESS_COARSE_LOCATION] == true) {
                Toast.makeText(requireContext(), "Access coarse location permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Access coarse location denied", Toast.LENGTH_SHORT).show()
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
        val fineLocationPermission = ContextCompat.checkSelfPermission(requireContext(), REQUIRED_PERMISSION_ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarseLocationPermission = ContextCompat.checkSelfPermission(requireContext(), REQUIRED_PERMISSION_ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return cameraPermission && storagePermission && readMediaImages && fineLocationPermission && coarseLocationPermission
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

        playAnimation()


        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin || isTokenExpired(user.exp)) {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            } else {
                token = "Bearer ${user.token}"
            }
        }

        binding.progressBar.visibility = View.GONE

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(
                arrayOf(REQUIRED_PERMISSION_CAMERA, REQUIRED_PERMISSION_WRITE_EXTERNAL_STORAGE, REQUIRED_PERMISSION_READ_EXTERNAL_STORAGE, REQUIRED_PERMISSION_READ_MEDIA_IMAGES, REQUIRED_PERMISSION_ACCESS_FINE_LOCATION, REQUIRED_PERMISSION_ACCESS_COARSE_LOCATION)
            )
        }

//        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

//        binding.buttonGallery.setOnClickListener{
//            startGallery()
//        }

        binding.btnScan.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(requireContext())
            val bottomSheetView = layoutInflater.inflate(R.layout.item_bottom_sheet, null)

            // Show the black background with animation
            binding.blackBg.apply {
                visibility = View.VISIBLE
                alpha = 0f
                animate().alpha(1f).setDuration(300).start()
            }

            // Add click listeners for camera and gallery options
            bottomSheetView.findViewById<LinearLayout>(R.id.camera).setOnClickListener {
                startCamera()
                bottomSheetDialog.dismiss()
                // Hide the black background with animation
                binding.blackBg.animate().alpha(0f).setDuration(300).withEndAction {
                    binding.blackBg.visibility = View.GONE
                }.start()
            }

            bottomSheetView.findViewById<LinearLayout>(R.id.gallery).setOnClickListener {
                startGallery()
                bottomSheetDialog.dismiss()
                // Hide the black background with animation
                binding.blackBg.animate().alpha(0f).setDuration(300).withEndAction {
                    binding.blackBg.visibility = View.GONE
                }.start()
            }

            // Hide the black background when user clicks on it
            binding.blackBg.setOnClickListener {
                binding.blackBg.animate().alpha(0f).setDuration(300).withEndAction {
                    binding.blackBg.visibility = View.GONE
                }.start()
            }

            bottomSheetDialog.setOnDismissListener {
                // Hide the black background with animation when the dialog is dismissed
                binding.blackBg.animate().alpha(0f).setDuration(300).withEndAction {
                    binding.blackBg.visibility = View.GONE
                }.start()
            }

            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
        }

//
        binding.btnClassify.setOnClickListener{
            if(viewModel.currentImageUri != null){
                viewModel.uploadImage(requireContext(), token)

                // Show the black background with animation
                binding.blackBg.apply {
                    visibility = View.VISIBLE
                    alpha = 0f
                    animate().alpha(1f).setDuration(300).start()
                }
            }
            else if(viewModel.currentImageUri == null){
                showToast("Please Add Your Image")
            }

        }



        viewModel.isLoading.observe(viewLifecycleOwner) { loadingState ->
            if (loadingState == true) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.isSuccess.observe(viewLifecycleOwner){ isSuccess ->

            binding.blackBg.animate().alpha(0f).setDuration(300).withEndAction {
                binding.blackBg.visibility = View.GONE
            }.start()

            if(isSuccess == true){

                viewModel.classificationResult.observe(viewLifecycleOwner){ result ->

                    val classificationres = result
                    Log.d("HomeFragment", "Hasil Klasifikasi: $classificationres")

                    result?.let{
                        val intent = Intent(requireContext(), DetailActivity::class.java)
                        intent.putExtra(DetailActivity.KEY_PROB, result.probability)
                        intent.putExtra(DetailActivity.KEY_CLASSNAME, result.className)
                        intent.putExtra(DetailActivity.KEY_URI, viewModel.currentImageUri)
                        intent.putExtra(DetailActivity.KEY_SOURCE, DetailActivity.SOURCE_HOME_FRAGMENT)
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


    fun isTokenExpired(expirationTime: Int): Boolean {
        // Waktu saat ini dalam detik (timestamp UNIX)
        val currentTime = System.currentTimeMillis() / 1000

        // Periksa apakah waktu saat ini lebih besar dari waktu kedaluwarsa (exp)
        return currentTime > expirationTime
    }


    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.currentImageUri = uri
            startCrop(uri)
            showImage()
            binding.btnClassify.visibility = View.VISIBLE

        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }


    private fun startCamera() {
        viewModel.currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(viewModel.currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
            startCrop(viewModel.currentImageUri!!)
            binding.btnClassify.visibility = View.VISIBLE
        }
    }


    private val cropImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val resultUri = UCrop.getOutput(result.data!!)
            if (resultUri != null) {
                viewModel.currentImageUri = resultUri
                showImage()
            } else {
                showToast("Error Not Valid")
            }
        } else if (result.resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(result.data!!)
            cropError?.let {
                showToast("Error: $it")
            }
        }
    }

    private fun startCrop(uri: Uri) {
        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
        val matrix = Matrix()

        val exif = ExifInterface(requireContext().contentResolver.openInputStream(uri)!!)
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        }

        val rotatedBitmap = Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
        )

        // Save rotated bitmap and continue with UCrop
        val rotatedUri = saveRotatedBitmap(rotatedBitmap, requireContext())

        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, "cropped_image.jpg"))
        val intent = UCrop.of(rotatedUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1024, 1024)
            .getIntent(requireContext())

        cropImage.launch(intent)
    }


    private fun showImage() {
        viewModel.currentImageUri?.let { uri->
            Log.d("Image URI", "showImage: $uri")
            binding.imageView.setImageURI(uri)
            binding.imageViewGreen.visibility = View.VISIBLE

        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun playAnimation() {

        binding.imageView.alpha = 0f
        binding.textViewClassifyOrderTitle?.alpha = 0f
        binding.textViewClassifyOrderSubtitle?.alpha = 0f
        binding.btnScan.alpha = 0f

        val image =
            ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(400)

        val titleText =
            ObjectAnimator.ofFloat(binding.textViewClassifyOrderTitle, View.ALPHA, 1f).setDuration(400)

        val subTitleText =
            ObjectAnimator.ofFloat(binding.textViewClassifyOrderSubtitle, View.ALPHA, 1f).setDuration(400)

        val btn =
            ObjectAnimator.ofFloat(binding.btnScan, View.ALPHA, 1f).setDuration(400)


        AnimatorSet().apply {
            playSequentially(
               image,
                titleText,
                subTitleText,
                btn
            )
            start()
        }
    }
}