package com.climus.climeet.presentation.ui.main.upload

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.customview.PublicType
import com.climus.climeet.presentation.ui.main.global.selectsector.SelectedSector
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UploadUiState(
    val publicState: PublicType = PublicType.EMPTY,
    val selectedSector: SelectedSector = SelectedSector()
)

sealed class UploadEvent{
    data class ShowPublicBottomSheet(val type: PublicType): UploadEvent()
    data object NavigateToSearchCragBottomSheet : UploadEvent()
    data class ShowToastMessage(val msg: String): UploadEvent()
}

@HiltViewModel
class UploadViewModel @Inject constructor(): ViewModel() {

    private val _event = MutableSharedFlow<UploadEvent>()
    val event: SharedFlow<UploadEvent> = _event.asSharedFlow()

    private val _uiState = MutableStateFlow(UploadUiState())
    val uiState: StateFlow<UploadUiState> = _uiState.asStateFlow()

    val description = MutableStateFlow("")
    val soundEnabled = MutableStateFlow(false)
    private var thumbnailUri : Uri? = null
    private var videoUri: Uri? = null

    fun setVideoUri(uri: Uri){
        videoUri = uri
    }

    fun showPublicBottomSheet(){
        viewModelScope.launch {
            _event.emit(UploadEvent.ShowPublicBottomSheet(uiState.value.publicState))
        }
    }

    fun navigateToSearchCragBottomSheet(){
        viewModelScope.launch {
            _event.emit(UploadEvent.NavigateToSearchCragBottomSheet)
        }
    }

    fun setPublicState(type: PublicType){
        _uiState.update { state ->
            state.copy(
                publicState = type
            )
        }
    }

    fun applyFilter(data: SelectedSector){

        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    selectedSector = data
                )
            }
        }
    }
}