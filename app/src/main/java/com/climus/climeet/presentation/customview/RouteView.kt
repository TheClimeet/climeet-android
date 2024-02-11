package com.climus.climeet.presentation.customview

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
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

    private var routeImageView: ImageView
    private var routeLevelName: TextView
    private var routeBorderView: ConstraintLayout

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_route_view, this, true)

        routeImageView = findViewById(R.id.route_view_iv_image)
        routeLevelName= findViewById(R.id.route_view_tv_level)
        routeBorderView = findViewById(R.id.route_view_root)


        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RouteView)
        val imgUrl = typedArray.getString(R.styleable.RouteView_routeImgUrl)
        val levelColor = typedArray.getString(R.styleable.RouteView_routeLevelColor)
        val levelName = typedArray.getString(R.styleable.RouteView_routeLevelName)
        typedArray.recycle()

        setRouteImgUrl(imgUrl)
        setRouteLevelName(levelName)
        setRouteLevelColor(levelColor)
    }

    fun setRouteImgUrl(url: String?){
        Glide.with(context)
            .load(url)
            .into(routeImageView)
    }

    fun setRouteLevelName(name: String?){
        routeLevelName.text = name
    }

    fun setRouteLevelColor(color: String?){
        color?.let {
            if(it.isNotBlank()){
                routeBorderView.backgroundTintList = ColorStateList.valueOf(it.toColorInt())
                routeLevelName.setTextColor(it.toColorInt())
            } else {
                routeBorderView.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
                routeLevelName.setTextColor(ContextCompat.getColor(context, R.color.white))
            }
        } ?: run {
            routeBorderView.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
            routeLevelName.setTextColor(ContextCompat.getColor(context, R.color.white))
        }
    }

}