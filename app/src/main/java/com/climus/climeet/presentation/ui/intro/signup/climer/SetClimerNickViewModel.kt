package com.climus.climeet.presentation.ui.intro.signup.climer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.intro.login.admin.AdminLoginEvent
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

    private fun nickObserve() {
        nick.onEach { nick ->
            when {
                nick.isEmpty() -> {
                    // 닉네임이 비어있을 때는 경고 메시지를 설정하지 않습니다.
                }

                !isNickNameValid(nick) -> {
                    warningText.value = "2~8글자의 한글과 숫자로 입력하세요."
                    Log.d("Teststst", "규칙위반")
                }

                isNickNameDuplicated(nick) -> {
                    warningText.value = "중복된 닉네임입니다."
                    Log.d("Teststst", "중복")
                }

                else -> {
                    warningText.value = ""
                    Log.d("Teststst", "옳다")
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

            // 이벤트 발송하여 프래그먼트에서 화면 전환 처리
            viewModelScope.launch {
                _event.emit(SetClimerNickEvent.NavigateToSetProfile)
            }
        } else {
            // 닉네임이 유효하지 않거나 중복될 경우 경고 메시지 업데이트
            warningText.value = "유효하지 않은 닉네임입니다."
        }
    }

}