package com.climus.climeet.presentation.ui.main.global.gymprofile.info

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.Review
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.global.gymprofile.model.GymBusinessHour
import com.climus.climeet.presentation.ui.main.global.gymprofile.model.GymPrice
import com.climus.climeet.presentation.ui.main.global.gymprofile.model.GymService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class GymProfileTabInfoUiState(
    val gymId: Long = 0L,
    val address: String? = "암장 정보가 비어있어요",
    val location: String? = "암장 정보가 비어있어요",
    val tel: String? = "암장 정보가 비어있어요",
    val startTime: String = "휴무",
    val gymBusinessHours: List<GymBusinessHour>? = emptyList(),
    val gymServiceList: List<GymService>? = emptyList(),
    val gymPriceList: List<GymPrice>? = emptyList(),
    val averageRating: Float = 0f,
    val reviewNum: Int = 0,
    val myGymReview: Review? = null,
    val gymReviewList: List<Review>? = emptyList(),
    val isTimeVisible: Boolean = false,
    val isTimeErrorVisible: Boolean = false,
    val isRvTimeVisible: Boolean = false,
    val isIvToggleVisible: Boolean = false
)

sealed class GymProfileInfoEvent {
    data object NavigateToGymReviewBottomSheetFragment : GymProfileInfoEvent()
    data object NavigateToGymReviewFromMyPage : GymProfileInfoEvent()
    data object NavigateToEditService : GymProfileInfoEvent()
}

@HiltViewModel
class GymProfileInfoViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GymProfileTabInfoUiState())
    val uiState: StateFlow<GymProfileTabInfoUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<GymProfileInfoEvent>()
    val event: SharedFlow<GymProfileInfoEvent> = _event.asSharedFlow()

    var gymId = 0L

    fun setCragId(id: Long) {
        gymId = id
    }

    fun getGymTabInfo() {
        viewModelScope.launch {
            when (val result = repository.getGymProfileTabInfo(gymId)) {
                is BaseState.Success -> {
                    // 성공
                    _uiState.update { state ->
                        state.copy(
                            gymId = gymId,
                            address = result.body.address ?: state.address,
                            location = result.body.location ?: state.location,
                            tel = result.body.tel ?: state.tel,
                            gymBusinessHours = result.body.businessHours?.let { hours ->
                                getCompleteBusinessHours(hours).map {
                                    GymBusinessHour(
                                        it.key,
                                        it.value
                                    )
                                }
                            } ?: state.gymBusinessHours,
                            gymServiceList = result.body.serviceList?.map { GymService(it) }
                                ?: state.gymServiceList,
                            gymPriceList = result.body.priceList?.map { GymPrice(it.key, it.value) }
                                ?: state.gymPriceList,
                            startTime = setTodayOpeningTime(state.gymBusinessHours),
                            isTimeVisible = state.gymBusinessHours.isNullOrEmpty().not(),
                            isTimeErrorVisible = state.gymBusinessHours.isNullOrEmpty(),
                            isRvTimeVisible = false,
                            isIvToggleVisible = false
                        )
                    }
                }

                is BaseState.Error -> {
                    result.msg // 서버 에러 메시지
                    Log.d("gym_profile", "정보 탭 불러오기 실패")
                }
            }
            _uiState.update { state ->
                state.copy(
                    startTime = setTodayOpeningTime(state.gymBusinessHours)
                )
            }
        }

        getReviewInfo()
    }


    fun getReviewInfo() {
        viewModelScope.launch {
            delay(500)
            repository.getGymReview(gymId, 0, 15).let { it ->
                when (it) {
                    is BaseState.Success -> {
                        // 성공
                        _uiState.update { state ->
                            state.copy(
                                reviewNum = it.body.result.summary.reviewCount,
                                averageRating = it.body.result.summary.averageRating,
                                myGymReview = it.body.result.summary.myReview ?: null,
                                gymReviewList = it.body.result.reviewList ?: state.gymReviewList
                            )
                        }
                    }

                    is BaseState.Error -> {
                        it.msg // 서버 에러 메시지
                        Log.d("gym_profile", "리뷰 불러오기 실패")
                    }
                }
            }
        }
    }

    private fun getCompleteBusinessHours(businessHours: Map<String, List<String>>): Map<String, List<String>> {
        val completeHours = mutableMapOf(
            "일" to listOf("휴무"),
            "월" to listOf("휴무"),
            "화" to listOf("휴무"),
            "수" to listOf("휴무"),
            "목" to listOf("휴무"),
            "금" to listOf("휴무"),
            "토" to listOf("휴무")
        )

        businessHours.forEach { (day, hours) ->
            completeHours[day] = hours
        }

        return completeHours
    }

    private fun setTodayOpeningTime(gymBusinessHours: List<GymBusinessHour>?): String {
        val today = getCurrentDayOfWeek()
        val todayBusinessHour = gymBusinessHours?.find { it.day == today }

        return if (todayBusinessHour != null && todayBusinessHour.hours.isNotEmpty() && todayBusinessHour.hours[0] != "휴무") {
            "${todayBusinessHour.hours[0]}에 영업 시작"
        } else {
            "휴무"
        }
    }

    private fun getCurrentDayOfWeek(): String {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")
        return daysOfWeek[dayOfWeek - 1] // Calendar.DAY_OF_WEEK는 1부터 시작하므로, 1을 빼줍니다.
    }

    fun onLayoutTimeTitleClick() {
        _uiState.update { state ->
            state.copy(
                isRvTimeVisible = !state.isRvTimeVisible,
                isIvToggleVisible = !state.isIvToggleVisible
            )
        }
    }

    fun onRvTimeClick() {
        _uiState.update { state ->
            state.copy(
                isRvTimeVisible = false,
                isIvToggleVisible = false
            )
        }
    }

    fun navigateToGymReviewBottomSheetFragment() {
        viewModelScope.launch {
            _event.emit(
                GymProfileInfoEvent.NavigateToGymReviewBottomSheetFragment
            )
        }
    }

    fun navigateToEditService() {
        viewModelScope.launch {
            _event.emit(
                GymProfileInfoEvent.NavigateToEditService
            )
        }
    }

    fun navigateToReviewFromMyPage() {
        viewModelScope.launch {
            _event.emit(
                GymProfileInfoEvent.NavigateToGymReviewFromMyPage
            )
        }
    }
}