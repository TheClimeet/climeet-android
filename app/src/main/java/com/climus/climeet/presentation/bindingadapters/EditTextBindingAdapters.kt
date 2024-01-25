package com.climus.climeet.presentation.bindingadapters

import android.content.Context
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.climus.climeet.R
import com.climus.climeet.presentation.ui.InputState


@BindingAdapter("textStateColor")
fun bindTextStateColor(et: EditText, state: InputState) {
    when(state){
        is InputState.Error -> et.setTextColor(ContextCompat.getColor(et.context, R.color.cm_red))
        else -> et.setTextColor(ContextCompat.getColor(et.context, R.color.white))
    }
}