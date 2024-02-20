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
            repository.getMyGymSkill(gymId).let {
                when (it) {
                    is BaseState.Success -> {
                        Log.d("gym_profile", "내 실력 : ${it.body}")
                        _uiState.update { state ->
                            state.copy(
                                mySkill = it.body
                            )
                        }
                    }

                    is BaseState.Error -> {
                        it.msg // 서버 에러 메시지
                        Log.d("API", it.msg)
                    }
                }
            }

            repository.getGymSkillDistribution(gymId).let {
                when (it) {
                    is BaseState.Success -> {
                        val list = mutableListOf<StickChartUiData>()

                        it.body.forEach {
                            val percent = if (it.percentage == 0) {
                                0.01
                            } else {
                                it.percentage
                            }
                            list.add(
                                StickChartUiData(
                                    // todo 차트 꼭대기 퍼센트 스트링
                                    percentString = "${it.percentage}%",
                                    // todo 차트 막대 길이비율 정하는 float값
                                    percent = (percent.toFloat() / 100),
                                    // todo 차트 하단에 레벨이름
                                    levelName = it.gymDifficultyName,
                                    // todo 색상 hex 값
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
                        it.msg // 서버 에러 메시지
                        Log.d("API", it.msg)
                    }
                }
            }
        }
    }
}