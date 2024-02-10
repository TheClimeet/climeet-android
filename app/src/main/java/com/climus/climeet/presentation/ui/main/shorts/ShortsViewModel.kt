package com.climus.climeet.presentation.ui.main.shorts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ShortsEvent{
    data object NavigateToSearchCragBottomSheet : ShortsEvent()
}

@HiltViewModel
class ShortsViewModel @Inject constructor(): ViewModel() {

    private val _event = MutableSharedFlow<ShortsEvent>()
    val event: SharedFlow<ShortsEvent> = _event.asSharedFlow()

    fun navigateToSearchCragBottomSheet() {
        viewModelScope.launch {
            _event.emit(ShortsEvent.NavigateToSearchCragBottomSheet)
        }
    }

}