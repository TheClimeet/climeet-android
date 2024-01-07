package com.climus.climeet.presentation.ui.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class IntroEvent{
    data object GoToGallery: IntroEvent()
}

@HiltViewModel
class IntroViewModel @Inject constructor(): ViewModel() {

    private val _event = MutableSharedFlow<IntroEvent>()
    val event: SharedFlow<IntroEvent> = _event.asSharedFlow()

    fun goToGallery(){
        viewModelScope.launch {
            _event.emit(IntroEvent.GoToGallery)
        }
    }

}