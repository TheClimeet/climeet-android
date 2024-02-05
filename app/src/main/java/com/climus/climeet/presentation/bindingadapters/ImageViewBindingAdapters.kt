package com.climus.climeet.presentation.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.climus.climeet.R

@BindingAdapter("profileImgUrl")
fun bindProfileImg(imageView: ImageView, url: String) {
    Glide.with(imageView.context)
        .load(url)
        .error(R.drawable.test)
        .into(imageView)
}

@BindingAdapter("imgUrl")
fun bindImgUrl(imageView: ImageView, url: String?) {
    url?.let{
        Glide.with(imageView.context)
            .load(it)
            .into(imageView)
    }
}

@BindingAdapter("formCheck")
fun bindFormCheck(iv: ImageView, data: String) {
    if (data.isBlank()) {
        iv.setImageResource(R.drawable.ic_check_off)
    } else {
        iv.setImageResource(R.drawable.ic_check_on)
    }

}