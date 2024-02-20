package com.climus.climeet.presentation.ui.main.global.gymprofile.community.bestclimber.detail.complete

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.app.App.Companion.sharedPreferences
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.GymCompleteBestClimberResponse
import com.climus.climeet.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GymProfileCompleteDetailUiState(
    val rankingList: List<GymCompleteBestClimberResponse> = emptyList()
)

@HiltViewModel
class GymProfileCompleteDetailViewModel @Inject constructor(private val repository: MainRepository): ViewModel() {

    private val _uiState = MutableStateFlow(GymProfileCompleteDetailUiState())
    val uiState: StateFlow<GymProfileCompleteDetailUiState> = _uiState.asStateFlow()

    var gymId = sharedPreferences.getLong("gymId", 0L)

    fun getClimberRankingOrderClearCount() {
        viewModelScope.launch {
            repository.getGymClimberRankingOrderClearCount(gymId).let {
                when(it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                rankingList = it.body
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