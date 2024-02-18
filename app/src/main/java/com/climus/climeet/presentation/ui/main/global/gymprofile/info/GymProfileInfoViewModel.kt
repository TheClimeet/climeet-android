package com.climus.climeet.presentation.ui.main.global.gymprofile.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class GymProfileInfoEvent {
    data class NavigateToGymReviewBottomSheetFragment(val id: Long) : GymProfileInfoEvent()
}

@HiltViewModel
class GymProfileInfoViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _event = MutableSharedFlow<GymProfileInfoEvent>()
    val event: SharedFlow<GymProfileInfoEvent> = _event.asSharedFlow()

    var gymId = 0L

    fun setGymId(id: Long) {
        gymId = id
    }

    fun navigateToGymReviewBottomSheetFragment() {
        viewModelScope.launch {
            _event.emit(
                GymProfileInfoEvent.NavigateToGymReviewBottomSheetFragment(gymId)
            )
        }
    }
}