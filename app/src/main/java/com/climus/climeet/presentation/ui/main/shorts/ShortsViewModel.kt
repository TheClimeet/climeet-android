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

sealed class ShortsEvent{
    data class NavigateToShortsDetail(val shortsId: Int)
}

@HiltViewModel
class ShortsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ShortsUiState())
    val uiState: StateFlow<ShortsUiState> = _uiState.asStateFlow()

    fun getInitData() {

        val thumbNailList = mutableListOf<ShortsThumbnailUiData>()

        for(i in 0 until 5){
            thumbNailList.add(ShortsThumbnailUiData(
                1,
                TEST_IMG,
                "파커스 강남",
                "#537049",
                "#433b80",
                ::navigateToShortsDetail
            ))
            thumbNailList.add(ShortsThumbnailUiData(
                2,
                TEST_IMG,
                "인천 클밋트",
                "#eb4034",
                "#eb4034",
                ::navigateToShortsDetail
            ))
            thumbNailList.add(ShortsThumbnailUiData(
                3,
                TEST_IMG,
                "피카부 동경",
                "#eb4034",
                "#537049",
                ::navigateToShortsDetail
            ))
        }

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
                    shortsThumbnailList = thumbNailList
                )
            }
        }
    }

    private fun navigateToShortsDetail(id: Long) {

    }

    private fun navigateToUpdatedFollowShorts(id: Long) {

    }

}