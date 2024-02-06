package com.climus.climeet.presentation.ui.main.upload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.ShortsDetailRequest
import com.climus.climeet.data.model.request.ShortsUploadRequest
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.customview.PublicType
import com.climus.climeet.presentation.ui.main.global.selectsector.model.SelectedFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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
    data object ShowLoading: UploadEvent()
    data object DismissLoading: UploadEvent()
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
    val compressProgress = MutableStateFlow(0)
    val isCompressDone = MutableStateFlow(false)
    private var videoFile : MultipartBody.Part ?= null

    val isDataReady = combine(thumbnailImg, isCompressDone){ thumbnailImg, isCompressDone->
        thumbnailImg.isNotBlank() && isCompressDone
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    fun startCompress(){
        isCompressDone.value = false
    }

    fun setCompressProgress(progress: Int){
        compressProgress.value = progress
    }

    fun finishCompress(file: MultipartBody.Part){
        // Compress 끝
        isCompressDone.value = true
        videoFile = file
    }

    fun setThumbnailImg(imgUrl: String) {
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
                _event.emit(UploadEvent.DismissLoading)
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
            _event.emit(UploadEvent.ShowLoading)
            videoFile?.let {
                repository.uploadFile(it).let { state ->
                    when (state) {
                        is BaseState.Success -> {
                            uploadShorts(state.body.imgUrl)
                        }

                        is BaseState.Error -> {
                            _event.emit(UploadEvent.ShowToastMessage(state.msg))
                            _event.emit(UploadEvent.DismissLoading)
                        }
                    }
                }
            }
        }
    }
}