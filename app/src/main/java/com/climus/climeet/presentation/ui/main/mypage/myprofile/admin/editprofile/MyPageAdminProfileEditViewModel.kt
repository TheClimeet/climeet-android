package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AdminProfileEditEvent{
    data object NavigateToBack : AdminProfileEditEvent()
    data object NavigateToProfile : AdminProfileEditEvent()
}

@HiltViewModel
class MyPageAdminProfileEditViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<AdminProfileEditEvent>()
    val event: SharedFlow<AdminProfileEditEvent> = _event.asSharedFlow()

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(AdminProfileEditEvent.NavigateToBack)
        }
    }

    fun navigateToNext() {
        // todo : 배경, 닉네임, 프사 변경된 것 반영
        viewModelScope.launch {
            _event.emit(AdminProfileEditEvent.NavigateToProfile)
        }
    }
}