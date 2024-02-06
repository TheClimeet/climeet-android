package com.climus.climeet.presentation.ui

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

internal fun Uri.toMultiPart(context: Context): MultipartBody.Part? {
    getRealPathFromUri(this, context)?.let{
        val file = File(it)
        val requestFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    } ?: run{
        return null
    }
}

internal fun Uri.toVideoMultiPart(context: Context): MultipartBody.Part? {
    getRealPathFromUri(this, context)?.let{
        val file = File(it)
        val requestFile = file.asRequestBody("video/mp4".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    } ?: run{
        return null
    }
}

// 절대경로 변환
private fun getRealPathFromUri(uri: Uri, context: Context): String? {
    return try{
        var filePath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.let {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                filePath = it.getString(columnIndex)
            }
            it.close()
        }
        filePath
    } catch(e: Exception){
       null
    }
}

fun Uri.toVideoThumbnail(context: Context): MultipartBody.Part? {
    return try {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(context, this)
        mediaMetadataRetriever.frameAtTime?.toMultiPart(context)
    } catch (ex: Exception) {
        null
    }
}

fun Bitmap.toMultiPart(context: Context): MultipartBody.Part? {
    return try{
        val filesDir = context.filesDir
        val imageFile = File(filesDir, "image.jpg")
        val os = FileOutputStream(imageFile)
        compress(Bitmap.CompressFormat.JPEG, 100, os)
        val requestFile = imageFile.asRequestBody("image/jpg".toMediaTypeOrNull())
        MultipartBody.Part.createFormData("file", imageFile.name, requestFile)
    } catch(e: Exception){
        null
    }
}

