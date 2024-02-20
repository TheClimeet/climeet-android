package com.climus.climeet.presentation.ui.main.global.climerprofile.viewpager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.customview.stickchart.StickChartUiData
import com.climus.climeet.presentation.ui.main.global.climerprofile.model.ProfileHomeGymUiData
import com.climus.climeet.presentation.ui.main.global.toProfileHomeGymUiData
import com.climus.climeet.presentation.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

data class ClimberProfileInfoUiState(
    val homeGymList: List<ProfileHomeGymUiData> = emptyList(),
    val chartUiList: List<StickChartUiData> = emptyList(),
    val averageDoneProgress: Int = 0,
    val percent: String = "",
    val userName: String = "",
    val userProfileImg: String = "",
    val followingString: String = "",
    val isFollower: Boolean = false
)

sealed class ClimberProfileEvent {
    data class NavigateToGymProfile(val id: Long) : ClimberProfileEvent()
}

@HiltViewModel
class ClimberProfileInfoViewModel @Inject constructor(private val repository: MainRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(ClimberProfileInfoUiState())
    val uiState: StateFlow<ClimberProfileInfoUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ClimberProfileEvent>()
    val event: SharedFlow<ClimberProfileEvent> = _event.asSharedFlow()

    private var userId: Long = 0

    fun setUserId(id: Long) {
        userId = id
        getStatistics()
        getUserHomeGyms()
    }

    private fun getStatistics() {
        viewModelScope.launch {
            repository.getClimberProfileStatistics(userId).let {
                when (it) {
                    is BaseState.Success -> {

                        val total  = it.body.totalCompletedCount ?: run { 0 }
                        val attempt =  it.body.attemptRouteCount ?: run { 1 }

                        val percent =
                            (total.toFloat() / attempt * 100).roundToInt()
                        _uiState.update { state ->
                            state.copy(
                                averageDoneProgress = percent,
                                percent = "$percent%"
                            )
                        }

                        // todo 여기부터가 차트그릴 데이터 파싱하는 부분
                        val list = mutableListOf<StickChartUiData>()

                        // todo 받은 차트값중에서 max값을 고르는 for문
                        var maxPercent = -1f
                        it.body.difficulty.forEach { data ->
                            if (maxPercent < data.value.toFloat()) {
                                maxPercent = data.value.toFloat()
                            }
                        }

                        // todo max값 차트값을 기준으로 퍼센테이지를 계산하는 로직

                        it.body.difficulty.forEach { data ->
                            val percent = if (data.value == 0) {
                                0
                            } else {
                                ((data.value.toFloat() / total) * 100).roundToInt()
                            }

                            list.add(
                                StickChartUiData(
                                    // todo 차트 꼭대기 퍼센트 스트링
                                    percentString = "$percent%",
                                    // todo 차트 막대 길이비율 정하는 float값
                                    percent = if (percent == 0) 0f else (data.value.toFloat() / maxPercent) * 0.8f,
                                    // todo 차트 하단에 레벨이름
                                    levelName = data.key,
                                    // todo 레벨에 대응되는 색상 hex 값
                                    levelHex = Constants.climeetColor[data.key]
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

                    }
                }
            }
        }
    }

    private fun getUserHomeGyms() {
        viewModelScope.launch {
            repository.getUserHomeGyms(userId).let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                homeGymList = it.body.map { data ->
                                    data.toProfileHomeGymUiData(::navigateToGymProfile)
                                }
                            )
                        }
                    }

                    is BaseState.Error -> {

                    }
                }
            }
        }
    }


    private fun navigateToGymProfile(id: Long) {
        viewModelScope.launch {
            _event.emit(ClimberProfileEvent.NavigateToGymProfile(id))
        }
    }

}