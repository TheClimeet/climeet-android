package com.climus.climeet.presentation.ui.main

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

sealed class MainEvent{
    data object GoToGalleryForVideo: MainEvent()
    data class ShowToastMessage(val msg: String) : MainEvent()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    private val _event = MutableSharedFlow<MainEvent>()
    val event: SharedFlow<MainEvent> = _event.asSharedFlow()

    private val _videoUri = MutableSharedFlow<Uri>()
    val videoUri: SharedFlow<Uri> = _videoUri.asSharedFlow()

    private val _shortsThumbnail = MutableSharedFlow<String>()
    val shortsThumbnail: SharedFlow<String> = _shortsThumbnail.asSharedFlow()

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

    fun fileToUrl(file: MultipartBody.Part, type: DataType) {
        viewModelScope.launch {
            repository.uploadFile(file).let {
                when (it) {
                    is BaseState.Success -> {
                        when(type){
                            DataType.SHORTS_THUMBNAIL -> {
                                _shortsThumbnail.emit(it.body.imgUrl)
                            }
                        }
                    }

                    is BaseState.Error -> _event.emit(MainEvent.ShowToastMessage(it.msg))
                }
            }
        }
    }
}

enum class DataType{
    SHORTS_THUMBNAIL
}