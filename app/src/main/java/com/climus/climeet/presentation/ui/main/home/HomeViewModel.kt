package com.climus.climeet.presentation.ui.main.home

import androidx.lifecycle.ViewModel
import com.climus.climeet.presentation.ui.intro.signup.climer.followcrag.FollowCragEvent
import com.climus.climeet.presentation.ui.intro.signup.climer.followcrag.FollowCragUiState
import com.climus.climeet.presentation.ui.intro.signup.climer.model.FollowCrag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

sealed class HomeEvent{

}

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<HomeEvent>()
    val event: SharedFlow<HomeEvent> = _event.asSharedFlow()

}