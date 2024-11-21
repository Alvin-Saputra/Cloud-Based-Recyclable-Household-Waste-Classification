package com.example.cloud_based_recyclable_household_waste_classification

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cloud_based_recyclable_household_waste_classification.databinding.ActivityDetailBinding
import java.util.Locale

class DetailActivity : AppCompatActivity() {

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

        val bundle = intent.extras
        if (bundle != null) {
            imageUri = bundle.getParcelable(KEY_URI)
            probability = bundle.getDouble(KEY_PROB)
            className = bundle.getString(KEY_CLASSNAME) ?: " "
        }
        binding.ivWasteImage.setImageURI(imageUri)
        binding.tvWasteName.text = className
        binding.tvClassificationPercentage.text = String.format(Locale.getDefault(), "%.2f%%", probability * 100)

        // Set description based on className
        setWasteInfo(className)

        binding.btnReturn.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setWasteInfo(className: String) {
        when (className.lowercase()) {
            "battery" -> {
                // Set the headers and descriptions for battery waste
                binding.tvDescriptionHeader.text = getString(R.string.description_header)
                binding.tvWasteDescription.text = getString(R.string.battery_description)

                binding.tvMethodHeader.text = getString(R.string.method_header)
                binding.tvMethodDescription.text = getString(R.string.battery_recycling_method)

                binding.tvBenefitsHeader.text = getString(R.string.benefits_header)
                binding.tvBenefitsDescription.text = getString(R.string.battery_recycling_benefits)

                binding.tvOutcomeHeader.text = getString(R.string.outcome_header)
                binding.tvOutcomeDescription.text = getString(R.string.battery_recycling_outcome)
            }
            "biological" -> {
                // Set the headers and descriptions for biological waste
                binding.tvDescriptionHeader.text = getString(R.string.description_header)
                binding.tvWasteDescription.text = getString(R.string.biological_description)

                binding.tvMethodHeader.text = getString(R.string.method_header)
                binding.tvMethodDescription.text = getString(R.string.biological_recycling_method)

                binding.tvBenefitsHeader.text = getString(R.string.benefits_header)
                binding.tvBenefitsDescription.text = getString(R.string.biological_recycling_benefits)

                binding.tvOutcomeHeader.text = getString(R.string.outcome_header)
                binding.tvOutcomeDescription.text = getString(R.string.biological_recycling_outcome)
            }
            "brown-glass" -> {
                // Set the headers and descriptions for brown glass waste
                binding.tvDescriptionHeader.text = getString(R.string.description_header)
                binding.tvWasteDescription.text = getString(R.string.brown_glass_description)

                binding.tvMethodHeader.text = getString(R.string.method_header)
                binding.tvMethodDescription.text = getString(R.string.brown_glass_recycling_method)

                binding.tvBenefitsHeader.text = getString(R.string.benefits_header)
                binding.tvBenefitsDescription.text = getString(R.string.brown_glass_recycling_benefits)

                binding.tvOutcomeHeader.text = getString(R.string.outcome_header)
                binding.tvOutcomeDescription.text = getString(R.string.brown_glass_recycling_outcome)
            }
            "cardboard" -> {
                // Set the headers and descriptions for cardboard waste
                binding.tvDescriptionHeader.text = getString(R.string.description_header)
                binding.tvWasteDescription.text = getString(R.string.cardboard_description)

                binding.tvMethodHeader.text = getString(R.string.method_header)
                binding.tvMethodDescription.text = getString(R.string.cardboard_recycling_method)

                binding.tvBenefitsHeader.text = getString(R.string.benefits_header)
                binding.tvBenefitsDescription.text = getString(R.string.cardboard_recycling_benefits)

                binding.tvOutcomeHeader.text = getString(R.string.outcome_header)
                binding.tvOutcomeDescription.text = getString(R.string.cardboard_recycling_outcome)
            }
            "clothes" -> {
                // Set the headers and descriptions for clothes waste
                binding.tvDescriptionHeader.text = getString(R.string.description_header)
                binding.tvWasteDescription.text = getString(R.string.clothes_description)

                binding.tvMethodHeader.text = getString(R.string.method_header)
                binding.tvMethodDescription.text = getString(R.string.clothes_recycling_method)

                binding.tvBenefitsHeader.text = getString(R.string.benefits_header)
                binding.tvBenefitsDescription.text = getString(R.string.clothes_recycling_benefits)

                binding.tvOutcomeHeader.text = getString(R.string.outcome_header)
                binding.tvOutcomeDescription.text = getString(R.string.clothes_recycling_outcome)
            }
            "green-glass" -> {
                // Set the headers and descriptions for green glass waste
                binding.tvDescriptionHeader.text = getString(R.string.description_header)
                binding.tvWasteDescription.text = getString(R.string.green_glass_description)

                binding.tvMethodHeader.text = getString(R.string.method_header)
                binding.tvMethodDescription.text = getString(R.string.green_glass_recycling_method)

                binding.tvBenefitsHeader.text = getString(R.string.benefits_header)
                binding.tvBenefitsDescription.text = getString(R.string.green_glass_recycling_benefits)

                binding.tvOutcomeHeader.text = getString(R.string.outcome_header)
                binding.tvOutcomeDescription.text = getString(R.string.green_glass_recycling_outcome)
            }
            "metal" -> {
                // Set the headers and descriptions for metal waste
                binding.tvDescriptionHeader.text = getString(R.string.description_header)
                binding.tvWasteDescription.text = getString(R.string.metal_description)

                binding.tvMethodHeader.text = getString(R.string.method_header)
                binding.tvMethodDescription.text = getString(R.string.metal_recycling_method)

                binding.tvBenefitsHeader.text = getString(R.string.benefits_header)
                binding.tvBenefitsDescription.text = getString(R.string.metal_recycling_benefits)

                binding.tvOutcomeHeader.text = getString(R.string.outcome_header)
                binding.tvOutcomeDescription.text = getString(R.string.metal_recycling_outcome)
            }
            "paper" -> {
                // Set the headers and descriptions for paper waste
                binding.tvDescriptionHeader.text = getString(R.string.description_header)
                binding.tvWasteDescription.text = getString(R.string.paper_description)

                binding.tvMethodHeader.text = getString(R.string.method_header)
                binding.tvMethodDescription.text = getString(R.string.paper_recycling_method)

                binding.tvBenefitsHeader.text = getString(R.string.benefits_header)
                binding.tvBenefitsDescription.text = getString(R.string.paper_recycling_benefits)

                binding.tvOutcomeHeader.text = getString(R.string.outcome_header)
                binding.tvOutcomeDescription.text = getString(R.string.paper_recycling_outcome)
            }
            "plastic" -> {
                // Set the headers and descriptions for plastic waste
                binding.tvDescriptionHeader.text = getString(R.string.description_header)
                binding.tvWasteDescription.text = getString(R.string.plastic_description)

                binding.tvMethodHeader.text = getString(R.string.method_header)
                binding.tvMethodDescription.text = getString(R.string.plastic_recycling_method)

                binding.tvBenefitsHeader.text = getString(R.string.benefits_header)
                binding.tvBenefitsDescription.text = getString(R.string.plastic_recycling_benefits)

                binding.tvOutcomeHeader.text = getString(R.string.outcome_header)
                binding.tvOutcomeDescription.text = getString(R.string.plastic_recycling_outcome)
            }
            "white-glass" -> {
                // Set the headers and descriptions for white glass waste
                binding.tvDescriptionHeader.text = getString(R.string.description_header)
                binding.tvWasteDescription.text = getString(R.string.white_glass_description)

                binding.tvMethodHeader.text = getString(R.string.method_header)
                binding.tvMethodDescription.text = getString(R.string.white_glass_recycling_method)

                binding.tvBenefitsHeader.text = getString(R.string.benefits_header)
                binding.tvBenefitsDescription.text = getString(R.string.white_glass_recycling_benefits)

                binding.tvOutcomeHeader.text = getString(R.string.outcome_header)
                binding.tvOutcomeDescription.text = getString(R.string.white_glass_recycling_outcome)
            }
            "shoes" -> {
                // Set the headers and descriptions for shoes waste
                binding.tvDescriptionHeader.text = getString(R.string.description_header)
                binding.tvWasteDescription.text = getString(R.string.shoes_description)

                binding.tvMethodHeader.text = getString(R.string.method_header)
                binding.tvMethodDescription.text = getString(R.string.shoes_recycling_method)

                binding.tvBenefitsHeader.text = getString(R.string.benefits_header)
                binding.tvBenefitsDescription.text = getString(R.string.shoes_recycling_benefits)

                binding.tvOutcomeHeader.text = getString(R.string.outcome_header)
                binding.tvOutcomeDescription.text = getString(R.string.shoes_recycling_outcome)
            }
            else -> {
                // Default case for unknown waste type
                binding.tvDescriptionHeader.text = getString(R.string.description_header)
                binding.tvWasteDescription.text = getString(R.string.waste_description)

                binding.tvMethodHeader.text = getString(R.string.method_header)
                binding.tvMethodDescription.text = getString(R.string.recycling_method)

                binding.tvBenefitsHeader.text = getString(R.string.benefits_header)
                binding.tvBenefitsDescription.text = getString(R.string.recycling_benefits)

                binding.tvOutcomeHeader.text = getString(R.string.outcome_header)
                binding.tvOutcomeDescription.text = getString(R.string.recycling_outcome)
            }
        }
    }
}
