package com.climus.climeet.presentation.ui.main.global.gymprofile.info

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.app.App.Companion.getContext
import com.climus.climeet.app.App.Companion.sharedPreferences
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.CreateGymProfileReviewRequest
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.global.gymprofile.model.GymBusinessHour
import com.climus.climeet.presentation.ui.main.global.gymprofile.model.GymPrice
import com.climus.climeet.presentation.ui.main.global.gymprofile.model.GymService
import com.climus.climeet.presentation.ui.main.global.selectsector.SelectSectorBottomSheetEvent
import com.climus.climeet.presentation.ui.main.record.calendar.createclimbingrecord.CreateClimbingRecordEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class GymReviewBottomSheetEvent {
    data object CreateReview : GymReviewBottomSheetEvent()
    data class ShowToastMessage(val msg: String) : GymReviewBottomSheetEvent()
}

@HiltViewModel
class GymReviewBottomSheetFragmentViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _event = MutableSharedFlow<GymReviewBottomSheetEvent>()
    val event: SharedFlow<GymReviewBottomSheetEvent> = _event.asSharedFlow()

    var gymId = 0L

    val reviewText = MutableLiveData<String>()
    val reviewRating = MutableLiveData<Float>(5F)

    fun getGymId() {
        gymId = sharedPreferences.getLong("gymId", 0L)
    }

    fun createReview() {
        viewModelScope.launch {

            if (reviewText.value.isNullOrBlank()) {
                _event.emit(GymReviewBottomSheetEvent.ShowToastMessage("리뷰를 입력해주세요."))
                return@launch
            }

            val request = CreateGymProfileReviewRequest(
                content = reviewText?.value!!,
                rating = reviewRating.value!!
            )

            repository.createGymReview(gymId, request).let { it ->
                when (it) {
                    is BaseState.Success -> {
                        // 성공
                        navigateToProfile()
                    }

                    is BaseState.Error -> {
                        it.msg // 서버 에러 메시지
                        Log.d("gym_profile", "정보 탭 불러오기 실패")
                    }
                }
            }
        }
    }

    fun navigateToProfile() {
        viewModelScope.launch {
            _event.emit(GymReviewBottomSheetEvent.CreateReview)
        }
    }

}