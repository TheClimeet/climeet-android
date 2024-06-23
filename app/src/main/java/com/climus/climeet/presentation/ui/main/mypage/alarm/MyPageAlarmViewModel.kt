package com.climus.climeet.presentation.ui.main.mypage.alarm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.NotificationUpdateRequest
import com.climus.climeet.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AlarmSettingSwitchState(
    val follower: Boolean = false,
    val like: Boolean = false,
    val comment: Boolean = false,
    val appPush: Boolean = false,
)

sealed class UpdateAlarmEvent {
    data class ShowToastMessage(val msg: String) : UpdateAlarmEvent()
}

@HiltViewModel
class MyPageAlarmViewModel @Inject constructor(val repository: MainRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AlarmSettingSwitchState())
    val uiState: StateFlow<AlarmSettingSwitchState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<UpdateAlarmEvent>()
    val event: SharedFlow<UpdateAlarmEvent> = _event.asSharedFlow()


    init {
        fetchSwitchStatesFromServer()
    }

    private fun fetchSwitchStatesFromServer() {
        // todo : 알림 상태 가져오는 api 연결
        // 더미 데이터
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    follower = true,
                    like = false,
                    comment = true,
                    appPush = false
                )
            }
        }
    }

    fun setFollowerSwitchState(isChecked: Boolean) {
        _uiState.update { state ->
            state.copy(
                follower = isChecked
            )
        }
        Log.d("alarm", "팔로워 : ${uiState.value.follower}")
    }

    fun setLikeSwitchState(isChecked: Boolean) {
        _uiState.update { state ->
            state.copy(
                like = isChecked
            )
        }
        Log.d("alarm", "좋아요 : ${uiState.value.like}")
    }

    fun setCommentSwitchState(isChecked: Boolean) {
        _uiState.update { state ->
            state.copy(
                comment = isChecked
            )
        }
        Log.d("alarm", "댓글 : ${uiState.value.comment}")
    }

    fun setAppPushSwitchState(isChecked: Boolean) {
        _uiState.update { state ->
            state.copy(
                appPush = isChecked
            )
        }
        Log.d("alarm", "앱푸시 : ${uiState.value.appPush}")
    }

    fun updateAlarmState() {
        viewModelScope.launch {
            val request = NotificationUpdateRequest(
                isAllowFollowNotification = uiState.value.follower,
                isAllowLikeNotification = uiState.value.like,
                isAllowCommentNotification = uiState.value.comment,
                isAllowAdNotification = uiState.value.appPush
            )
            repository.updateNotification(request).let {
                when(it){
                    is BaseState.Success -> {
                        Log.d("mypage_alarm", "알림 설정 업데이트")
                    }

                    is BaseState.Error -> {
                        _event.emit(UpdateAlarmEvent.ShowToastMessage(it.msg))
                    }
                }
            }
        }
    }

}