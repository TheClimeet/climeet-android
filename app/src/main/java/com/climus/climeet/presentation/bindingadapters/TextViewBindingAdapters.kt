package com.climus.climeet.presentation.bindingadapters

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.climus.climeet.R
import com.climus.climeet.presentation.ui.InputState

@BindingAdapter("keyword", "searchResult")
fun bindSearchResult(tv: TextView, keyword: String, searchResult: String) {

    if (keyword.isNotBlank()) {
        val sIndex = searchResult.indexOf(keyword)
        if (sIndex != -1) {
            val spannable = SpannableString(searchResult)
                .apply {
                    setSpan(
                        ForegroundColorSpan(tv.context.getColor(R.color.cm_main)),
                        sIndex,
                        sIndex + keyword.length,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                    )
                }
            tv.text = spannable
        }
    }

}

@BindingAdapter("helperMessage")
fun bindHelperMessage(tv: TextView, state: InputState) {
    when (state) {
        is InputState.Empty ->
            tv.visibility = View.INVISIBLE

        is InputState.Success -> {
            tv.visibility = View.VISIBLE
            tv.text = state.msg
            tv.setTextColor(ContextCompat.getColor(tv.context, R.color.cm_main))
        }

        is InputState.Error -> {
            tv.visibility = View.VISIBLE
            tv.text = state.msg
            tv.setTextColor(ContextCompat.getColor(tv.context, R.color.cm_red))
        }
    }
}