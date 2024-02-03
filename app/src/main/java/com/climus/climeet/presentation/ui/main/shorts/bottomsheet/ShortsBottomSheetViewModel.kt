package com.climus.climeet.presentation.ui.main.shorts.bottomsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.main.shorts.bottomsheet.selectsector.SelectedSector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ShortsBottomSheetViewModel : ViewModel() {

    private val _applyFilter = MutableSharedFlow<SelectedSector>()
    val applyFilter: SharedFlow<SelectedSector> = _applyFilter.asSharedFlow()

    fun applyFilter(selectedSector: SelectedSector) {
        viewModelScope.launch {
            _applyFilter.emit(selectedSector)
        }
    }
}