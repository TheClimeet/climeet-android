package com.climus.climeet.presentation.ui.main.global.gymprofile.community.completionrate

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.app.App
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.customview.stickchart.StickChartUiData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt


data class GymCompletionUiState(
    val mySkill: String? = "",
    val chartUiList: List<StickChartUiData> = emptyList()
)

@HiltViewModel
class GymProfileAvgCompletionViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GymCompletionUiState())
    val uiState: StateFlow<GymCompletionUiState> = _uiState.asStateFlow()

    var gymId = App.sharedPreferences.getLong("gymId", 0L)

    init {
        getMyStatus()
    }

    private fun getMyStatus() {

        viewModelScope.launch {
            when (val myGymSkillResult = repository.getMyGymSkill(gymId)) {
                is BaseState.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            mySkill = myGymSkillResult.body.string()
                        )
                    }
                }

                is BaseState.Error -> {
                    myGymSkillResult.msg // 서버 에러 메시지
                    Log.d("API", myGymSkillResult.msg)
                }
            }

            when (val result = repository.getGymStatsWeek(gymId)) {
                is BaseState.Success -> {
                    val body = result.body

                    val maxPercent = body.difficulty.maxOfOrNull { it.count } ?: 0
                    var totalCount = body.difficulty.sumBy { it.count }

                    Log.d("gym_profile", "총 횟수 : $totalCount")

                    val list = if (totalCount == 0) {
                        emptyList()
                    } else {
                        body.difficulty.map { item ->
                            val percent =
                                ((item.count.toFloat() / totalCount.toFloat()) * 100).roundToInt()

                            val color = if (item.gymDifficultyName == _uiState.value.mySkill) {
                                "#BEDF22"
                            } else {
                                "#FFFFFF"
                            }

                            StickChartUiData(
                                percentString = "$percent%",
                                percent = if (maxPercent == 0) 0f else (item.count.toFloat() / maxPercent.toFloat()) * 0.8f,
                                levelName = item.gymDifficultyName,
                                levelHex = item.gymDifficultyColor,
                                levelStringColor = color
                            )
                        }

                    }

                    _uiState.update { state ->
                        state.copy(
                            chartUiList = list
                        )
                    }

                }

                is BaseState.Error -> {
                    _uiState.update { state ->
                        state.copy(
                            chartUiList = emptyList()
                        )
                    }
                    Log.d("testfucking", "Error: ${result.msg}")
                }
            }
        }
    }
}