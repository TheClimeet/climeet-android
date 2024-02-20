package com.climus.climeet.presentation.ui.main.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.app.App
import com.climus.climeet.presentation.util.Constants.X_MODE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class MyPageEvent {
    data object NavigateToAccount : MyPageEvent()
    data object NavigateToAlarm : MyPageEvent()
    data object NavigateToAnnounce : MyPageEvent()
    data object NavigateToCheerUp : MyPageEvent()
    data object NavigateToFollow : MyPageEvent()
    data object NavigateToAdminMyProfile : MyPageEvent()
    data object NavigateToClimerMyProfile : MyPageEvent()
    data object NavigateToMyShorts : MyPageEvent()
    data object NavigateToPolicy : MyPageEvent()
    data object NavigateToSendOpinion : MyPageEvent()
    data object NavigateToReview : MyPageEvent()
    data object Logout : MyPageEvent()
}

@HiltViewModel
class MyPageViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<MyPageEvent>()
    val event: SharedFlow<MyPageEvent> = _event.asSharedFlow()


    fun navigateToAccount() {
        viewModelScope.launch {
            _event.emit(MyPageEvent.NavigateToAccount)
        }
    }

    fun navigateToAlarm() {
        viewModelScope.launch {
            _event.emit(MyPageEvent.NavigateToAlarm)
        }
    }

    fun navigateToAnnounce() {
        viewModelScope.launch {
            _event.emit(MyPageEvent.NavigateToAnnounce)
        }
    }

    fun navigateToCheerUp() {
        viewModelScope.launch {
            _event.emit(MyPageEvent.NavigateToCheerUp)
        }
    }

    fun navigateToFollow() {
        viewModelScope.launch {
            _event.emit(MyPageEvent.NavigateToFollow)
        }
    }

    fun navigateToMyProfile() {
        viewModelScope.launch {

            val mode = App.sharedPreferences.getString(X_MODE, null)

            mode?.let {
                when (it) {
                    "CLIMER" -> _event.emit(MyPageEvent.NavigateToClimerMyProfile)
                    "ADMIN" -> _event.emit(MyPageEvent.NavigateToAdminMyProfile)
                }
            } ?: run {

            }

        }
    }

    fun navigateToMyShorts() {
        viewModelScope.launch {
            _event.emit(MyPageEvent.NavigateToMyShorts)
        }
    }

    fun navigateToPolicy() {
        viewModelScope.launch {
            _event.emit(MyPageEvent.NavigateToPolicy)
        }
    }

    fun navigateToSendOpinion() {
        viewModelScope.launch {
            _event.emit(MyPageEvent.NavigateToSendOpinion)
        }
    }

    fun navigateToReview() {
        viewModelScope.launch {
            _event.emit(MyPageEvent.NavigateToReview)
        }
    }

    fun logout() {
        viewModelScope.launch {
            _event.emit(MyPageEvent.Logout)
        }
    }


}