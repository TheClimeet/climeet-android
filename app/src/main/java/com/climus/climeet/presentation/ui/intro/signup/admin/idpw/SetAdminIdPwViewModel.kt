package com.climus.climeet.presentation.ui.intro.signup.admin.idpw

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.intro.signup.admin.AdminSignupForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SetAdminIdPwEvent{
    data object NavigateToBack : SetAdminIdPwEvent()
    data object NavigateToNext : SetAdminIdPwEvent()
}

@HiltViewModel
class SetAdminIdPwViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<SetAdminIdPwEvent>()
    val event: SharedFlow<SetAdminIdPwEvent> = _event.asSharedFlow()

    val warningTextId = MutableStateFlow("")
    val warningTextPw = MutableStateFlow("")

    val id = MutableStateFlow("")
    val pw = MutableStateFlow("")
    val isNextButtonEnabled = MutableStateFlow(false)

    // 버튼 상태 변화
    private fun updateNextButtonState() {
        // Log.d("admin", "button valid : " + (isIdValid(id.value) && isPasswordValid(pw.value)))
        isNextButtonEnabled.value = isIdValid(id.value) && isPasswordValid(pw.value)
    }

    // 아이디 유효성 검사
    private fun isIdValid(id: String): Boolean {
        // todo : API 호출해 아이디 중복 처리 (동작 확인차 임시로 값 넣음)
        return id != "test"
    }

    // 비밀번호 유효성 검사
    // @!%*?&의 기호도 허용
    private fun isPasswordValid(pw: String): Boolean {
        val passwordPattern = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@\$@!%*?&]{8,}\$")
        return passwordPattern.matches(pw)
    }

    // id, pw가 변경될 때 경고 텍스트를 초기화
    init {
        idObserve()
        pwObserve()
    }

    private fun idObserve() {
        id.onEach {
            if (id.value.isBlank()) {
                warningTextId.value = ""
            } else {
                if (!isIdValid(id.value)) {
                    warningTextId.value = "중복된 아이디 입니다."
                } else{
                    warningTextId.value = ""
                }
            }
            updateNextButtonState()
        }.launchIn(viewModelScope)
    }

    private fun pwObserve() {
        pw.onEach {
            if (pw.value.isBlank()) {
                warningTextPw.value = ""
            } else {
                if (!isPasswordValid(pw.value)) {
                    warningTextPw.value = "영문과 숫자 조합 8자리 이상 입력하세요."
                } else {
                    warningTextPw.value = ""
                }
            }
            updateNextButtonState()
        }.launchIn(viewModelScope)
    }

    // 사업자 등록증 설정으로 이동
    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(SetAdminIdPwEvent.NavigateToBack)
        }
    }

    // 개인정보 설정으로 이동
    fun navigateToNext(){
        val nowId = id.value
        val nowPw = pw.value

        if (nowId.isNotBlank() && nowPw.isNotBlank() && isNextButtonEnabled.value) {
            // 아이디 비번 저장
            AdminSignupForm.setId(nowId)
            AdminSignupForm.setPw(nowPw)

            Log.d("admin", "아이디 비번 : ${AdminSignupForm.id}, ${AdminSignupForm.pw}")

            // 화면 전환
            viewModelScope.launch {
                _event.emit(SetAdminIdPwEvent.NavigateToNext)
            }
        }
    }
}