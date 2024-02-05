package com.climus.climeet.presentation.bindingadapters

import android.widget.Button
import androidx.databinding.BindingAdapter
import com.climus.climeet.R

@BindingAdapter("clearBtnState")
fun bindClearBtn(btn: Button, state: Boolean) {
    if(state) {
        btn.setBackgroundResource(R.drawable.rect_mainfill_nostroke_10radius)
    } else {
        btn.setBackgroundResource(R.drawable.rect_lightgrayfill_nostroke_10radius)
    }
}