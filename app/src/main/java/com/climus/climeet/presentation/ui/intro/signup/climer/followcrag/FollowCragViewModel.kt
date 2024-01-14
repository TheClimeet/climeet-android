package com.climus.climeet.presentation.ui.intro.signup.climer.followcrag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.intro.signup.climer.model.FollowCrag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FollowCragUiState(
    val searchList: List<FollowCrag> = emptyList()
)

sealed class FollowCragEvent{
    data object NavigateToBack: FollowCragEvent()
    data object NavigateToHowToKnow: FollowCragEvent()
}

@HiltViewModel
class FollowCragViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(FollowCragUiState())
    val uiState: StateFlow<FollowCragUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<FollowCragEvent>()
    val event: SharedFlow<FollowCragEvent> = _event.asSharedFlow()

    val keyword = MutableStateFlow("")

    init {
        observeKeyword()
    }

    private fun observeKeyword() {
        keyword.onEach {

            if(it.isBlank()){
                _uiState.update { state ->
                    state.copy(
                        searchList = emptyList()
                    )
                }
            } else {
                // todo 타이핑 할때마다 API 호출
                _uiState.update { state ->
                    state.copy(
                        searchList = listOf(
                            FollowCrag(null, "더클라임 클라이밍 강남점", 70, it,false),
                            FollowCrag(null, "더클라임 클라이밍 논현점", 30, it,false),
                            FollowCrag(null, "더클라임 클라이밍 마곡점", 120, it,false),
                            FollowCrag(null, "더클라임 클라이밍 홍대점", 12, it,false),
                            FollowCrag(null, "더클라임 클라이밍 강남점", 70, it,false),
                            FollowCrag(null, "더클라임 클라이밍 논현점", 30, it,false),
                            FollowCrag(null, "더클라임 클라이밍 마곡점", 120, it,false),
                            FollowCrag(null, "더클라임 클라이밍 홍대점", 12, it,false),
                            FollowCrag(null, "더클라임 클라이밍 강남점", 70, it,false),
                            FollowCrag(null, "더클라임 클라이밍 논현점", 30, it,false),
                            FollowCrag(null, "더클라임 클라이밍 마곡점", 120, it,false),
                            FollowCrag(null, "더클라임 클라이밍 홍대점", 12, it,false),
                            FollowCrag(null, "더현대 홍대점", 126, it,false),
                            FollowCrag(null, "가톨릭대 홍대점", 126, it,false)
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun deleteKeyword() {
        keyword.value = ""
        _uiState.update { state ->
            state.copy(
                searchList = emptyList()
            )
        }
    }

    fun navigateToHowToKnow() {
        viewModelScope.launch {
            _event.emit(FollowCragEvent.NavigateToHowToKnow)
        }
    }

    fun navigateToBack(){
        viewModelScope.launch {
            _event.emit(FollowCragEvent.NavigateToBack)
        }
    }
}