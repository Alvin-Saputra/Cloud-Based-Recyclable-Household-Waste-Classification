package com.example.cloud_based_recyclable_household_waste_classification.ui.custom_view

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.core.content.ContextCompat
import com.example.cloud_based_recyclable_household_waste_classification.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CustomEmailTextInputEditText : TextInputEditText {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val textInputLayout = parent.parent as? TextInputLayout
                textInputLayout?.let {
                    error = null
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Cek apakah mode gelap atau terang
                val currentMode = resources.configuration.uiMode and
                        android.content.res.Configuration.UI_MODE_NIGHT_MASK
                val textColor = if (currentMode == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
                    ContextCompat.getColor(context, R.color.white)  // Warna teks untuk Dark Mode
                } else {
                    ContextCompat.getColor(context, R.color.black)  // Warna teks untuk Day Mode
                }

                setTextColor(textColor)

                if (s != null && s.length < 8) {
                    setTextColor(ContextCompat.getColor(context, R.color.red))
                } else {
                    setTextColor(textColor)  // Kembalikan ke warna normal jika panjang teks valid
                }
            }

            override fun afterTextChanged(s: Editable?) {
                val textInputLayout = parent.parent as? TextInputLayout
                if (textInputLayout != null) {
                    if (s != null && !Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                        textInputLayout.error = "Invalid Email Address"
                    } else {
                        textInputLayout.error = null
                    }
                }
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }
}
