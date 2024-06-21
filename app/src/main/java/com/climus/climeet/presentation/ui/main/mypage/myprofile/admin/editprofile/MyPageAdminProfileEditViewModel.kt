package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.InputState
import com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.editprofile.EditClimberProfileEvent
import com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.editprofile.SetClimberNickUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SetAdminNickUiState(
    val nameState: InputState = InputState.Empty,
)

sealed class AdminProfileEditEvent{
    data object NavigateToBack : AdminProfileEditEvent()
    data object NavigateToProfile : AdminProfileEditEvent()
}

@HiltViewModel
class MyPageAdminProfileEditViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SetAdminNickUiState())
    val uiState: StateFlow<SetAdminNickUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<AdminProfileEditEvent>()
    val event: SharedFlow<AdminProfileEditEvent> = _event.asSharedFlow()

    // 이미지 관리
    private val imageUpdated = MutableStateFlow(false)
    var profileImage = ""

    val nextAvailable = MutableStateFlow(false)

    init {
        viewModelScope.launch{
            nextAvailable.value = true
        }
    }




    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(AdminProfileEditEvent.NavigateToBack)
        }
    }

    fun navigateToProfile() {
        // todo : 배경, 닉네임, 프사 변경된 것 반영
        viewModelScope.launch {
            _event.emit(AdminProfileEditEvent.NavigateToProfile)
        }
    }
}