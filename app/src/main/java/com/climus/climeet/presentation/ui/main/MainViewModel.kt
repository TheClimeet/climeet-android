package com.climus.climeet.presentation.ui.main

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MainEvent{
    data object GoToGalleryForVideo: MainEvent()
}

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    private val _event = MutableSharedFlow<MainEvent>()
    val event: SharedFlow<MainEvent> = _event.asSharedFlow()

    private val _videoUri = MutableSharedFlow<Uri>()
    val videoUri: SharedFlow<Uri> = _videoUri.asSharedFlow()

    fun goToGalleryForVideo(){
        viewModelScope.launch {
            _event.emit(MainEvent.GoToGalleryForVideo)
        }
    }

    fun setVideoUri(uri: Uri){
        viewModelScope.launch {
            _videoUri.emit(uri)
        }
    }
}