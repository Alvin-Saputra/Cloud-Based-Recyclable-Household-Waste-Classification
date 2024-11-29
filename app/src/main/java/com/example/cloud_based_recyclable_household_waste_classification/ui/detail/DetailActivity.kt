package com.example.cloud_based_recyclable_household_waste_classification.ui.detail

import android.animation.AnimatorSet
import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cloud_based_recyclable_household_waste_classification.ui.main.MainActivity
import com.example.cloud_based_recyclable_household_waste_classification.R
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.ResultsItem
import com.example.cloud_based_recyclable_household_waste_classification.databinding.ActivityDetailBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var ViewModel: DetailViewModel

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>

    companion object {
        const val KEY_PROB = "key_prob"
        const val KEY_CLASSNAME = "key_class"
        const val KEY_URI = "key_uri"
    }

    private var probability: Double = 0.0
    private var className: String = ""
    private var imageUri: Uri? = null

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        binding.btnReturn.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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
            toggleVisibility(binding.tvDescription, binding.tvRecyclingMethod, binding.tvBenefits, binding.tvRecyclingOutcome)
        }

        binding.expandable2.setOnClickListener {
            toggleVisibility(binding.tvRecyclingMethod, binding.tvDescription, binding.tvRecyclingOutcome, binding.tvBenefits)
        }

        binding.expandable3.setOnClickListener {
            toggleVisibility(binding.tvRecyclingOutcome, binding.tvDescription, binding.tvBenefits, binding.tvRecyclingMethod)
        }

        binding.expandable4.setOnClickListener {
            toggleVisibility(binding.tvBenefits, binding.tvRecyclingMethod, binding.tvDescription, binding.tvRecyclingOutcome)
        }

// ====== Bottom Sheet =====



//      ====== Intent Classification Result=====
        val bundle = intent.extras
        if (bundle != null) {
            imageUri = bundle.getParcelable(KEY_URI)
            probability = bundle.getDouble(KEY_PROB)
            className = bundle.getString(KEY_CLASSNAME) ?: " "
        }
        binding.ivWasteImage.setImageURI(imageUri)
        binding.tvWasteName.text = className
        binding.tvClassificationPercentage.text = String.format(Locale.getDefault(), "%.2f%%", probability * 100)

        setWasteInfo(className)


//     ======= Articles=======

        ViewModel.getArticles(className)

        ViewModel.articles.observe(this){ articles ->
            var result = articles
            setArticleData(articles)

        }
//     ======= Articles=======

        playAnimation()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    private fun setArticleData(listEvents: List<ResultsItem>) {
        val list = listEvents

        binding.rvArticles.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
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
        val textSwipeUp = ObjectAnimator.ofFloat(binding.textViewSwipeUp, View.ALPHA, 0f, 1f, 0f).apply {
            duration = 2400 // Durasi total animasi (400ms naik, 400ms turun)
            repeatCount = ObjectAnimator.INFINITE // Mengulangi animasi secara terus-menerus
            repeatMode = ObjectAnimator.RESTART // Mengulang dari awal setelah selesai
        }

        AnimatorSet().apply {
            play(textSwipeUp)
            start()
        }
    }

    private fun playAnimationBottomSheet(){
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
        binding.tvDescription.text = getString(resources.getIdentifier("${className}_description", "string", packageName))
        binding.tvBenefits.text = getString(resources.getIdentifier("${className}_recycling_benefits", "string", packageName))
        binding.tvRecyclingMethod.text = getString(resources.getIdentifier("${className}_recycling_method", "string", packageName))
        binding.tvRecyclingOutcome.text = getString(resources.getIdentifier("${className}_recycling_outcome", "string", packageName))


        if(className == "trash"){
            binding.tvDescription.text = getString(resources.getIdentifier("${className}_description", "string", packageName))
            binding.tvBenefits.text = getString(resources.getIdentifier("${className}_disposal_suggestion", "string", packageName))
            binding.tvRecyclingMethod.text = getString(resources.getIdentifier("${className}_how_to_reduce", "string", packageName))

            binding.titleRecyclingMethod.text = "Disposal Suggestion"
            binding.titleRecyclingOutcome.text = "How To Reduce"
        }

    }

}
