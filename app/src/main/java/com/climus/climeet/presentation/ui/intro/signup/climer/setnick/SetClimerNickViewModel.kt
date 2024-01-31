package com.climus.climeet.presentation.ui.intro.signup.climer.setnick

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.IntroRepository
import com.climus.climeet.presentation.ui.InputState
import com.climus.climeet.presentation.ui.intro.signup.climer.ClimerSignupForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SetClimerNickUiState(
    val nickState: InputState = InputState.Empty
)

sealed class SetClimerNickEvent {
    data object NavigateToBack : SetClimerNickEvent()
    data object NavigateToSetProfile : SetClimerNickEvent()

    data class ShowToastMessage(val msg: String) : SetClimerNickEvent()
}

@HiltViewModel
class SetClimerNickViewModel @Inject constructor(
    private val repository: IntroRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SetClimerNickUiState())
    val uiState: StateFlow<SetClimerNickUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SetClimerNickEvent>()
    val event: SharedFlow<SetClimerNickEvent> = _event.asSharedFlow()

    val nick = MutableStateFlow("")
    val nickAvailable = MutableStateFlow(false)
    val nextAvailable = MutableStateFlow(false)
    var checkNick = ""

    init {
        nickObserve()
    }

    private fun isNickNameValid(nickName: String): Boolean {
        return nickName.matches("^[가-힣0-9]{2,8}\$".toRegex())
    }

    fun isNickNameDuplicated() {
        viewModelScope.launch {
            if (nickAvailable.value) {
                repository.climberNickNameCheck(nick.value).let {
                    when (it) {
                        is BaseState.Success -> {
                            if (it.body) {
                                _uiState.update { state ->
                                    state.copy(
                                        nickState = InputState.Success("사용 가능한 닉네임입니다.")

                                    )
                                }
                                nextAvailable.value = true
                                checkNick = nick.value
                            } else {
                                _uiState.update { state ->
                                    state.copy(
                                        nickState = InputState.Error("중복된 닉네임입니다.")
                                    )
                                }
                            }

                        }

                        is BaseState.Error -> {
                            _event.emit(SetClimerNickEvent.ShowToastMessage(it.msg))
                        }
                    }
                }
            }
        }
    }

    private fun nickObserve() {
        nick.onEach {
            if (it.isNotBlank()) {
                if (isNickNameValid(nick.value) && nick.value != checkNick) {
                    nickAvailable.value = true
                    _uiState.update { state ->
                        state.copy(
                            nickState = InputState.Success("닉네임 중복을 확인해주세요.")
                        )
                    }
                    nickAvailable.value = true
                    nextAvailable.value = false
                } else {
                    _uiState.update { state ->
                        state.copy(
                            nickState = InputState.Error("2~8글자의 한글과 숫자로 입력하세요.")
                        )
                    }
                    nickAvailable.value = false
                    nextAvailable.value = false
                }

            } else {
                _uiState.update { state ->
                    state.copy(
                        nickState = InputState.Empty
                    )
                }
                nickAvailable.value = false
                nextAvailable.value = false
            }
        }.launchIn(viewModelScope)
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(SetClimerNickEvent.NavigateToBack)
        }
    }

    fun navigateToSetProfile() {
        ClimerSignupForm.setNickName(nick.value)
        viewModelScope.launch {
            _event.emit(SetClimerNickEvent.NavigateToSetProfile)
        }
    }

}