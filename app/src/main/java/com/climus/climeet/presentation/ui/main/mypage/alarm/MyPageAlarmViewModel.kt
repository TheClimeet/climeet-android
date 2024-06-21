package com.climus.climeet.presentation.ui.main.mypage.alarm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

@HiltViewModel
class MyPageAlarmViewModel @Inject constructor(repository: MainRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AlarmSettingSwitchState())
    val uiState: StateFlow<AlarmSettingSwitchState> = _uiState.asStateFlow()

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
        // todo : 수정 api 호출
    }

    fun setLikeSwitchState(isChecked: Boolean) {
        _uiState.update { state ->
            state.copy(
                like = isChecked
            )
        }
        Log.d("alarm", "좋아요 : ${uiState.value.like}")
        // todo : 수정 api 호출
    }

    fun setCommentSwitchState(isChecked: Boolean) {
        _uiState.update { state ->
            state.copy(
                comment = isChecked
            )
        }
        Log.d("alarm", "댓글 : ${uiState.value.comment}")
        // todo : 수정 api 호출
    }

    fun setAppPushSwitchState(isChecked: Boolean) {
        _uiState.update { state ->
            state.copy(
                appPush = isChecked
            )
        }
        Log.d("alarm", "앱푸시 : ${uiState.value.appPush}")
        // todo : 앱 푸시 알림은 어떻게 설정?
    }

}