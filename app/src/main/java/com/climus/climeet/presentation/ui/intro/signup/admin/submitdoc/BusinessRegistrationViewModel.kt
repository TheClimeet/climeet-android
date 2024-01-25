package com.climus.climeet.presentation.ui.intro.signup.admin.submitdoc

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

sealed class BusinessRegistrationEvent{
    data object NavigateToBack: BusinessRegistrationEvent()
    data object NavigateToSetAdminIdPw: BusinessRegistrationEvent()
}

@HiltViewModel
class BusinessRegistrationViewModel @Inject constructor(): ViewModel() {

    private val _event = MutableSharedFlow<BusinessRegistrationEvent>()
    val event: SharedFlow<BusinessRegistrationEvent> = _event.asSharedFlow()


    fun navigateToSetAdminIdPw(){
        viewModelScope.launch {
            _event.emit(BusinessRegistrationEvent.NavigateToSetAdminIdPw)
        }
    }


}