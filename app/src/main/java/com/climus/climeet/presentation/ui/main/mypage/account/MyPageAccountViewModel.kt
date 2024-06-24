package com.climus.climeet.presentation.ui.main.mypage.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.config.DataStoreManager
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.UserProfileInfoResponse
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

data class MyPageAccountUiState(
    val myProfile: UserProfileInfoResponse? = null,
)

sealed class MyPageAccountEvent {
    data object ShowLogoutDialog : MyPageAccountEvent()
    data object ShowWithdrawDialog : MyPageAccountEvent()
}

@HiltViewModel
class MyPageAccountViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyPageAccountUiState())
    val uiState: StateFlow<MyPageAccountUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<MyPageAccountEvent>()
    val event: SharedFlow<MyPageAccountEvent> = _event.asSharedFlow()

    private var userMode: String? = null
    private var userType: String? = null

    // 유저, 관리자 구분
    fun checkUserMode(): Boolean {
        viewModelScope.launch {
            userMode = dataStoreManager.getLoginMode()
        }
        return userMode == "ADMIN"
    }

    fun checkLoginType(): String? {
        viewModelScope.launch {
            userType = dataStoreManager.getLoginType()
        }
        return userType
    }

    fun getUserProfile() {
        viewModelScope.launch {
            repository.getUserProfile().let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                myProfile = it.body
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

    fun deleteLoginType() {
        viewModelScope.launch {
            dataStoreManager.deleteLoginType()
        }
    }

    fun showLogoutDialog() {
        viewModelScope.launch {
            _event.emit(MyPageAccountEvent.ShowLogoutDialog)
        }
    }

    fun showWithdrawDialog() {
        viewModelScope.launch {
            _event.emit(MyPageAccountEvent.ShowWithdrawDialog)
        }
    }
}