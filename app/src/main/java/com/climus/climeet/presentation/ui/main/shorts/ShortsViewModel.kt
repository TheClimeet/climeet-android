package com.climus.climeet.presentation.ui.main.shorts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.shorts.adapter.ShortsDetailListener
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsThumbnailUiData
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsUiData
import com.climus.climeet.presentation.ui.main.shorts.model.UpdatedFollowUiData
import com.climus.climeet.presentation.util.Constants.TEST_IMG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class ShortsUiState(
    val updatedFollowList: List<UpdatedFollowUiData> = emptyList(),
    val shortsList: List<ShortsUiData> = emptyList(),
    val shortsThumbnailList: List<ShortsThumbnailUiData> = emptyList(),
    val sortType: SortType = SortType.POPULAR,
    val page: Int = 0,
    val hasNext: Boolean = true
)

sealed class ShortsEvent {
    data class NavigateToShortsDetail(val shortsId: Int)
    data class ShowToastMessage(val msg: String) : ShortsEvent()
}

@HiltViewModel
class ShortsViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel(), ShortsDetailListener {

    private val _uiState = MutableStateFlow(ShortsUiState())
    val uiState: StateFlow<ShortsUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ShortsEvent>()
    val event: SharedFlow<ShortsEvent> = _event.asSharedFlow()

    fun getUpdatedFollow() {

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
        }
    }

    fun getPopularShorts(option: ShortsOption) {

        viewModelScope.launch {
            if (uiState.value.hasNext) {
                repository.getPopularShorts(uiState.value.page, 10).let {
                    when (it) {
                        is BaseState.Success -> {

                            val shortsThumbnailUiData = it.body.result.map { data ->
                                data.toShortsThumbnailUiData(
                                    ::navigateToShortsDetail
                                )
                            }

                            val shortsUiData = it.body.result.map { data ->
                                data.toShortsUiData()
                            }

                            _uiState.update { state ->
                                state.copy(
                                    page = uiState.value.page + 1,
                                    hasNext = it.body.hasNext,
                                    shortsThumbnailList = if (option == ShortsOption.NEXT_PAGE) uiState.value.shortsThumbnailList + shortsThumbnailUiData else shortsThumbnailUiData,
                                    shortsList = if (option == ShortsOption.NEXT_PAGE) uiState.value.shortsList + shortsUiData else shortsUiData
                                )
                            }
                        }

                        is BaseState.Error -> {
                            _event.emit(ShortsEvent.ShowToastMessage(it.msg))
                        }
                    }
                }
            }
        }
    }

    private fun getRecentShorts(option: ShortsOption) {

        viewModelScope.launch {
            if (uiState.value.hasNext) {
                repository.getRecentShorts(uiState.value.page, 10).let {
                    when (it) {
                        is BaseState.Success -> {

                            val shortsThumbnailUiData = it.body.result.map { data ->
                                data.toShortsThumbnailUiData(
                                    ::navigateToShortsDetail
                                )
                            }

                            val shortsUiData = it.body.result.map { data ->
                                data.toShortsUiData()
                            }

                            _uiState.update { state ->
                                state.copy(
                                    page = uiState.value.page + 1,
                                    hasNext = it.body.hasNext,
                                    shortsThumbnailList = if (option == ShortsOption.NEXT_PAGE) uiState.value.shortsThumbnailList + shortsThumbnailUiData else shortsThumbnailUiData,
                                    shortsList = if (option == ShortsOption.NEXT_PAGE) uiState.value.shortsList + shortsUiData else shortsUiData
                                )
                            }
                        }

                        is BaseState.Error -> {
                            _event.emit(ShortsEvent.ShowToastMessage(it.msg))
                        }
                    }
                }
            }
        }
    }

    fun changeSortType() {

        when (uiState.value.sortType) {
            SortType.POPULAR -> {
                _uiState.update { state ->
                    state.copy(
                        hasNext = true,
                        page = 0,
                        sortType = SortType.RECENT
                    )
                }
                getRecentShorts(ShortsOption.NEW_SORT)
            }

            SortType.RECENT -> {
                _uiState.update { state ->
                    state.copy(
                        hasNext = true,
                        page = 0,
                        sortType = SortType.POPULAR
                    )
                }
                getPopularShorts(ShortsOption.NEW_SORT)
            }
        }
    }

    fun callNextList() {
        when (uiState.value.sortType) {
            SortType.POPULAR -> {
                getPopularShorts(ShortsOption.NEXT_PAGE)
            }

            SortType.RECENT -> {
                getRecentShorts(ShortsOption.NEXT_PAGE)
            }
        }
    }

    private fun navigateToShortsDetail(id: Long) {

    }

    private fun navigateToUpdatedFollowShorts(id: Long) {

    }

    override fun onClickBookMarkBtn(shortsId: Long) {
        TODO("Not yet implemented")
    }

    override fun onClickDescription() {
        TODO("Not yet implemented")
    }

    override fun showShareDialog() {
        TODO("Not yet implemented")
    }

    override fun showCommentDialog(shortsId: Long) {
        TODO("Not yet implemented")
    }

    override fun onClickLikeBtn(shortsId: Long) {
        TODO("Not yet implemented")
    }

    override fun navigateToProfileDetail(userId: Long) {
        TODO("Not yet implemented")
    }

    override fun navigateToSectorShorts(sectorId: Long) {
        TODO("Not yet implemented")
    }
}

enum class ShortsOption() {
    NEXT_PAGE,
    NEW_SORT
}

enum class SortType(val text: String) {
    POPULAR("인기순"),
    RECENT("최신순")
}