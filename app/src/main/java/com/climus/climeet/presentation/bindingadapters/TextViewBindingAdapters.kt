package com.climus.climeet.presentation.bindingadapters

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.databinding.BindingAdapter
import com.climus.climeet.R
import com.climus.climeet.presentation.ui.InputState
import java.lang.Math.round

@BindingAdapter("keyword", "searchResult")
fun bindSearchResult(tv: TextView, keyword: String?, searchResult: String?) {

    keyword?.let {
        searchResult?.let {
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

@BindingAdapter("textHexColor")
fun bindTextHexColor(tv: TextView, hexColor: String) {
    if (hexColor.isNotBlank()) {
        tv.setTextColor(hexColor.toColorInt())
    }
}

@BindingAdapter("selectedColor")
fun bindSelectedColor(tv: TextView, state: Boolean) {
    if (state) {
        tv.setTextColor(ContextCompat.getColor(tv.context, R.color.cm_main))
    } else {
        tv.setTextColor(ContextCompat.getColor(tv.context, R.color.white))
    }
}

@BindingAdapter("compressProgress")
fun bindCompressProgress(tv: TextView, progress: Int) {
    tv.text = "$progress%"
}

@BindingAdapter("socialCount")
fun bindSocialCount(tv: TextView, count: Int) {
    if (count < 1000) {
        tv.text = count.toString()
    } else if (count < 1000000) {
        tv.text = round((count / 1000).toDouble()).toString() + "K"
    } else {
        tv.text = round((count / 1000000).toDouble()).toString() + "M"
    }
}

@BindingAdapter("textFromInt")
fun TextView.bindTextFromInt(value: Int) {
    text = value.toString()
}

@BindingAdapter("textFromFloat")
fun TextView.bindTextFromFloat(value: Float) {
    text = value.toString()
}