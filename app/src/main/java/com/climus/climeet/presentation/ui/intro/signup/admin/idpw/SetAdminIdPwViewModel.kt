package com.climus.climeet.presentation.ui.intro.signup.admin.idpw

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.climus.climeet.R
import com.climus.climeet.presentation.ui.intro.signup.admin.AdminSignupForm
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
    val idViewColor = MutableStateFlow(R.color.cm_grey4)
    val pwViewColor = MutableStateFlow(R.color.cm_grey4)

    val warningIdColor = warningTextId.map { text ->
        if (text != "") R.color.cm_red else R.color.white
    }.asLiveData()

    val warningPwColor = warningTextPw.map { text ->
        if (text != "") R.color.cm_red else R.color.white
    }.asLiveData()

    val id = MutableStateFlow("")
    val pw = MutableStateFlow("")
    val isNextButtonEnabled = MutableStateFlow(false)
    var idValid = MutableStateFlow(false)

    // 버튼 상태 변화
    private fun updateNextButtonState() {
        isNextButtonEnabled.value = idValid.value && isPasswordValid(pw.value)
    }

    // 아이디 유효성 검사 -> 버튼 누를 때 호출
    fun checkIdDuplication() {
        viewModelScope.launch {
            if (!isIdValid(id.value)) {
                warningTextId.value = "중복된 아이디 입니다."
                idViewColor.value = R.color.cm_grey4
            } else {
                warningTextId.value = ""
                idViewColor.value = R.color.cm_main
            }
            updateNextButtonState()
        }
    }
    fun isIdValid(id: String): Boolean {
        // todo : API 호출해 아이디 중복 처리 (동작 확인차 임시로 값 넣음)
        val result = id != "test"

        idValid.value = result
        Log.d("admin", "[isIdValid] idValid = ${idValid.value}")

        return result
    }

    // 비밀번호 유효성 검사 ( @!%*?.&의 기호도 허용 )
    private fun isPasswordValid(pw: String): Boolean {
        val passwordPattern = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@\$@!%*?.&]{8,}\$")
        return passwordPattern.matches(pw)
    }

    init {
        idObserve()
        pwObserve()
    }

    private fun idObserve() {
        id.onEach {
            idValid.value = false
            //Log.d("admin", "[idObserve] idValid = ${idValid.value}")

            if (id.value.isBlank()) {
                warningTextId.value = ""
                idViewColor.value = R.color.cm_grey4
            } else {
                if (!idValid.value) {
                    idViewColor.value = R.color.cm_grey4
                } else{
                    warningTextId.value = ""
                    idViewColor.value = R.color.cm_main
                }
            }
            updateNextButtonState()
        }.launchIn(viewModelScope)
    }

    private fun pwObserve() {
        pw.onEach {
            if (pw.value.isBlank()) {
                warningTextPw.value = ""
                pwViewColor.value = R.color.cm_grey4
            } else {
                if (!isPasswordValid(pw.value)) {
                    warningTextPw.value = "영문과 숫자 조합 8자리 이상 입력하세요."
                    pwViewColor.value = R.color.cm_grey4

                } else {
                    warningTextPw.value = ""
                    pwViewColor.value = R.color.cm_main
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