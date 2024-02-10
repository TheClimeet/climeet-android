package com.climus.climeet.presentation.ui.main.shorts.player

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ShortsDetailUiState(
    val isBookMarked: Boolean = false,
    val isFavorite: Boolean = false,
    val isWholeText: Boolean = false,
    val singleLineText: String = "",
    val wholeText: String = ""
)

sealed class ShortsDetailEvent{

}

@HiltViewModel
class ShortsDetailViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(ShortsDetailUiState())
    val uiState: StateFlow<ShortsDetailUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ShortsDetailEvent>()
    val event: SharedFlow<ShortsDetailEvent> = _event.asSharedFlow()

    fun setInitData(
        bookMarkState: Boolean,
        favoriteState: Boolean,
        description: String
    ) {
        _uiState.update { state ->
            state.copy(
                isBookMarked = bookMarkState,
                isFavorite = favoriteState,
                singleLineText = if (description.length > 18) description.substring(0..17) + " ..." else description,
                wholeText = description
            )
        }
    }

    fun changeDescriptionState(){
        _uiState.update { state ->
            state.copy(
                isWholeText = !uiState.value.isWholeText
            )
        }
    }


}