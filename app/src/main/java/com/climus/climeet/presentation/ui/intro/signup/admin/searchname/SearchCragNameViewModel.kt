package com.climus.climeet.presentation.ui.intro.signup.admin.searchname

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.IntroRepository
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.intro.signup.admin.model.SearchCragUiData
import com.climus.climeet.presentation.ui.intro.signup.admin.toSearchCragUiData
import com.climus.climeet.presentation.util.Constants.TAG
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
    data class NavigateToSetCragName(val id: Long, val name: String, val imgUrl: String): SearchCragNameEvent()
}

@HiltViewModel
class SearchCragNameViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

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
            Log.d(TAG,it)

            if(it.isBlank()){
                _uiState.update { state ->
                    state.copy(
                        searchList = emptyList()
                    )
                }
            } else {
                mainRepository.searchGym(it).let{ result ->
                    when(result){
                        is BaseState.Success -> {
                            if(result.body.isNotEmpty()){
                                _uiState.update { state ->
                                    state.copy(
                                        searchList = result.body.map{ item -> item.toSearchCragUiData(
                                            it,::navigateToSetCragName
                                        )}
                                    )
                                }
                            }
                        }

                        is BaseState.Error -> {
                            // todo 에러일떄 예외처리
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

    fun navigateToBack(){
        viewModelScope.launch {
            _event.emit(SearchCragNameEvent.NavigateToBack)
        }
    }
}