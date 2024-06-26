package com.climus.climeet.presentation.ui.main.mypage

import android.util.Log
import okhttp3.MultipartBody

object CameraImageForm {

    private lateinit var image: MultipartBody.Part

    fun setImage(img: MultipartBody.Part) {
        image = img

        Log.d("form", "이미지 파일 저장 : 이름=${img.headers?.get("Content-Disposition")}, " +
                "타입=${img.body.contentType()}, 크기=${img.body.contentLength()}")
    }

    fun getImagePath(): MultipartBody.Part {
        return image
    }
}