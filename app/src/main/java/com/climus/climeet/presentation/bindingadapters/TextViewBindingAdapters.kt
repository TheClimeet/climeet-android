package com.climus.climeet.presentation.bindingadapters

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.climus.climeet.R

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