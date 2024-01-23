package com.climus.climeet.presentation.ui.intro

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.intro.signup.admin.AdminSignupForm
import com.climus.climeet.presentation.ui.intro.signup.climer.ClimerSignupForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

sealed class IntroEvent {
    data object GoToGallery : IntroEvent()
    data class CheckAlarmPermission( val confirmAction: () -> Unit) : IntroEvent()
    data class ShowToastMessage(val msg: String): IntroEvent()
}

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _event = MutableSharedFlow<IntroEvent>()
    val event: SharedFlow<IntroEvent> = _event.asSharedFlow()

    private val _imageUri = MutableSharedFlow<Uri>()
    val imageUri: SharedFlow<Uri> = _imageUri.asSharedFlow()

    private var urlType: UrlType? = null

    fun goToGallery(type: UrlType) {
        urlType = type
        viewModelScope.launch {
            _event.emit(IntroEvent.GoToGallery)
        }
    }

    fun checkAlarmPermission(confirmAction: () -> Unit) {
        viewModelScope.launch {
            _event.emit(IntroEvent.CheckAlarmPermission(confirmAction))
        }
    }

    fun setImage(uri: Uri) {
        viewModelScope.launch {
            _imageUri.emit(uri)
        }
    }

    fun fileToUrl(file: MultipartBody.Part){
        viewModelScope.launch {
            repository.uploadImage(file).let{
                when(it){
                    is BaseState.Success -> {
                        when(urlType){
                            UrlType.ADMIN_BACKGROUND -> AdminSignupForm.setBackgroundUrl(it.body.imgUrl)
                            UrlType.ADMIN_BUSINESS_REGISTRATION -> AdminSignupForm.setBusinessRegistrationUrl(it.body.imgUrl)
                            UrlType.CLIMER_PROFILE -> ClimerSignupForm.setImageUrl(it.body.imgUrl)
                            else -> {}
                        }
                    }
                    is BaseState.Error -> _event.emit(IntroEvent.ShowToastMessage(it.msg))
                }
            }
        }
    }

}

enum class UrlType{
    ADMIN_BACKGROUND,
    ADMIN_BUSINESS_REGISTRATION,
    CLIMER_PROFILE
}