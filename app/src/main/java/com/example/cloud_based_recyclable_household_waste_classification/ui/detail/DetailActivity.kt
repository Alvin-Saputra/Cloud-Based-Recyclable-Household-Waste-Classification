package com.example.cloud_based_recyclable_household_waste_classification.ui.detail

import android.animation.AnimatorSet
import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.cloud_based_recyclable_household_waste_classification.ui.main.MainActivity
import com.example.cloud_based_recyclable_household_waste_classification.R
import com.example.cloud_based_recyclable_household_waste_classification.data.pref.UserViewModelFactory
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.ResultsItem
import com.example.cloud_based_recyclable_household_waste_classification.databinding.ActivityDetailBinding
import com.example.cloud_based_recyclable_household_waste_classification.ui.home.HomeFragment
import com.example.cloud_based_recyclable_household_waste_classification.ui.login.LoginActivity
import com.example.cloud_based_recyclable_household_waste_classification.ui.saved.SavedFragment
import com.example.cloud_based_recyclable_household_waste_classification.ui.saved.SavedViewModel
import com.example.cloud_based_recyclable_household_waste_classification.ui.utils.downloadImageAndSave
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private val ViewModel by viewModels<DetailViewModel> {
        UserViewModelFactory.getInstance(this)
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>

    companion object {
        const val KEY_PROB = "key_prob"
        const val KEY_CLASSNAME = "key_class"
        const val KEY_URI = "key_uri"
        const val KEY_URL = "key_url"
        const val KEY_ITEM_ID = "key_item_id"
        const val KEY_SOURCE = "key_source"
        const val KEY_ACTIVE_SAVED = "key_active_saved"
        const val SOURCE_HOME_FRAGMENT = "source_home_fragment"
        const val SOURCE_SAVED_ADAPTER = "source_saved_adapter"
    }

    private var probability: Double = 0.0
    private var className: String = ""
    private var imageUri: Uri? = null
    private var imageUrl: String = ""
    private var itemId: String = ""
    private var activeSaved: Boolean = false
    private var email: String? = null
    private var token: String? = null




    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        ViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)


        ViewModel.getSession().observe(this) { user ->
            if (!user.isLogin || isTokenExpired(user.exp)) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                token = "Bearer ${user.token}"
                email = user.email
            }
        }
        val bundle = intent.extras
        binding.btnReturn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("fragmentToOpen", "SavedFragment")
            }
            startActivity(intent)
            finish()
        }

// ====== Bottom Sheet =====
        bottomSheetBehavior = BottomSheetBehavior.from(binding.sheet)
        bottomSheetBehavior.apply {
            peekHeight = 100 // Height when partially expanded
            state = BottomSheetBehavior.STATE_COLLAPSED // Initial state
        }


        binding.layouts.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.layouts2.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.layouts3.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.layouts4.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        binding.expandable.setOnClickListener {
            toggleVisibility(
                binding.tvDescription,
                binding.tvRecyclingMethod,
                binding.tvBenefits,
                binding.tvRecyclingOutcome
            )
        }

        binding.expandable2.setOnClickListener {
            toggleVisibility(
                binding.tvRecyclingMethod,
                binding.tvDescription,
                binding.tvRecyclingOutcome,
                binding.tvBenefits
            )
        }

        binding.expandable3.setOnClickListener {
            toggleVisibility(
                binding.tvRecyclingOutcome,
                binding.tvDescription,
                binding.tvBenefits,
                binding.tvRecyclingMethod
            )
        }

        binding.expandable4.setOnClickListener {
            toggleVisibility(
                binding.tvBenefits,
                binding.tvRecyclingMethod,
                binding.tvDescription,
                binding.tvRecyclingOutcome
            )
        }

// ====== Bottom Sheet =====


//      ====== Intent Classification Result=====

        if (bundle != null) {
            val source = bundle.getString(KEY_SOURCE)

            if ((source == SOURCE_HOME_FRAGMENT)) {
                imageUri = bundle.getParcelable(KEY_URI)
                probability = bundle.getDouble(KEY_PROB)
                className = bundle.getString(KEY_CLASSNAME) ?: " "
                binding.ivWasteImage.setImageURI(imageUri)
                binding.tvWasteName.text = className
                binding.tvClassificationPercentage.text =
                    String.format(Locale.getDefault(), "%.2f%%", probability * 100)

            } else if ((source == SOURCE_SAVED_ADAPTER)) {
                imageUrl = bundle.getString(KEY_URL) ?: ""
                probability = bundle.getDouble(KEY_PROB)
                className = bundle.getString(KEY_CLASSNAME) ?: " "
                activeSaved = bundle.getBoolean(KEY_ACTIVE_SAVED)
                if (activeSaved) {
                    binding.btnSave.setImageResource(R.drawable.ic_bookmark_active)
                }
                itemId = bundle.getString(KEY_ITEM_ID) ?: ""

                Glide.with(this)
                    .load(imageUrl)
                    .into(binding.ivWasteImage)
                binding.tvWasteName.text = className
                binding.tvClassificationPercentage.text =
                    String.format(Locale.getDefault(), "%.2f%%", probability * 100)
            }

            setWasteInfo(className)
        }


//     ======= Articles=======

        ViewModel.getArticles(className)

        ViewModel.articles.observe(this) { articles ->
            var result = articles
            setArticleData(articles)

        }
//     ======= Articles=======

        playAnimation()


