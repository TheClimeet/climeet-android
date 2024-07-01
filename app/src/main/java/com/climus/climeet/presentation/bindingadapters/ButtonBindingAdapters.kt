package com.climus.climeet.presentation.bindingadapters

import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.climus.climeet.R
import com.climus.climeet.presentation.ui.main.global.selectsector.FloorBtnState
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.MyPageAdminRouteFindingUiState

@BindingAdapter("floorBtnState")
fun bindFloorBtnState(btn: Button, state: FloorBtnState) {
    when (state) {
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

@BindingAdapter("clearBtnState")
fun bindClearBtn(btn: Button, state: Boolean) {
    if (state) {
        btn.setBackgroundResource(R.drawable.rect_mainfill_nostroke_10radius)
    } else {
        btn.setBackgroundResource(R.drawable.rect_lightgrayfill_nostroke_10radius)
    }
}


@BindingAdapter("favoriteBtnState")
fun bindFavoriteBtnState(btn: ImageButton, state: Boolean) {
    if(state){
        btn.setImageResource(R.drawable.ic_favorite_on)
    } else {
        btn.setImageResource(R.drawable.ic_favorite_off)
    }
}

@BindingAdapter("bookMarkBtnState")
fun bindBookMarkBtnState(btn: ImageButton, state: Boolean) {
    if(state){
        btn.setImageResource(R.drawable.ic_bookmark_on)
    } else {
        btn.setImageResource(R.drawable.ic_bookmark_off)
    }
}

@BindingAdapter("isEnabledBasedOnLists")
fun setEnabledBasedOnLists(button: AppCompatButton, uiState: MyPageAdminRouteFindingUiState?) {
    uiState?.let {
        val isEnabled = it.levelList.isNotEmpty() && it.sectorList.isNotEmpty() && it.chipList.isNotEmpty()
        button.isEnabled = isEnabled
        val context = button.context
        button.setTextColor(
            ContextCompat.getColor(
                context,
                if (isEnabled) R.color.black else R.color.white
            )
        )
        button.background = ContextCompat.getDrawable(
            context,
            if (isEnabled) R.drawable.rect_mainfill_nostroke_8radius else R.drawable.rect_grey8fill_nostroke_8radius
        )
    }
}