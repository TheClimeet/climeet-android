package com.climus.climeet.presentation.ui.intro.signup.admin.personal

import android.util.Log
import android.util.Patterns
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

sealed class SetAdminPersonalEvent{
    data object NavigateToBack : SetAdminPersonalEvent()
    data object NavigateToNext : SetAdminPersonalEvent()
}

@HiltViewModel
class SetAdminPersonalViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<SetAdminPersonalEvent>()
    val event: SharedFlow<SetAdminPersonalEvent> = _event.asSharedFlow()

    val warningTextName = MutableStateFlow("")
    val warningTextPhone = MutableStateFlow("")
    val warningTextEmail = MutableStateFlow("")

    val name = MutableStateFlow("")
    val phone = MutableStateFlow("")
    val email = MutableStateFlow("")
    val isNextButtonEnabled = MutableStateFlow(false)

    // 버튼 상태 변화
    fun updateNextButtonState() {
        // Log.d("admin", "button valid : " + (isNameValid(name.value) && isPhoneValid(phone.value) && isEmailValid(email.value)))
        isNextButtonEnabled.value = isNameValid(name.value) && isPhoneValid(phone.value) && isEmailValid(email.value)
    }

    // 유효성 검사
    private fun isNameValid(name: String): Boolean {
        val namePattern = Regex("^[가-힣]{2,6}\$")
        return namePattern.matches(name)
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPhoneValid(phone: String): Boolean {
        val phonePattern = Regex("^(010|011|016|017|018|019)-[0-9]{3,4}-[0-9]{4}\$")
        return phonePattern.matches(phone)
    }

    init {
        nameObserve()
        phoneObserve()
        emailObserve()
    }

    private fun nameObserve() {
        name.onEach {
            if (name.value.isBlank()) {
                warningTextName.value = ""
            } else {
                if (!isNameValid(name.value)) {
                    warningTextName.value = "이름을 정확히 입력해주세요."
                } else{
                    warningTextName.value = ""
                }
            }
            updateNextButtonState()
        }.launchIn(viewModelScope)
    }

    private fun phoneObserve() {
        phone.onEach {
            if (phone.value.isBlank()) {
                warningTextPhone.value = ""
            } else {
                if (!isPhoneValid(phone.value)) {
                    warningTextPhone.value = "전화번호 형식이 아닙니다."
                } else{
                    warningTextPhone.value = ""
                }
            }
            updateNextButtonState()
        }.launchIn(viewModelScope)
    }

    private fun emailObserve() {
        email.onEach {
            if (email.value.isBlank()) {
                warningTextEmail.value = ""
            } else {
                if (!isEmailValid(email.value)) {
                    warningTextEmail.value = "이메일 형식이 아닙니다."
                } else{
                    warningTextEmail.value = ""
                }
            }
            updateNextButtonState()
        }.launchIn(viewModelScope)
    }


    // 아이디, 비번 설정으로 이동
    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(SetAdminPersonalEvent.NavigateToBack)
        }
    }

    // 배경화면 설정으로 이동
    fun navigateToNext() {
        val nowName = name.value
        val nowPhone = phone.value
        val nowEmail = email.value

        if (nowName.isNotBlank() && nowPhone.isNotBlank() &&nowEmail.isNotBlank() && isNextButtonEnabled.value){
            // 이름, 전화번호, 이메일 저장
            AdminSignupForm.setName(nowName)
            AdminSignupForm.setPhone(nowPhone)
            AdminSignupForm.setEmail(nowEmail)

            Log.d("admin", "이름 전화번호 이메일 : ${AdminSignupForm.name}, ${AdminSignupForm.phoneNum}, ${AdminSignupForm.email}")

            // 화면 전환
            viewModelScope.launch {
                _event.emit(SetAdminPersonalEvent.NavigateToNext)
            }
        }
    }
}