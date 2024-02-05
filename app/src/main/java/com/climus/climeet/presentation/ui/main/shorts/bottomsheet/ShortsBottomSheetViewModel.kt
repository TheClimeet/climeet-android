package com.climus.climeet.presentation.ui.main.shorts.bottomsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.main.global.selectsector.model.SelectedRoute
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

sealed class ShortsBottomSheetEvent{
    data object DismissDialog: ShortsBottomSheetEvent()
    data class ApplyFilter(val selectedRoute: SelectedRoute): ShortsBottomSheetEvent()
}

class ShortsBottomSheetViewModel : ViewModel() {

    private val _event = MutableSharedFlow<ShortsBottomSheetEvent>()
    val event: SharedFlow<ShortsBottomSheetEvent> = _event.asSharedFlow()

    fun applyFilter(selectedRoute: SelectedRoute) {
        viewModelScope.launch {
            _event.emit(ShortsBottomSheetEvent.ApplyFilter(selectedRoute))
        }
    }

    fun dismissDialog(){
        viewModelScope.launch {
            _event.emit(ShortsBottomSheetEvent.DismissDialog)
        }
    }
}