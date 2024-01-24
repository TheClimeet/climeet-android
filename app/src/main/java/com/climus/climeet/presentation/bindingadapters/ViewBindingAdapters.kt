package com.climus.climeet.presentation.bindingadapters

import android.view.View
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
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