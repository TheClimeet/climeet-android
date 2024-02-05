package com.climus.climeet.presentation.ui.main.upload

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject


sealed class UploadEvent{

}

@HiltViewModel
class UploadViewModel @Inject constructor(): ViewModel() {

    private val _event = MutableSharedFlow<UploadEvent>()
    val event: SharedFlow<UploadEvent> = _event.asSharedFlow()

    val description = MutableStateFlow("")
    val soundEnabled = MutableStateFlow(false)
    private var thumbnailUri : Uri? = null
    private var videoUri: Uri? = null
    val publicState = MutableStateFlow("")
    val selectCragName = MutableStateFlow("")
    val selectRoute = MutableStateFlow("")


    fun setVideoUri(uri: Uri){
        videoUri = uri
    }



}