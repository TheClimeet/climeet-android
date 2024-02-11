package com.climus.climeet.presentation.ui.main.record.timer.stopwatch.selectcrag

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.intro.signup.admin.model.SearchCragUiData
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

data class CragNameUiState(
    var searchList: List<SearchCragUiData> = emptyList(),
    val progressState: Boolean = false,
    val emptyResultState: Boolean = false
)

sealed class CragSelectEvent{
    data class NavigateToBack(val id: Long, val name: String) : CragSelectEvent()
    data class ShowToastMessage(val msg: String): CragSelectEvent()
}

@HiltViewModel
class TimerCragSelectBottomSheetViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(CragNameUiState())
    val uiState: StateFlow<CragNameUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CragSelectEvent>()
    val event: SharedFlow<CragSelectEvent> = _event.asSharedFlow()

    private var curJob: Job? = null

    val keyword = MutableStateFlow("")

    // 선택된 암장 정보
    private val _selectedCrag = MutableLiveData<SearchCragUiData?>()
    val selectedCrag: MutableLiveData<SearchCragUiData?> get() = _selectedCrag

    fun selectItem(item: SearchCragUiData) {
        _selectedCrag.value = item
    }

    fun resetItem() {
        _selectedCrag.value = null
        Log.d("TIMER", "선택된 암장 정보 초기화 -> ${selectedCrag.value}")
    }

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
                    repository.searchAvailableGym(it, 0, 15).let { result ->
                        when (result) {
                            is BaseState.Success -> {
                                if (result.body.result.isNotEmpty()) {
                                    _uiState.update { state ->
                                        state.copy(
                                            searchList = result.body.result.map { item ->
                                                item.toSearchCragUiData(
                                                    it, ::navigateToBack
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
                                            emptyResultState = true,
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
                                _event.emit(CragSelectEvent.ShowToastMessage(result.msg))
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

    fun navigateToBack(cragId: Long, cragName: String, cragImgUrl: String) {
        viewModelScope.launch {
            _event.emit(
                CragSelectEvent.NavigateToBack(cragId, cragName)
            )
        }
    }
}