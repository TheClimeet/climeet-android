package com.climus.climeet.presentation.ui.intro.signup.admin.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.IntroRepository
import com.climus.climeet.presentation.ui.intro.signup.admin.AdminSignupForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SetAdminAlarmEvent {
    data object NavigateToBack : SetAdminAlarmEvent()
    data object NavigateToNextAlarmOn : SetAdminAlarmEvent()
    data object NavigateToNextAlarmOff : SetAdminAlarmEvent()
    data class ShowToastMessage(val msg: String): SetAdminAlarmEvent()
    data object ShowLoading: SetAdminAlarmEvent()
    data object DismissLoading: SetAdminAlarmEvent()
}

@HiltViewModel
class SetAdminAlarmViewModel @Inject constructor(
    private val repository: IntroRepository
) : ViewModel() {

    private val _event = MutableSharedFlow<SetAdminAlarmEvent>()
    val event: SharedFlow<SetAdminAlarmEvent> = _event.asSharedFlow()

    // 서비스 설정으로 이동
    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(SetAdminAlarmEvent.NavigateToBack)
        }
    }

    // 알림 수신 동의 후 완료 화면으로 이동
    fun navigateToNextAlarmOn() {
        // 알림 설정
        AdminSignupForm.setAlarm(true)

        viewModelScope.launch {
            _event.emit(SetAdminAlarmEvent.ShowLoading)
            repository.managerSignUp(AdminSignupForm.getSignupRequest()).let{
                when(it){
                    is BaseState.Success -> {
                        _event.emit(SetAdminAlarmEvent.NavigateToNextAlarmOn)
                    }
                    is BaseState.Error -> {
                        _event.emit(SetAdminAlarmEvent.DismissLoading)
                        _event.emit(SetAdminAlarmEvent.ShowToastMessage(it.msg))
                    }
                }
            }
        }
    }

    // 알림 수신 거부 루 완료 화면으로 이동
    fun navigateToNextAlarmOff() {
        // 알림 거부
        AdminSignupForm.setAlarm(false)

        viewModelScope.launch {
            _event.emit(SetAdminAlarmEvent.ShowLoading)
            repository.managerSignUp(AdminSignupForm.getSignupRequest()).let{
                when(it){
                    is BaseState.Success -> {
                        _event.emit(SetAdminAlarmEvent.NavigateToNextAlarmOff)
                    }
                    is BaseState.Error -> {
                        _event.emit(SetAdminAlarmEvent.DismissLoading)
                        _event.emit(SetAdminAlarmEvent.ShowToastMessage(it.msg))
                    }
                }
            }
        }
    }
}