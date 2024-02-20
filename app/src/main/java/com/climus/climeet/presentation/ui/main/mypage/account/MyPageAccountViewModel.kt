package com.climus.climeet.presentation.ui.main.mypage.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.UserProfileInfoResponse
import com.climus.climeet.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyPageAccountUiState(
    val myProfile: UserProfileInfoResponse? = null
)

@HiltViewModel
class MyPageAccountViewModel @Inject constructor(private val repository: MainRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(MyPageAccountUiState())
    val uiState: StateFlow<MyPageAccountUiState> = _uiState.asStateFlow()

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

}