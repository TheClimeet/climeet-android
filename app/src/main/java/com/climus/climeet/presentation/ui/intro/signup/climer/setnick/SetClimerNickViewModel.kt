package com.climus.climeet.presentation.ui.intro.signup.climer.setnick

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.climus.climeet.R
import com.climus.climeet.presentation.ui.intro.signup.climer.ClimerSignupForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SetClimerNickEvent {
    data object NavigateToBack : SetClimerNickEvent()
    data object NavigateToSetProfile : SetClimerNickEvent()
}

@HiltViewModel
class SetClimerNickViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<SetClimerNickEvent>()
    val event: SharedFlow<SetClimerNickEvent> = _event.asSharedFlow()

    val warningText = MutableStateFlow("")

    val warningTextColor = warningText.map { text ->
        if (text != "사용 가능한 아이디입니다.") R.color.cm_red else R.color.cm_grey6
    }.asLiveData()

    val nick = MutableStateFlow("")

    private val nickNamePattern = "^[가-힣0-9]{2,8}\$".toRegex()

    init {
        nickObserve()
    }

    private fun isNickNameValid(nickName: String): Boolean {
        return nickName.matches(nickNamePattern)
    }

    private fun isNickNameDuplicated(nickName: String): Boolean {
        // 중복 처리 로직
        return false
    }

    val isNextButtonVisible = nick.map { newNick ->
        newNick.isNotBlank() && isNickNameValid(newNick) && !isNickNameDuplicated(newNick)
    }.asLiveData()

    private var hasUserStartedTyping = false

    val isErrorVisible = nick.map { newNick ->
        // 입력이 시작되었고, 닉네임이 유효하지 않거나 중복된 경우 버튼이 보이게 함
        newNick.isNotEmpty() && (!isNickNameValid(newNick) || isNickNameDuplicated(newNick))
    }.asLiveData()

    private fun nickObserve() {
        nick.onEach { nick ->
            hasUserStartedTyping = nick.isNotEmpty()

            when {
                nick.isEmpty() -> {
                    warningText.value = ""
                }

                !isNickNameValid(nick) -> {
                    warningText.value = "2~8글자의 한글과 숫자로 입력하세요."
                }

                isNickNameDuplicated(nick) -> {
                    warningText.value = "중복된 닉네임입니다."
                }

                else -> {
                    warningText.value = "사용 가능한 아이디입니다."
                }
            }
        }.launchIn(viewModelScope)
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(SetClimerNickEvent.NavigateToBack)
        }
    }


    fun navigateToSetProfile() {
        val currentNick = nick.value
        if (currentNick.isNotBlank() && isNickNameValid(currentNick) && !isNickNameDuplicated(
                currentNick
            )
        ) {
            // ClimerSignupForm에 닉네임 설정
            ClimerSignupForm.setNickName(currentNick)

            viewModelScope.launch {
                _event.emit(SetClimerNickEvent.NavigateToSetProfile)
            }
        } else {

        }
    }

}