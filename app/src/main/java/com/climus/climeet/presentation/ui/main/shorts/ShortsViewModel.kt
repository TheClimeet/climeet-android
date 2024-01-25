package com.climus.climeet.presentation.ui.main.shorts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsThumbnailUiData
import com.climus.climeet.presentation.ui.main.shorts.model.UpdatedFollowUiData
import com.climus.climeet.presentation.util.Constants.TEST_IMG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class ShortsUiState(
    val updatedFollowList: List<UpdatedFollowUiData> = emptyList(),
    val shortsThumbnailList: List<ShortsThumbnailUiData> = emptyList()
)

@HiltViewModel
class ShortsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ShortsUiState())
    val uiState: StateFlow<ShortsUiState> = _uiState.asStateFlow()

    fun getData() {
        viewModelScope.launch {

            _uiState.update { state ->
                state.copy(
                    updatedFollowList = listOf(
                        UpdatedFollowUiData(
                            1,
                            TEST_IMG,
                            "파커스 강남",
                            ::navigateToUpdatedFollowShorts
                        )
                    )
                )
            }

            _uiState.update { state ->
                state.copy(
                    shortsThumbnailList = listOf(
                        ShortsThumbnailUiData(
                            1,
                            TEST_IMG,
                            "파커스 강남",
                            "#eb4034",
                            "#433b80",
                            ::navigateToShortsDetail
                        )
                    )
                )
            }
        }
    }

    private fun navigateToShortsDetail(id: Long) {

    }

    private fun navigateToUpdatedFollowShorts(id: Long) {

    }

}