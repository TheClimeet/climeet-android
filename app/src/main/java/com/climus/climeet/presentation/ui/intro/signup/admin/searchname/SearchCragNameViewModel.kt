package com.climus.climeet.presentation.ui.intro.signup.admin.searchname

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.intro.signup.admin.model.SearchCragUiData
import com.climus.climeet.presentation.util.Constants.TEST_IMG
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

data class SearchCragNameUiState(
    val searchList: List<SearchCragUiData> = emptyList()
)

sealed class SearchCragNameEvent{
    data object NavigateToBack: SearchCragNameEvent()
    data class NavigateToSetCragName(val id: Long): SearchCragNameEvent()
}

@HiltViewModel
class SearchCragNameViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SearchCragNameUiState())
    val uiState: StateFlow<SearchCragNameUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SearchCragNameEvent>()
    val event: SharedFlow<SearchCragNameEvent> = _event.asSharedFlow()

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
                            SearchCragUiData(2, TEST_IMG, "더클라이머 연남", it, ::navigateToSetCragName),
                            SearchCragUiData(1, TEST_IMG, "더클라이머 마곡", it,::navigateToSetCragName),
                            SearchCragUiData(2, TEST_IMG, "더클라이머 연남", it,::navigateToSetCragName),
                            SearchCragUiData(5, TEST_IMG, "더클라이머 암재", it,::navigateToSetCragName),
                            SearchCragUiData(6, TEST_IMG, "더클라이머 암곡", it,::navigateToSetCragName),
                            SearchCragUiData(7, TEST_IMG, "더클라이머 어곡", it,::navigateToSetCragName),
                            SearchCragUiData(8, TEST_IMG, "더클라이머 이곡", it,::navigateToSetCragName),
                            SearchCragUiData(1, TEST_IMG, "더클라이머 마곡", it,::navigateToSetCragName),
                            SearchCragUiData(2, TEST_IMG, "더클라이머 연남", it,::navigateToSetCragName),
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

    private fun navigateToSetCragName(id: Long) {
        viewModelScope.launch {
            _event.emit(SearchCragNameEvent.NavigateToSetCragName(id))
        }
    }
}