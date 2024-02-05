package com.climus.climeet.presentation.ui.main.upload.bottomsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.main.global.selectsector.model.SelectedRoute
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

sealed class UploadBottomSheetEvent{
    data object DismissDialog: UploadBottomSheetEvent()
    data class ApplyFilter(val selectedRoute: SelectedRoute): UploadBottomSheetEvent()
}

class UploadBottomSheetViewModel : ViewModel() {

    private val _event = MutableSharedFlow<UploadBottomSheetEvent>()
    val event: SharedFlow<UploadBottomSheetEvent> = _event.asSharedFlow()

    fun applyFilter(selectedRoute: SelectedRoute) {
        viewModelScope.launch {
            _event.emit(UploadBottomSheetEvent.ApplyFilter(selectedRoute))
        }
    }

    fun dismissDialog(){
        viewModelScope.launch {
            _event.emit(UploadBottomSheetEvent.DismissDialog)
        }
    }
}