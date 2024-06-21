package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AdminBackgroundEditEvent{
    data object NavigateToBack : AdminBackgroundEditEvent()
    data object NavigateToNext : AdminBackgroundEditEvent()
}

@HiltViewModel
class MyPageAdminProfileEditBackgroundViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<AdminBackgroundEditEvent>()
    val event: SharedFlow<AdminBackgroundEditEvent> = _event.asSharedFlow()

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(AdminBackgroundEditEvent.NavigateToBack)
        }
    }

    fun navigateToNext() {
        // todo : 배경 사진 바꿨으면 저장
        viewModelScope.launch {
            _event.emit(AdminBackgroundEditEvent.NavigateToNext)
        }
    }
}