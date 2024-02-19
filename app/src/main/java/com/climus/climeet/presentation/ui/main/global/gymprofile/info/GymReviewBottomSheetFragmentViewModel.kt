package com.climus.climeet.presentation.ui.main.global.gymprofile.info

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.app.App.Companion.sharedPreferences
import com.climus.climeet.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class GymReviewBottomSheetEvent {
    data object CreateReview : GymReviewBottomSheetEvent()
}

@HiltViewModel
class GymReviewBottomSheetFragmentViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _event = MutableSharedFlow<GymReviewBottomSheetEvent>()
    val event: SharedFlow<GymReviewBottomSheetEvent> = _event.asSharedFlow()

    var gymId = 0L

    fun getGymId() {
        gymId = sharedPreferences.getLong("gymId", 0L)
        Log.d("gym_profile", "암장 리뷰 아이디 : $gymId")
    }

    fun navigateToProfile() {
        viewModelScope.launch {
            _event.emit(GymReviewBottomSheetEvent.CreateReview)
        }
    }
}