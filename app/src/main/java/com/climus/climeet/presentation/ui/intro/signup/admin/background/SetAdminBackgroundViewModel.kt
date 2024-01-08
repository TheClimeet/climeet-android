package com.climus.climeet.presentation.ui.intro.signup.admin.background

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.intro.signup.admin.AdminSignupForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SetAdminBackgroundEvent {
    data object NavigateToBack : SetAdminBackgroundEvent()
    data object NavigateToNext : SetAdminBackgroundEvent()
}

@HiltViewModel
class SetAdminBackgroundViewModel @Inject constructor() : ViewModel(){

    private val _event = MutableSharedFlow<SetAdminBackgroundEvent>()
    val event: SharedFlow<SetAdminBackgroundEvent> = _event.asSharedFlow()

    var imgUri = MutableLiveData<String?>()
    val isNextButtonEnabled = imgUri.map { it != null }

    // 개인정보 설정으로 이동
    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(SetAdminBackgroundEvent.NavigateToBack)
        }
    }

    // 서비스 설정으로 이동
    fun navigateToNext() {
        Log.d("admin", "배경사진 Uri : ${AdminSignupForm.backgroundImage}")

        viewModelScope.launch {
            _event.emit(SetAdminBackgroundEvent.NavigateToNext)
        }
    }
}