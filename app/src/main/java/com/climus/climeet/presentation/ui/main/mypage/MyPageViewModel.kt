package com.climus.climeet.presentation.ui.main.mypage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.app.App
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.customview.stickchart.StickChartUiData
import com.climus.climeet.presentation.ui.main.global.gymprofile.GymProfileInfoUiState
import com.climus.climeet.presentation.util.Constants.X_MODE
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

data class MyPageUiState(
    val userId: Long = 0L,
    val userName: String = "",
    val profileImgUrl: String = "",
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val isManager: Boolean = false
)

sealed class MyPageEvent {
    data object NavigateToAccount : MyPageEvent()
    data object NavigateToAlarm : MyPageEvent()
    data object NavigateToAnnounce : MyPageEvent()
    data object NavigateToCheerUp : MyPageEvent()
    data object NavigateToFollow : MyPageEvent()
    data object NavigateToAdminMyProfile : MyPageEvent()
    data object NavigateToClimerMyProfile : MyPageEvent()
    data object NavigateToMyShorts : MyPageEvent()
    data object NavigateToPolicy : MyPageEvent()
    data object NavigateToSendOpinion : MyPageEvent()
    data object NavigateToReview : MyPageEvent()
    data object Logout : MyPageEvent()
}

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyPageUiState())
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<MyPageEvent>()
    val event: SharedFlow<MyPageEvent> = _event.asSharedFlow()

    fun getProfileInfo() {
        viewModelScope.launch {
            repository.getMyPageProfile().let {
                when(it){
                    is BaseState.Success -> {

                        val profileInfo = it.body

                        _uiState.update { state ->
                            state.copy(
                                userId = profileInfo.userId,
                                userName = profileInfo.userName,
                                profileImgUrl = profileInfo.profileImgUrl,
                                followerCount = profileInfo.followerCount,
                                followingCount = profileInfo.followingCount,
                                isManager = profileInfo.isManager
                            )
                        }
                    }

                    is BaseState.Error -> {
                        it.msg // 서버 에러 메시지
                        Log.d("API", it.msg)
                    }
                }
            }
        }
    }

    fun navigateToAccount() {
        viewModelScope.launch {
            _event.emit(MyPageEvent.NavigateToAccount)
        }
    }

    fun navigateToAlarm() {
        viewModelScope.launch {
            _event.emit(MyPageEvent.NavigateToAlarm)
        }
    }

    fun navigateToAnnounce() {
        viewModelScope.launch {
            _event.emit(MyPageEvent.NavigateToAnnounce)
        }
    }

    fun navigateToCheerUp() {
        viewModelScope.launch {
            _event.emit(MyPageEvent.NavigateToCheerUp)
        }
    }

    fun navigateToFollow() {
        viewModelScope.launch {
            _event.emit(MyPageEvent.NavigateToFollow)
        }
    }

    fun navigateToMyProfile() {
        viewModelScope.launch {

            val mode = App.sharedPreferences.getString(X_MODE, null)

            mode?.let {
                when (it) {
                    "CLIMER" -> _event.emit(MyPageEvent.NavigateToClimerMyProfile)
                    "ADMIN" -> _event.emit(MyPageEvent.NavigateToAdminMyProfile)
                }
            } ?: run {

            }

        }
    }

    fun navigateToMyShorts() {
        viewModelScope.launch {
            _event.emit(MyPageEvent.NavigateToMyShorts)
        }
    }

    fun navigateToPolicy() {
        viewModelScope.launch {
            _event.emit(MyPageEvent.NavigateToPolicy)
        }
    }

    fun navigateToSendOpinion() {
        viewModelScope.launch {
            _event.emit(MyPageEvent.NavigateToSendOpinion)
        }
    }

    fun navigateToReview() {
        viewModelScope.launch {
            _event.emit(MyPageEvent.NavigateToReview)
        }
    }

    fun logout() {
        viewModelScope.launch {
            _event.emit(MyPageEvent.Logout)
        }
    }


}