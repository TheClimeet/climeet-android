package com.climus.climeet.presentation.ui.main.shorts.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
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

data class ShortsDetailUiState(
    val isBookMarked: Boolean = false,
    val bookMarkCount: Int = 0,
    val isFavorite: Boolean = false,
    val favoriteCount: Int = 0,
    val isWholeText: Boolean = false,
    val singleLineText: String = "",
    val wholeText: String = ""
)

sealed class ShortsDetailEvent {
    data class ShowToastMessage(val msg: String) : ShortsDetailEvent()
    data object ShowCommentDialog : ShortsDetailEvent()
    data object ShowShareDialog : ShortsDetailEvent()
    data object NavigateToProfileDetail : ShortsDetailEvent()
    data object NavigateToRouteShorts : ShortsDetailEvent()
    data class PatchFavorite(val state: Boolean) : ShortsDetailEvent()
    data class PatchBookMark(val state: Boolean) : ShortsDetailEvent()
}

@HiltViewModel
class ShortsDetailViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShortsDetailUiState())
    val uiState: StateFlow<ShortsDetailUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ShortsDetailEvent>()
    val event: SharedFlow<ShortsDetailEvent> = _event.asSharedFlow()

    private var shortsId = -1L

    fun setInitData(
        shortsId: Long,
        bookMarkState: Boolean,
        bookMarkCount: Int,
        favoriteState: Boolean,
        favoriteCount: Int,
        description: String
    ) {
        this.shortsId = shortsId
        _uiState.update { state ->
            state.copy(
                isBookMarked = bookMarkState,
                bookMarkCount = bookMarkCount,
                isFavorite = favoriteState,
                favoriteCount = favoriteCount,
                singleLineText = if (description.length > 18) description.substring(0..17) + " ..." else description,
                wholeText = description
            )
        }
    }

    fun changeDescriptionState() {
        _uiState.update { state ->
            state.copy(
                isWholeText = !uiState.value.isWholeText
            )
        }
    }

    fun patchFavorite() {
        viewModelScope.launch {
            repository.patchFavorite(shortsId).let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isFavorite = !uiState.value.isFavorite,
                                favoriteCount = if (uiState.value.isFavorite) uiState.value.favoriteCount - 1 else uiState.value.favoriteCount + 1
                            )
                        }

                        _event.emit(ShortsDetailEvent.PatchFavorite(uiState.value.isFavorite))
                    }

                    is BaseState.Error -> {
                        _event.emit(ShortsDetailEvent.ShowToastMessage(it.msg))
                    }
                }
            }
        }
    }

    fun patchBookMark() {
        viewModelScope.launch {
            repository.patchBookMark(shortsId).let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isBookMarked = !uiState.value.isBookMarked,
                                bookMarkCount = if (uiState.value.isBookMarked) uiState.value.bookMarkCount - 1 else uiState.value.bookMarkCount + 1
                            )
                        }

                        _event.emit(ShortsDetailEvent.PatchBookMark(uiState.value.isBookMarked))
                    }

                    is BaseState.Error -> {
                        _event.emit(ShortsDetailEvent.ShowToastMessage(it.msg))
                    }
                }
            }
        }
    }

    fun showCommentDialog() {
        viewModelScope.launch {
            _event.emit(ShortsDetailEvent.ShowCommentDialog)
        }
    }

    fun showShareDialog() {
        viewModelScope.launch {
            _event.emit(ShortsDetailEvent.ShowShareDialog)
        }
    }

    fun navigateToRouteShorts() {
        viewModelScope.launch {
            _event.emit(ShortsDetailEvent.NavigateToRouteShorts)
        }
    }

    fun navigateToProfileDetail() {
        viewModelScope.launch {
            _event.emit(ShortsDetailEvent.NavigateToProfileDetail)
        }
    }


}