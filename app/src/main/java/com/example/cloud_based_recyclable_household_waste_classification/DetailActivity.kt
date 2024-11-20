package com.example.cloud_based_recyclable_household_waste_classification

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cloud_based_recyclable_household_waste_classification.databinding.ActivityDetailBinding

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
        if (bundle!=null) {
            imageUri = bundle.getParcelable<Uri>(KEY_URI)
            probability = bundle.getDouble(KEY_PROB)
            className = bundle.getString(KEY_CLASSNAME)?: " "
        }
        binding.ivWasteImage.setImageURI(imageUri!!)
        binding.tvWasteName.text = className
        binding.tvClassificationPercentage.text = String.format("%.2f%%", probability * 100)

        binding.btnReturn.setOnClickListener{
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}