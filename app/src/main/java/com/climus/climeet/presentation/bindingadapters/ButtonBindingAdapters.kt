package com.climus.climeet.presentation.bindingadapters

import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.climus.climeet.R
import com.climus.climeet.presentation.ui.main.shorts.bottomsheet.selectsector.FloorBtnState

@BindingAdapter("floorBtnState")
fun bindFloorBtnState(btn: Button, state: FloorBtnState) {
    when(state){
        is FloorBtnState.FloorSelected -> {
            btn.setBackgroundResource(R.drawable.rect_mainfill_nostroke_20radius)
            btn.setTextColor(ContextCompat.getColor(btn.context, R.color.black))
        }
        is FloorBtnState.FloorUnSelected -> {
            btn.setBackgroundResource(R.drawable.rect_silver2fill_nostroke_20radius)
            btn.setTextColor(ContextCompat.getColor(btn.context, R.color.white))
        }
    }
}