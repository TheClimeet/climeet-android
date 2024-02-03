package com.climus.climeet.presentation.ui.main.shorts.bottomsheet.searchcrag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.intro.signup.admin.model.SearchCragUiData
import com.climus.climeet.presentation.ui.intro.signup.admin.toSearchCragUiData
import com.climus.climeet.presentation.ui.main.shorts.toSearchCragUiData
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

data class SearchCragBottomSheetUiState(
    val searchList: List<SearchCragUiData> = emptyList(),
    val progressState: Boolean = false,
    val emptyResultState: Boolean = false
)

sealed class SearchCragBottomSheetEvent {
    data class NavigateToSelectSectorBottomSheet(val id: Long, val name: String) :
        SearchCragBottomSheetEvent()

    data class ShowToastMessage(val msg: String) : SearchCragBottomSheetEvent()
}

@HiltViewModel
class SearchCragBottomSheetViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchCragBottomSheetUiState())
    val uiState: StateFlow<SearchCragBottomSheetUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SearchCragBottomSheetEvent>()
    val event: SharedFlow<SearchCragBottomSheetEvent> = _event.asSharedFlow()

    private var curJob: Job? = null

    val keyword = MutableStateFlow("")

    init {
        observeKeyword()
    }
    
    fun getHomeGym(){
        viewModelScope.launch { 
            // todo 최초 키워드 입력 전 홈짐 불러오기
        }
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
                    repository.searchAvailableGym(it, 0, 15).let { result ->
                        when (result) {
                            is BaseState.Success -> {
                                if (result.body.result.isNotEmpty()) {
                                    _uiState.update { state ->
                                        state.copy(
                                            searchList = result.body.result.map { item ->
                                                item.toSearchCragUiData(
                                                    it, ::navigateToSelectSectorBottomSheet
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
                                _event.emit(SearchCragBottomSheetEvent.ShowToastMessage(result.msg))
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

    fun navigateToSelectSectorBottomSheet(cragId: Long, cragName: String, cragImgUrl: String) {

        viewModelScope.launch {
            _event.emit(
                SearchCragBottomSheetEvent.NavigateToSelectSectorBottomSheet(
                    cragId,
                    cragName
                )
            )
        }
    }
}