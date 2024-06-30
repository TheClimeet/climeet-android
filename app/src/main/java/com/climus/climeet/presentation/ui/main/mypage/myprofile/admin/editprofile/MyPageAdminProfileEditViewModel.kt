package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.editprofile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.PatchAdminNameRequest
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.InputState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SetAdminNickUiState(
    val nameState: InputState = InputState.Empty,
)

sealed class AdminProfileEditEvent{
    data object NavigateToBack : AdminProfileEditEvent()
    data object NavigateToProfile : AdminProfileEditEvent()
    data class ShowToastMessage(val msg: String) : AdminProfileEditEvent()
}

@HiltViewModel
class MyPageAdminProfileEditViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SetAdminNickUiState())
    val uiState: StateFlow<SetAdminNickUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<AdminProfileEditEvent>()
    val event: SharedFlow<AdminProfileEditEvent> = _event.asSharedFlow()

    val loading = MutableStateFlow(false)
    val nextAvailable = MutableStateFlow(false)

    val newName = MutableStateFlow("")
    private val backgroundUpdated = MutableStateFlow(false)
    private val profileUpdated = MutableStateFlow(false)
    private var profileImageToChange = ""

    var profileImage = ""
    var gymName = ""

    init {
        imageObserve()
        nameObserve()
    }

    fun initState(state: Boolean, name: String, profile: String) {
        backgroundUpdated.value = state
        nextAvailable.value = state
        profileImage = profile
        gymName = name
    }

    private fun imageObserve(){
        AdminEditProfileForm.profileUriState.onEach { uri ->
            if (uri.isNotBlank()) {
                profileUpdated.value = true
                nextAvailable.value = true
                profileImageToChange = AdminEditProfileForm.getProfileImagePath()
            }
        }.launchIn(viewModelScope)
    }

    private fun nameObserve() {
        newName.onEach {
            if (it.isNotBlank()) {
                nextAvailable.value = true
            } else {
                nextAvailable.value = backgroundUpdated.value || profileUpdated.value
            }
        }.launchIn(viewModelScope)
    }

    private fun updateGymProfile(){
        viewModelScope.launch{
            // todo : 배경
            if (backgroundUpdated.value){
                val image = AdminEditProfileForm.getBackgroundPath()
                repository.updateAdminBackgroundImage(image).let {
                    when (it) {
                        is BaseState.Success -> {
                            Log.d("mypage_admin", "배경 수정")
                        }
                        is BaseState.Error -> {
                            _event.emit(AdminProfileEditEvent.ShowToastMessage(it.msg))
                        }
                    }
                }
            }

            // todo : 프로필
            if (profileUpdated.value){
                repository.updateAdminProfileImage(profileImageToChange).let {
                    when (it) {
                        is BaseState.Success -> {
                            Log.d("mypage_admin", "프로필 이미지 수정")
                        }
                        is BaseState.Error -> {
                            _event.emit(AdminProfileEditEvent.ShowToastMessage(it.msg))
                        }
                    }
                }
            }

            // todo : 암장 이름
            if(newName.value != gymName && newName.value.isNotEmpty()){
                Log.d("mypage_admin", "수정된 암장 이름 : $newName")
                repository.updateAdminName(PatchAdminNameRequest(newName.value)).let {
                    when(it) {
                        is BaseState.Success -> {
                            Log.d("mypage_admin", "프로필 이름 수정")
                            AdminEditProfileForm.setNameUpdatedState(true)
                        }
                        is BaseState.Error -> {
                            _event.emit(AdminProfileEditEvent.ShowToastMessage(it.msg))
                        }
                    }
                }
            }

            AdminEditProfileForm.resetState()
        }
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(AdminProfileEditEvent.NavigateToBack)
        }
    }

    fun navigateToProfile() {
        updateGymProfile()
        viewModelScope.launch {
            loading.value = true
            delay(2000)
            loading.value = false
            _event.emit(AdminProfileEditEvent.NavigateToProfile)
        }
    }
}