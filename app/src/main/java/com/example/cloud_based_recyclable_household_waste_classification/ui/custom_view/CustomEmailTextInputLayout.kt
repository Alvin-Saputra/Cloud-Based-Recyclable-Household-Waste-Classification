package com.example.cloud_based_recyclable_household_waste_classification.ui.custom_view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CustomEmailTextInputLayout : TextInputLayout {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var editText: TextInputEditText? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        hint = editText?.hint
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "email"
    }


}