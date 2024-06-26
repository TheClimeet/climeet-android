package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.info

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.GymServiceUpdateRequest
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.intro.signup.admin.model.ServiceUiData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class EditAdminServiceEvent {
    data object NavigateToBack : EditAdminServiceEvent()
    data object NavigateToProfile : EditAdminServiceEvent()
    data class ShowToastMessage(val msg: String) : EditAdminServiceEvent()
}

@HiltViewModel
class MyPageAdminProfileEditServiceViewModel @Inject constructor(val repository: MainRepository) :
    ViewModel() {

    private val _event = MutableSharedFlow<EditAdminServiceEvent>()
    val event: SharedFlow<EditAdminServiceEvent> = _event.asSharedFlow()

    private val _serviceList =
        MutableLiveData<List<ServiceUiData>>(emptyList())
    val serviceList: LiveData<List<ServiceUiData>> = _serviceList

    private var initialServiceList: List<ServiceUiData> = emptyList()
    val isNextButtonEnabled = MutableStateFlow(false)

    private fun updateNextButtonState() {
        // 초기 서비스 리스트와 비교해 isSelected 값이 바뀐게 있을 때만 버튼 활성화
        val hasChanges = _serviceList.value?.any { service ->
            initialServiceList.find { it.id == service.id }?.isSelected != service.isSelected
        } ?: false
        isNextButtonEnabled.value = hasChanges
    }

    // 서비스 리스트 가져오기
    fun setInitialServices(initialServices: List<ServiceUiData>) {
        initialServiceList = initialServices.map { it.copy() }
        _serviceList.value = initialServices
    }

    // 선택된 서비스 관리
    fun toggleServiceSelection(position: Int) {
        _serviceList.value?.get(position)?.isSelected =
            _serviceList.value?.get(position)?.isSelected != true

        _serviceList.value = _serviceList.value // 변경 반영

        updateNextButtonState()
    }

    private fun updateGymService() {
        val selectedServiceTitles =
            _serviceList.value?.filter { it.isSelected }
                ?.map { it.title.replace(" ", "_") } // 공백을 언더바로 대체
                ?: emptyList()

        val request = GymServiceUpdateRequest(serviceList = selectedServiceTitles)

        viewModelScope.launch {
            repository.updateGymService(request).let {
                when (it) {
                    is BaseState.Success -> {
                        Log.d("mypage_admin", "암장 서비스 수정 반영")
                    }

                    is BaseState.Error -> {
                        _event.emit(EditAdminServiceEvent.ShowToastMessage(it.msg))
                    }
                }
            }
        }
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(EditAdminServiceEvent.NavigateToBack)
        }
    }

    fun navigateToProfile() {
        updateGymService()
        viewModelScope.launch {
            _event.emit(EditAdminServiceEvent.NavigateToProfile)
        }
    }

}