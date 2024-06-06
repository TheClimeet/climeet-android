package com.climus.climeet.presentation.ui.main.global.gymprofile.community.skill

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.app.App.Companion.sharedPreferences
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

data class GymSkillUiState(
    val mySkill: String? = "",
    val chartUiList: List<StickChartUiData> = emptyList()
)

@HiltViewModel
class GymProfileSkillViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GymSkillUiState())
    val uiState: StateFlow<GymSkillUiState> = _uiState.asStateFlow()

    var gymId = sharedPreferences.getLong("gymId", 0L)

    init {
        getMyStatus()
    }

    private fun getMyStatus() {
        viewModelScope.launch {
            when (val mySkillResult = repository.getMyGymSkill(gymId)) {
                is BaseState.Success -> {
                    val mySkill = mySkillResult.body.string()
                    Log.d("gym_profile", "내 실력 : $mySkill")
                    _uiState.update { state ->
                        state.copy(mySkill = mySkill)
                    }
                }

                is BaseState.Error -> {
                    Log.d("API", mySkillResult.msg)
                }
            }
            when (val gymSkillDistributionResult =
                repository.getGymSkillDistribution(gymId)) {
                is BaseState.Success -> {
                    val list = mutableListOf<StickChartUiData>()
                    gymSkillDistributionResult.body.forEach {
                        val percent = if (it.percentage == 0) {
                            0
                        } else {
                            it.percentage
                        }
                        val color = if (it.gymDifficultyName == _uiState.value.mySkill) {
                            "#BEDF22"
                        } else {
                            "#FFFFFF"
                        }

                        list.add(
                            StickChartUiData(
                                percentString = "${it.percentage}%",
                                percent = maxOf((percent.toFloat() / 100) * 0.8f, 0.001f),
                                levelName = it.gymDifficultyName,
                                levelHex = it.gymDifficultyColor,
                                levelStringColor = color
                            )
                        )
                    }
                    _uiState.update { state ->
                        state.copy(chartUiList = list)
                    }
                }

                is BaseState.Error -> {
                    Log.d("API", "skill Error : ${gymSkillDistributionResult.msg}")
                }
            }
        }
    }
}