//        ======save data======
        binding.btnSave.setOnClickListener {
            if (activeSaved == false && bundle?.getString(KEY_SOURCE) == SOURCE_HOME_FRAGMENT) {
                // Simpan data
                ViewModel.SaveClassificationResult(
                    this,
                    token ?: "",
                    email ?: "",
                    className,
                    probability,
                    imageUri
                )
                activeSaved = true
                binding.btnSave.setImageResource(R.drawable.ic_bookmark_active)
            }

            else if (activeSaved == true && bundle?.getString(KEY_SOURCE) == SOURCE_HOME_FRAGMENT) {
                // Hapus data
                ViewModel.deleteClassificationResult(email ?: "", itemId, token ?: "")
                activeSaved = false
                binding.btnSave.setImageResource(R.drawable.ic_bookmark_inactive)
            }


            else if (activeSaved == true && bundle?.getString(KEY_SOURCE) == SOURCE_SAVED_ADAPTER) {
                ViewModel.deleteClassificationResult(email ?: "", itemId, token ?: "")
                activeSaved = false
                binding.btnSave.setImageResource(R.drawable.ic_bookmark_inactive)
            }

            else if (activeSaved == false && bundle?.getString(KEY_SOURCE) == SOURCE_SAVED_ADAPTER) {
                val imageUrl = bundle.getString(KEY_URL) ?: ""
                val imageUri = Uri.parse(imageUrl)
                downloadImageAndSave(this, imageUri) { localUri ->
                    localUri?.let {
                        ViewModel.SaveClassificationResult(
                            this,
                            token ?: "",
                            email ?: "",
                            className,
                            probability,
                            it
                        )
                        activeSaved = true
                        binding.btnSave.setImageResource(R.drawable.ic_bookmark_active)
                    } ?: run {
                    }
                }

            }
        }


//        =====save data========

        ViewModel.savingClassfication.observe(this) { data ->
            val result = data
            itemId = data.documentId
        }

        ViewModel.isSuccess.observe(this) { isSuccess ->
            if (isSuccess == true) {
                showToast("Success Saving")
                // Pembaruan UI jika perlu
            }
        }

        ViewModel.isSuccessDelete.observe(this) { isSuccess ->
            if (isSuccess == true) {
                showToast("Success Delete")
                // Pembaruan UI jika perlu
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    private fun setArticleData(listEvents: List<ResultsItem>) {
        val list = listEvents

        binding.rvArticles.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val NewsAdapter = ArticleAdapter(list)
        binding.rvArticles.adapter = NewsAdapter

    }

    private fun toggleVisibility(visibleView: View, vararg hiddenViews: View) {
        if (visibleView.visibility == View.GONE) {
            visibleView.visibility = View.VISIBLE
            hiddenViews.forEach { it.visibility = View.GONE }
        } else {
            visibleView.visibility = View.GONE
        }
    }


    private fun playAnimation() {
        val textSwipeUp =
            ObjectAnimator.ofFloat(binding.textViewSwipeUp, View.ALPHA, 0f, 1f, 0f).apply {
                duration = 2400 // Durasi total animasi (400ms naik, 400ms turun)
                repeatCount = ObjectAnimator.INFINITE // Mengulangi animasi secara terus-menerus
                repeatMode = ObjectAnimator.RESTART // Mengulang dari awal setelah selesai
            }

        AnimatorSet().apply {
            play(textSwipeUp)
            start()
        }
    }

    private fun playAnimationBottomSheet() {
        val bottomSheet = ObjectAnimator.ofFloat(binding.sheet, View.ALPHA, 0f, 1f, 0f).apply {
            duration = 2400 // Durasi total animasi (400ms naik, 400ms turun)
            repeatCount = ObjectAnimator.INFINITE // Mengulangi animasi secara terus-menerus
            repeatMode = ObjectAnimator.RESTART // Mengulang dari awal setelah selesai
        }

        AnimatorSet().apply {
            play(bottomSheet)
            start()
        }
    }

    private fun setWasteInfo(className: String) {
        binding.tvDescription.text =
            getString(resources.getIdentifier("${className}_description", "string", packageName))
        binding.tvBenefits.text = getString(
            resources.getIdentifier(
                "${className}_recycling_benefits",
                "string",
                packageName
            )
        )
        binding.tvRecyclingMethod.text = getString(
            resources.getIdentifier(
                "${className}_recycling_method",
                "string",
                packageName
            )
        )
        binding.tvRecyclingOutcome.text = getString(
            resources.getIdentifier(
                "${className}_recycling_outcome",
                "string",
                packageName
            )
        )


        if (className == "trash") {
            binding.tvDescription.text = getString(
                resources.getIdentifier(
                    "${className}_description",
                    "string",
                    packageName
                )
            )
            binding.tvBenefits.text = getString(
                resources.getIdentifier(
                    "${className}_disposal_suggestion",
                    "string",
                    packageName
                )
            )
            binding.tvRecyclingMethod.text = getString(
                resources.getIdentifier(
                    "${className}_how_to_reduce",
                    "string",
                    packageName
                )
            )

            binding.titleRecyclingMethod.text = "Disposal Suggestion"
            binding.titleRecyclingOutcome.text = "How To Reduce"
        }

    }

    fun isTokenExpired(expirationTime: Int): Boolean {
        // Waktu saat ini dalam detik (timestamp UNIX)
        val currentTime = System.currentTimeMillis() / 1000

        // Periksa apakah waktu saat ini lebih besar dari waktu kedaluwarsa (exp)
        return currentTime > expirationTime
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}