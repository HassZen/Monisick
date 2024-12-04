package com.capstone.monisick.ui.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.capstone.monisick.R

class PasswordCustomView : AppCompatEditText {
    private lateinit var passwordIcon: Drawable
    private lateinit var visibilityOnIcon: Drawable
    private lateinit var visibilityOffIcon: Drawable
    private var isPasswordVisible: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        updatePasswordTransformation()
    }

    private fun init() {
        passwordIcon = ContextCompat.getDrawable(context, R.drawable.ic_lock_24) as Drawable
        visibilityOnIcon =
            ContextCompat.getDrawable(context, R.drawable.ic_visibility_on) as Drawable
        visibilityOffIcon =
            ContextCompat.getDrawable(context, R.drawable.ic_visibility_off) as Drawable
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        compoundDrawablePadding = 16
        hint = context.getString(R.string.password_hint)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setAutofillHints(AUTOFILL_HINT_PASSWORD)
        }
        setCompoundDrawablesWithIntrinsicBounds(passwordIcon, null, visibilityOffIcon, null)
        handlePasswordVisibilityToggle()
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty() && s.length < 8) error =
                    context.getString(R.string.password_error)
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handlePasswordVisibilityToggle() {
        setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (right - compoundPaddingRight)) {
                    isPasswordVisible = !isPasswordVisible
                    updatePasswordTransformation()
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }
    }

    private fun updatePasswordTransformation() {
        if (isPasswordVisible) {
            transformationMethod = null
            setCompoundDrawablesWithIntrinsicBounds(passwordIcon, null, visibilityOnIcon, null)
        } else {
            transformationMethod = PasswordTransformationMethod.getInstance()
            setCompoundDrawablesWithIntrinsicBounds(passwordIcon, null, visibilityOffIcon, null)
        }
        setSelection(text?.length ?: 0)
    }
}