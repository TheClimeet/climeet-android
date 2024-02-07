package com.climus.climeet.presentation.customview

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import com.bumptech.glide.Glide
import com.climus.climeet.R

class RouteView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_route_view, this, true)

        val routeImageView: ImageView = findViewById(R.id.route_view_iv_image)
        val routeLevelName: TextView = findViewById(R.id.route_view_tv_level)
        val routeBorderView: ConstraintLayout = findViewById(R.id.route_view_root)


        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RouteView)
        val imgUrl = typedArray.getString(R.styleable.RouteView_imgUrl)
        val levelColor = typedArray.getString(R.styleable.RouteView_levelColor)
        val levelName = typedArray.getString(R.styleable.RouteView_levelName)
        typedArray.recycle()

        Glide.with(context)
            .load(imgUrl)
            .into(routeImageView)

        routeLevelName.text = levelName

        levelColor?.let {
            routeBorderView.backgroundTintList = ColorStateList.valueOf(it.toColorInt())
            routeLevelName.setTextColor(it.toColorInt())
        } ?: run {
            routeBorderView.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
            routeLevelName.setTextColor(ContextCompat.getColor(context, R.color.white))
        }

    }

}