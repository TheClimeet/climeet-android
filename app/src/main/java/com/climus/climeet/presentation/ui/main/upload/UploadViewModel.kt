package com.climus.climeet.presentation.ui.main.upload

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.customview.PublicType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class UploadEvent{
    data class ShowPublicBottomSheet(val type: PublicType): UploadEvent()
}

@HiltViewModel
class UploadViewModel @Inject constructor(): ViewModel() {

    private val _event = MutableSharedFlow<UploadEvent>()
    val event: SharedFlow<UploadEvent> = _event.asSharedFlow()

    val description = MutableStateFlow("")
    val soundEnabled = MutableStateFlow(false)
    private var thumbnailUri : Uri? = null
    private var videoUri: Uri? = null
    val publicState = MutableStateFlow(PublicType.EMPTY)
    val selectCragName = MutableStateFlow("")
    val selectRoute = MutableStateFlow("")


    fun setVideoUri(uri: Uri){
        videoUri = uri
    }

    fun showPublicBottomSheet(){
        viewModelScope.launch {
            _event.emit(UploadEvent.ShowPublicBottomSheet(publicState.value))
        }
    }

    fun setPublicState(state: PublicType){
        publicState.update { state }
    }

}