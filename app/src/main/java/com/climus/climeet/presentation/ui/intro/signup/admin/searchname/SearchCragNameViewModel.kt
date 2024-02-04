package com.climus.climeet.presentation.ui.intro.signup.admin.searchname

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.intro.signup.admin.model.SearchCragUiData
import com.climus.climeet.presentation.ui.intro.signup.admin.toSearchCragUiData
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

data class SearchCragNameUiState(
    val searchList: List<SearchCragUiData> = emptyList(),
    val progressState: Boolean = false,
    val emptyResultState: Boolean = false
)

sealed class SearchCragNameEvent {
    data object NavigateToBack : SearchCragNameEvent()
    data class NavigateToSetCragName(val id: Long, val name: String, val imgUrl: String) :
        SearchCragNameEvent()
    data class ShowToastMessage(val msg: String) : SearchCragNameEvent()
}

@HiltViewModel
class SearchCragNameViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchCragNameUiState())
    val uiState: StateFlow<SearchCragNameUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SearchCragNameEvent>()
    val event: SharedFlow<SearchCragNameEvent> = _event.asSharedFlow()

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
                    repository.searchGym(it,0,15).let { result ->
                        when (result) {
                            is BaseState.Success -> {
                                if (result.body.result.isNotEmpty()) {
                                    _uiState.update { state ->
                                        state.copy(
                                            searchList = result.body.result.map { item ->
                                                item.toSearchCragUiData(
                                                    it, ::navigateToSetCragName
                                                )
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
                                _event.emit(SearchCragNameEvent.ShowToastMessage(result.msg))
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

    private fun navigateToSetCragName(id: Long, cragName: String, imgUrl: String) {
        viewModelScope.launch {
            _event.emit(SearchCragNameEvent.NavigateToSetCragName(id, cragName, imgUrl))
        }
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(SearchCragNameEvent.NavigateToBack)
        }
    }
}