package com.climus.climeet.presentation.ui.main.shorts.player

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.OptIn
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.Player.STATE_READY
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentShortsDetailBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.shorts.adapter.ShortsDetailListener
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsUiData
import com.climus.climeet.presentation.util.Constants.TEST_IMG
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShortsDetailFragment @Inject constructor(
    private val data: ShortsUiData,
    private val listener: ShortsDetailListener?
) : BaseFragment<FragmentShortsDetailBinding>(R.layout.fragment_shorts_detail) {

    private var player: ExoPlayer? = null
    private val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
    private val viewModel: ShortsDetailViewModel by viewModels()
    private val parentViewModel: ShortsPlayerViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setInitData(data.shortsId, data.isBookMarked, data.bookMarkCount, data.isLiked, data.likeCount, data.description)
        binding.vm = viewModel
        binding.item = data
        initEventObserve()
        setImage()
        setPlayer()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is ShortsDetailEvent.ShowToastMessage -> showToastMessage(it.msg)
                    is ShortsDetailEvent.NavigateToProfileDetail -> listener?.navigateToProfileDetail(
                        data.userId
                    )

                    is ShortsDetailEvent.NavigateToRouteShorts -> {
                        // todo 이거 routeId로 변경. 서버 API 수정되어야함
                        data.sectorId?.let{ id ->
                            listener?.navigateToRouteShorts(id)
                        }
                    }

                    is ShortsDetailEvent.ShowShareDialog -> {
                        val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
                        intent.type = "text/plain"
                        intent.putExtra(Intent.EXTRA_TEXT, "클밋 에서 숏츠를 공유했어요!\n${data.videoUrl}\n\n클밋 다운로드 하기\nplaystore:://climeet.com")
                        startActivity(Intent.createChooser(intent,"공유"))
                    }
                    is ShortsDetailEvent.ShowCommentDialog -> listener?.showCommentDialog(data.shortsId, data.profileImgUrl)

                    is ShortsDetailEvent.PatchFavorite -> parentViewModel.patchFavorite(data.shortsId, it.state)
                    is ShortsDetailEvent.PatchBookMark -> parentViewModel.patchBookMark(data.shortsId, it.state)
                }
            }
        }
    }

    private fun setImage() {
        Glide.with(this)
            .load(data.profileImgUrl)
            .error(TEST_IMG)
            .into(binding.ivProfile)
    }

    @OptIn(UnstableApi::class)
    private fun setPlayer() {
        player = ExoPlayer.Builder(requireContext())
            .build().apply {
                val source = if (data.videoUrl.contains("m3u8")) getHlsMediaSource()
                else getProgressiveMediaSource()
                setMediaSource(source)
                prepare()
                addListener(playerListener)
            }
        player?.playWhenReady = false
    }

    @OptIn(UnstableApi::class)
    private fun getHlsMediaSource(): MediaSource {
        return HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(data.videoUrl))
    }

    @OptIn(UnstableApi::class)
    private fun getProgressiveMediaSource(): MediaSource {
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(data.videoUrl)))
    }

    private fun releasePlayer() {
        player?.apply {
            playWhenReady = false
            release()
        }
        player = null
    }

    private fun pause() {
        player?.playWhenReady = false
    }

    private fun play() {
        player?.playWhenReady = true
    }

    private fun restartPlayer() {
        player?.seekTo(0)
        player?.playWhenReady = true
    }

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            when (playbackState) {
                STATE_ENDED -> restartPlayer()
                STATE_READY -> {
                    binding.exoPlayer.player = player
                    play()
                }

                else -> {}
            }
        }

    }

    override fun onPause() {
        pause()
        super.onPause()
    }

    override fun onResume() {
        play()
        super.onResume()
    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }


}