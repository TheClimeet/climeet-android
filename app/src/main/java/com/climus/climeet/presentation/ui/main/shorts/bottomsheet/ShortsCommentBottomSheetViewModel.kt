package com.climus.climeet.presentation.ui.main.shorts.bottomsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsCommentUiData
import com.climus.climeet.presentation.ui.main.shorts.toShortsCommentUiData
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

data class ShortsCommentBottomSheetUiState(
    val page: Int = 0,
    val hasNext: Boolean = true,
    val shortsCommentList: List<ShortsCommentUiData> = emptyList()
)

sealed class ShortsCommentBottomSheetEvent {
    data class ShowToastMessage(val msg: String) : ShortsCommentBottomSheetEvent()
}

@HiltViewModel
class ShortsCommentBottomSheetViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShortsCommentBottomSheetUiState())
    val uiState: StateFlow<ShortsCommentBottomSheetUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ShortsCommentBottomSheetEvent>()
    val event: SharedFlow<ShortsCommentBottomSheetEvent> = _event.asSharedFlow()

    private var shortsId: Long = -1L

    fun setShortsId(id: Long) {
        shortsId = id
        getCommentList()
    }

    fun getCommentList() {
        if (uiState.value.hasNext) {
            viewModelScope.launch {
                repository.getShortsCommentList(shortsId, uiState.value.page, 15).let {
                    when (it) {
                        is BaseState.Success -> {
                            _uiState.update { state ->
                                state.copy(
                                    page = uiState.value.page + 1,
                                    hasNext = it.body.hasNext,
                                    shortsCommentList = uiState.value.shortsCommentList + it.body.result.map { data ->
                                        data.toShortsCommentUiData(
                                            showMoreComment = ::getMoreSubComment,
                                            addSubComment = ::addSubComment,
                                            changeLikeStatus = ::changeLikeStatus
                                        )
                                    }
                                )
                            }
                        }

                        is BaseState.Error -> _event.emit(
                            ShortsCommentBottomSheetEvent.ShowToastMessage(
                                it.msg
                            )
                        )
                    }
                }
            }
        }

    }

    private fun getMoreSubComment(parentCommentId: Long) {

    }

    private fun addSubComment(parentCommentId: Long) {

    }

    private fun changeLikeStatus(commentId: Long, isLike: Boolean, isDislike: Boolean) {
        viewModelScope.launch {
            repository.patchShortsCommentInteraction(commentId, isLike, isDislike).let{
                when(it){
                    is BaseState.Success -> {

                    }

                    is BaseState.Error -> {

                    }
                }
            }
        }
    }
}