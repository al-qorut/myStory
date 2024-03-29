package com.alqorut.mystory.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.alqorut.mystory.R

class MyPassEditText: AppCompatEditText {

    private lateinit var bg: Drawable
    private var colorHint: Int? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = bg
        setHintTextColor(colorHint as Int)
        hint = context.getString(R.string.password)
        textSize = 17f
    }

    private fun init() {
        bg = ContextCompat.getDrawable(context, R.drawable.bg_edit_text) as Drawable
        colorHint = ContextCompat.getColor(context, R.color.grey)
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isValidPassword(s)) error = context.getString(R.string.eror_password)
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun isValidPassword(password: CharSequence?) = if (password.isNullOrEmpty()) false else password.length >= 8
}