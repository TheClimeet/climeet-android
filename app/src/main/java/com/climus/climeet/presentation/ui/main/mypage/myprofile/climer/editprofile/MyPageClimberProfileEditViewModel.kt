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
    data object NavigateToBack : EditClimberProfileEvent()
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
    var userNickname = ""
    val nickname = MutableStateFlow("")
    val nickAvailable = MutableStateFlow(false)
    val nickUpdated = MutableStateFlow(false)
    private var checkNick = ""

    // 이미지 관리
    private val imageUpdated = MutableStateFlow(false)
    var profileImage = ""

    val nextAvailable = MutableStateFlow(false)


    init {
        nickObserve()
        imageObserve()
    }

    fun initProfile(userName: String, image: String) {
        userNickname = userName
        profileImage = image
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
                                checkNick = nickname.value
                                nextAvailable.value = true
                                nickUpdated.value = true
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
                    nickUpdated.value = false
                } else {
                    _uiState.update { state ->
                        state.copy(
                            nickState = InputState.Error("2~8글자의 한글과 숫자로 입력하세요.")
                        )
                    }
                    nickAvailable.value = false
                    nextAvailable.value = false
                    nickUpdated.value = false
                }

            } else {
                _uiState.update { state ->
                    state.copy(
                        nickState = InputState.Empty
                    )
                }
                nickAvailable.value = false
                nickUpdated.value = false
                // 만약 이미지 불러오고 닉네임은 빈칸일 때, 이미지만 바꾼다고 판단하여 다음 버튼 활성화
                nextAvailable.value = imageUpdated.value
            }
        }.launchIn(viewModelScope)
    }

    private fun imageObserve() {
        ClimberEditProfileForm.imageUriState.onEach { uri ->
            if (uri.isNotBlank()) {
                imageUpdated.value = true
                nextAvailable.value = true
            }
        }.launchIn(viewModelScope)
    }

    private fun updateProfile() {
        if (imageUpdated.value) {
            // todo 이미지 업데이트
            Log.d("mypage", "프로필 이미지 바꿈")
        }

        if (nickUpdated.value) {
            // todo 닉네임 업데이트
            Log.d("mypage", "닉네임 바꿈")
        }
        ClimberEditProfileForm.resetState()
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(EditClimberProfileEvent.NavigateToBack)
        }
    }

    fun navigateToProfile() {
        updateProfile()
        viewModelScope.launch {
            _event.emit(EditClimberProfileEvent.NavigateToProfile)
        }
    }

}