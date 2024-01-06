package com.climus.climeet.presentation.ui.intro.signup.admin.setname

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.intro.signup.admin.model.CragInfoUiData
import com.climus.climeet.presentation.util.Constants.TEST_IMG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SetCragNameEvent {
    data object NavigateToBack : SetCragNameEvent()
    data object NavigateToBusinessRegistration : SetCragNameEvent()
    data class NavigateToSetCragNameError(
        val cragId: Long,
        val imgUrl: String,
        val cragName: String
    ): SetCragNameEvent()
}

@HiltViewModel
class SetCragNameViewModel @Inject constructor() : ViewModel() {

    private val _uiCragInfo = MutableStateFlow(CragInfoUiData())
    val uiCragInfo: StateFlow<CragInfoUiData> = _uiCragInfo.asStateFlow()

    private val _event = MutableSharedFlow<SetCragNameEvent>()
    val event: SharedFlow<SetCragNameEvent> = _event.asSharedFlow()

    private var cragId: Long? = null

    fun setCragId(id: Long) {
        cragId = id
        getCragInfo()
    }

    private fun getCragInfo() {
        // todo 서버통신

        _uiCragInfo.update { state ->
            state.copy(
                imgUrl = TEST_IMG,
                name = "클라이머스 클라이밍",
                state = false
            )
        }
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(SetCragNameEvent.NavigateToBack)
        }
    }

    fun navigateToBusinessRegistration() {
        viewModelScope.launch {
            _event.emit(SetCragNameEvent.NavigateToBusinessRegistration)
        }
    }

    fun navigateToSetCragNameError(){
        viewModelScope.launch {
            cragId?.let{ cragId ->
                _event.emit(SetCragNameEvent.NavigateToSetCragNameError(
                    cragId,
                    uiCragInfo.value.imgUrl,
                    uiCragInfo.value.name
                ))
            }
        }
    }

}