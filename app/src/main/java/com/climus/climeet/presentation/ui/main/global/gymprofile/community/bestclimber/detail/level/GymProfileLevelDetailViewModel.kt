package com.climus.climeet.presentation.ui.main.global.gymprofile.community.bestclimber.detail.level

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.app.App.Companion.sharedPreferences
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.BestLevelCimberSimpleResponse
import com.climus.climeet.data.model.response.GymLevelBestClimberResponse
import com.climus.climeet.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GymProfileLevelDetailUiState (
    val rankingList: List<GymLevelBestClimberResponse> = emptyList()
)

@HiltViewModel
class GymProfileLevelDetailViewModel @Inject constructor(private val repository: MainRepository): ViewModel() {
    private val _uiState = MutableStateFlow(GymProfileLevelDetailUiState())
    val uiState: StateFlow<GymProfileLevelDetailUiState> = _uiState.asStateFlow()

    var gymId = sharedPreferences.getLong("gymId", 0L)

    fun getClimberRankingOrderLevel() {
        viewModelScope.launch {

            repository.getGymClimberRankingOrderLevel(gymId).let {
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
