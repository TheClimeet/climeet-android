package com.climus.climeet.presentation.ui.main.global.climerprofile.viewpager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.customview.stickchart.StickChartUiData
import com.climus.climeet.presentation.ui.main.global.climerprofile.model.ProfileHomeGymUiData
import com.climus.climeet.presentation.ui.main.global.toProfileHomeGymUiData
import com.climus.climeet.presentation.ui.main.record.model.SelectGymData
import com.climus.climeet.presentation.ui.main.record.stats.StatsEvent
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
    val gymList: List<SelectGymData> = emptyList(),
    val averageDoneProgress: Int = 0,
    val percent: String = ""
)

sealed class ClimberProfileEvent {
    data class NavigateToGymProfile(val id: Long) : ClimberProfileEvent()
    data object ShowPopupWindow : ClimberProfileEvent()
    data class ShowToastMessage(val msg: String) : ClimberProfileEvent()
}

@HiltViewModel
class ClimberProfileInfoViewModel @Inject constructor(private val repository: MainRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(ClimberProfileInfoUiState())
    val uiState: StateFlow<ClimberProfileInfoUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ClimberProfileEvent>()
    val event: SharedFlow<ClimberProfileEvent> = _event.asSharedFlow()

    private val _selectedGymId = MutableStateFlow<Int>(0)
    val selectedGymId: StateFlow<Int> = _selectedGymId.asStateFlow()

    private var userId: Long = 0

    var isListShow = MutableStateFlow(false)
    var selectedGymName = MutableStateFlow("클밋 기준")


    fun setUserId(id: Long) {
        userId = id
        getStatistics()
        getUserHomeGyms()
        getMyClimbedGymList()
    }

    private fun getMyClimbedGymList() {
//        viewModelScope.launch {
//            val climbedDate = selectedDate.value?.let {
//                it
//            } ?: run {
//                LocalDate.now()
//            }
//            // todo 사용자의 userId를 어떻게 가져오지
//            repository.getUserClimbedGymList(1, climbedDate.year, climbedDate.monthValue)
//                .let { result ->
//                    when (result) {
//                        is BaseState.Success -> {
//                            _uiState.update { state ->
//                                state.copy(
//                                    gymList = listOf(
//                                        SelectGymData(0, "클밋 기준", ::onGymClicked)
//                                    ) + result.body.visitedClimbingGym.map {
//                                        it.toSelectGymData(::onGymClicked)
//                                    }
//                                )
//                            }
//                        }
//
//                        is BaseState.Error -> {
//                            _uiState.update { state ->
//                                state.copy(
//                                    gymList = listOf(
//                                        SelectGymData(0, "클밋 기준", ::onGymClicked)
//                                    )
//                                )
//                            }
//                            _event.emit(StatsEvent.ShowToastMessage("암장을 불러오지 못했습니다!"))
//                        }
//                    }
//                }
//        }

        val dummyGyms = listOf(
            SelectGymData(id = 0, name = "클밋 기준", onClickListener = ::onGymClicked),
            SelectGymData(id = 1, name = "더 클라임 신사점", onClickListener = ::onGymClicked),
            SelectGymData(id = 2, name = "피커스 구로", onClickListener = ::onGymClicked),
            SelectGymData(id = 3, name = "클라이머스 연남점", onClickListener = ::onGymClicked),
            SelectGymData(id = 4, name = "서울숲 구로", onClickListener = ::onGymClicked),
            SelectGymData(id = 5, name = "더 클라임 연남점", onClickListener = ::onGymClicked),
            SelectGymData(id = 6, name = "나는 짱", onClickListener = ::onGymClicked),
        )
        _uiState.value = _uiState.value.copy(gymList = dummyGyms)
    }

    private fun onGymClicked(gym: SelectGymData) {
        _selectedGymId.value = gym.id
        changeListShow()
        selectedGymName.update { gym.name }
        if (gym.id == 0) {
            getStatistics()
        } else {
            // todo gymId에 따른 값들 가져오기
            getStatistics()
        }
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
                        _uiState.update { state ->
                            state.copy(
                                chartUiList = emptyList()
                            )
                        }
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

    fun showPopupWindow() {
        changeListShow()
        viewModelScope.launch {
            _event.emit(ClimberProfileEvent.ShowPopupWindow)
        }
    }

    fun changeListShow() {
        viewModelScope.launch {
            isListShow.value = !isListShow.value
        }
    }

    private fun navigateToGymProfile(id: Long) {
        viewModelScope.launch {
            _event.emit(ClimberProfileEvent.NavigateToGymProfile(id))
        }
    }

}