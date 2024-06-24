package com.climus.climeet.presentation.ui.main.mypage.announce

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

data class AnnounceDetailUiData(
    val boardId: Long = 0,
    val title: String = "",
    val createdAt: String = "",
    val profileImageUrl: String = "",
    val profileName: String = "",
    val followerCount: String = "",
    val followingCount: String = "",
    val content: String = "",
    val likeCount: String = "",
    val imageList: List<String>? = emptyList(),
    val likeState: Boolean = false,
)

sealed class AnnouncementDetailEvent {
    data class SetRVAdapter(val imageList: List<String>) : AnnouncementDetailEvent()
    data class ShowToastMessage(val msg: String) : AnnouncementDetailEvent()
}

@HiltViewModel
class AnnounceDetailViewModel @Inject constructor(val repository: MainRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AnnounceDetailUiData())
    val uiState: StateFlow<AnnounceDetailUiData> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<AnnouncementDetailEvent>()
    val event: SharedFlow<AnnouncementDetailEvent> = _event.asSharedFlow()

    fun getAnnouncement(boardId: Long) {
        viewModelScope.launch {
            repository.getAnnouncementDetail(boardId).let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                boardId = it.body.boardId,
                                title = it.body.title,
                                createdAt = formatDate(it.body.createdAt),
                                profileImageUrl = it.body.profileImageUrl,
                                profileName = it.body.profileName,
                                followerCount = it.body.followerCount.toString(),
                                followingCount = it.body.followingCount.toString(),
                                content = it.body.content,
                                likeCount = it.body.likeCount.toString(),
                                imageList = it.body.imageList,
                                likeState = it.body.likeStatus
                            )
                        }
                        uiState.value.imageList?.let {
                            setImageList(it)
                        }

                    }

                    is BaseState.Error -> {
                        _event.emit(AnnouncementDetailEvent.ShowToastMessage(it.msg))
                    }
                }
            }
        }
    }

    fun setLike() {
        _uiState.update { state ->
            state.copy(
                likeState = !uiState.value.likeState
            )
        }

        if (uiState.value.likeState) {
            _uiState.update { state ->
                state.copy(
                    likeCount = (uiState.value.likeCount.toInt() + 1).toString()
                )
            }
            updateLike(true)
        } else {
            _uiState.update { state ->
                state.copy(
                    likeCount = (uiState.value.likeCount.toInt() - 1).toString()
                )
            }
            updateLike(false)
        }
    }

    private fun updateLike(status: Boolean) {
        viewModelScope.launch {
            if (status) {
                repository.updateAnnouncementLike(uiState.value.boardId).let {
                    when (it) {
                        is BaseState.Success -> {
                            Log.d("mypage", "공지 졸아요")
                        }

                        is BaseState.Error -> {
                            _event.emit(AnnouncementDetailEvent.ShowToastMessage(it.msg))
                        }
                    }
                }
            } else{
                repository.updateAnnouncementUnlike(uiState.value.boardId).let {
                    when (it) {
                        is BaseState.Success -> {
                            Log.d("mypage", "공지 졸아요 취소")
                        }

                        is BaseState.Error -> {
                            _event.emit(AnnouncementDetailEvent.ShowToastMessage(it.msg))
                        }
                    }
                }
            }
        }
    }

    private fun formatDate(time: String): String {
        val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date = isoFormatter.parse(time)

        val targetFormat = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
        return targetFormat.format(date)
    }

    private fun setImageList(imageList: List<String>) {
        viewModelScope.launch {
            _event.emit(AnnouncementDetailEvent.SetRVAdapter(imageList))
        }
    }
}