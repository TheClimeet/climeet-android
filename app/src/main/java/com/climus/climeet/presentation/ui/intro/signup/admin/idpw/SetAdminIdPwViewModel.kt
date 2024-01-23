package com.climus.climeet.presentation.ui.intro.signup.admin.idpw

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.IntroRepository
import com.climus.climeet.presentation.ui.InputState
import com.climus.climeet.presentation.ui.intro.signup.admin.AdminSignupForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SetAdminIdPwEvent{
    data object NavigateToBack : SetAdminIdPwEvent()
    data object NavigateToNext : SetAdminIdPwEvent()
}

data class SetAdminIdPwUiState(
    val idState: InputState = InputState.Empty,
    val pwState: InputState = InputState.Empty
)

@HiltViewModel
class SetAdminIdPwViewModel @Inject constructor(
    private val repository: IntroRepository
) : ViewModel() {

    private val _event = MutableSharedFlow<SetAdminIdPwEvent>()
    val event: SharedFlow<SetAdminIdPwEvent> = _event.asSharedFlow()

    private val _uiState = MutableStateFlow(SetAdminIdPwUiState())
    val uiState: StateFlow<SetAdminIdPwUiState> = _uiState.asStateFlow()

    val id = MutableStateFlow("")
    val pw = MutableStateFlow("")
    private val idAvailable = MutableStateFlow(false)
    private val pwAvailable = MutableStateFlow(false)

    val isDataReady = combine(idAvailable, pwAvailable){ id, pw ->
        id && pw
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    init {
        idObserve()
        pwObserve()
    }

    private fun idObserve() {
        id.onEach {
            if(it.isNotBlank()){
                _uiState.update { state ->
                    state.copy(
                        idState = InputState.Empty
                    )
                }
                idAvailable.value = false
            }
        }.launchIn(viewModelScope)
    }

    private fun pwObserve() {
        pw.onEach {

            if(it.isNotBlank()) {
                if (isPasswordValid(pw.value)) {
                    _uiState.update { state ->
                        state.copy(
                            pwState = InputState.Success("적합한 비밀번호 입니다")
                        )
                    }
                    pwAvailable.value = true
                } else {
                    _uiState.update { state ->
                        state.copy(
                            pwState = InputState.Error("영문과 숫자 조합 8자리 이상 입력하세요.")
                        )
                    }
                    pwAvailable.value = false
                }
            } else {
                _uiState.update { state ->
                    state.copy(
                        pwState = InputState.Empty
                    )
                }
                pwAvailable.value = false
            }

        }.launchIn(viewModelScope)
    }

    // 아이디 유효성 검사 -> 버튼 누를 때 호출
    fun checkIdDuplication() {
        viewModelScope.launch {
            repository.managerIdCheck(id.value).let{
                when(it){
                    is BaseState.Success -> {
                        if(it.body){
                            _uiState.update { state ->
                                state.copy(
                                    idState = InputState.Success("사용 가능한 아이디 입니다")
                                )
                            }
                            idAvailable.value = true
                        } else {
                            _uiState.update { state ->
                                state.copy(
                                    idState = InputState.Error("중복된 아이디입니다.")
                                )
                            }
                            idAvailable.value = false
                        }
                    }
                    is BaseState.Error -> {
                        _uiState.update { state ->
                            state.copy(
                                idState = InputState.Error("중복검사 실패.")
                            )
                        }
                        idAvailable.value = false
                    }
                }
            }
        }
    }

    // 비밀번호 유효성 검사 ( @!%*?.&의 기호도 허용 )
    private fun isPasswordValid(pw: String): Boolean {
        val passwordPattern = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@\$@!%*?.&]{8,}\$")
        return passwordPattern.matches(pw)
    }

    // 사업자 등록증 설정으로 이동
    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(SetAdminIdPwEvent.NavigateToBack)
        }
    }

    // 개인정보 설정으로 이동
    fun navigateToNext(){
        viewModelScope.launch {
            AdminSignupForm.setId(id.value)
            AdminSignupForm.setPw(pw.value)
            _event.emit(SetAdminIdPwEvent.NavigateToNext)
        }
    }
}