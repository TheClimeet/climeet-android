package com.climus.climeet.presentation.ui.main.shorts.bottomsheet.searchcrag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SearchCragBottomSheetEvent{
    data object NavigateToSelectSectorBottomSheet: SearchCragBottomSheetEvent()
}

@HiltViewModel
class SearchCragBottomSheetViewModel @Inject constructor(): ViewModel() {

    private val _event = MutableSharedFlow<SearchCragBottomSheetEvent>()
    val event: SharedFlow<SearchCragBottomSheetEvent> = _event.asSharedFlow()

    fun navigateToSelectSectorBottomSheet(){
        viewModelScope.launch {
            _event.emit(SearchCragBottomSheetEvent.NavigateToSelectSectorBottomSheet)
        }
    }
}