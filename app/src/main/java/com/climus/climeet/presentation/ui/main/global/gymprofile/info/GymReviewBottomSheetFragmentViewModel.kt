package com.climus.climeet.presentation.ui.main.global.gymprofile.info

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.app.App.Companion.sharedPreferences
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.CreateGymProfileReviewRequest
import com.climus.climeet.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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
                        Log.d("gym_profile", "리뷰 업로드 실패 ${it.msg}")
                    }
                }
            }
        }
    }

    fun editReview() {
        viewModelScope.launch {

            if (reviewText.value.isNullOrBlank()) {
                _event.emit(GymReviewBottomSheetEvent.ShowToastMessage("리뷰를 입력해주세요."))
                return@launch
            }

            val request = CreateGymProfileReviewRequest(
                content = reviewText?.value!!,
                rating = reviewRating.value!!
            )

            Log.d("gym_profile", "수정된 리뷰 : $request")

            repository.editGymReview(gymId, request).let { it ->
                when (it) {
                    is BaseState.Success -> {
                        // 다음 리뷰를 위해 값 초기화
                        reviewRating.value = 5F
                        reviewText.value = ""
                        navigateToProfile()
                    }

                    is BaseState.Error -> {
                        it.msg // 서버 에러 메시지
                        Log.d("gym_profile", "리뷰 수정 실패 ${it.msg}")
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