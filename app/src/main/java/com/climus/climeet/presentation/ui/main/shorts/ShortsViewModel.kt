package com.climus.climeet.presentation.ui.main.shorts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.global.selectsector.model.SelectedFilter
import com.climus.climeet.presentation.ui.main.shorts.adapter.ShortsDetailListener
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsThumbnailUiData
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsUiData
import com.climus.climeet.presentation.ui.main.shorts.model.UpdatedFollowUiData
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
    val curFilter: SelectedFilter = SelectedFilter(),
    val page: Int = 0,
    val hasNext: Boolean = true
)

sealed class ShortsEvent {
    data object NavigateToSearchCragBottomSheet : ShortsEvent()
    data class NavigateToShortsDetail(val shortsId: Long, val position: Int) : ShortsEvent()
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

    companion object {
        const val ITEM = 0
        const val GOTO_FOLLOW = 1
    }

    fun getUpdatedFollow() {

        viewModelScope.launch {
            repository.getShortsUpdatedFollow().let {
                when (it) {
                    is BaseState.Success -> {

                        _uiState.update { state ->
                            state.copy(
                                updatedFollowList = it.body.map { data ->
                                    data.toUpdatedFollowUiData(
                                        ITEM,
                                        ::navigateToFollowerPage,
                                        ::navigateToAddFollow
                                    )
                                } + UpdatedFollowUiData(
                                    viewType = GOTO_FOLLOW,
                                    onClickListener = ::navigateToFollowerPage,
                                    navigateToAddFollow = ::navigateToAddFollow
                                )
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

    fun getShorts(option: ShortsOption) {

        // todo API 업데이트 되면, 필터 적용해서 API CALL

        viewModelScope.launch {
            if (uiState.value.hasNext) {
                val result = when (uiState.value.sortType) {
                    SortType.POPULAR -> {
                        repository.getPopularShorts(uiState.value.page, 10)
                    }

                    SortType.RECENT -> {
                        repository.getRecentShorts(uiState.value.page, 10)
                    }
                }

                result.let {
                    when (it) {
                        is BaseState.Success -> {

                            val shortsThumbnailUiData = it.body.result.map { data ->
                                data.toShortsThumbnailUiData(
                                    ::navigateToShortsPlayer
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
                getShorts(ShortsOption.NEW_SORT)
            }

            SortType.RECENT -> {
                _uiState.update { state ->
                    state.copy(
                        hasNext = true,
                        page = 0,
                        sortType = SortType.POPULAR
                    )
                }
                getShorts(ShortsOption.NEW_SORT)
            }
        }
    }

    fun applyFilter(sectorInfo: SelectedFilter) {
        _uiState.update { state ->
            state.copy(
                page = 0,
                hasNext = true,
                curFilter = sectorInfo
            )
        }
    }

    fun deleteFilter() {
        _uiState.update { state ->
            state.copy(
                page = 0,
                hasNext = true,
                curFilter = SelectedFilter()
            )
        }
    }

    private fun navigateToShortsPlayer(id: Long, position: Int) {
        viewModelScope.launch {
            _event.emit(ShortsEvent.NavigateToShortsDetail(id,position))
        }
    }

    private fun navigateToFollowerPage(id: Long) {

    }

    private fun navigateToAddFollow() {

    }

    fun navigateToSearchCragBottomSheet() {
        viewModelScope.launch {
            _event.emit(ShortsEvent.NavigateToSearchCragBottomSheet)
        }
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