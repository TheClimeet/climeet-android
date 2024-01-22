package com.climus.climeet.presentation.ui.intro.signup.admin.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.intro.signup.admin.AdminSignupForm
import com.climus.climeet.presentation.ui.intro.signup.admin.model.ServiceUiData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SetAdminServiceEvent {
    data object NavigateToBack : SetAdminServiceEvent()
    data object NavigateToNext : SetAdminServiceEvent()
}

@HiltViewModel
class SetAdminServiceViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<SetAdminServiceEvent>()
    val event: SharedFlow<SetAdminServiceEvent> = _event.asSharedFlow()

    private val _serviceList = MutableLiveData<List<ServiceUiData>>(emptyList()) // fragment에서 넘겨준 서비스 리스트 저장
    val serviceList: LiveData<List<ServiceUiData>> = _serviceList

    val isNextButtonEnabled = MutableStateFlow(false)

    // 버튼 상태 변화
    private fun updateNextButtonState() {
        isNextButtonEnabled.value = _serviceList.value?.any { it.isSelected } ?: false
    }

    // 서비스 리스트 가져오기
    fun setInitialServices(initialServices: List<ServiceUiData>) {
        _serviceList.value = initialServices
    }

    // 선택된 서비스 관리
    fun toggleServiceSelection(position: Int) {
        // true, false 상태를 반전시켜준다
        _serviceList.value?.get(position)?.isSelected =
            _serviceList.value?.get(position)?.isSelected != true

        // 변경 반영
        _serviceList.value = _serviceList.value

        updateNextButtonState()
    }

    // 배경화면 설정으로 이동
    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(SetAdminServiceEvent.NavigateToBack)
        }
    }

    // 알림 설정으로 이동
    fun navigateToNext(){
        if(isNextButtonEnabled.value){
            // 선택된 서비스의 title만 추출하여 AdminSignupForm에 저장
            val selectedServiceTitles = _serviceList.value?.filter { it.isSelected }?.map { it.title } ?: emptyList()
            AdminSignupForm.setSelectedServices(selectedServiceTitles)

            Log.d("admin", "서비스 상태 : ${AdminSignupForm.services}")

            viewModelScope.launch {
                _event.emit(SetAdminServiceEvent.NavigateToNext)
            }
        }
    }
}