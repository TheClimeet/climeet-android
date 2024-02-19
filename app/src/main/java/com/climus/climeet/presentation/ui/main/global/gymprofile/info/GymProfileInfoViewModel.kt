package com.climus.climeet.presentation.ui.main.global.gymprofile.info

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.global.gymprofile.model.GymBusinessHour
import com.climus.climeet.presentation.ui.main.global.gymprofile.model.GymPrice
import com.climus.climeet.presentation.ui.main.global.gymprofile.model.GymService
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

data class GymProfileTabInfoUiState(
    val gymId: Long = 0L,
    val address: String = "",
    val location: String = "",
    val tel: String = "",
    val gymBusinessHours: List<GymBusinessHour> = emptyList(),
    val gymServiceList: List<GymService> = emptyList(),
    val gymPriceList: List<GymPrice> = emptyList()
)

sealed class GymProfileInfoEvent {
    data class NavigateToGymReviewBottomSheetFragment(val id: Long) : GymProfileInfoEvent()
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
        Log.d("gym_profile", "info 뷰모델 암장 아이디 : $gymId")
    }

    fun getGymTabInfo() {
        viewModelScope.launch {
            repository.getGymProfileTabInfo(gymId).let { it ->
                when (it) {
                    is BaseState.Success -> {
                        // 성공
                        _uiState.update { state ->
                            state.copy(
                                gymId = gymId,
                                address = it.body.address,
                                location = it.body.location,
                                tel = it.body.tel,
                                gymBusinessHours = it.body.businessHours.map { GymBusinessHour(it.key, it.value) },
                                gymServiceList = it.body.serviceList.map { GymService(it) },
                                gymPriceList = it.body.priceList.map { GymPrice(it.key, it.value) }
                            )
                        }
                        Log.d("gym_profile", "정보 탭 불러오기 성공")
                    }

                    is BaseState.Error -> {
                        it.msg // 서버 에러 메시지
                        Log.d("gym_profile", "정보 탭 불러오기 실패")
                    }
                }
            }
        }
    }

    fun navigateToGymReviewBottomSheetFragment() {
        viewModelScope.launch {
            _event.emit(
                GymProfileInfoEvent.NavigateToGymReviewBottomSheetFragment(gymId)
            )
        }
    }
}