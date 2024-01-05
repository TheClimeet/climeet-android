package com.climus.climeet.presentation.ui.intro.signup.admin.searchname

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.intro.signup.admin.model.SearchCragUiData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class SearchCragNameUiState(
    val searchList: List<SearchCragUiData> = emptyList()
)

@HiltViewModel
class SearchCragNameViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SearchCragNameUiState())
    val uiState: StateFlow<SearchCragNameUiState> = _uiState.asStateFlow()

    val keyword = MutableStateFlow("")

    init {
        observeKeyword()
    }

    private fun observeKeyword() {
        keyword.onEach {

            // todo 타이핑 할때마다 API 호출


        }.launchIn(viewModelScope)
    }
}