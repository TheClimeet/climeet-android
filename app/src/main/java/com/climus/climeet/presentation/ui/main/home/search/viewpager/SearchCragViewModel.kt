package com.climus.climeet.presentation.ui.main.home.search.viewpager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.UserFollowSimpleResponse
import com.climus.climeet.data.model.response.UserHomeGymDetailResponse
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.intro.signup.climer.model.FollowCrag
import com.climus.climeet.presentation.ui.main.home.search.model.FollowClimber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchCragUiState(
    val followGymList: List<UserHomeGymDetailResponse> = emptyList(),
    val followingList: List<UserFollowSimpleResponse> = emptyList(),
    val searchList: List<FollowCrag> = emptyList(),
    val progressState: Boolean = false,
    val emptyResultState: Boolean = false,
)

@HiltViewModel
class SearchCragViewModel @Inject constructor(private val repository: MainRepository): ViewModel() {

    private val _uiState = MutableStateFlow(SearchCragUiState())
    val uiState: StateFlow<SearchCragUiState> = _uiState.asStateFlow()

    fun getGymFollowing() {
        viewModelScope.launch {
            repository.getGymsFollowing().let {
                when(it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                followGymList = it.body
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