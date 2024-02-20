package com.climus.climeet.presentation.ui.main.mypage.follow.viewpager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.BestClearClimberSimpleResponse
import com.climus.climeet.data.model.response.UserFollowSimpleResponse
import com.climus.climeet.data.model.response.UserFollowingInfoResponse
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.home.search.model.FollowClimber
import com.climus.climeet.presentation.ui.main.home.viewpager.ranking.CompleteClimbingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FollowerUiState(
    val followerGymList: List<UserFollowSimpleResponse> = emptyList(),
    val followerList: List<UserFollowingInfoResponse> = emptyList(),
    val searchClimberList: List<FollowClimber> = emptyList(),
)

@HiltViewModel
class FollowerViewModel @Inject constructor(private val repository: MainRepository): ViewModel() {

    private val _uiState = MutableStateFlow(FollowerUiState())
    val uiState: StateFlow<FollowerUiState> = _uiState.asStateFlow()


    fun getUserFollowing(userId: Long, userCategory: String) {
        viewModelScope.launch {
            repository.getUserFollowing(userId, userCategory).let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                followerList = it.body
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