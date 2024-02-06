package com.climus.climeet.presentation.ui.main.upload

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.app.App
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.ShortsDetailRequest
import com.climus.climeet.data.model.request.ShortsUploadRequest
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.customview.PublicType
import com.climus.climeet.presentation.ui.main.global.selectsector.model.SelectedFilter
import com.climus.climeet.presentation.ui.toVideoMultiPart
import com.climus.climeet.presentation.util.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

data class UploadUiState(
    val publicState: PublicType = PublicType.PUBLIC,
    val selectedFilter: SelectedFilter = SelectedFilter(),
)

sealed class UploadEvent {
    data class ShowPublicBottomSheet(val type: PublicType) : UploadEvent()
    data object NavigateToSearchCragBottomSheet : UploadEvent()
    data class ShowToastMessage(val msg: String) : UploadEvent()
}

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _event = MutableSharedFlow<UploadEvent>()
    val event: SharedFlow<UploadEvent> = _event.asSharedFlow()

    private val _uiState = MutableStateFlow(UploadUiState())
    val uiState: StateFlow<UploadUiState> = _uiState.asStateFlow()

    val description = MutableStateFlow("")
    val soundEnabled = MutableStateFlow(false)
    val thumbnailImg = MutableStateFlow("")
    private var videoFile : MultipartBody.Part ?= null

    fun setVideoFIle(file: MultipartBody.Part){
        videoFile = file
    }

    fun setThumbnailImg(imgUrl: String) {
        Log.d(TAG, imgUrl)
        thumbnailImg.value = imgUrl
    }

    fun showPublicBottomSheet() {
        viewModelScope.launch {
            _event.emit(UploadEvent.ShowPublicBottomSheet(uiState.value.publicState))
        }
    }

    fun navigateToSearchCragBottomSheet() {
        viewModelScope.launch {
            _event.emit(UploadEvent.NavigateToSearchCragBottomSheet)
        }
    }

    fun setPublicState(type: PublicType) {
        _uiState.update { state ->
            state.copy(
                publicState = type
            )
        }
    }

    fun applyFilter(data: SelectedFilter) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    selectedFilter = data
                )
            }
        }
    }

    private fun uploadShorts(videoUrl: String) {
        viewModelScope.launch {
            repository.uploadShorts(
                ShortsUploadRequest(
                    video = videoUrl,
                    thumbnailImage = thumbnailImg.value,
                    createShortsRequest = ShortsDetailRequest(
                        climbingGymId = uiState.value.selectedFilter.cragId,
                        routeId = uiState.value.selectedFilter.routeId,
                        sectorId = uiState.value.selectedFilter.sectorId,
                        description = description.value,
                        public = true,
                        soundEnabled = soundEnabled.value
                    )
                )
            ).let {
                when (it) {
                    is BaseState.Success -> {
                        _event.emit(UploadEvent.ShowToastMessage("Success Upload"))
                    }

                    is BaseState.Error -> {
                        _event.emit(UploadEvent.ShowToastMessage(it.msg))
                    }
                }
            }
        }
    }

    fun storeVideo() {
        viewModelScope.launch {
            videoFile?.let {
                repository.uploadFile(it).let { state ->
                    when (state) {
                        is BaseState.Success -> {
                            uploadShorts(state.body.imgUrl)
                        }

                        is BaseState.Error -> {
                            _event.emit(UploadEvent.ShowToastMessage(state.msg))
                        }
                    }
                }
            }
        }
    }
}