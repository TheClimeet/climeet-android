package com.climus.climeet.presentation.ui.intro.signup.climer.followcrag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class FollowCragEvent {
    data object NavigateToBack : FollowCragEvent()
    data object NavigateToNext : FollowCragEvent()
}

@HiltViewModel
class FollowCragViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<FollowCragEvent>()
    val event: SharedFlow<FollowCragEvent> = _event.asSharedFlow()


    fun navigateToNext() {
        viewModelScope.launch {
            _event.emit(FollowCragEvent.NavigateToNext)
        }
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(FollowCragEvent.NavigateToBack)
        }
    }

}