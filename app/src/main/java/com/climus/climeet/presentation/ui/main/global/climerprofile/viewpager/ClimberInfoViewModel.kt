package com.climus.climeet.presentation.ui.main.global.climerprofile.viewpager

import androidx.lifecycle.ViewModel
import com.climus.climeet.data.model.response.BestClearClimberSimpleResponse
import com.climus.climeet.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class InfoUiState(
    val rankingList: List<BestClearClimberSimpleResponse> = emptyList()
)

@HiltViewModel
class InfoViewModel @Inject constructor(private val repository: MainRepository): ViewModel() {

    private val _uiState = MutableStateFlow(InfoUiState())
    val uiState: StateFlow<InfoUiState> = _uiState.asStateFlow()


}