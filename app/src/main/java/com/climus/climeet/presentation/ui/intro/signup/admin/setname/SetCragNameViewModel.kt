package com.climus.climeet.presentation.ui.intro.signup.admin.setname

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.IntroRepository
import com.climus.climeet.presentation.ui.intro.signup.admin.AdminSignupForm
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
class SetCragNameViewModel @Inject constructor(
    private val repository: IntroRepository
) : ViewModel() {

    private val _uiCragInfo = MutableStateFlow(CragInfoUiData())
    val uiCragInfo: StateFlow<CragInfoUiData> = _uiCragInfo.asStateFlow()

    private val _event = MutableSharedFlow<SetCragNameEvent>()
    val event: SharedFlow<SetCragNameEvent> = _event.asSharedFlow()

    private var cragId: Long? = null
    private var cragName: String = ""

    fun setCragInfo(id: Long, name: String, imgUrl: String) {
        cragId = id
        cragName = name
        AdminSignupForm.setCragName(name)

        _uiCragInfo.update { state ->
            state.copy(
                imgUrl = imgUrl,
                name = name,
                state = true
            )
        }
        checkCragState()
    }

    private fun checkCragState(){
        viewModelScope.launch {
            repository.managerGymNameCheck(cragName).let{
                when(it){
                    is BaseState.Success -> {
                        if(it.body){
                            _uiCragInfo.update { state ->
                                state.copy(
                                    state = true
                                )
                            }
                        } else {
                            _uiCragInfo.update { state ->
                                state.copy(
                                    state = false 
                                )
                            }
                        }
                    }
                    is BaseState.Error -> {

                        // todo 예외처리
                        
                    }
                }
            }
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