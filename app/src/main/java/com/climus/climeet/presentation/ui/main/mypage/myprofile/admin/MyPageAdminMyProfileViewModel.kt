package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MyPageAdminProfileEvent {
    data object NavigateToEditAdminProfile : MyPageAdminProfileEvent()
}

@HiltViewModel
class MyPageAdminMyProfileViewModel @Inject constructor(val repository: MainRepository): ViewModel(){

    private val _event = MutableSharedFlow<MyPageAdminProfileEvent>()
    val event: SharedFlow<MyPageAdminProfileEvent> = _event.asSharedFlow()

    fun navigateToEditPage() {
        viewModelScope.launch {
            _event.emit(MyPageAdminProfileEvent.NavigateToEditAdminProfile)
        }
    }
}