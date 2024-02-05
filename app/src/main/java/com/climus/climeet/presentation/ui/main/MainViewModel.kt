package com.climus.climeet.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MainEvent{
    data object GoToGalleryForVideo: MainEvent()
}

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    private val _event = MutableSharedFlow<MainEvent>()
    val event: SharedFlow<MainEvent> = _event.asSharedFlow()

    fun goToGalleryForVideo(){
        viewModelScope.launch {
            _event.emit(MainEvent.GoToGalleryForVideo)
        }
    }
}