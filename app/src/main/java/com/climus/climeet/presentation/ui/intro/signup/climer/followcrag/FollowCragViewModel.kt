package com.climus.climeet.presentation.ui.intro.signup.climer.followcrag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.intro.signup.climer.model.FollowCrag
import com.climus.climeet.presentation.ui.intro.signup.climer.toFollowCrag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
    val searchList: List<FollowCrag> = emptyList(),
    val progressState: Boolean = false,
    val emptyResultState: Boolean = false
)

sealed class FollowCragEvent{
    data object NavigateToBack: FollowCragEvent()
    data object NavigateToHowToKnow: FollowCragEvent()
    data class ShowToastMessage(val msg: String): FollowCragEvent()
}

@HiltViewModel
class FollowCragViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FollowCragUiState())
    val uiState: StateFlow<FollowCragUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<FollowCragEvent>()
    val event: SharedFlow<FollowCragEvent> = _event.asSharedFlow()

    private var curJob: Job? = null

    val keyword = MutableStateFlow("")

    init {
        observeKeyword()
    }

    private fun observeKeyword() {
        keyword.onEach {
            if (it.isBlank()) {
                _uiState.update { state ->
                    state.copy(
                        searchList = emptyList(),
                        emptyResultState = false
                    )
                }
            } else {
                curJob?.cancel()

                _uiState.update { state ->
                    state.copy(
                        progressState = true,
                        emptyResultState = false
                    )
                }

                curJob = viewModelScope.launch {
                    delay(500)
                    repository.searchGym(it).let { result ->
                        when (result) {
                            is BaseState.Success -> {
                                if (result.body.isNotEmpty()) {
                                    _uiState.update { state ->
                                        state.copy(
                                            searchList = result.body.map { item ->
                                                item.toFollowCrag(it)
                                            },
                                            progressState = false
                                        )
                                    }
                                } else {
                                    _uiState.update { state ->
                                        state.copy(
                                            searchList = emptyList(),
                                            progressState = false,
                                            emptyResultState = true
                                        )
                                    }
                                }
                            }

                            is BaseState.Error -> {
                                _uiState.update { state ->
                                    state.copy(
                                        progressState = false,
                                        emptyResultState = true
                                    )
                                }
                                _event.emit(FollowCragEvent.ShowToastMessage(result.msg))
                            }
                        }
                    }
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