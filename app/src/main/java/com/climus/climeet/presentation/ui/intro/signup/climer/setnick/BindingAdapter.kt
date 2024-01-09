package com.climus.climeet.presentation.ui.intro.signup.climer.setnick

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("textColorResource")
    fun setTextColorResource(textView: TextView, colorResId: Int) {
        textView.setTextColor(ContextCompat.getColor(textView.context, colorResId))
    }
}