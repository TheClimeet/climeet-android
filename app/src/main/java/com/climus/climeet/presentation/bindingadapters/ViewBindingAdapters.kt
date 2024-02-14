package com.climus.climeet.presentation.bindingadapters

import android.content.res.ColorStateList
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.databinding.BindingAdapter
import androidx.media3.ui.PlayerView
import com.climus.climeet.R
import com.climus.climeet.presentation.ui.InputState
import com.climus.climeet.presentation.ui.intro.SignUpState

@BindingAdapter("viewStateColor")
fun bindViewStateColor(v: View, state: InputState) {
    when (state) {
        is InputState.Empty -> v.setBackgroundColor(
            ContextCompat.getColor(
                v.context,
                R.color.cm_grey4
            )
        )

        is InputState.Success -> v.setBackgroundColor(
            ContextCompat.getColor(
                v.context,
                R.color.cm_main
            )
        )

        is InputState.Error -> v.setBackgroundColor(
            ContextCompat.getColor(
                v.context,
                R.color.cm_red
            )
        )
    }
}

@BindingAdapter("signUpProgress")
fun bindSignUpProgress(pb: ProgressBar, state: SignUpState){
    when(state){
        is SignUpState.Empty -> {
            pb.visibility = View.INVISIBLE
        }
        is SignUpState.InProgress -> {
            pb.visibility = View.VISIBLE
            pb.progress = state.progress
        }
    }
}

@BindingAdapter("viewHexColor")
fun bindViewHexColor(v : View, hexColor: String) {
    if(hexColor.isNotBlank()){
        v.backgroundTintList = ColorStateList.valueOf(hexColor.toColorInt())
    }
}

@BindingAdapter("playerAlpha")
fun bindPlayerAlpha(pv: PlayerView, state: Boolean){
    if(state){
        pv.alpha = 0.3F
    } else {
        pv.alpha = 1F
    }
}

@BindingAdapter("isDataNull")
fun bindIsDataNull(v: View, data: String?){
    data?.let{
        v.visibility = View.VISIBLE
    } ?: run{
        v.visibility = View.INVISIBLE
    }

}
