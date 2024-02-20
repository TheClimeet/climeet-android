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
            repository.getGymStatsWeek(gymId).let { result ->
                when (result) {
                    is BaseState.Success -> {
                        val body = result.body

                        val list = mutableListOf<StickChartUiData>()

                        var maxPercent = -1f
                        var totalCount = 0
                        body.difficulty.forEach {
                            if (maxPercent < it.count.toFloat()) {
                                maxPercent = it.count.toFloat()
                            }
                            totalCount += it.count
                        }

                        Log.d("gym_profile", "총 횟수 : $totalCount")

                        body.difficulty.forEach {
                            val percent = if (it.count == 0) {
                                0
                            } else {
                                ((it.count.toFloat() / totalCount.toFloat()) * 100).roundToInt()
                            }
                            list.add(
                                StickChartUiData(
                                    percentString = "$percent%",
                                    percent = maxOf((it.count.toFloat() / maxPercent) * 0.8f, 0.001f),
                                    levelName = it.gymDifficultyName,
                                    levelHex = it.gymDifficultyColor
                                )
                            )
                        }

                        _uiState.update { state ->
                            state.copy(
                                chartUiList = list
                            )
                        }

                    }
                    is BaseState.Error -> {
                        result.msg // 서버 에러 메시지
                        Log.d("API", result.msg)
                    }
                }
            }
        }
    }
}