package com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.editprofile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.IntroRepository
import com.climus.climeet.presentation.ui.InputState
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

data class SetClimberNickUiState(
    val nickState: InputState = InputState.Empty,
)

sealed class EditClimberProfileEvent {
    data object NavigateToBack :EditClimberProfileEvent()
    data object NavigateToProfile : EditClimberProfileEvent()
    data class ShowToastMessage(val msg: String) : EditClimberProfileEvent()
}

@HiltViewModel
class MyPageClimberProfileEditViewModel @Inject constructor(
    private val introRepository: IntroRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SetClimberNickUiState())
    val uiState: StateFlow<SetClimberNickUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<EditClimberProfileEvent>()
    val event: SharedFlow<EditClimberProfileEvent> = _event.asSharedFlow()

    // 닉네임 관리
    val nickname = MutableStateFlow("")
    val nickAvailable = MutableStateFlow(false)
    val nextAvailable = MutableStateFlow(false)
    var checkNick = ""

    val imageUpdated = MutableStateFlow(false)


    init {
        nickObserve()
    }

    // todo 닉네임
    private fun isNickNameValid(nickName: String): Boolean {
        return nickName.matches("^[가-힣0-9]{2,8}\$".toRegex())
    }

    fun isNickNameDuplicated() {
        viewModelScope.launch {
            if (nickAvailable.value) {
                introRepository.climberNickNameCheck(nickname.value).let {
                    when (it) {
                        is BaseState.Success -> {
                            if (it.body) {
                                _uiState.update { state ->
                                    state.copy(
                                        nickState = InputState.Success("사용 가능한 닉네임입니다.")
                                    )
                                }
                                ClimberEditProfileForm.setNickName(nickname.value)
                                nextAvailable.value = true
                                checkNick = nickname.value
                                checkNextState()
                            } else {
                                _uiState.update { state ->
                                    state.copy(
                                        nickState = InputState.Error("중복된 닉네임입니다.")
                                    )
                                }
                            }

                        }

                        is BaseState.Error -> {
                            _event.emit(EditClimberProfileEvent.ShowToastMessage(it.msg))
                        }
                    }
                }
            }
        }
    }

    private fun nickObserve() {
        nickname.onEach {
            Log.d("nickObserve", "Nick value: $it")
            if (it.isNotBlank()) {
                if (isNickNameValid(nickname.value) && nickname.value != checkNick) {
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

    fun setImageUpdated(state: Boolean){
        viewModelScope.launch {
            imageUpdated.value = state
        }

        if(state) {
            checkNextState()
        }
    }

    // 수정 반영 버튼 상태 관리
    private fun checkNextState() {
        nextAvailable.value = nickAvailable.value && imageUpdated.value
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(EditClimberProfileEvent.NavigateToBack)
        }
    }

    fun navigateToProfile() {
        viewModelScope.launch {
            _event.emit(EditClimberProfileEvent.NavigateToProfile)
        }
    }

}