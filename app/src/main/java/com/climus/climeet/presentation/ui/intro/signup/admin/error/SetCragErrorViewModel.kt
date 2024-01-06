package com.climus.climeet.presentation.ui.intro.signup.admin.error

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class SetCragErrorViewModel @Inject constructor() : ViewModel() {

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
    ){

    }
}