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

class MyEmailEditText: AppCompatEditText {
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
        hint = context.getString(R.string.email)
        textSize = 18f
    }

    private fun init() {
        bg = ContextCompat.getDrawable(context, R.drawable.bg_edit_text) as Drawable
        colorHint = ContextCompat.getColor(context, R.color.grey)
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && !isValidEmail(s)) error = context.getString(R.string.email_td_sesuai)
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun isValidEmail(email: CharSequence) = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}