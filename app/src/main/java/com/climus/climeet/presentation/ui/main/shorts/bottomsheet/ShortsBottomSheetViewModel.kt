package com.climus.climeet.presentation.ui.main.shorts.bottomsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.main.shorts.bottomsheet.selectsector.SelectedSector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

sealed class ShortsBottomSheetEvent{
    data object DismissDialog: ShortsBottomSheetEvent()
    data class ApplyFilter(val selectedSector: SelectedSector): ShortsBottomSheetEvent()
}

class ShortsBottomSheetViewModel : ViewModel() {

    private val _event = MutableSharedFlow<ShortsBottomSheetEvent>()
    val event: SharedFlow<ShortsBottomSheetEvent> = _event.asSharedFlow()

    fun applyFilter(selectedSector: SelectedSector) {
        viewModelScope.launch {
            _event.emit(ShortsBottomSheetEvent.ApplyFilter(selectedSector))
        }
    }

    fun dismissDialog(){
        viewModelScope.launch {
            _event.emit(ShortsBottomSheetEvent.DismissDialog)
        }
    }
}