package com.climus.climeet.presentation.ui.intro.signup.admin.error

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SetCragErrorEvent {
    data object NavigateToErrorComplete : SetCragErrorEvent()
    data object NavigateToBack : SetCragErrorEvent()
}

@HiltViewModel
class SetCragErrorViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<SetCragErrorEvent>()
    val event: SharedFlow<SetCragErrorEvent> = _event.asSharedFlow()

    private val cragId = MutableStateFlow(-1L)
    val cragImgUrl = MutableStateFlow("")
    val cragName = MutableStateFlow("")

    val name = MutableStateFlow("")
    val phone = MutableStateFlow("")
    val email = MutableStateFlow("")
    val position = MutableStateFlow("")

    val isDataReady =
        combine(
            name,
            phone,
            email,
            position
        ) { name, phone, email, position ->
            name.isNotBlank() && phone.isNotBlank() && email.isNotBlank() && position.isNotBlank()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            false
        )

    fun setCragInfo(
        id: Long,
        imgUrl: String,
        name: String
    ) {
        cragId.value = id
        cragImgUrl.value = imgUrl
        cragName.value = name
    }

    fun submitErrorForm() {
        viewModelScope.launch {

            // todo 오류 form API 통신

            _event.emit(SetCragErrorEvent.NavigateToErrorComplete)
        }
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(SetCragErrorEvent.NavigateToBack)
        }
    }
}