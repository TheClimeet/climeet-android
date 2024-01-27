package com.climus.climeet.presentation.ui.main.record.createclimbingrecord.selectcrag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreateSelectCragUiState(
    val searchList: List<FollowCrag> = emptyList(),
    val progressState: Boolean = false,
    val emptyResultState: Boolean = false
)

sealed class CreateSelectCragEvent{
    data object NavigateToBack: CreateSelectCragEvent()
}

@HiltViewModel
class CreateSelectCragViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow(CreateSelectCragUiState())
    val uiState: StateFlow<CreateSelectCragUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CreateSelectCragEvent>()
    val event: SharedFlow<CreateSelectCragEvent> = _event.asSharedFlow()

    val keyword = MutableStateFlow("")

    fun navigateToBack(){
        viewModelScope.launch {
            _event.emit(CreateSelectCragEvent.NavigateToBack)
        }
    }

}