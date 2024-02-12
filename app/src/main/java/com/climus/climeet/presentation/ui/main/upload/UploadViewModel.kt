package com.climus.climeet.presentation.ui.main.upload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.ShortsDetailRequest
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.customview.PublicType
import com.climus.climeet.presentation.ui.main.global.selectsector.model.SelectedFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

data class UploadUiState(
    val publicState: PublicType = PublicType.PUBLIC,
    val selectedFilter: SelectedFilter = SelectedFilter(),
)

sealed class UploadEvent {
    data class ShowPublicBottomSheet(val type: PublicType) : UploadEvent()
    data object NavigateToSearchCragBottomSheet : UploadEvent()
    data class ShowToastMessage(val msg: String) : UploadEvent()
    data object NavigateToUploadComplete : UploadEvent()
}

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _event = MutableSharedFlow<UploadEvent>()
    val event: SharedFlow<UploadEvent> = _event.asSharedFlow()

    private val _uiState = MutableStateFlow(UploadUiState())
    val uiState: StateFlow<UploadUiState> = _uiState.asStateFlow()

    private val _uploadComplete = MutableSharedFlow<Boolean>()
    val uploadComplete: SharedFlow<Boolean> = _uploadComplete.asSharedFlow()

    val description = MutableStateFlow("")
    val soundEnabled = MutableStateFlow(false)
    val thumbnailImg = MutableStateFlow("")

    val compressProgress = MutableStateFlow(0)
    val isCompressDone = MutableStateFlow(false)

    val uploadProgress = MutableStateFlow(0)
    val isUploadDone = MutableStateFlow(false)

    private var videoSize = 0L
    private var videoFile: MultipartBody.Part? = null

    val isDataReady = combine(thumbnailImg, isCompressDone) { thumbnailImg, isCompressDone ->
        thumbnailImg.isNotBlank() && isCompressDone
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    fun initViewModel() {
        _uiState.value = UploadUiState()
        compressProgress.value = 0
        isCompressDone.value = false
        uploadProgress.value = 0
        isUploadDone.value = false
    }

    fun startCompress() {
        isCompressDone.value = false
    }

    fun setCompressProgress(progress: Int) {
        compressProgress.value = progress
    }

    fun finishCompress(file: MultipartBody.Part, size: Long) {
        // Compress 끝
        isCompressDone.value = true
        videoFile = file
        videoSize = size
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

    fun uploadShorts() {
        viewModelScope.launch {
            startUploadCount()
            _event.emit(UploadEvent.NavigateToUploadComplete)
            repository.uploadShorts(
                video = videoFile,
                thumbnail = thumbnailImg.value.toRequestBody("text/plain".toMediaTypeOrNull()),
                body = ShortsDetailRequest(
                    climbingGymId = uiState.value.selectedFilter.cragId,
                    routeId = uiState.value.selectedFilter.routeId,
                    sectorId = uiState.value.selectedFilter.sectorId,
                    description = description.value,
                    public = uiState.value.publicState.value,
                    soundEnabled = soundEnabled.value
                )
            ).let {
                when (it) {
                    is BaseState.Success -> {
                        isUploadDone.value = true
                        _uploadComplete.emit(true)
                    }

                    is BaseState.Error -> {
                        isUploadDone.value = true
                        _event.emit(UploadEvent.ShowToastMessage(it.msg))
                        _uploadComplete.emit(false)
                    }
                }
            }
        }
    }

    private fun startUploadCount() {
        viewModelScope.launch {
            isUploadDone.value = false
            uploadProgress.value = 0

            val predictSecond = videoSize * 0.0008
            val secondPerProgress = (predictSecond / 100).toLong()

            while (uploadProgress.value < 100) {
                delay(secondPerProgress)
                uploadProgress.value = uploadProgress.value + 1
            }
        }
    }
}