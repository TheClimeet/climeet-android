package com.climus.climeet.presentation.ui.main.mypage.follow.viewpager

import androidx.lifecycle.ViewModel
import com.climus.climeet.data.model.response.UserFollowSimpleResponse
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.home.searchcrag.model.FollowClimber
import com.climus.climeet.presentation.ui.main.home.viewpager.ranking.CompleteClimbingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class FollowingUiState(
    val followingList: List<UserFollowSimpleResponse> = emptyList(),
    val followerList: List<UserFollowSimpleResponse> = emptyList(),
    val searchClimberList: List<FollowClimber> = emptyList(),
)

@HiltViewModel
class FollowingViewModel @Inject constructor(private val repository: MainRepository): ViewModel() {

    private val _uiState = MutableStateFlow(CompleteClimbingUiState())
    val uiState: StateFlow<CompleteClimbingUiState> = _uiState.asStateFlow()

}